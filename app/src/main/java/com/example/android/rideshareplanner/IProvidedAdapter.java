package com.example.android.rideshareplanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class IProvidedAdapter extends RecyclerView.Adapter <IProvidedAdapter.myViewHolder> {
    public LayoutInflater inflater;
    Context context;
    static MyRidesDataModel selectedItem;
    public static List<MyRidesDataModel> data = new ArrayList<>();


    public IProvidedAdapter (Context context, List<MyRidesDataModel> data){
        inflater=LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.carpool_list_view, parent,false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {
        MyRidesDataModel rides = data.get(position);
        holder.bookedSeatstext.setVisibility(View.VISIBLE);
        holder.bookedSeats.setVisibility(View.VISIBLE);
        holder.bookedSeats.setText(rides.getSeatsBooked());
        holder.Fairtext.setVisibility(View.VISIBLE);
        holder.Fair.setVisibility(View.VISIBLE);
        holder.Fair.setText(rides.getFairPerKm());
        holder.view.setVisibility(View.VISIBLE);
        holder.startingDate.setText(rides.getStartingDate());
        holder.startingTime.setText(rides.getStartingTime());
        holder.arrivalTime.setText("- " +rides.getArrivalTime().toString());
        holder.startingLocation.setText(rides.getStartingAddress());
        holder.destinationLocation.setText(rides.getDestinationAddress());
        holder.availableSeats.setText("Available Seats: "+rides.getSeatsAvailable());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = data.get(position);
                Intent intent = new Intent(context,RideDetails.class);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        TextView bookedSeatstext,bookedSeats,Fairtext,Fair;
        TextView startingDate;
        TextView startingTime;
        TextView arrivalTime;
        TextView availableSeats;
        TextView startingLocation;
        TextView destinationLocation;
        View view;

        public myViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.cplv_line1);
            bookedSeatstext = itemView.findViewById(R.id.cplv_text_bookedSeats);
            bookedSeats = itemView.findViewById(R.id.cplv_bookedSeats);
            Fairtext = itemView.findViewById(R.id.cplv_text_Fair);
            Fair = itemView.findViewById(R.id.cplv_Fair);
            startingDate = itemView.findViewById(R.id.cplv_date);
            startingTime = itemView.findViewById(R.id.cplv_time);
            arrivalTime = itemView.findViewById(R.id.cplv_est_arrival_time);
            availableSeats = itemView.findViewById(R.id.cplv_seats);
            startingLocation = itemView.findViewById(R.id.cplv_source);
            destinationLocation = itemView.findViewById(R.id.cplv_destination);

        }
    }


}
