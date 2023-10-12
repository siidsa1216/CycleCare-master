package com.example.cyclecare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cyclecare.ConnectCycleCareActivity;
import com.example.cyclecare.Fragments.BikeProfileFragment;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    Context context;
    List<Bike> bikeList;
    boolean isFragment;

    FirebaseUser firebaseUser;

    public BikeAdapter(Context context, List<Bike> bikeList) {
        this.context = context;
        this.bikeList = bikeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bike_profile_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Bike bike = bikeList.get(position);
        // Get the data associated with the clicked item

        holder.bikeName.setText(bike.getBikeName());
        holder.bikeBrand.setText(bike.getBikeBrand());
        holder.bikeModel.setText(bike.getBikeModel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data associated with the clicked item
                String bikeId= bike.getBikeId();
                String bikeName = bike.getBikeName();
                String bikeDesc = bike.getBikeName();
                String bikePicUrl= bike.getBikeImg();



                // Open a new activity and pass the data
                Intent intent = new Intent(v.getContext(), ConnectCycleCareActivity.class);
                intent.putExtra("bikeId", bikeId);
                intent.putExtra("bikeName", bikeName);
                intent.putExtra("bikeDesc", bikeDesc);
                intent.putExtra("itemPhotoUrl", bikePicUrl);
                v.getContext().startActivity(intent);

//                //direct to connect to cyclecare activity
//                Intent intent = new Intent(context, ConnectCycleCareActivity.class);
//                // If you want to pass data to the new activity, you can use putExtra
//                intent.putExtra("bikeId", bike.getBikeId()); // Replace "bikeId" with the actual data key
//                // Add other putExtra statements for additional data
//
//                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
       return bikeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView bikeName, bikeModel, bikeBrand;
        Button parkBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardItem);
            bikeName = itemView.findViewById(R.id.bikeNametxt);
            bikeModel = itemView.findViewById(R.id.bikeModeltxt);
            bikeBrand = itemView.findViewById(R.id.bikeBrandtxt);
            parkBtn = itemView.findViewById(R.id.parkBtn);
        }
    }
}
