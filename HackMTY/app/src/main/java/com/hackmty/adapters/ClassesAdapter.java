package com.hackmty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hackmty.Controllers.ImagesController;
import com.hackmty.MainActivity;
import com.hackmty.R;
import com.hackmty.fragments.RoomsFragment;
import com.hackmty.models.SchoolClass;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {

    // FIELDS
    private Context context;
    private List<SchoolClass> classes;

    // Empty Constructor
    public ClassesAdapter(Context context, List<SchoolClass> classes) {
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
        holder.bind(classes.get(position));
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
        private SchoolClass classe;
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
        public void bind(SchoolClass classToBind) {
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
            tvProfessors.setText(professorsString.substring(0, professorsString.length() - 2));
            Glide.with(context)
                    .load(classToBind.getImage().getUrl())
                    .into(ivClass);
        }

        @Override
        public void onClick(View v) {

            // Hide navigation view
            ((MainActivity) context).hideBottomNavBar();

            // Create new bundle
            //Bundle bundle = new Bundle();
            //bundle.putParcelable(Classe.TAG, classe);

            RoomsFragment roomsFragment = RoomsFragment.newInstance(classe);
            ((MainActivity)context)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, roomsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
