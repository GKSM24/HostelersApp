package com.example.hostelers.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hostelers.R;
import com.example.hostelers.backend.HostelBoardersListItemResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoarderListAdapter extends ArrayAdapter<HostelBoardersListItemResult> {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";

    public BoarderListAdapter(Context context, List<HostelBoardersListItemResult> list){
        super(context, 0, list);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null){
            resultView = LayoutInflater.from(getContext()).inflate(R.layout.notify_boarder_list_item_layout, parent, false);
        }

        HostelBoardersListItemResult item = getItem(position);

        TextView id = resultView.findViewById(R.id.boarder_id_notify), name = resultView.findViewById(R.id.boarder_name_notify);
        final String id_text = item.getId();
        id.setText(id_text.substring(id_text.length()-8));
        name.setText(item.getName());

        Button notifyButton = resultView.findViewById(R.id.notifyBtn);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.boarder_notify_dialog_layout, null);
                TextView id_text_view = dialogView.findViewById(R.id.tv_notify_boarder_id);
                final EditText message_edit_text = dialogView.findViewById(R.id.et_notify_msg);
                id_text_view.setText(id_text);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setView(dialogView)
                            .setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendMessage(id_text, message_edit_text.getText().toString());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //empty
                                }
                            }).show();

            }
        });

        return resultView;
    }

    private void sendMessage(String boarderId, String message){
        SharedPreferences preferences = getContext().getSharedPreferences("WardenUser", Context.MODE_PRIVATE);
        String hostelName = preferences.getString("HostelName", null), hostelLocation = preferences.getString("HostelLocation", null);
        HashMap<String, String> notificationDetails = new HashMap<>();
        notificationDetails.put("boarderId", boarderId);
        notificationDetails.put("hostelName", hostelName);
        notificationDetails.put("hostelLocation", hostelLocation);
        notificationDetails.put("message", message);
        Call<Void> call = retrofitInterface.executeNotifyBoarder(notificationDetails);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int response_code = response.code();
                if(response_code == 200){
                    Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            }
        });
    }
}
