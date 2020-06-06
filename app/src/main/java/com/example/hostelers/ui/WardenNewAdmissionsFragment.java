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
import android.widget.ListView;

import com.example.hostelers.R;
import com.example.hostelers.backend.AdmissionsResult;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardenNewAdmissionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardenNewAdmissionsFragment extends Fragment {

    public WardenNewAdmissionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment WardenNewAdmissionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WardenNewAdmissionsFragment newInstance() {
        WardenNewAdmissionsFragment fragment = new WardenNewAdmissionsFragment();
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
        return inflater.inflate(R.layout.fragment_warden_new_admissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ListView admissionsList = view.findViewById(R.id.lv_warden_new_admissions);
        AdmissionsDataFetchViewModel dataFetchViewModel = new AdmissionsDataFetchViewModel();
        SharedPreferences preferences = getActivity().getSharedPreferences("WardenUser", Context.MODE_PRIVATE);
        String hName = preferences.getString("HostelName", null), hLocation = preferences.getString("HostelLocation", null), wId = preferences.getString("WardenId", null);
        dataFetchViewModel.setData(hName, hLocation, wId);
        LiveData<ArrayList<AdmissionsResult>> admissionsData = dataFetchViewModel.getData();
        admissionsData.observe(getViewLifecycleOwner(), new Observer<ArrayList<AdmissionsResult>>() {
            @Override
            public void onChanged(ArrayList<AdmissionsResult> admissionsResults) {
                AdmissionsListAdapter adapter = new AdmissionsListAdapter(getContext(), admissionsResults);
                admissionsList.setAdapter(adapter);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
