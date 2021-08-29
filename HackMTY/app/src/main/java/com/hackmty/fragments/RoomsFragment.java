package com.hackmty.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hackmty.R;
import com.hackmty.models.Classe;
import com.hackmty.models.ClassRoom;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {

    private static final String TAG = "RoomsFragment";
    private static final String CLASS_PARAM = "class";

    private Classe classe;

    private List<ClassRoom> rooms;

    public RoomsFragment() {
        // Required empty public constructor
    }

    public static RoomsFragment newInstance(Classe classe) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putParcelable(CLASS_PARAM, classe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classe = getArguments().getParcelable(CLASS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rooms = new ArrayList<>();

        queryRooms();
    }

    private void queryRooms(){
        ParseQuery<ClassRoom> query = ParseQuery.getQuery(ClassRoom.class);
        query.whereEqualTo(ClassRoom.KEY_CLASS, classe);
        query.findInBackground(new FindCallback<ClassRoom>() {
            @Override
            public void done(List<ClassRoom> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while getting the rooms");
                    return;
                }
                rooms.clear();
                rooms.addAll(objects);
            }
        });
    }
}