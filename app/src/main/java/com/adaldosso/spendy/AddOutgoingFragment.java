package com.adaldosso.spendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddOutgoingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_outgoing, container, false);

        // http://www.mkyong.com/android/android-spinner-drop-down-list-example/
        Spinner categoryList = (Spinner) view.findViewById(R.id.categoryList);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryList.setAdapter(dataAdapter);
        return view;
    }

    public void setSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        EditText selectedDate = (EditText) getView().findViewById(R.id.dateEdit);
        selectedDate.setText(String.format("%02d/%02d/%04d",  dayOfMonth, (monthOfYear + 1),  year));
    }
}
