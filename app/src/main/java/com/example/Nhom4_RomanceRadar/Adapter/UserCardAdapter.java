package com.example.Nhom4_RomanceRadar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Nhom4_RomanceRadar.R;
import com.example.Nhom4_RomanceRadar.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.ViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;


    public UserCardAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.nameTextView.setText(user.displayName);
        holder.ageTextView.setText(String.valueOf(calculateAge(user.birth)));
        holder.bioTextView.setText(user.bio);

        Glide.with(context)
                .load(user.photoUrl)
                .into(holder.profileImage);
    }

    private int calculateAge(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(birthDate);
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.setTime(date);
            int birthYear = calendar.get(Calendar.YEAR);

            int age = currentYear - birthYear;
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (currentMonth > calendar.get(Calendar.MONTH) + 1 || (currentMonth == calendar.get(Calendar.MONTH) + 1 && currentDay >= calendar.get(Calendar.DAY_OF_MONTH))) {
                return age;
            } else {
                return age - 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<User> getUserList() {
        return userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameTextView, ageTextView, bioTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.item_name);
            ageTextView = itemView.findViewById(R.id.item_age);
            bioTextView = itemView.findViewById(R.id.item_bio);
        }
    }
}