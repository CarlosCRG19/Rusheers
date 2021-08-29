package com.hackmty.adapters;

import static com.hackmty.Controllers.ImagesController.openImage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hackmty.R;
import com.hackmty.models.User;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    // FIELDS
    private Context context;
    private List<ParseFile> notes;

    public NotesAdapter(Context context, List<ParseFile> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // VIEWHOLDER (implements an interface to handle clicks)
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // VIEWS
        private ImageView ivNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get image view from layout
            ivNote = itemView.findViewById(R.id.ivNote);
            // Create a listener that applies to the whole viewholder
            itemView.setOnClickListener(this);
        }

        // Connect note data to the view
        public void bind(ParseFile roomNote) {
            Glide.with(context).load(roomNote.getUrl()).into(ivNote);
        }

        // If the post is clicked, a detail view is launched in which the user can see specific details for this post
        @Override
        public void onClick(View v) {
                String photoUrl = notes.get(getAdapterPosition()).getUrl();
                openImage(photoUrl, context, ivNote);
            }
        }

}
