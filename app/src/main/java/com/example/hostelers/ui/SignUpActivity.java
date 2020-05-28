package com.example.hostelers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.R;
import com.example.hostelers.backend.BoarderSignUpResult;
import com.example.hostelers.backend.RetrofitInterface;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private IdProofsFragment id_proof_fragment;
    private FormFragment formFragment;
    private RulesFragment rulesFragment;
    private PaymentFragment paymentFragment;
    private boolean isFormFilled, areIdsAttached, areRulesAgreed;
    private String father_name_text, b_name_text, email_txt, mobile, job, paymentMethod,BASE_URL = "http://10.0.2.2:3000";
    private Bitmap photo_img, proof_img;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public SignUpActivity(){
        formFragment = FormFragment.newInstance();
        rulesFragment = RulesFragment.newInstance();
        id_proof_fragment = IdProofsFragment.newInstance();
        paymentFragment = PaymentFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(this);
        setViewPagerAdapter(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViewPagerAdapter(ViewPager viewPager){
        SignUpViewAdapter adapter = new SignUpViewAdapter(getSupportFragmentManager());
        adapter.addFragment(formFragment, "Form");
        adapter.addFragment(rulesFragment, "Rules");
        adapter.addFragment(id_proof_fragment, "Id Proofs");
        adapter.addFragment(paymentFragment, "Payment");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View v = viewPager.getRootView();
        switch (position) {
            case 0:
                isFormFilled = verifyForm(v,false);
                break;
            case 1:
                areRulesAgreed = verifyRulesAgreement(v);
                break;
            case 2:
                areIdsAttached = verifyIdProofsAttachments(v);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        String title = "Note";
        final View v = viewPager.getRootView();
        switch (position){
            case 0:
                isFormFilled = verifyForm(v, true);
                if(!isFormFilled) {
                    openAlertDialog(title, "Mandatory Fields:\nYour Name\nFather Name\nEmail(must be valid Ex:abc@gmail.com)\nMobile number(must have 10 digits)");
                }
                break;
            case 1:
                areRulesAgreed = verifyRulesAgreement(v);
                if(!areRulesAgreed) {
                    openAlertDialog(title, "If you agree to the given rules.\nPlease click \"I agree to the rules\"");
                }
                break;
            case 2:
                areIdsAttached = verifyIdProofsAttachments(v);
                if(!areIdsAttached) {
                    openAlertDialog(title, "Please attach image files for photo and proof");
                }
                break;
            case 3:
                Button pay = v.findViewById(R.id.make_payment_button);
                if(!verifyPayment(v)) {
                    openAlertDialog(title, "Please select a hostel payment method!");
                }
                pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isFormFilled && areRulesAgreed && areIdsAttached && verifyPayment(v)){
                                sendDetails();
                                finish();
                            }
                            else{
                                openAlertDialog("Error", "Please fill all the mandatory fields in the form\nMake sure you agreed to rules and attach all the proofs\nSelect hostel payment method");
                            }
                        }
                    });
                break;
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        //empty override
    }

    private boolean verifyForm(View view, boolean isTabSelected){
        boolean flag = true;
        EditText b_name = view.findViewById(R.id.boarder_name), b_father_name = view.findViewById(R.id.father_name), email = view.findViewById(R.id.email), mobile_num = view.findViewById(R.id.mobile_number);
        Spinner job_spinner = view.findViewById(R.id.designation_spinner);
        b_name_text = b_name.getText().toString();
        father_name_text = b_father_name.getText().toString();
        email_txt = email.getText().toString();
        mobile = mobile_num.getText().toString();
        if(father_name_text.isEmpty()){
            if(isTabSelected)
                b_father_name.setError("Mandatory: Can't be empty");
            flag = false;
        }
        if(b_name_text.isEmpty()){
            if(isTabSelected)
                b_name.setError("Mandatory: Can't be empty");
            flag = false;
        }
        if(email_txt.isEmpty()){
            if(isTabSelected)
                email.setError("Mandatory: Can't be empty");
            flag = false;
        }else{
            if(!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@\\w{3,10}\\.com", email_txt)){
                if(isTabSelected)
                    email.setError("Invalid email address");
                flag = false;
            }
        }
        if(mobile.isEmpty()){
            if(isTabSelected)
                mobile_num.setError("Mandatory: Can't be empty");
            flag = false;
        }else{
            if(mobile.length() != 10){
                if(isTabSelected)
                    mobile_num.setError("Must have 10 digits!");
                flag = false;
            }
        }
        switch(job_spinner.getSelectedItemPosition()){
            case 0:
                job = "Student";
                break;
            case 1:
                job = "Employee";
                break;
            case 2:
                job = "Other";
                break;
        }
        return flag;
    }

    private boolean verifyRulesAgreement(View view){
        CheckBox rules_check = view.findViewById(R.id.rules_agreement_check);
        return rules_check.isChecked();
    }

    private boolean verifyIdProofsAttachments(View v){
        ImageView photo = v.findViewById(R.id.photo_image), proof = v.findViewById(R.id.id_image);
        photo.buildDrawingCache();
        photo_img = photo.getDrawingCache();
        proof.buildDrawingCache();
        proof_img = proof.getDrawingCache();
        return photo.getVisibility() == View.VISIBLE && proof.getVisibility() == View.VISIBLE;
    }

    private boolean verifyPayment(View view){
        RadioButton monthly = view.findViewById(R.id.hostel_pay_monthly), quarterly = view.findViewById(R.id.hostel_pay_quarterly), yearly = view.findViewById(R.id.hostel_pay_annual);
        if(monthly.isChecked()) {
            paymentMethod = "monthly";
            return true;
        }
        if(quarterly.isChecked()) {
            paymentMethod = "quarterly";
            return true;
        }
        if(yearly.isChecked()) {
            paymentMethod = "yearly";
            return true;
        }
        return false;
    }

    private void openAlertDialog(String title, String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title).setMessage(message).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //empty
            }
        }).show();
    }

    private void sendDetails(){
        Intent fromIntent = getIntent();
        HashMap<String, String> details = new HashMap<>();
        details.put("hostelName", fromIntent.getStringExtra("hostelName"));
        details.put("hostelLocation", fromIntent.getStringExtra("hostelLocation"));
        details.put("boarderName", b_name_text);
        details.put("fatherName", father_name_text);
        details.put("boarderJob", job);
        details.put("email", email_txt);
        details.put("mobileNumber", mobile);
        details.put("idProof", imageToString(proof_img));
        details.put("photo", imageToString(photo_img));
        details.put("paymentMethod", paymentMethod);
        Call<BoarderSignUpResult> call = retrofitInterface.executeBoarderSignUp(details);
        call.enqueue(new Callback<BoarderSignUpResult>() {
            @Override
            public void onResponse(Call<BoarderSignUpResult> call, Response<BoarderSignUpResult> response) {
                int response_code = response.code();
                if(response_code == 200) {
                    BoarderSignUpResult result = response.body();
                    sendMail(result);
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful! Please Check your mail box from Hostelers App", Toast.LENGTH_LONG).show();
                }
                else if(response_code == 409){
                    openAlertDialog("Error", "entry already exists!");
                }
            }

            @Override
            public void onFailure(Call<BoarderSignUpResult> call, Throwable t) {
                openAlertDialog("Error", "Connection to the Server Failed!");
            }
        });
    }

    private String imageToString(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] bytesData = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytesData, Base64.DEFAULT);
    }

    private void sendMail(BoarderSignUpResult result){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        String name = result.getBoarderName(), id = result.getBoarderId(), h_name = result.getHostelName(), w_name = result.getWardenName();
        String w_number = result.getWardenMobile(), w_email = result.getWardenEmail(), b_email = result.getBoarderEmail();
        String text = getString(R.string.boarder_signUp_email_text, name, id, h_name, w_name, w_number, w_email);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{b_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Welcome to Hostelers!");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
