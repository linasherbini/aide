package com.driver.aid.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.driver.aid.FormValidator;
import com.driver.aid.Model.Driver;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.RealmResults;

import static com.driver.aid.FormValidator.extractText;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getName();
    private RealmResults<Driver> result;
    private FirebaseUser user;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
    }

    private EditText  driverFullName, driverEmail, driverPhone, driverPlateNumber, driverCarType, driverNationalID, oldPassword,newPassword,emailPassword;
    private Button saveProfile,changePasword,updateEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        driverFullName = (EditText) view.findViewById(R.id.driverFullname);
        oldPassword= (EditText) view.findViewById(R.id.oldPassword);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        driverNationalID = (EditText) view.findViewById(R.id.driverNationalID);
        driverEmail = (EditText) view.findViewById(R.id.driverEmail);
        driverPhone = (EditText) view.findViewById(R.id.driverPhone);
        driverCarType = (EditText) view.findViewById(R.id.driverCarType);
        driverPlateNumber = (EditText) view.findViewById(R.id.driverPlateNumber);
        saveProfile = view.findViewById(R.id.save_button);
        changePasword= view.findViewById(R.id.change_password);
        emailPassword= view.findViewById(R.id.currentPasswordForEmailChange);
        updateEmail= view.findViewById(R.id.change_email);
        result = RealmRemoteManager.getInstance().getCurrentDriver(this::bindView);

        saveProfile.setOnClickListener(v -> {

            RealmRemoteManager.getInstance().updateDriverInfo(
                    extractText(driverFullName),
                    extractText(driverNationalID),
                    extractText(driverPhone),
                    extractText(driverCarType),
                    extractText(driverPlateNumber)
                    );

            Toast.makeText(getContext(),"updated!",Toast.LENGTH_SHORT).show();
        });
        updateEmail.setOnClickListener(v->{

            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), extractText(emailPassword));
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updateEmail(extractText(driverEmail))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    RealmRemoteManager.getInstance().updateDriverEmail(extractText(driverEmail));
                                                    Toast.makeText(getContext(),"Email updated successfully!",Toast.LENGTH_SHORT).show();

                                                } else {
                                                    if(task.getException()!=null){

                                                        Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getContext(),"unable to change Email!",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }
                                        });


                            } else {
                                Toast.makeText(getContext(),"current password is not correct!",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Error auth failed");
                            }
                        }
                    });
        });
        changePasword.setOnClickListener(v->{
            if(FormValidator.isValid(newPassword)&&FormValidator.isValid(oldPassword)){

                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), extractText(oldPassword));
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(extractText(newPassword)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),"Password updated!",Toast.LENGTH_SHORT).show();
                                                oldPassword.setText("");
                                                newPassword.setText("");
                                                Log.d(TAG, "Password updated");
                                            } else {
                                                if(task.getException()!=null){

                                                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getContext(),"unable to change password!",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(),"old password is not correct!",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Error auth failed");
                                }
                            }
                        });
            }

        });
    }

    private void bindView(Driver driver) {
        driverFullName.setText(driver.getFullName());
        driverNationalID.setText(driver.getNationalID());
        driverEmail.setText(driver.getEmail());
        driverPhone.setText(driver.getPhone());
        driverCarType.setText(driver.getCarType());
        driverPlateNumber.setText(driver.getPlateNumber());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        result=null;
    }
}
