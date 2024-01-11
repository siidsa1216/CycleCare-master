package com.example.cyclecare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyclecare.Model.User;
import com.example.cyclecare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    Context context;
    List<User> userList;



    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList= userList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);

        holder.userName.setText(user.getUsername());
        holder.phoneNumber.setText(user.getPhoneNum());
        holder.status.setText(user.getUsertype());
        holder.accountStatus.setText(user.getStatus());

        try {
            Picasso.get().load(user.getProfilePicUrl()).placeholder(R.drawable.logo).into(holder.user_image);
        }catch (Exception e)
        {
            holder.user_image.setImageResource(R.drawable.logo);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CircleImageView user_image;
        TextView userName, phoneNumber, status, accountStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.userCardItem);
            user_image = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.userNametxt);
            phoneNumber = itemView.findViewById(R.id.phoneNumbertxt);
            status = itemView.findViewById(R.id.statustxt);
            accountStatus = itemView.findViewById(R.id.accountStatus);
        }
    }

}


