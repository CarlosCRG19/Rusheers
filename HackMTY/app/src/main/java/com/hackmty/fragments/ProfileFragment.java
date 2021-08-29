package com.hackmty.fragments;

import static com.hackmty.Controllers.ImagesController.loadCircleImage;
import static com.hackmty.Controllers.ImagesController.openImage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackmty.LoginActivity;
import com.hackmty.R;
import com.hackmty.adapters.RoomsProfileAdapter;
import com.hackmty.models.ClassRoom;
import com.hackmty.models.User;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of displaying the profile of a user.
 */
public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private ParseUser user;
    private ImageView ivProfileImage;
    private TextView tvName, tvUserName, tvEmail;
    private RecyclerView rvItems;
    protected List<ClassRoom> allRooms;
    private RoomsProfileAdapter adapter;
    private FloatingActionButton fabEditProfile;
    private LottieAnimationView lottieLoadingItems;
    Button logoutBtn;
    public ProfileFragment(ParseUser user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get references
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvName = view.findViewById(R.id.tvName);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvEmail);
        logoutBtn = view.findViewById(R.id.btnLogout);

        rvItems = view.findViewById(R.id.rvItems);
        fabEditProfile = view.findViewById(R.id.fabEditProfile);
        lottieLoadingItems = view.findViewById(R.id.lottieLoadingItems);
        //Assign values
        loadCircleImage(getContext(), user.getParseFile(User.KEY_PFP), ivProfileImage);

        tvName.setText(user.getString(User.KEY_NAME));
        tvEmail.setText(user.getEmail());
        tvUserName.setText(user.getUsername());
        allRooms = new ArrayList<>();
        adapter = new RoomsProfileAdapter(getContext(), allRooms);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), 3));

        queryRooms();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        AppCompatActivity activity = (AppCompatActivity)getContext();
                        activity.finish();
                    }
                });
            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoUrl = user.getParseFile(User.KEY_PFP).getUrl();
                openImage(photoUrl, getContext(), ivProfileImage);
            }
        });

        if(user.equals(ParseUser.getCurrentUser()))
            fabEditProfile.setVisibility(FloatingActionButton.VISIBLE);
        else
            fabEditProfile.setVisibility(FloatingActionButton.GONE);

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity) getContext();
//                FragmentManager fm = activity.getSupportFragmentManager();
//                EditProfileDialogFragment editProfileDialogFragment = EditProfileDialogFragment.newInstance(user);
//                editProfileDialogFragment.show(fm,"fragment_rent_item");
            }
        });
    }

    /**
     * Query for getting the items of an specified user.
     */
    private void queryRooms() {
        // Specify which class to query
        ParseQuery<ClassRoom> query = ParseQuery.getQuery(ClassRoom.class);
        //the items created most recently will come first and the oldest ones will come last.
        query.addDescendingOrder(ClassRoom.KEY_CREATED_AT);
        query.whereEqualTo(ClassRoom.KEY_USERS, user);
        lottieLoadingItems.setVisibility(LottieAnimationView.VISIBLE);
        // Retrieve all the posts
        query.findInBackground(new FindCallback<ClassRoom>() {
            @Override
            public void done(List<ClassRoom> rooms, ParseException e) {
                if (e != null) {
                    return;
                }
                allRooms.addAll(rooms);
                adapter.notifyDataSetChanged();
                lottieLoadingItems.setVisibility(LottieAnimationView.GONE);
            }
        });
    }
}