package com.hackmty.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_room, container, false);
    }
}