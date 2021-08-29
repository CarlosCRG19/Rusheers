package com.hackmty.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackmty.R;
import com.hackmty.adapters.NotesAdapter;
import com.hackmty.models.ClassRoom;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    public static final String TAG = "NotesFragment"; // TAG for log messages

    // Views
    TextView tvRoomName;
    RecyclerView rvNotes;
    FloatingActionButton fbTakePhoto;

    // Helpers
    SwipeRefreshLayout swipeContainer;

    // Model to store photos
    List<ParseFile> roomNotes;
    NotesAdapter adapter;

    // Room for these notes
    ClassRoom room;

    // Layout manager
    GridLayoutManager gridLayoutManager;

    // Empty constructor
    public NotesFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get room from arguments
        room = getArguments().getParcelable(ClassRoom.TAG);

        // Declare array that will hold notes and its adapter
        roomNotes = new ArrayList<>();
        adapter = new NotesAdapter(getContext(), roomNotes);

        // Set views from layout
        setViews(view);
        // Code for specific views
        tvRoomName.setText(room.getName());

        // Setup Recycler view
        rvNotes = view.findViewById(R.id.rvNotes);
        rvNotes.setAdapter(adapter);
        // Set layout manager on RV
        gridLayoutManager = new GridLayoutManager(getContext(), 3); // use 3 columns for grid
        rvNotes.setLayoutManager(gridLayoutManager);
        // Get notes for the room
        if(room.getNotes() != null) {
            roomNotes.addAll(room.getNotes());
            adapter.notifyDataSetChanged();
        }

        setListeners();
        setRefreshFeature(view);
    }

    // Lets the user refresh the profile posts swiping down on the RV
    private void setRefreshFeature(View view) {
        // Get view from layout
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Make query to get newest
                roomNotes.clear();
                roomNotes.addAll(room.getNotes());
                adapter.notifyDataSetChanged();
                // Hide refreshing icon
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setViews(View view) {
        tvRoomName = view.findViewById(R.id.tvRoomName);
        fbTakePhoto = view.findViewById(R.id.fbTakePhoto);
    }

    private void setListeners() {
        fbTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create bundle to pass room
                Bundle bundle = new Bundle();
                bundle.putParcelable(ClassRoom.TAG , room);
                // Create new instance of fragment
                ComposeFragment fragment = new ComposeFragment();
                fragment.setArguments(bundle);
                // Launch fragment
                fragment.show(getChildFragmentManager(), ComposeFragment.TAG);
            }
        });

    }


}
