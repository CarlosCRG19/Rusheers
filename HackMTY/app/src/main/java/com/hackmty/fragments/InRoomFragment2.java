package com.hackmty.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackmty.MainActivity;
import com.hackmty.R;
import com.hackmty.adapters.UserAdapter;
import com.hackmty.models.ClassRoom;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;

public class InRoomFragment2 extends Fragment {

    // VIEWS
    private TextView tvRoomName, tvDescription, tvRoomUrl, tvMembers;
    private FloatingActionButton fbTimer;
    private RecyclerView rvMembers;
    private FloatingActionButton fbChat;
    private FrameLayout infoContainer;

    // Variables for members RV
    private List<ParseUser> members;
    private UserAdapter adapter;

    // Room related with this fragment
    ClassRoom room;

    // Empty Constructor
    public InRoomFragment2() {};
    public InRoomFragment2(ClassRoom room) {
        this.room = room;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_in_room_2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get room from arguments
        room = (ClassRoom) getArguments().get(ClassRoom.TAG);

        // Set views
        setViews(view);
        // Populate view
        populateViews();
        setUpOnClickListeners();

        // RV setup
        members = room.getMembers();
        adapter = new UserAdapter(getContext(), members);
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMembers.setAdapter(adapter);

        fbTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoContainer.setVisibility(View.VISIBLE);
                TimerFragment timerFragment = new TimerFragment(room, getActivity());

                getChildFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.bottom_up_fragment, R.anim.bottom_down_fragment)
                        .replace(R.id.optionContainer, timerFragment)
                        .commit();
            }
        });
    }

    // VIEW METHODS

    private void setViews(View view) {
        // Room views
        tvRoomName = view.findViewById(R.id.tvRoomName);
        tvRoomUrl = view.findViewById(R.id.tvRoomUrl);
        tvMembers = view.findViewById(R.id.tvMembers);
        tvDescription = view.findViewById(R.id.tvDescription);
        // Members recycler view
        rvMembers = view.findViewById(R.id.rvMembers);
        fbChat = view.findViewById(R.id.fbChat);
        fbTimer = view.findViewById(R.id.fbTimer);
        infoContainer = view.findViewById(R.id.optionContainer);
    }

    private void populateViews() {
        // Room views
        tvRoomName.setText(room.getName());
        tvDescription.setText(room.getDescription());
        tvRoomUrl.setText(room.getMeetingUrl());
        // Members views
        String membersSize = String.valueOf(room.getMembers().size());
        tvMembers.setText(membersSize);
    }

    private void setUpOnClickListeners(){
        fbChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
    }

    private void openChat(){
        ChatFragment chatFragment = ChatFragment.newInstance(room);

        ((MainActivity)getContext())
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContainer, chatFragment)
                .addToBackStack(null)
                .commit();
    }
}

