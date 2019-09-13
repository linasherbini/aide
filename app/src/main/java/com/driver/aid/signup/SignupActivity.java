package com.driver.aid.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.driver.aid.LoggedInUserManager;
import com.driver.aid.driver.DriverHomeActivity;
import com.driver.aid.EmployeeHomeActivity;
import com.driver.aid.Model.Driver;
import com.driver.aid.Model.Employee;
import com.driver.aid.Model.Shop;
import com.driver.aid.RealmRemoteManager;
import com.driver.aid.shop.ShopHomeActivity;
import com.driver.aid.UserType;
import com.driver.aid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.driver.aid.FormValidator.extractText;
import static com.driver.aid.FormValidator.isValid;
import static com.driver.aid.UserType.DRIVER;
import static com.driver.aid.UserType.EMPLOYEE;
import static com.driver.aid.UserType.SHOP;

public class SignupActivity extends AppCompatActivity {

    private static final String AUTH_URL = "https://driveraid.us1a.cloud.realm.io";
    private EditText empFullName, empEmail, empPhone, empSkills, empDegree, empExp, empPassword;
    private EditText driverFullName, driverEmail, driverPhone, driverPlateNumber, driverCarType, driverNationalID, driverPassword;
    private EditText shopFullName, shopEmail, shopPhone, shopBranch, shopPassword;
    private View driverContainer, shopContainer, employeeContainer;
    private Button empCreateAccount;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static Intent newIntent(Context context) {
        return new Intent(context, SignupActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        initialseEmployeeViews();
        initialseDriverViews();
        initialseShopViews();
        initiaseContainers();
        empCreateAccount = (Button) findViewById(R.id.createAccount);
        AppCompatSpinner spinner = findViewById(R.id.userTypeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        show(driverContainer);
                        break;
                    }
                    case 1: {
                        show(employeeContainer);
                        break;
                    }
                    case 2: {
                        show(shopContainer);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        empCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {

                    switch (getCurretnForm()) {
                        case EMPLOYEE: {
                            signupEmployee();
                            break;
                        }
                        case DRIVER: {
                            signupDriver();
                            break;
                        }
                        case SHOP: {
                            signupShop();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void signupShop() {
        {

            progressDialog.setMessage("Regisring User...");
            progressDialog.show();
            String fullName = extractText(this.shopFullName);
            String shopEmail = extractText(this.shopEmail);
            String shopPhone = extractText(this.shopPhone);
            String branch = extractText(this.shopBranch);
            String shopPassword = extractText(this.shopPassword);
            Shop shop = new Shop(null, fullName, branch, shopEmail, shopPhone);
            firebaseAuth.createUserWithEmailAndPassword(shopEmail, shopPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                Toast.makeText(SignupActivity.this, "Registered Successful", Toast.LENGTH_SHORT)
                                        .show();
                                sendVerifyEmail();
                                updateUser(fullName);

                                loginShopToRealm(shop);
                            } else {
                                if (task.getException() != null) {

                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignupActivity.this, "could not registered .please try again",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });

        }
    }

    private void signupDriver() {

        progressDialog.setMessage("Regisring User...");
        progressDialog.show();
        String fullName = extractText(this.driverFullName);
        String driverEmail = extractText(this.driverEmail);
        String driverphoneNumber = extractText(this.driverPhone);
        String driverPlateNumber = extractText(this.driverPlateNumber);
        String driverCarType = extractText(this.driverCarType);
        String driverNationalID = extractText(this.driverNationalID);
        String driverPassword = extractText(this.driverPassword);
        Driver driver = new Driver(null, fullName, driverphoneNumber, driverEmail, driverPlateNumber, driverCarType, driverNationalID);
        firebaseAuth.createUserWithEmailAndPassword(driverEmail, driverPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            Toast.makeText(SignupActivity.this, "Registered Successful", Toast.LENGTH_SHORT)
                                    .show();
                            sendVerifyEmail();
                            updateUser(fullName);

                            loginDriverToRealm(driver);
                        } else {
                            Toast.makeText(SignupActivity.this, "could not registered .please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void loginDriverToRealm(Driver driver) {


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
                                    SyncUser user = SyncUser.logIn(credentials, AUTH_URL);
                                    return user;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<SyncUser>() {
                                @Override
                                public void accept(SyncUser syncUser) throws Exception {
                                    RealmRemoteManager.init(syncUser);
                                    driver.setUserId(syncUser.getIdentity());
                                    RealmRemoteManager.getInstance().updateDriverInfo(driver);
                                    LoggedInUserManager.init(getCurretnForm(), driver);
                                    finish();
                                    startHome();
                                }
                            }));
                } else {
                    Toast.makeText(SignupActivity.this, "unable to connect to realm!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loginEmployeeToRealm(Employee employee) {


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
                                    SyncUser user = SyncUser.logIn(credentials, AUTH_URL);
                                    return user;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<SyncUser>() {
                                @Override
                                public void accept(SyncUser syncUser) throws Exception {
                                    RealmRemoteManager.init(syncUser);
                                    employee.setUserId(syncUser.getIdentity());
                                    RealmRemoteManager.getInstance().updateEmployee(employee);
                                    LoggedInUserManager.init(getCurretnForm(),employee);
                                    finish();
                                    startHome();
                                }
                            }));
                } else {
                    Toast.makeText(SignupActivity.this, "unable to connect to realm!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loginShopToRealm(Shop shop) {
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
                                    SyncUser user = SyncUser.logIn(credentials, AUTH_URL);
                                    return user;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<SyncUser>() {
                                @Override
                                public void accept(SyncUser syncUser) throws Exception {
                                    RealmRemoteManager.init(syncUser);
                                    shop.setUserId(syncUser.getIdentity());
                                    RealmRemoteManager.getInstance().updateShopInfo(shop);
                                    LoggedInUserManager.init(getCurretnForm(), shop);
                                    finish();
                                    startHome();
                                }
                            }));
                } else {
                    Toast.makeText(SignupActivity.this, "unable to connect to realm!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startHome() {
        switch (getCurretnForm()) {
            case DRIVER: {
                startActivity(DriverHomeActivity.newIntent(SignupActivity.this));
                break;
            }
            case SHOP: {
                startActivity(ShopHomeActivity.newIntent(SignupActivity.this));
                break;
            }
            case EMPLOYEE: {
                startActivity(EmployeeHomeActivity.newIntent(SignupActivity.this));
            }
        }
    }


    private void sendVerifyEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification();
    }

    private void updateUser(String fullName) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest =
                new UserProfileChangeRequest.Builder().setDisplayName(fullName)
                        .build();
        firebaseUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        finish();
                    }
                });
    }

    private void signupEmployee() {
        {

            progressDialog.setMessage("Regisring User...");
            progressDialog.show();
            String fullName = extractText(this.empFullName);
            String email = extractText(this.empEmail);
            String phone = extractText(this.empPhone);
            String skills = extractText(this.empSkills);
            String experience = extractText(this.empExp);
            String degree = extractText(this.empDegree);
            String password = extractText(this.empPassword);
            Employee employee = new Employee(fullName, email, phone, skills, degree, experience, null, null,"unconfirmed");
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                Toast.makeText(SignupActivity.this, "Registered Successful", Toast.LENGTH_SHORT)
                                        .show();
                                sendVerifyEmail();
                                updateUser(fullName);

                                loginEmployeeToRealm(employee);
                            } else {
                                Toast.makeText(SignupActivity.this, "could not registered .please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }


    private boolean isValidForm() {
        switch (getCurretnForm()) {
            case DRIVER:
                return isValid(driverCarType) &&
                        isValid(driverEmail) &&
                        isValid(driverFullName) &&
                        isValid(driverNationalID) &&
                        isValid(driverPassword) &&
                        isValid(driverPlateNumber) &&
                        isValid(driverPhone);
            case SHOP:
                return isValid(shopBranch) &&
                        isValid(shopEmail) &&
                        isValid(shopFullName) &&
                        isValid(shopPhone) &&
                        isValid(shopPassword);
            case EMPLOYEE:
                return  isValid(empEmail) &&
                        isValid(empPassword) &&
                        isValid(empDegree) &&
                        isValid(empExp) &&
                        isValid(empPhone) &&
                        isValid(empSkills) &&
                        isValid(empFullName);
        }
        return false;
    }

    private UserType getCurretnForm() {
        if (driverContainer.getVisibility() == View.VISIBLE) {
            return DRIVER;
        } else if (employeeContainer.getVisibility() == View.VISIBLE) {
            return EMPLOYEE;
        } else {
            return SHOP;
        }
    }


    private void show(View container) {
        driverContainer.setVisibility(View.GONE);
        shopContainer.setVisibility(View.GONE);
        employeeContainer.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    private void initiaseContainers() {
        driverContainer = findViewById(R.id.driverContainer);
        shopContainer = findViewById(R.id.repairShopContainer);
        employeeContainer = findViewById(R.id.employeeContainer);
    }

    private void initialseShopViews() {
        shopFullName = (EditText) findViewById(R.id.rsName);
        shopPassword = (EditText) findViewById(R.id.rsPassword);
        shopEmail = (EditText) findViewById(R.id.rsEmail);
        shopPhone = (EditText) findViewById(R.id.rsPhone);
        shopBranch = (EditText) findViewById(R.id.rsBranch);
    }

    private void initialseDriverViews() {
        driverFullName = (EditText) findViewById(R.id.driverFullname);
        driverPassword = (EditText) findViewById(R.id.driverPassword);
        driverNationalID = (EditText) findViewById(R.id.driverNationalID);
        driverEmail = (EditText) findViewById(R.id.driverEmail);
        driverPhone = (EditText) findViewById(R.id.driverPhone);
        driverCarType = (EditText) findViewById(R.id.driverCarType);
        driverPlateNumber = (EditText) findViewById(R.id.driverPlateNumber);
    }

    private void initialseEmployeeViews() {
        empFullName = (EditText) findViewById(R.id.empFullname);
        empPassword = (EditText) findViewById(R.id.empPassword);
        empEmail = (EditText) findViewById(R.id.empEmail);
        empPhone = (EditText) findViewById(R.id.empPhone);
        empSkills = (EditText) findViewById(R.id.empSkills);
        empDegree = (EditText) findViewById(R.id.empDegree);
        empExp = (EditText) findViewById(R.id.empExp);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
