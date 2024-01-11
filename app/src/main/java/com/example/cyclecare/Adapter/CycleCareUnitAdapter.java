package com.example.cyclecare.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyclecare.BikeProfileEdit;
import com.example.cyclecare.ConnectCycleCareActivity;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.Model.CycleCare;
import com.example.cyclecare.QRActivity;
import com.example.cyclecare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CycleCareUnitAdapter extends RecyclerView.Adapter<CycleCareUnitAdapter.ViewHolder>{

    Context context;
    List<CycleCare> cycleCareList;
    boolean isFragment;

    public CycleCareUnitAdapter(Context context, List<CycleCare> cycleCareList, Boolean isFragment) {
        this.context = context;
        this.cycleCareList= cycleCareList;
        this.isFragment = isFragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cyclecare_item, parent, false);
        return new CycleCareUnitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CycleCare cycleCare = cycleCareList.get(position);

        holder.unitName.setText(cycleCare.getUnitName());
        holder.status.setText(cycleCare.getUnitNumber());

        if (isFragment) {
            holder.moreOptions.setVisibility(View.VISIBLE);
        } else {
            holder.moreOptions.setVisibility(View.GONE);
        }

        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, cycleCare.getUnitID());
            }
        });



        DatabaseReference cycleCareReference = FirebaseDatabase.getInstance().getReference("CycleCareUnit")
                .child(cycleCare.getUnitID());

        cycleCareReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CycleCare cycleCareData = snapshot.getValue(CycleCare.class);
                    if (cycleCareData != null && cycleCareData.getStatus().equals("occupied")) {
                        // If the status is occupied, hide the item
                        holder.itemView.setVisibility(View.GONE);
                    } else {
                        // If the status is not occupied, display the item
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle the case where the CycleCare data doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFragment) {

                    // Get the data associated with the clicked item
                    String cycleCareId= cycleCare.getUnitID();

                    // Open a new activity and pass the data
                    Intent intent = new Intent(v.getContext(), QRActivity.class);
                    intent.putExtra("cycleCareId", cycleCareId);
                    v.getContext().startActivity(intent);

//                //direct to connect to cyclecare activity
//                Intent intent = new Intent(context, ConnectCycleCareActivity.class);
//                // If you want to pass data to the new activity, you can use putExtra
//                intent.putExtra("bikeId", bike.getBikeId()); // Replace "bikeId" with the actual data key
//                // Add other putExtra statements for additional data
//
//                context.startActivity(intent);
                }



            }
        });
    }

    private void showPopupMenu(View v, String unitID) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.options, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_edit:

                        return true;

                    case R.id.menu_delete:

                        deleteItem(unitID);

                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    private void deleteItem(String unitID) {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this cyclecare unit");

        // Add positive button (yes)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the item if the user clicks "Yes"
                DatabaseReference bikeReference = FirebaseDatabase.getInstance().getReference("CycleCareUnit")
                        .child(unitID);

                bikeReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                       // deleteAssociatedParkRecords(bikeId);
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


    @Override
    public int getItemCount() {
        return cycleCareList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView unitName, status;
        Button moreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardItemCC);
            unitName = itemView.findViewById(R.id.unitNametxt);
            status = itemView.findViewById(R.id.statustxt);
            moreOptions = itemView.findViewById(R.id.moreOption);
        }
    }
}
