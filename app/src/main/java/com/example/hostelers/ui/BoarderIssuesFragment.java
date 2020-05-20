package com.example.hostelers.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hostelers.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoarderIssuesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoarderIssuesFragment extends Fragment {

    public BoarderIssuesFragment() {
        // Required empty public constructor
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
}
