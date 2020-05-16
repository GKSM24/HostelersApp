package com.example.hostelers;

import android.view.KeyEvent;
import android.widget.TextView;

public class MyEditTextListener implements TextView.OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v.getText().toString().isEmpty()){
            v.setError("Mandatory: Can't be Empty.");
        }
        return true;
    }
}
