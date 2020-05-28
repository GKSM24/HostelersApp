package com.example.hostelers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.hostelers.R;
import com.google.android.material.tabs.TabLayout;

import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment {


    public FormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment FormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText b_name = view.findViewById(R.id.boarder_name), b_father_name = view.findViewById(R.id.father_name), b_email = view.findViewById(R.id.email), b_mobile = view.findViewById(R.id.mobile_number);
        b_name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean onChange){
                if(b_name.getText().toString().isEmpty())
                    b_name.setError("Mandatory: Can't be Empty.");
            }
        });
        b_father_name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean onChange){
                if(b_father_name.getText().toString().isEmpty())
                    b_father_name.setError("Mandatory: Can't be Empty.");
            }
        });
        b_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = b_email.getText().toString();
                if(email.isEmpty()){
                    b_email.setError("Mandatory: Can't be Empty.");
                }else{
                    if (!Pattern.matches("^[\\w$^*&#!][\\w$^*&#!.@]{0,63}@\\w{3,10}\\.com",email)){
                        b_email.setError("Mandatory: email invalid");
                    }
                }
            }
        });
        b_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String mobile_num = b_mobile.getText().toString();
                if(mobile_num.isEmpty()){
                    b_mobile.setError("Mandatory: Can't be Empty.");
                }else{
                    if (mobile_num.length() != 10){
                        b_mobile.setError("Mandatory: should have 10 digits");
                    }
                }
            }
        });
    }
}