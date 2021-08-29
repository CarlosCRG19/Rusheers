package com.hackmty.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackmty.MainActivity;
import com.hackmty.R;
import com.hackmty.fragments.InRoomFragment2;
import com.hackmty.models.ClassRoom;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    public static final String TAG = "RoomsAdapter";
    private final Context context;
    private final List<ClassRoom> rooms;

    public RoomsAdapter(Context context, List<ClassRoom> rooms){
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.ViewHolder holder, int position) {
        holder.bind(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvRoomName, tvHost, tvChatEnable, tvTags, tvDescription;
        EditText etPasscode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvHost = itemView.findViewById(R.id.tvHost);
            tvChatEnable = itemView.findViewById(R.id.tvChatEnable);
            tvTags = itemView.findViewById(R.id.tvTags);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(ClassRoom classRoom){
            tvRoomName.setText(classRoom.getName());
            tvHost.setText(classRoom.getHost().getUsername());
            String chatEnable = classRoom.getChatEnabled()?"enable":"unable";
            tvChatEnable.setText(chatEnable);
            tvTags.setText(classRoom.getTags().toString());
            tvDescription.setText(classRoom.getDescription());
        }

        @Override
        public void onClick(View view) {
            // grab position; null check; pass in as Parcel wrapped extra
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) // position exists :)
            {
                ClassRoom room = rooms.get(position);
                Log.i(TAG,"onClick room: " + room.getName());

                if (room.getPasscode().isEmpty() || room.getUsers().contains(ParseUser.getCurrentUser())) {
                    openRoom(room);
                }
                else
                {
                    View editDialog = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
                    etPasscode = editDialog.findViewById(R.id.etDialog);

                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("enter passcode")
                            .setMessage("room is private! please enter a passcode")
                            .setView(editDialog)
                            .setPositiveButton("submit", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    String passcode = etPasscode.getText().toString();
                                    if (passcode.equals(room.getPasscode())) {
                                        openRoom(room);
                                    }
                                    else
                                        Toast.makeText(context, "passcode incorrect!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        }
    }

    public void openRoom(ClassRoom room) {
        ParseUser user = ParseUser.getCurrentUser();
        List<ParseUser> users = room.getUsers();

        if(room.getHost() != user && !users.contains(user)) {
                users.add(ParseUser.getCurrentUser());
                room.setUsers(users);
                room.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });
        }

        InRoomFragment2 inRoomFragment = new InRoomFragment2();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ClassRoom.TAG, room);
        inRoomFragment.setArguments(bundle);
        ((MainActivity)context)
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer, inRoomFragment)
                .addToBackStack(null)
                .commit();
    }
}
