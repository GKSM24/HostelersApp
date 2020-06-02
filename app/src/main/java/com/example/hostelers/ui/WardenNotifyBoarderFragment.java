package com.example.hostelers.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelers.R;
import com.example.hostelers.backend.HostelBoardersListItemResult;

import java.util.ArrayList;
import java.util.List;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ListView boardersList = view.findViewById(R.id.boarders_list);
        SearchView boarderSearch = view.findViewById(R.id.boarder_search);
        final DataFetchViewModel fetchViewModel = new DataFetchViewModel();
        SharedPreferences preferences = requireActivity().getSharedPreferences("WardenUser", Context.MODE_PRIVATE);
        fetchViewModel.setData(preferences.getString("HostelName", null), preferences.getString("HostelLocation", null));
        final LiveData<List<HostelBoardersListItemResult>> listLiveData = fetchViewModel.getData();
        listLiveData.observe(getViewLifecycleOwner(), new Observer<List<HostelBoardersListItemResult>>() {
            @Override
            public void onChanged(List<HostelBoardersListItemResult> hostelBoardersListItemResults) {
                BoarderListAdapter adapter = new BoarderListAdapter(getContext(), hostelBoardersListItemResults);
                boardersList.setAdapter(adapter);
            }
        });
        boarderSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchViewModel.executeSearchData(newText);
                return false;
            }
        });
    }
}
