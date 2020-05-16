package com.example.hostelers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.*;


public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        final EditText id = findViewById(R.id.etId), new_password = findViewById(R.id.etPwd), confirm_password = findViewById(R.id.etRe_enterPwd);
        Button submit = findViewById(R.id.btnSubmit);
        MyEditTextListener myTextListener = new MyEditTextListener();
        id.setOnEditorActionListener(myTextListener);
        new_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String new_password_text = new_password.getText().toString();
                if (new_password_text.isEmpty()) {
                    new_password.setError("Mandatory: Can't be Empty.");
                } else{
                    int pwd_len = new_password_text.length();
                    if(pwd_len < 8){
                        new_password.setError("Mandatory: The number of characters used must be at least 8 and less than 16");
                    } else{
                        if(!Pattern.matches("^[A-Z][\\w@!%$]{7,15}",new_password_text)){
                            new_password.setError("Mandatory:\nThe password must start with a Upper Case alphabet\nIt can contain only letters, digits and special characters like @, !, % and $");
                        }
                    }
                }
                return true;
            }
        });
        confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String confirm_pwd_text = confirm_password.getText().toString();
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (confirm_pwd_text.isEmpty()) {
                    confirm_password.setError("Mandatory: Can't be Empty.");
                }else{
                    if(!new_password.getText().toString().equals(confirm_pwd_text)){
                        confirm_password.setError("Mandatory: Password not matching!");
                    }
                }
                return true;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String id_text = id.getText().toString(), new_password_text = new_password.getText().toString(), confirm_pwd_text = confirm_password.getText().toString();
                if (id_text.isEmpty()) {
                    id.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }
                if (new_password_text.isEmpty()) {
                    new_password.setError("Mandatory: Can't be Empty.");
                    flag = false;
                } else{
                    int pwd_len = new_password_text.length();
                    if(pwd_len < 8){
                        new_password.setError("Mandatory: The number of characters used must be at least 8 and less than 16");
                        flag = false;
                    } else{
                        if(!Pattern.matches("^[A-Z][\\w@!%$]{7,15}",new_password_text)){
                            new_password.setError("Mandatory:\nThe password must with a Upper Case alphabet\nIt can contain only letters, digits and special characters like @, !, % and $");
                            flag = false;
                        }
                    }
                }
                if (confirm_pwd_text.isEmpty()) {
                    confirm_password.setError("Mandatory: Can't be Empty.");
                    flag = false;
                }else{
                    if(!new_password_text.equals(confirm_pwd_text)){
                        confirm_password.setError("Mandatory: Password not matching!");
                        flag = false;
                    }
                }
                if(flag){
                    Toast.makeText(getApplicationContext(), "Password Changed to " + new_password.getText().toString(), Toast.LENGTH_LONG).show();
                    finish(); // also helps us to go back to previous activity
                }
            }
        });
    }
}
