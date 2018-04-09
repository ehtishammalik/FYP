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


public class FragmentTab extends android.support.v4.app.Fragment {
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_tab, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new ProfilelvAdapter(getContext(),setData());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
    public List<ProfileListviewItems> setData(){
        String [] eventnames = {"CNIC", "Driver's Licence", "Phone","Email"};
        int [] images = {R.drawable.ic_phone_white,R.drawable.ic_email_white_24dp_1x,R.drawable.ic_phone_white_24dp_1x,R.drawable.ic_email_white_24dp_2x};
        String add = "ADD";
        String verified = "Verified";
        List<ProfileListviewItems> data = new ArrayList<>();
        for (int i=0; i< eventnames.length;i++){
            ProfileListviewItems temp = new ProfileListviewItems();
            temp.setEventName(eventnames[i]);
            temp.setValue(add);
            temp.setIsVerified(verified);
            temp.setImageId(images[i]);
            data.add(temp);
        }
        return data;
    }
}