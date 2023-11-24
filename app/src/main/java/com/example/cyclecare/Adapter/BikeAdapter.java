package com.example.cyclecare.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyclecare.BikeProfileActivity;
import com.example.cyclecare.ConnectCycleCareActivity;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    Context context;
    List<Bike> bikeList;
    boolean isFragment;

    FirebaseUser firebaseUser;


    public BikeAdapter(Context context, List<Bike> bikeList, boolean isFragment) {
        this.context = context;
        this.bikeList = bikeList;
        this.isFragment = isFragment;
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


        // Set the initial visibility state
        if (isFragment) {
            holder.moreOption.setVisibility(View.GONE);
        } else {
            holder.moreOption.setVisibility(View.VISIBLE);
        }

        holder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFragment){
                    // Save parkId to SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("bikeId", bike.getBikeId());
                    editor.apply();

                    // Open a new activity and pass the data
                    Intent intent = new Intent(v.getContext(), ConnectCycleCareActivity.class);
                    intent.putExtra("bikeId", bike.getBikeId());
                    intent.putExtra("bikeName", bike.getBikeName());
                    intent.putExtra("bikeDesc", bike.getBikeBrand());
                    v.getContext().startActivity(intent);

                }
                else {
                    // Save parkId to SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("bikeId", bike.getBikeId());
                    editor.apply();

                    // Open a new activity and pass the data
                    Intent intent = new Intent(v.getContext(), BikeProfileActivity.class);
                    v.getContext().startActivity(intent);


                }
            }
        });


    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.options, popupMenu.getMenu());

        // Set item click listeners
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_edit:

                        // Handle edit action
                        // Implement your edit logic here
                        return true;

                    case R.id.menu_delete:
                        // Handle delete action
                        // Implement your delete logic here
                        return true;

                    default:
                        return false;
                }
            }
        });

        // Show the popup menu
        popupMenu.show();
    }


    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView bikeName, bikeModel, bikeBrand;
        Button moreOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardItem);
            bikeName = itemView.findViewById(R.id.bikeNametxt);
            bikeModel = itemView.findViewById(R.id.bikeModeltxt);
            bikeBrand = itemView.findViewById(R.id.bikeBrandtxt);
            moreOption = itemView.findViewById(R.id.moreOption);
        }
    }
}
