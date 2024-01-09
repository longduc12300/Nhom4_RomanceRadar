package com.example.Nhom4_RomanceRadar.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Nhom4_RomanceRadar.R;
import com.example.Nhom4_RomanceRadar.Adapter.UserCardAdapter;
import com.example.Nhom4_RomanceRadar.databinding.FragmentHomeBinding;
import com.example.Nhom4_RomanceRadar.helper.FirebaseHelper;
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

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    UserCardAdapter userCardAdapter;
    List<User> userList = new ArrayList<>();

    boolean dataLoaded = false;
    String currentId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentId = user.getUid();

        RecyclerView rvcUser = root.findViewById(R.id.rcvUser);
        userCardAdapter = new UserCardAdapter(getContext(), userList);
        rvcUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rvcUser.setAdapter(userCardAdapter);
        rvcUser.setNestedScrollingEnabled(false);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && !user.uid.equals(currentId)) {
                        checkUserPreferences(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                User user = userCardAdapter.getUserList().get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    dislikeUser(user);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    likeUser(user);
                }

                userCardAdapter.getUserList().remove(position);
                userCardAdapter.notifyItemRemoved(position);


            }

        });

        helper.attachToRecyclerView(rvcUser);



        return root;
    }

    private void dislikeUser(User user) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addDislikedUser(currentId, user.uid);
    }

    private void likeUser(User user) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addLikedUser(currentId, user.uid);
    }

    private void checkUserPreferences(User user) {
        DatabaseReference currentdislikedUsersRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentId).child("dislikedUsers");
        DatabaseReference currentLikedHistoryRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(currentId).child("likedHistory");

        currentdislikedUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                if (!dataSnapshot.hasChild(user.uid)) {
                    currentLikedHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(user.uid)) {
                                userList.add(user);
                                userCardAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}