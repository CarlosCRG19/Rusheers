package com.hackmty.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.dragtoclose.DragToClose;
import com.google.android.material.tabs.TabLayout;
import com.hackmty.R;
import com.hackmty.models.Room;
import com.hackmty.models.User;

import java.util.List;

public class InRoomFragment extends Fragment {

    public static final String TAG = "InRoomActivity";
    public Room room;
    public ViewPager2 viewPager;
    Toolbar toolbar;
    TextView tvTitle, tvHost, tvUsers, tvDescription, tvLink;
    CardView cvTimer, cvList, cvChat, cvMusic;
    TabLayout tabLayout;
    DragToClose dragToClose;
    List<User> users;

    public InRoomFragment(Room room) {
        this.room = room;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_in_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = viewPager.findViewById(R.id.tvTitle);
        tvHost = viewPager.findViewById(R.id.tvHost);
        tvUsers = viewPager.findViewById(R.id.tvUsers);
        tvDescription = viewPager.findViewById(R.id.tvDescription);
        tvLink = viewPager.findViewById(R.id.tvLink);
        cvTimer = viewPager.findViewById(R.id.cvTimer);
        cvList = viewPager.findViewById(R.id.cvList);
        cvChat = viewPager.findViewById(R.id.cvChat);
        cvMusic = viewPager.findViewById(R.id.cvMusic);
        viewPager = viewPager.findViewById(R.id.viewPager);
        dragToClose = viewPager.findViewById(R.id.dragToClose);
    }
}