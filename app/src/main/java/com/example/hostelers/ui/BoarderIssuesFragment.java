package com.example.hostelers.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hostelers.R;
import com.example.hostelers.backend.PreviousIssuesResult;
import com.example.hostelers.backend.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoarderIssuesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoarderIssuesFragment extends Fragment {

    public BoarderIssuesFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BoarderIssuesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoarderIssuesFragment newInstance() {
        return new BoarderIssuesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boarder_issues, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Spinner spinnerIssueType = view.findViewById(R.id.spinner_boarder_issues_types), spinnerIssueTypeCategory = view.findViewById(R.id.spinner_boarder_type_category);
        final EditText issueMessage = view.findViewById(R.id.issue_description);
        Button submit = view.findViewById(R.id.boarder_issues_submit_btn);
        final ListView previousIssuesList = view.findViewById(R.id.boarder_previous_issues_list_view);
        SharedPreferences preferences = getActivity().getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
        String id = preferences.getString("BoarderId", null), hostelName = preferences.getString("HostelName", null), hostelLocation = preferences.getString("HostelLocation", null);
        BoarderPreviousIssuesViewModel viewModel = new BoarderPreviousIssuesViewModel();
        viewModel.setData(id, hostelName, hostelLocation);
        LiveData<ArrayList<PreviousIssuesResult>> listData = viewModel.getData();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int issuesTypePos = spinnerIssueType.getSelectedItemPosition(), typeCategoryPosition = spinnerIssueTypeCategory.getSelectedItemPosition();
              String message = issueMessage.getText().toString(), issueType="", typeCategory="";
              switch (issuesTypePos){
                  case 0:
                      issueType = "Room Issue";
                      break;
                  case 1:
                      issueType = "Personal Issue";
                      break;

              }
              switch (typeCategoryPosition){
                  case 0:
                      typeCategory = "Electrical";
                      break;
                  case 1:
                      typeCategory = "Water";
                      break;
                  case 2:
                      typeCategory = "Carpentry";
                      break;
                  case 3:
                      typeCategory = "Submit Leave Letter";
                      break;
                  case 4:
                      typeCategory = "Health Problem";
                      break;
              }
              notifyWarden(issueType, typeCategory, message);
              spinnerIssueType.setSelection(0);
              spinnerIssueTypeCategory.setSelection(0);
              issueMessage.setText("");
            }
        });
        listData.observe(getViewLifecycleOwner(), new Observer<ArrayList<PreviousIssuesResult>>() {
            @Override
            public void onChanged(ArrayList<PreviousIssuesResult> previousIssuesResults) {
                BoarderPreviousIssuesListAdapter adapter = new BoarderPreviousIssuesListAdapter(getContext(), previousIssuesResults);
                previousIssuesList.setAdapter(adapter);
            }
        });
    }

    private void notifyWarden(String issue, String category, String msg){
        SharedPreferences preferences = getActivity().getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
        String id = preferences.getString("BoarderId", null), hostelName = preferences.getString("HostelName", null), hostelLocation = preferences.getString("HostelLocation", null);
        String BASE_URL = "http://10.0.2.2:3000";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();;
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> details = new HashMap<>();
        details.put("boarderId", id);
        details.put("hostelName", hostelName);
        details.put("hostelLocation", hostelLocation);
        details.put("issueType", issue);
        details.put("typeCategory", category);
        details.put("issueDescription", msg);
        Call<Void> call = retrofitInterface.createBoarderIssue(details);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(getContext(), "Issue Sent", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Connect Failed", Toast.LENGTH_LONG).show();
            }
        });

    }
}
