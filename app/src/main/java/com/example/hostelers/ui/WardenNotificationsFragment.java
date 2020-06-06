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
import com.example.hostelers.backend.WardenNotificationsResult;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WardenNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WardenNotificationsFragment extends Fragment {

    public WardenNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment WardenNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WardenNotificationsFragment newInstance() {
        WardenNotificationsFragment fragment = new WardenNotificationsFragment();
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
        return inflater.inflate(R.layout.fragment_warden_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ListView notifications = view.findViewById(R.id.lv_warden_notifications);
        SharedPreferences preferences = getContext().getSharedPreferences("WardenUser", Context.MODE_PRIVATE);
        String id = preferences.getString("WardenId", null), hostelName = preferences.getString("HostelName", null), hostelLocation = preferences.getString("HostelLocation", null);
        WardenNotificationsViewModel viewModel = new WardenNotificationsViewModel();
        viewModel.setData(id, hostelName, hostelLocation);
        LiveData<ArrayList<WardenNotificationsResult>> liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<WardenNotificationsResult>>() {
            @Override
            public void onChanged(ArrayList<WardenNotificationsResult> wardenNotificationsResults) {
                WardenNotificationsListAdapter adapter = new WardenNotificationsListAdapter(getContext(), wardenNotificationsResults);
                notifications.setAdapter(adapter);
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
