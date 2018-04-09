package com.example.android.rideshareplanner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class ISharedTab extends android.support.v4.app.Fragment {
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ishared_tab, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ishared_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new ISharedAdapter(getContext(),setData());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
    public List<carpoolItems> setData(){
        String [] dateAndDay = {"March 15", "March 17", "March 18","March 20"};
        String [] departureTime = {"10:00", "11:00", "12:00","01:00"};
        String [] estArrivalTime = {"07:00 (March 15)","08:00 (March 15)","09:00 (March 15)","10:00 (March 15)"};

        List<carpoolItems> data = new ArrayList<>();
        for (int i=0; i< dateAndDay.length;i++){
            carpoolItems temp = new carpoolItems();
            temp.setDateAndDay(dateAndDay[i]);
            temp.setDepartureTime(departureTime[i]);
            temp.setEstArrivalTime(estArrivalTime[i]);
            data.add(temp);
        }
        return data;
    }
}