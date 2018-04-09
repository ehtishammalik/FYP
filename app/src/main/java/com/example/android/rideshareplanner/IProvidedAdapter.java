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
    private static FirebaseDatabase database;
    private static DatabaseReference myRef;
    public static List<carpoolItems> data = new ArrayList<>();


    public IProvidedAdapter (Context context, List<carpoolItems> data){
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
        carpoolItems items = new carpoolItems();
        items = data.get(position);
        holder.bookedSeatstext.setVisibility(View.VISIBLE);
        holder.bookedSeats.setVisibility(View.VISIBLE);
        holder.Fairtext.setVisibility(View.VISIBLE);
        holder.Fair.setVisibility(View.VISIBLE);
        holder.view.setVisibility(View.VISIBLE);
        holder.text1.setText(items.getDateAndDay());
        holder.text2.setText(items.getDepartureTime());
        holder.text3.setText(items.getEstArrivalTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        TextView text1;
        TextView text2;
        TextView text3;
        View view;

        public myViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.cplv_line1);
            bookedSeatstext = itemView.findViewById(R.id.cplv_text_bookedSeats);
            bookedSeats = itemView.findViewById(R.id.cplv_bookedSeats);
            Fairtext = itemView.findViewById(R.id.cplv_text_Fair);
            Fair = itemView.findViewById(R.id.cplv_Fair);
            text1 = itemView.findViewById(R.id.cplv_date);
            text2 = itemView.findViewById(R.id.cplv_time);
            text3 = itemView.findViewById(R.id.cplv_est_arrival_time);
        }
    }


}
