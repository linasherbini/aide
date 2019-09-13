package com.driver.aid;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.driver.aid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText email;
    Button sendEmail;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        email=(EditText)findViewById(R.id.email);
        sendEmail=(Button)findViewById(R.id.sendEmail);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter user email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("resetting password...");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this,
                                            "reset password email sent, please check your email", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(ResetPasswordActivity.this,
                                            "Unable to send reset password email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
