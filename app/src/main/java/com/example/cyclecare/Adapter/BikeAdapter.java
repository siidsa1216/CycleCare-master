package com.example.cyclecare.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.cyclecare.BikeProfileEdit;
import com.example.cyclecare.ConnectCycleCareActivity;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.Model.Notification;
import com.example.cyclecare.NotificationActivity;
import com.example.cyclecare.R;
import com.example.cyclecare.SelectCyccleCareActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the data associated with the clicked item

        holder.bikeName.setText(bike.getBikeName());
        holder.bikeBrand.setText(bike.getBikeBrand());
        holder.bikeModel.setText(bike.getBikeModel());
        // Set the initial visibility state
        if (isFragment) {
            holder.moreOption.setVisibility(View.GONE);
            holder.itemView.setVisibility(bike.getIsparked() ? View.GONE : View.VISIBLE);
        } else {
            holder.moreOption.setVisibility(View.VISIBLE);
        }

        holder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, bike.getBikeId());
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
                    Intent intent = new Intent(v.getContext(), SelectCyccleCareActivity.class);
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

    private void showPopupMenu(View view, String bikeId) {
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

                        // Create an Intent for BikeProfileEdit activity
                        Intent intent = new Intent(context, BikeProfileEdit.class);

                        // Pass the bikeId to BikeProfileEdit activity
                        intent.putExtra("bikeId", bikeId);

                        // Start the BikeProfileEdit activity
                        context.startActivity(intent);

                        return true;

                    case R.id.menu_delete:

                        deleteItem(bikeId);

                        return true;

                    default:
                        return false;
                }
            }
        });

        // Show the popup menu
        popupMenu.show();
    }

    private void deleteItem(String bikeId) {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this bike?");

        // Add positive button (yes)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the item if the user clicks "Yes"
                DatabaseReference bikeReference = FirebaseDatabase.getInstance().getReference("Bikes")
                        .child(firebaseUser.getUid()).child(bikeId);

                bikeReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        deleteAssociatedParkRecords(bikeId);
                        // Bike deleted successfully
                        notifyDataSetChanged();
                        Toast.makeText(context, "Bike deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle error
                        Toast.makeText(context, "Failed to delete bike", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Add negative button (no)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing if the user clicks "No"
            }
        });

        // Show the AlertDialog
        builder.show();

    }

    private void deleteAssociatedParkRecords(String bikeId) {
        DatabaseReference parkReference = FirebaseDatabase.getInstance().getReference("Park")
                .child(firebaseUser.getUid());

        Query query = parkReference.orderByChild("bikeId").equalTo(bikeId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
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
