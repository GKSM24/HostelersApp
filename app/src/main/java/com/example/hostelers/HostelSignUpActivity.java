package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class HostelSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_sign_up);

        final EditText hostelName = findViewById(R.id.hostel_name), hostelLocation = findViewById(R.id.location), hostelWarden = findViewById(R.id.warden_name);
        final EditText emailWarden = findViewById(R.id.warden_email_hostelSignUp), mobile = findViewById(R.id.warden_mobile_hostelSignup);
        Button submit = findViewById(R.id.hostel_signUp_submit);
        MyEditTextListener myTextListener = new MyEditTextListener();
        hostelName.setOnEditorActionListener(myTextListener);
        hostelLocation.setOnEditorActionListener(myTextListener);
        hostelWarden.setOnEditorActionListener(myTextListener);
        emailWarden.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String email = emailWarden.getText().toString();
                if(email.isEmpty()){
                    emailWarden.setError("Mandatory: Can't be Empty.");
                }else{
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@[\\w.]{3,10}",email)){
                        emailWarden.setError("Mandatory: email invalid");
                    }
                }
                return true;
            }
        });
        mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String mobile_num = mobile.getText().toString();
                if(mobile_num.isEmpty()){
                    mobile.setError("Mandatory: Can't be Empty.");
                }else{
                    if (mobile_num.length() != 10){
                        mobile.setError("Mandatory: should have 10 digits");
                    }
                }
                return true;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                String hostelName_text = hostelName.getText().toString(), hostelLocation_text = hostelLocation.getText().toString(), wardenName = hostelWarden.getText().toString(), email = emailWarden.getText().toString(), mobile_num = mobile.getText().toString();
                if (hostelName_text.isEmpty()) {
                    hostelName.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (hostelLocation_text.isEmpty()) {
                    hostelLocation.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (wardenName.isEmpty()) {
                    hostelWarden.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (email.isEmpty()) {
                    emailWarden.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }else{
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@[\\w.]{4,20}",email)){
                        emailWarden.setError("Mandatory: email invalid");
                    }
                }
                if (mobile_num.isEmpty()) {
                    mobile.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }else{
                    if (mobile_num.length() != 10){
                        mobile.setError("Mandatory: should have 10 digits");
                    }
                }
                if (flag) {
                    Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
