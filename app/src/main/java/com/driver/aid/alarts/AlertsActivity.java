package com.driver.aid.alarts;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;


import com.driver.aid.R;
import com.driver.aid.SharedPreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class AlertsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,AntiDrowseDialog.AntiDrowseDialogListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mClient;
    Switch speedSwitch;
    Switch antiDrowseAlertSwitch;
    FrameLayout confirmLayout;
    Button confirmLoc;
    ToneGenerator tg;
    LocationRequest mLocationRequest;
    public double mlat;
    public double mlon;

    int speedLimitVal;
    float detectedSpeed;


    private TextToSpeech tts;
    private SharedPreferenceManager sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alerts);
        sharedPref = new SharedPreferenceManager(this);
        if (googleServicesAvailable()) {
            setContentView(R.layout.activity_alerts);

            speedSwitch = (Switch) findViewById(R.id.speedAlert);
            antiDrowseAlertSwitch = findViewById(R.id.antiDrowseAlert);
            confirmLayout = (FrameLayout) findViewById(R.id.confirmLayout);
            confirmLoc = (Button) findViewById(R.id.confirmLoc);
            tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            boolean isScheduled = isAlarmScheduled(this, sharedPref);
            antiDrowseAlertSwitch.setChecked(isScheduled);

            antiDrowseAlertSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AntiDrowseDialog.newInstance().show(getSupportFragmentManager(), AntiDrowseDialog.TAG);
                } else {
                    stopService(AlarmService.newIntent(this));
                    cancelAlarms(sharedPref,this);

                }
            });
            speedSwitch.setChecked(sharedPref.getSpeedAlarmStatus());
            speedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        activateSpeedAlarm();
                    } else {
                        confirmLayout.setVisibility(View.INVISIBLE);
                        sharedPref.setSpeedAlarmStatus(false);
                    }

                }
            });
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private void activateSpeedAlarm() {
        sharedPref.setSpeedAlarmStatus(true);
        confirmLayout.setVisibility(View.VISIBLE);
        initMap();
        confirmLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapReady(mGoogleMap);
                confirmLayout.setVisibility(View.INVISIBLE);
            }
        });
    }


    static boolean isAlarmScheduled(Context context, SharedPreferenceManager sharedPref) {
        List<String> alarms = sharedPref.getSavedAntiDrowseId();
        if (alarms.isEmpty()) {
            return false;
        }
        for (String id : alarms
        ) {

            Integer integerId = Integer.parseInt(id);
            Intent intent = new Intent(context, OnAlarmReceiver.class);
            intent.setAction(OnAlarmReceiver.ACTION_ALARM_RECEIVER);
            boolean isWorking = (PendingIntent.getBroadcast(context, integerId, intent, PendingIntent.FLAG_NO_CREATE) != null);
            if (isWorking) {
                return true;
            }
        }

        return false;
    }

    public static void cancelAlarms(SharedPreferenceManager sharedPref,Context context) {
        List<String> ids = sharedPref.getSavedAntiDrowseId();
        if (!ids.isEmpty()) {
            for (String idString :
                    ids) {
                Integer idInteger = Integer.parseInt(idString);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(context, OnAlarmReceiver.class);
                myIntent.setAction(OnAlarmReceiver.ACTION_ALARM_RECEIVER);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, idInteger, myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
        }
        sharedPref.clearAlarms();
    }

    private void initMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        // goToLocationZoom(39.008224, -76.8984527, 15);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());

    }

    @Override
    public void onDismissed() {
        if (!isAlarmScheduled(this, sharedPref)) {
            antiDrowseAlertSwitch.setChecked(false);
        }
    }

    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude look up the
         * address, and return it
         *
         * @return A string containing the address of the current location, or
         * an empty string if no address can be found, or an error
         * message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                addresses = geocoder.getFromLocation(mlat,
                        mlon, 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments "
                        + mlat + " , "
                        + mlon
                        + " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                String addressText = String.format(
                        "%s, %s, %s", address, city, country);

                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }

        protected void onPostExecute(String address) {
            // get the address and road type
            String roadType = getRoadType(address);
            speedLimitVal = getSpeedLimit(roadType);

        }

    }

    private void voiceAlert() {
        this.tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int arg0) {
                tts.setLanguage(Locale.US);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");
                tts.speak("You are going too fast and above the speed limit.", TextToSpeech.QUEUE_ADD, map);
            }

        });
    }

    // get type of road based on the keyword from the location address string
    private String getRoadType(String address) {
        String str;
        if (address.matches("(.*)(Hwy|Fwy|Highway|Freeway)(.*)")) {
            str = "highway";
        } else if (address.matches("(.*)(Express way|Expy|Expressway)(.*)")) {
            str = "express way";
        } else {
            str = "local";
        }
        return str;
    }

    // get speed limit based on type of road
    public int getSpeedLimit(String roadType) {
        if (roadType.equals("highway")) {
            speedLimitVal = 140;
        } else if (roadType.equals("express way")) {
            speedLimitVal = 120;
        } else {
            speedLimitVal = 90;
        }

        return speedLimitVal;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);

            mlat = location.getLatitude();
            mlon = location.getLongitude();

            // display detected speed
            detectedSpeed = (float) (location.getSpeed() * 3.6);

            new GetAddressTask(AlertsActivity.this).execute(location);

            // trigger the voice alarm when detectedSpeed >  speedLimitVal
            if ((detectedSpeed > 0.0) && (speedLimitVal != 0) && (detectedSpeed > speedLimitVal)) {
                voiceAlert();
            }
        }

    }
}