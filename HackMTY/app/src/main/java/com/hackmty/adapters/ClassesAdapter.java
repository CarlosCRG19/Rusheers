package com.hackmty.adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackmty.R;
import com.hackmty.models.Classe;

import java.util.List;

import okio.BufferedSink;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {

    // FIELDS
    private Context context;
    private List<Classe> classes;

    // Empty Constructor
    public ClassesAdapter(Context context, List<Classe> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesAdapter.ViewHolder holder, int position) {
        // Bind class to vh
        //holder.bind((Classe) classes.get(position));
        Log.i("TEST", classes.get(position).getName());
    }

    @Override
    public int getItemCount() { return classes.size(); } // Size of classes array



    // OTHER METHODS

    // Add an entire array of classes
    /*public void addAll(List<Classe> classesList) {
        classes.addAll(classesList);
        notifyDataSetChanged();
    }*/

    // Remove all classes from the adapter
    public void clear() {
        classes.clear();
        notifyDataSetChanged();
    }

    // VIEW HOLDER

    // Class responsible of binding each classes data with their respective row
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Specific class
        private Classe classe;
        // Views from layout
        private ImageView ivClass;
        private TextView tvClassName, tvClassCode, tvProfessors;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get views from layout
            ivClass = itemView.findViewById(R.id.ivClass);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassCode = itemView.findViewById(R.id.tvClassCode);
            tvProfessors = itemView.findViewById(R.id.tvProfessors);
            // Set click listener
            itemView.setOnClickListener(this);
        }

        // Populates the views with the data from the class
        public void bind(Classe classToBind) {
            // Set class of member variable
            classe = classToBind;
            // Set info inside views
            tvClassName.setText(classToBind.getName());
            tvClassCode.setText(classToBind.getCode());
            List<String> professors = classToBind.getProfessors();
            String professorsString = "";
            for(String professor:professors) {
                professorsString += professor + ", ";
            }
            tvProfessors.setText(professorsString.substring(0, professorsString.length() - 1));
        }

        @Override
        public void onClick(View v) {
            // Create new bundle
            Bundle bundle = new Bundle();
            bundle.putParcelable(Classe.TAG, classe);
        }
    }
}
