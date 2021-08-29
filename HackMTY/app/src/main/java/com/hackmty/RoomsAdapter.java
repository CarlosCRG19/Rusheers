package com.hackmty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackmty.models.ClassRoom;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

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

        private TextView tvRoomName;
        private TextView tvHost;
        private TextView tvChatEnable;
        private TextView tvTags;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

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

        }
    }
}
