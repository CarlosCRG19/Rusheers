package com.hackmty.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackmty.R;
import com.hackmty.RoomsAdapter;
import com.hackmty.models.ClassRoom;
import com.hackmty.models.SchoolClass;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {

    private static final String TAG = "RoomsFragment";
    private static final String CLASS_PARAM = "class";

    private SchoolClass schoolClass;

    private List<ClassRoom> rooms;
    private RoomsAdapter adapter;

    private RecyclerView rvRooms;
    private ImageView ivClassImage;
    private FloatingActionButton fb;

    public RoomsFragment() {
        // Required empty public constructor
    }

    public static RoomsFragment newInstance(SchoolClass classe) {
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
            schoolClass = getArguments().getParcelable(CLASS_PARAM);
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
        adapter = new RoomsAdapter(getContext(), rooms);

        rvRooms = view.findViewById(R.id.rvRooms);
        ivClassImage = view.findViewById(R.id.ivClassImage);
        fb = view.findViewById(R.id.fb);

        rvRooms.setAdapter(adapter);
        rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        queryRooms();

        setUpOnClickListeners();
    }

    private void setUpOnClickListeners(){
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putParcelable("schoolClass", schoolClass);
                CreateRoomDialogFragment createRoomDialogFragment = new CreateRoomDialogFragment();
                createRoomDialogFragment.setArguments(bundle);
                createRoomDialogFragment.show(fm,"");
            }
        });
    }

    private void queryRooms(){
        ParseQuery<ClassRoom> query = ParseQuery.getQuery(ClassRoom.class);
        query.whereEqualTo(ClassRoom.KEY_CLASS, schoolClass);
        query.findInBackground(new FindCallback<ClassRoom>() {
            @Override
            public void done(List<ClassRoom> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while getting the rooms");
                    return;
                }
                rooms.clear();
                rooms.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}