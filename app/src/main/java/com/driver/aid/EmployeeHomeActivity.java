package com.driver.aid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.driver.aid.Model.Employee;
import com.driver.aid.alarts.AlertsActivity;
import com.driver.aid.signup.SignupActivity;
import com.driver.aid.R;

public class EmployeeHomeActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context,EmployeeHomeActivity.class);
    }
    TextView name,email,skills,expereince,confirmationStatus,confirmMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_home_screen);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        skills=findViewById(R.id.skills);
        expereince=findViewById(R.id.experience);
        confirmationStatus=findViewById(R.id.status);
        confirmMessage=findViewById(R.id.confirmationMessage);
        Employee employee = LoggedInUserManager.getInstance().getEmployee();

        name.setText(employee.getEmpFullName());
        email.setText(employee.getEmpEmail());
        skills.setText(employee.getEmpSkills());
        expereince.setText(employee.getEmpExp());

        if("confirmed".equals(employee.getStatus())){
            confirmationStatus.setText("Confirmed");
            confirmMessage.setText(R.string.statusConfirmedMessage);
        }else{
            confirmationStatus.setText("unconfirmed");
            confirmMessage.setText(R.string.statusUnConfirmedMessage);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.home_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout){
            RealmRemoteManager.getInstance().logout();
            LoggedInUserManager.getInstance().logOut();
            startActivity(SplashActivity.newIntent(this));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
