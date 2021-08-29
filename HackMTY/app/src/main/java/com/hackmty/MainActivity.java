package com.hackmty;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hackmty.fragments.ClassesFragment;
import com.hackmty.fragments.CreateRoomDialogFragment;
import com.hackmty.fragments.ProfileFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity"; // TAG for log messages

    // Navigation component
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Object responsible of adding, removing or replacing Fragments in the stack
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Assign bottom navigation bar from layout
        bottomNavigationView = findViewById(R.id.bnvMenu);

        // Handle fragment changes with listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                // Check which item was selected using ids
                if (item.getItemId() == R.id.action_main) {
                    fragment = new ClassesFragment();
                } else {
                    fragment = new ProfileFragment(ParseUser.getCurrentUser());
                }
                // Change to selected fragment using manager
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Use first element as default
        bottomNavigationView.setSelectedItemId(R.id.action_main);
    }


    //-- OTHER METHODS --//

    public void showBottomNavBar() {
        // Change visibility of toolbar
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavBar() {
        // Change visibility of toolbar
        bottomNavigationView.setVisibility(View.GONE);
    }

}