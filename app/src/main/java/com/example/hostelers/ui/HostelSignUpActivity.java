package com.example.hostelers.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.backend.HostelSignUpResult;
import com.example.hostelers.backend.RetrofitInterface;
import com.example.hostelers.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HostelSignUpActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    private int IMAGE_REQUEST = 1;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_sign_up);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        final EditText hostelName = findViewById(R.id.hostel_name), hostelLocation = findViewById(R.id.location), hostelWarden = findViewById(R.id.warden_name);
        final EditText emailWarden = findViewById(R.id.warden_email_hostelSignUp), mobile = findViewById(R.id.warden_mobile_hostelSignup);
        final Button submit = findViewById(R.id.hostel_signUp_submit), uploadButton = findViewById(R.id.doc_upload_btn);
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
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@\\w{3,10}\\.com",email)){
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

        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getImageIntent.setType("image/*");
                getImageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                if(getImageIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(getImageIntent, IMAGE_REQUEST);
                }
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
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@\\w{3,10}\\.com",email)){
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
                if(imageBitmap == null){
                    Snackbar.make(uploadButton, "Please Upload the proof for Hostel", Snackbar.LENGTH_LONG).show();
                    flag = false;
                }
                else{
                    int size = (imageBitmap.getHeight()*imageBitmap.getWidth()*3)/1024;
                    if(size > 5000){
                        Snackbar.make(uploadButton, "The uploaded image size should be less than 150kb", Snackbar.LENGTH_LONG).show();
                        flag = false;
                    }
                }
                if (flag) {
                    HashMap<String, String> details = new HashMap<>();
                    details.put("hostelName",hostelName_text);
                    details.put("hostelLocation", hostelLocation_text);
                    details.put("wardenName", wardenName);
                    details.put("wardenEmail", email);
                    details.put("wardenNumber", mobile_num);
                    details.put("hostelDocument", imageToString());
                    Call<HostelSignUpResult> hostelDetails = retrofitInterface.executeHostelSignUp(details);
                    hostelDetails.enqueue(new Callback<HostelSignUpResult>() {
                        @Override
                        public void onResponse(Call<HostelSignUpResult> call, Response<HostelSignUpResult> response) {
                            int response_code = response.code();
                            if(response_code == 200){
                                HostelSignUpResult result = response.body();
                                Toast.makeText(getApplicationContext(), "SignUp Successful!\n Please check your mail for credentials.", Toast.LENGTH_LONG).show();
                                sendMail(result.getEmail(),result.getWardenId(), result.getWardenKey());
                                finish();
                            }
                            else if(response_code == 409){
                                openAlertDialog("User Exists!");
                            }
                        }

                        @Override
                        public void onFailure(Call<HostelSignUpResult> call, Throwable t) {
                            openAlertDialog("Request Error! Try Again.");
                        }
                    });
                }
            }
        });

    }

    private String imageToString(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,30, byteOutputStream);
        byte[] imagesBytes = byteOutputStream.toByteArray();
        return Base64.encodeToString(imagesBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri img = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), img);
                ((TextView)findViewById(R.id.hostel_doc_proof_tv)).setText(img.getLastPathSegment()+".jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openAlertDialog(String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(message).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        }).show();
    }

    private void sendMail(String email, String id, String key){
        String message = "Welcome to Hostelers!\nYour Warden ID: "+id+"\nYour Password: "+key+"\n Thank You for being a part of Hostelers!.";
        String emails[] = {email};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Hostelers Credentials");
        intent.putExtra(Intent.EXTRA_TEXT,message);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
