package com.example.hostelers.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hostelers.R;
import com.example.hostelers.backend.HostelBoardersListItemResult;

import java.util.List;

public class BoarderListAdapter extends ArrayAdapter<HostelBoardersListItemResult> {

    public BoarderListAdapter(Context context, List<HostelBoardersListItemResult> list){
        super(context, 0, list);
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
                id_text_view.setText(id_text);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setView(dialogView)
                            .setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // send message to the server
                                    Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
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
}
