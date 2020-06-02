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
import com.example.hostelers.backend.BoarderNotificationItemResult;
import com.example.hostelers.backend.HostelBoardersListItemResult;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoarderNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoarderNotificationsFragment extends Fragment {

    public BoarderNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BoarderNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoarderNotificationsFragment newInstance() {
        return new BoarderNotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boarder_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ListView boarder_notifications = view.findViewById(R.id.boarder_notifications_list);
        final BoarderNotificationsDataFetchViewModel notificationsFetchViewModel = new BoarderNotificationsDataFetchViewModel();
        SharedPreferences preferences = requireActivity().getSharedPreferences("BoarderUser", Context.MODE_PRIVATE);
        notificationsFetchViewModel.setData(preferences.getString("HostelName", null), preferences.getString("HostelLocation", null), preferences.getString("BoarderId", null));
        final LiveData<List<BoarderNotificationItemResult>> listLiveData = notificationsFetchViewModel.getData();
        listLiveData.observe(getViewLifecycleOwner(), new Observer<List<BoarderNotificationItemResult>>() {
            @Override
            public void onChanged(List<BoarderNotificationItemResult> boarderNotifications) {
                BoarderNotificationsListAdapter adapter = new BoarderNotificationsListAdapter(getContext(), boarderNotifications);
                boarder_notifications.setAdapter(adapter);
            }
        });
    }
}
