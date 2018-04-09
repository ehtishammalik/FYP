package com.example.android.rideshareplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ehtisham on 12/11/17.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    private EditText from, to;
    public static TextView when;
    private Button create;
    static String selectedTime,selectedDate;
    ProfilelvAdapter adapter;

    //Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        List<ProfileListviewItems> data = new ArrayList<>();
        data = setData();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profile_lv);
        adapter = new ProfilelvAdapter(getContext(),data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
