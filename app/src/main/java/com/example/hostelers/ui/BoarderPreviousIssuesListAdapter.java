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
import com.example.hostelers.backend.PreviousIssuesResult;

import java.util.ArrayList;

public class BoarderPreviousIssuesListAdapter extends ArrayAdapter<PreviousIssuesResult> {

    public BoarderPreviousIssuesListAdapter(Context context, ArrayList<PreviousIssuesResult> list){
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if(resultView == null){
            resultView = LayoutInflater.from(getContext()).inflate(R.layout.boarder_issues_list_item, parent, false);
        }

        PreviousIssuesResult item = getItem(position);
        TextView issueType = resultView.findViewById(R.id.issue_type), category = resultView.findViewById(R.id.issue_type_category), description = resultView.findViewById(R.id.issue_description_message);
        final String issue_text = item.getIssueType(), category_text = item.getTypeCategory(), description_text = item.getIssueDescription();
        issueType.setText(issue_text);
        category.setText(category_text);
        description.setText(description_text);

        View layoutView = resultView.findViewById(R.id.boarder_issues_item_layout);
        layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.boarder_issue_details_layout, null);
                ((TextView)dialogView.findViewById(R.id.boarder_issue_details_issue_type)).setText(issue_text);
                ((TextView)dialogView.findViewById(R.id.boarder_issue_details_type_category)).setText(category_text);
                ((TextView)dialogView.findViewById(R.id.boarder_issue_details_description)).setText(description_text);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Issue Details")
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
