package com.driver.aid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.driver.aid.signup.SignupActivity;
import com.driver.aid.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    LinearLayout choice;
    TextView choiceT, signIn;
    Button driverButton, empButton, rsButton;

    public static Intent newIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choice = (LinearLayout) findViewById(R.id.choice);
        choiceT = (TextView) findViewById(R.id.choiceT);
        signIn = (TextView) findViewById(R.id.signIn);
        driverButton = (Button) findViewById(R.id.driverButton);
        rsButton = (Button) findViewById(R.id.rsButton);
        empButton = (Button) findViewById(R.id.empButton);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            login(choice);
        }

    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        startActivity(SignupActivity.newIntent(this));

    }
}
