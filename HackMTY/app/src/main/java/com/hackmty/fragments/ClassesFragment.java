package com.hackmty.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hackmty.MainActivity;
import com.hackmty.R;
import com.hackmty.adapters.ClassesAdapter;
import com.hackmty.models.SchoolClass;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ClassesFragment extends Fragment {

    public static final String TAG = "ClassesFragment";

    // VIEWS
    private RecyclerView rvClasses;

    // Model to store the student's classes
    private List<SchoolClass> classes;
    // Adapter to control rows
    private ClassesAdapter adapter;
    Toolbar toolbar;
    SearchView searchView;

    // Empty constructor
    public ClassesFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).showBottomNavBar();

        // Init class list and adapter
        classes = new ArrayList<>();
        adapter = new ClassesAdapter(getContext(), classes);

        // Get recycler view
        rvClasses = view.findViewById(R.id.rvClasses);
        toolbar = view.findViewById(R.id.toolbar);
        rvClasses.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClasses.setAdapter(adapter);

        // Get classes from database
        queryClasses();
        toolbar.inflateMenu(R.menu.menu_top);
        toolbar.setTitle("Buscar");
        Menu menu = toolbar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Busca por nombre o codigo!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (classes != null) {
                }
                return true;
            }
        });
    }

    // Makes a query to parse DataBase and gets the classes in which the student is listed
    private void queryClasses() {
        // Specify which class we want to query
        ParseQuery<SchoolClass> query = ParseQuery.getQuery(SchoolClass.class);
        query.findInBackground(new FindCallback<SchoolClass>() {
            @Override
            public void done(List<SchoolClass> objects, ParseException e) {
                // Check if classes were found
                if (e != null) {
                    Log.e(TAG, "No classes found");
                }
                // Clear list of classes
                //adapter.clear();
                // Add classes to adapter
                //adapter.addAll(objects);

                classes.clear();
                classes.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
