package com.example.Nhom4_RomanceRadar.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Nhom4_RomanceRadar.R;
import com.example.Nhom4_RomanceRadar.Adapter.UserCardAdapter;
import com.example.Nhom4_RomanceRadar.databinding.FragmentDashboardBinding;
import com.example.Nhom4_RomanceRadar.helper.FirebaseHelper;
import com.example.Nhom4_RomanceRadar.model.Message;
import com.example.Nhom4_RomanceRadar.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LikeListFragment extends Fragment {

    private FragmentDashboardBinding binding;

    UserCardAdapter userCardAdapter;
    List<User> userList = new ArrayList<>();

    boolean dataLoaded = false;
    String currentId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
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

        DatabaseReference likedUsersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentId).child("likedUsers");
        likedUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userList.clear();
                    String uid = snapshot.getKey();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {

                            User user = userDataSnapshot.getValue(User.class);
                            if (user != null) {
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
                    showInputDialog(user);
                }

                userCardAdapter.getUserList().remove(position);
                userCardAdapter.notifyItemRemoved(position);


            }

        });

        helper.attachToRecyclerView(rvcUser);




        return root;
    }

    private void showInputDialog(User user) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.match_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        EditText inputEditText = dialogView.findViewById(R.id.edtMatchMess);
        ImageButton sendButton = dialogView.findViewById(R.id.btnMatchSend);

        AlertDialog dialog = builder.create();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    removedLikedUser(user);
                    sendMessageToFirebase(user, message);
                    dialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

        dialog.setCanceledOnTouchOutside(false);
    }

    private void sendMessageToFirebase(User user, String messageContent) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        String currentUserId = currentUser.getUid();
        String receiverId = user.uid;

        Message message = new Message();
        message.setSenderId(currentUserId);
        message.setReceiverId(receiverId);
        message.setContent(messageContent);
        message.setTimestamp(System.currentTimeMillis());


        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");
        messagesRef.child(generateRoomId(currentUserId, receiverId)).push().setValue(message);
        messagesRef.child(generateRoomId(currentUserId, receiverId)).child("user1").setValue(currentUserId);
        messagesRef.child(generateRoomId(currentUserId, receiverId)).child("user2").setValue(receiverId);
        messagesRef.child(generateRoomId(currentUserId, receiverId)).child("lastMess").setValue(messageContent);


    }

    private String generateRoomId(String userId1, String userId2) {
        String[] sortedUserIds = {userId1, userId2};
        Arrays.sort(sortedUserIds);
        return sortedUserIds[0] + "_" + sortedUserIds[1];
    }

    private void removedLikedUser(User user) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.removedLikedUser(currentId, user.uid);
    }



    private void dislikeUser(User user) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addDislikedUser(currentId, user.uid);
    }

    private void likeUser(User user) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addLikedUser(currentId, user.uid);
    }

    private void addLikedHistory(User user) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addLikedUser(currentId, user.uid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}