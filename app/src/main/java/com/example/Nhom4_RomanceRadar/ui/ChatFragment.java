package com.example.Nhom4_RomanceRadar.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Nhom4_RomanceRadar.Adapter.RoomAdapter;
import com.example.Nhom4_RomanceRadar.Adapter.UserCardAdapter;
import com.example.Nhom4_RomanceRadar.R;
import com.example.Nhom4_RomanceRadar.databinding.FragmentNotificationsBinding;
import com.example.Nhom4_RomanceRadar.model.Room;
import com.example.Nhom4_RomanceRadar.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    RoomAdapter roomAdapter;
    List<Room> roomList = new ArrayList<>();

    String currentId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentId = user.getUid();


        RecyclerView rcvRoom = root.findViewById(R.id.rcvRoom);
        roomAdapter = new RoomAdapter(roomList, currentId);
        rcvRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvRoom.setAdapter(roomAdapter);
        rcvRoom.setNestedScrollingEnabled(false);

        loadRooms();

        return root;
    }

    private void loadRooms() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();

                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    Room room = roomSnapshot.getValue(Room.class);
                    if (room != null) {
                        roomList.add(room);
                    }
                }

                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}