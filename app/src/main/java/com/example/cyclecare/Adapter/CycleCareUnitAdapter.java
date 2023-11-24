package com.example.cyclecare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyclecare.ConnectCycleCareActivity;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.Model.CycleCare;
import com.example.cyclecare.QRActivity;
import com.example.cyclecare.R;

import java.util.List;

public class CycleCareUnitAdapter extends RecyclerView.Adapter<CycleCareUnitAdapter.ViewHolder>{

    Context context;
    List<CycleCare> cycleCareList;
    boolean isFragment;

    public CycleCareUnitAdapter(Context context, List<CycleCare> cycleCareList) {
        this.context = context;
        this.cycleCareList= cycleCareList;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        holder.connectBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             //direct to connect to cyclecare activity
             Intent intent = new Intent(context, QRActivity.class);
             context.startActivity(intent);
         }
     });
    }

    @Override
    public int getItemCount() {
        return cycleCareList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView unitName, status;
        Button connectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardItemCC);
            unitName = itemView.findViewById(R.id.unitNametxt);
            status = itemView.findViewById(R.id.statustxt);
            connectBtn = itemView.findViewById(R.id.connectBtn);
        }
    }
}
