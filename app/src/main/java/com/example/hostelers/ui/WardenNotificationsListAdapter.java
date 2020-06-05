package com.example.hostelers.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.hostelers.R;
import com.example.hostelers.backend.WardenNotificationsResult;

import java.util.ArrayList;

public class WardenNotificationsListAdapter extends ArrayAdapter<WardenNotificationsResult> {

    public WardenNotificationsListAdapter(Context context, ArrayList<WardenNotificationsResult> list){
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null){
            resultView = LayoutInflater.from(getContext()).inflate(R.layout.warden_notifications_list_item, parent, false);
        }

        WardenNotificationsResult item = getItem(position);

        TextView b_id_view = resultView.findViewById(R.id.warden_notification_boarder_id), b_issue_type = resultView.findViewById(R.id.warden_notification_issue_type), b_issue_category = resultView.findViewById(R.id.warden_notification_issue_type_category);
        TextView descriptionView = resultView.findViewById(R.id.warden_notification_issue_description_message);
        final String bId = item.getBoarderId(), issue = item.getIssueType(), category =  item.getTypeCategory(), description = item.getIssueDescription(), roomNumber = item.getRoomNumber();
        b_id_view.setText(bId); b_issue_type.setText(issue); descriptionView.setText(description);
        b_issue_category.setText(category);

        View itemLayoutView = resultView.findViewById(R.id.warden_notification_item_layout);
        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.warden_notification_details, null);
                ((TextView)dialogView.findViewById(R.id.warden_notification_details_boarder_id)).setText(bId);
                ((TextView)dialogView.findViewById(R.id.warden_notification_details_issue_type)).setText(issue);
                ((TextView)dialogView.findViewById(R.id.warden_notification_details_type_category)).setText(category);
                ((TextView)dialogView.findViewById(R.id.warden_notification_details_description)).setText(description);
                ((TextView)dialogView.findViewById(R.id.warden_notification_details_boarder_room)).setText(roomNumber);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Notification Details")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //empty
                            }
                        }).setView(dialogView).show();
            }
        });

        return resultView;
    }

}
