package com.hackmty.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.dragtoclose.DragToClose;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hackmty.R;
import com.hackmty.adapters.PageAdapter;
import com.hackmty.models.ClassRoom;
import com.hackmty.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class InRoomFragment extends Fragment {

    public static final String TAG = "InRoomActivity";
    public ClassRoom room;
    public ViewPager2 viewPager;
    Toolbar toolbar;
    TextView tvTitle, tvHost, tvUsers, tvDescription, tvLink;
    CardView cvTimer, cvList, cvChat, cvMusic;
    TabLayout tabLayout;
    DragToClose dragToClose;
    List<ParseUser> users;
    CircleIndicator3 indicator;

    public InRoomFragment(ClassRoom room) {
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
        tvTitle = view.findViewById(R.id.tvTitle);
        tvHost = view.findViewById(R.id.tvHost);
        tvUsers = view.findViewById(R.id.tvUsers);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvLink = view.findViewById(R.id.tvLink);
        cvTimer = view.findViewById(R.id.cvTimer);
        cvList = view.findViewById(R.id.cvList);
        cvChat = view.findViewById(R.id.cvChat);
        cvMusic = view.findViewById(R.id.cvMusic);
        viewPager = view.findViewById(R.id.viewPager);

        dragToClose = view.findViewById(R.id.dragToClose);

        users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser());
        //updateUsers();

        tvTitle.setText(room.getName());
        tvHost.setText("host: " + room.getHost().getUsername());
        HashSet<String> usernames = new HashSet<>();
        for (ParseUser user : room.getUsers())
        {
            usernames.add(user.getUsername());
        }
        tvUsers.setText("in room: " + usernames.toString());
        tvDescription.setText(room.getDescription());
        tvLink.setText(room.getMeetingUrl());

        PageAdapter pAdapter = new PageAdapter(getActivity(), room);
        viewPager.setAdapter(pAdapter);
        indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (TabLayoutMediator.TabConfigurationStrategy) (tab, position) ->
        {
            switch (position)
            {
                case 0:
                    tab.setIcon(R.drawable.ic_baseline_chat_bubble);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_baseline_timer);
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_baseline_format_list_bulleted);
                    break;
                case 3:
                    tab.setIcon(R.drawable.ic_baseline_music_note);
                    break;
            }
        }).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                dragToClose.openDraggableContainer();
                dragToClose.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
                if (dragToClose.getVisibility() == View.VISIBLE)
                {
                    dragToClose.closeDraggableContainer();
                    dragToClose.setVisibility(View.GONE);
                }
                else
                {
                    dragToClose.openDraggableContainer();
                    dragToClose.setVisibility(View.VISIBLE);
                }
            }
        });

        //cvTimer.setOnClickListener(this);
        //cvList.setOnClickListener(this);
        //cvChat.setOnClickListener(this);
        //cvMusic.setOnClickListener(this);
    }
}