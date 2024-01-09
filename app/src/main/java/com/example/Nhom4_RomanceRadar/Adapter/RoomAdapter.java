package com.example.Nhom4_RomanceRadar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Nhom4_RomanceRadar.R;
import com.example.Nhom4_RomanceRadar.model.Room;
import com.example.Nhom4_RomanceRadar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private String loggedInUserId;

    public RoomAdapter(List<Room> roomList, String loggedInUserId) {
        this.roomList = roomList;
        this.loggedInUserId = loggedInUserId;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        String otherUserId = (loggedInUserId.equals(room.getUser1())) ? room.getUser2() : room.getUser1();
        loadUserName(otherUserId, holder.receiveName, holder.receiveImage);
        holder.receiveMess.setText(room.getLastMess());


    }

    private void loadUserName(String userId, TextView textView, ImageView imageView) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String displayName = user.displayName;
                        textView.setText(displayName);

                        if (user.photoUrl != null && !user.photoUrl.isEmpty()) {
                            Glide.with(imageView.getContext())
                                    .load(user.photoUrl)
                                    .into(imageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView receiveImage;
        TextView receiveName;
        TextView receiveMess;

        RoomViewHolder(View itemView) {
            super(itemView);
            receiveImage = itemView.findViewById(R.id.receiveImage);
            receiveName = itemView.findViewById(R.id.receiveName);
            receiveMess = itemView.findViewById(R.id.receiveMess);
        }
    }
}
