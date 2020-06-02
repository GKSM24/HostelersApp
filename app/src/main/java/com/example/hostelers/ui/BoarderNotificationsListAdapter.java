package com.example.hostelers.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.hostelers.R;
import com.example.hostelers.backend.BoarderNotificationItemResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoarderNotificationsListAdapter extends ArrayAdapter<BoarderNotificationItemResult> {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";
    boolean deleteResult = false;

    public BoarderNotificationsListAdapter(Context context, List<BoarderNotificationItemResult> list){
        super(context, 0, list);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null){
            resultView = LayoutInflater.from(getContext()).inflate(R.layout.notification_item_layout, parent, false);
        }

        BoarderNotificationItemResult item = getItem(position);
        TextView from_text_view = resultView.findViewById(R.id.from_person), message_text_view = resultView.findViewById(R.id.boarder_notification_message);
        final String person = "Warden", message = item.getMessage();
        from_text_view.setText(person);
        message_text_view.setText(message);

        View root_view = resultView.findViewById(R.id.notification_item_root_layout);
        root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.notification_dialog_layout, null);
                TextView msg_from = dialogView.findViewById(R.id.notification_dialog_from), msg = dialogView.findViewById(R.id.notification_dialog_message_content);
                msg_from.setText(person);
                msg.setText(message);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setView(dialogView)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //empty
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMessage(position);
                            }
                        }).show();
            }
        });

        ImageButton deleteButton = resultView.findViewById(R.id.delete_img_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(position);
            }
        });

        return resultView;
    }

    private boolean deleteMessage(final int position){
        SharedPreferences preferences = getContext().getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
        Call<Void> call = retrofitInterface.deleteBoarderNotification(preferences.getString("HostelName", null), preferences.getString("HostelLocation", null), preferences.getString("BoarderId", null), position);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    System.out.println("Message deleted!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Connection Failure");
            }
        });
        return deleteResult;
    }
}
