package com.hackmty.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackmty.Controllers.ImagesController;
import com.hackmty.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;


import java.util.List;

// Class to populate the User search on create screen
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    // FIELDS
    private Context context;
    private List<ParseUser> users;

    // CONSTRUCTOR
    public UserAdapter(Context context, List<ParseUser> parseUsers) {
        this.context = context;
        this.users = parseUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.member_layout, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Add an entire array of hours
    public void addAll(List<ParseUser> userList) {
        users.addAll(userList);
        notifyDataSetChanged();
    }

    // Removes all businesses from the adapter
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {

        public static final String USER_TAG = "user"; // tag to pass user

        // VIEWS
        private ImageView ivUserImage;
        // USER FOR BINDING
        private ParseUser user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get views from layout
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            // Set click listener to go to the users profile
        }

        // Connect hour data to the views
        public void bind(ParseUser userToBind) {
            // Assign user value
            user = userToBind;

            // Get profile picture
            ParseFile profileImage = null;
            try {
                profileImage = (ParseFile) user.fetchIfNeeded().get("profilePic");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Load Profile Picture using static method
            ImagesController.loadCircleImage(context, profileImage, ivUserImage);

        }
    }

}
