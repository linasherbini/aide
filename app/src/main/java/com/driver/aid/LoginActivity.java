package com.driver.aid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.aid.driver.DriverHomeActivity;
import com.driver.aid.shop.ShopHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.driver.aid.FormValidator.extractText;
import static com.driver.aid.FormValidator.isValid;

public class LoginActivity extends AppCompatActivity {
    private static final String AUTH_URL = "https://driveraid.us1a.cloud.realm.io";


    EditText username, password;
    Button LoginB;
    TextView resetPass;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_login);
        compositeDisposable = new CompositeDisposable();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        LoginB = (Button) findViewById(R.id.LoginB);
        resetPass = (TextView) findViewById(R.id.resetPass);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(reset);
            }
        });

        // if the driver clicks create account option
        password.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!isValid(username) || !isValid(password)) {
                        Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    } else {
                        performLogin(extractText(username), extractText(password));
                    }
                    return true;
                }
                return false;
            }
        });

        LoginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid(username) || !isValid(password)) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    performLogin(extractText(username), extractText(password));
                }
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            connectRealm();
        }

    }

    private void performLogin(String email, String password) {
        progressDialog.setMessage("Logging in  User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            connectRealm();
                        } else {
                            Toast.makeText(LoginActivity.this, "user name or password incorrect!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void connectRealm() {
        FirebaseFunctions.getInstance().getHttpsCallable("myAuthFunction")
                .call(null).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    Map<String, String> result = (Map<String, String>) task.getResult().getData();

                    compositeDisposable.add(Observable.just(result.get("token"))
                            .map(new Function<String, SyncUser>() {
                                @Override
                                public SyncUser apply(String token) throws Exception {
                                    SyncCredentials credentials = SyncCredentials.jwt(result.get("token"));
                                    return SyncUser.logIn(credentials, AUTH_URL);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(syncUser -> {

                                        RealmRemoteManager.init(syncUser);
                                        RealmRemoteManager.getInstance().updateLoggedInUser(userType->{
                                            startHome(userType);
                                            finish();
                                        });
                                    }
                            ,throwable -> Toast.makeText(LoginActivity.this,"unable to complete login!",Toast.LENGTH_SHORT).show()
                            ));
                } else {
                    Toast.makeText(LoginActivity.this, "unable to connect to realm!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startHome(UserType userType) {
        switch (userType) {
            case DRIVER:
                startActivity(DriverHomeActivity.newIntent(this));
                break;
            case EMPLOYEE:
                startActivity(EmployeeHomeActivity.newIntent(this));
                break;
            case SHOP:
                startActivity(ShopHomeActivity.newIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
