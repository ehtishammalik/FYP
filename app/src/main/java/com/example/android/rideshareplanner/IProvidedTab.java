package com.example.android.rideshareplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class IProvidedTab extends android.support.v4.app.Fragment {
    RecyclerView mRecyclerView;
    DatabaseReference databaseReference;
    List<MyRidesDataModel> myRides;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iprovided_tab, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.iprovided_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new IProvidedAdapter(getContext(),setData());
        mRecyclerView.setAdapter(mAdapter);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.show();

        try {
            Thread.sleep(5000);
            progressDialog.dismiss();
            mAdapter = new IProvidedAdapter(getContext(),myRides);
            mRecyclerView.setAdapter(mAdapter);
            return view;

        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        new DownloadData().execute();
        return view;

    }

    private class DownloadData extends AsyncTask<Void, Integer, List<MyRidesDataModel>> {
        protected List<MyRidesDataModel> doInBackground(Void... prams) {
            final List<MyRidesDataModel> allMyRides = new ArrayList<>();

            databaseReference = FirebaseDatabase.getInstance().getReference("My Rides");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot i : dataSnapshot.getChildren()) {
                            MyRidesDataModel myRidesDataModel = new MyRidesDataModel();
                            myRidesDataModel = new MyRidesDataModel();
                            myRidesDataModel = i.getValue(MyRidesDataModel.class);
                            allMyRides.add(myRidesDataModel);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });


            return allMyRides;
        }

        protected void onPostExecute(List<MyRidesDataModel> result) {
            myRides = result;
        }
    }

    public List<MyRidesDataModel> setData(){
        String [] dateAndDay = {"March 15", "March 17", "March 18","March 20"};
        String [] departureTime = {"10:00", "11:00", "12:00","01:00"};
        String [] estArrivalTime = {"07:00 (March 15)","08:00 (March 15)","09:00 (March 15)","10:00 (March 15)"};

        List<MyRidesDataModel> data = new ArrayList<>();
//        for (int i=0; i< dateAndDay.length;i++){
//            carpoolItems temp = new carpoolItems();
//            temp.setDateAndDay(dateAndDay[i]);
//            temp.setDepartureTime(departureTime[i]);
//            temp.setEstArrivalTime(estArrivalTime[i]);
//            data.add(temp);
//        }
        MyRidesDataModel myRidesDataModel = new MyRidesDataModel();
        myRidesDataModel.setDestinationAddress("jjfdjfkdj");
        return data;
    }
}