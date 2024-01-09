package com.example.Nhom4_RomanceRadar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.Nhom4_RomanceRadar.R;
import com.example.Nhom4_RomanceRadar.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private TextView displayNameTextView, emailTextView, bioTextView, birthTextView;
    private ImageView profileImageView, image1, image2, image3;
    private ImageButton editButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        displayNameTextView = view.findViewById(R.id.display_name_text_view);
        emailTextView = view.findViewById(R.id.email_text_view);
        bioTextView = view.findViewById(R.id.bio_text_view);
        birthTextView = view.findViewById(R.id.birth_text_view);
        profileImageView = view.findViewById(R.id.profile_image_view);
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        editButton = view.findViewById(R.id.edit_button);


        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Client.client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            loadUserInfo(user.getUid());
        }
    }

    private void loadUserInfo(String user) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User userInfo = snapshot.getValue(User.class);

                    if (userInfo != null) {
                        displayNameTextView.setText(userInfo.displayName);
                        emailTextView.setText("Email: " + userInfo.email);
                        Glide.with(requireContext())
                                .load(userInfo.photoUrl)
                                .into(profileImageView);
                        bioTextView.setText(userInfo.bio);
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedBirthDate = "";
                        try {
                            Date date = inputFormat.parse(userInfo.birth);
                            formattedBirthDate = outputFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        birthTextView.setText("Ngày sinh: " + formattedBirthDate);
                        Glide.with(requireContext())
                                .load(userInfo.image1)
                                .into(image1);
                        Glide.with(requireContext())
                                .load(userInfo.image2)
                                .into(image2);
                        Glide.with(requireContext())
                                .load(userInfo.image3)
                                .into(image3);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}