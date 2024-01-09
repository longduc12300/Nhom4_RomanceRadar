package com.example.Nhom4_RomanceRadar.helper;

import androidx.annotation.NonNull;

import com.example.Nhom4_RomanceRadar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {

    private DatabaseReference mDatabase;

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void getUsers(final OnGetDataListener listener) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    listener.onSuccess(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.toException());
            }
        });
    }

    public void addLikedUser(String currentUserId, String likedUserId) {
        DatabaseReference userReference = mDatabase.child(likedUserId).child("likedUsers");
        userReference.child(currentUserId).setValue(true);
        DatabaseReference userReference2 = mDatabase.child(currentUserId).child("likedHistory");
        userReference2.child(likedUserId).setValue(true);
    }

    public void removedLikedUser(String currentUserId, String likedUserId) {
        DatabaseReference userReference = mDatabase.child(currentUserId).child("likedUsers");
        userReference.child(likedUserId).removeValue();
        DatabaseReference userReference2 = mDatabase.child(currentUserId).child("likedHistory");
        userReference2.child(likedUserId).setValue(true);

    }

    public void addDislikedUser(String currentUserId, String dislikedUserId) {
        DatabaseReference userReference = mDatabase.child(currentUserId).child("dislikedUsers");
        userReference.child(dislikedUserId).setValue(true);
    }

    public void addLikedHistory(String currentUserId, String likedUserId) {
        DatabaseReference userReference = mDatabase.child(currentUserId).child("likedHistory");
        userReference.child(likedUserId).setValue(true);
    }


    public void getLikedUsers(String userId, final OnGetListListener listener) {
        DatabaseReference likedUsersRef = mDatabase.child(userId).child("likedUsers");
        likedUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> likedUsers;
                likedUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    likedUsers.add(snapshot.getKey());
                }
                listener.onGetList(likedUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getDislikedUsers(String userId, final OnGetListListener listener) {
        DatabaseReference dislikedUsersRef = mDatabase.child(userId).child("dislikedUsers");
        dislikedUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> dislikedUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    dislikedUsers.add(snapshot.getKey());
                }
                listener.onGetList(dislikedUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getLikedHistory(String userId, final OnGetListListener listener) {
        DatabaseReference likedHistoryRef = mDatabase.child(userId).child("likedHistory");
        likedHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> likedHistory = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    likedHistory.add(snapshot.getKey());
                }
                listener.onGetList(likedHistory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface OnGetDataListener {
        void onSuccess(User user);
        void onFailure(Exception e);
    }

    public interface OnGetListListener {
        void onGetList(List<String> list);
        void onFailure(Exception e);
    }
}