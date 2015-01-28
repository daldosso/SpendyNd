package com.adaldosso.spendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddOutgoingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_outgoing, container, false);
        return view;
    }

    public void setSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        EditText selectedDate = (EditText) getView().findViewById(R.id.dateEdit);
        selectedDate.setText(String.format("%02d/%02d/%04d",  dayOfMonth, (monthOfYear + 1),  year));
    }
}
