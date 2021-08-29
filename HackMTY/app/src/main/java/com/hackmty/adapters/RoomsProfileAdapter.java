package com.hackmty.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hackmty.MainActivity;
import com.hackmty.R;
import com.hackmty.fragments.InRoomFragment;
import com.hackmty.fragments.InRoomFragment2;
import com.hackmty.fragments.RoomsFragment;
import com.hackmty.models.ClassRoom;
import com.parse.ParseFile;

import java.util.List;

/**
 * This adapter is implemented by the RecyclerView of the user's item in their profile.
 * It is responsible for displaying all the items of a user in images.
 */
public class RoomsProfileAdapter extends RecyclerView.Adapter<RoomsProfileAdapter.ViewHolder> {

    private Context context;
    private List<ClassRoom> rooms;

    public RoomsProfileAdapter(Context context, List<ClassRoom> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsProfileAdapter.ViewHolder holder, int position) {
        ClassRoom room = rooms.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    /**
     * Clean all elements of the recycler view.
     */
    public void clear() {
        rooms.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        public void bind(ClassRoom room) {
            //Bind the post data to the view elements
            ParseFile image = room.getClasse().getImage();
            //condition to check if there is an image attached
            if (image != null) {
                RequestOptions mediaOptions = new RequestOptions();
                mediaOptions = mediaOptions.transforms(new CenterCrop(), new RoundedCorners(20));
                Glide.with(context).load(image.getUrl()).apply(mediaOptions).into(ivImage);
            }
        }

        /**
         * OnClickListener of each item. It opens the PostDetailsFragment with the details of the post clicked.
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                InRoomFragment2 inRoomFragment = new InRoomFragment2();
                Bundle bundle = new Bundle();
                bundle.putParcelable(ClassRoom.TAG, rooms.get(position));
                inRoomFragment.setArguments(bundle);
                ((MainActivity)context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContainer, inRoomFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}
