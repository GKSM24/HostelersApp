package com.example.hostelers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardenNotifyBoarderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardenNotifyBoarderFragment extends Fragment {


    public WardenNotifyBoarderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment WardenNotifyBoarderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WardenNotifyBoarderFragment newInstance() {
        WardenNotifyBoarderFragment fragment = new WardenNotifyBoarderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_warden_notify_boarder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        final EditText bid = view.findViewById(R.id.et_notify_boarder_id), msg = view.findViewById(R.id.et_notify_msg);
        MyEditTextListener listener = new MyEditTextListener();
        bid.setOnEditorActionListener(listener);
        msg.setOnEditorActionListener(listener);
        Button sendMsg = view.findViewById(R.id.btn_send_msg);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String boarder_id = bid.getText().toString(), message = msg.getText().toString();
                if(boarder_id.isEmpty()){
                    bid.setError("Mandatory: Can't be empty. Enter Boarder Id");
                    flag = false;
                }
                if(boarder_id.isEmpty()){
                    msg.setText(R.string.generic_message_text);
                }
                if(flag){
                    Toast.makeText(getContext(), "Message Sent!", Toast.LENGTH_LONG).show();
                    refreshScreen(view);
                }
            }
        });

    }
    private void refreshScreen(View view){
        EditText bid = view.findViewById(R.id.et_notify_boarder_id), msg = view.findViewById(R.id.et_notify_msg);
        bid.setText("");
        msg.setText("");
    }
}
