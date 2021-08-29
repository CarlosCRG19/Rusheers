package com.hackmty.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.hackmty.R;
import com.hackmty.models.ClassRoom;
import com.hackmty.models.SchoolClass;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.zeeshan.material.multiselectionspinner.MultiSelectionSpinner;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateRoomDialogFragment extends DialogFragment {

    interface CreateRoomDialogInterface {
        void updateAdapter();
    }

    public static final String TAG = "CreateActivity";
    public List<String> tags, allTags;
    List<String> createdTags, selectedTags;
    EditText etName, etDescription, etPasscode, etTags, etMusic, etMeetingUrl;
    TextView tvTitle, tvTags, tvRequired;
    Switch switchChat;
    Button btnCreate;
    MultiSelectionSpinner spinner;
    SchoolClass schoolClass;
    CreateRoomDialogInterface listener;

    public CreateRoomDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_room_dialog, container, false);
        schoolClass = getArguments().getParcelable( "schoolClass");

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener = (CreateRoomDialogInterface) getParentFragment();
        //allTags = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("allTags"));
        allTags = new ArrayList<>();
        allTags.add("Math");
        allTags.add("Fun");
        allTags.add("Languages");
        allTags.add("Fast");
        allTags.add("Slow");
        allTags.add("Begginers");
        allTags.add("Advanced");
        allTags.add("Friends");

        tags = new ArrayList<>();

        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        etPasscode = view.findViewById(R.id.etPassword);
        etTags = view.findViewById(R.id.etTags);
        //etMusic = view.findViewById(R.id.etMusic);
        etMeetingUrl = view.findViewById(R.id.etMeetingUrl);
        //tvTitle = view.findViewById(R.id.tvTitle);
        tvTags = view.findViewById(R.id.tvTags);
        //tvRequired = view.findViewById(R.id.tvRequired);
        switchChat = view.findViewById(R.id.switchChat);
        btnCreate = view.findViewById(R.id.btnCreate);
        spinner = view.findViewById(R.id.multiSelectSpinner);

        spinner.setItems(allTags);
        selectedTags = new ArrayList<>();
        spinner.setOnItemSelectedListener(new MultiSelectionSpinner.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position)
            {
                String selectedTag = allTags.get(position);
                Log.i(TAG, "item selected: " + selectedTag + "; isSelected: " + isSelected);
                if (isSelected && !selectedTags.contains(selectedTag))
                    selectedTags.add(selectedTag);
                else if (!isSelected && selectedTags.contains(selectedTag))
                    selectedTags.remove(selectedTag);
            }

            @Override
            public void onSelectionCleared()
            {
                spinner.clear();
                selectedTags.clear();
                spinner.setItems(allTags);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                String passcode = etPasscode.getText().toString();
                //String musicLink = etMusic.getText().toString();
                String meetingUrl = etMeetingUrl.getText().toString();;

                if (name.isEmpty() || description.isEmpty())
                {
                    Toast.makeText(getContext(), "required fields cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean chat = switchChat.isChecked();
                ParseUser currentUser = ParseUser.getCurrentUser();

                String tagsUnparsed = etTags.getText().toString(); // getting new created tags
                createdTags = new ArrayList<>(Arrays.asList(tagsUnparsed.toLowerCase().split("\\s*, \\s*")));
                for (String tag : allTags)
                    if (createdTags.contains(tag))
                        createdTags.remove(tag);
                Log.i(TAG, "createdTags: " + createdTags + "; selectedTags: " + selectedTags);
                tags.addAll(createdTags);
                tags.addAll(selectedTags);

                createRoom(name, description, passcode, chat, currentUser, tags, meetingUrl);
                //RoomsFragment roomsFragment = new RoomsFragment();
                //transaction.replace(R.id.flContainer, roomsFragment);
                //transaction.addToBackStack(null);
                //transaction.commit();
            }

            private void createRoom(String name, String description, String passcode, Boolean chat, ParseUser currentUser, List<String> tags, String meetingUrl)
            {
                ClassRoom room = new ClassRoom();
                room.setClasse(schoolClass);
                room.setHost(ParseUser.getCurrentUser());


                //room.add("users", ParseUser.getCurrentUser());

                List<ParseUser> users = new ArrayList<>();
                users.add(ParseUser.getCurrentUser());
                room.setUsers(users);

                room.setName(name);
                room.setDescription(description);
                room.setPasscode(passcode);
                room.setChatEnabled(chat);
                room.setHost(currentUser);
                room.setTags(tags);
                //room.setMusic(musicLink);
                room.setMeetingUrl(meetingUrl);
                room.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if (e != null)
                        {
                            Log.e(TAG, "issue in creating room!", e);
                            return;
                        }
                        Log.i(TAG, "room created successfully!");
                        listener.updateAdapter();
                        getDialog().dismiss();
                    }
                });
            }
        });
    }
}
