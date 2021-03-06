package com.adaldosso.spendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutgoingFragment extends Fragment implements AbsListView.OnItemClickListener {

    private AbsListView listView;
    private JSONAdapter jsonAdapter;
    private JSONArray jsonArray;

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonAdapter = new JSONAdapter(getActivity(), jsonArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.outgoing, null);
                }
                TextView textMonth =(TextView)convertView.findViewById(R.id.month);
                TextView textYear =(TextView)convertView.findViewById(R.id.year);
                TextView textAmount =(TextView)convertView.findViewById(R.id.amount);
                JSONObject jsonData = getItem(position);
                if (null != jsonData ){
                    String month = "";
                    String year = "";
                    String amount = "";
                    try {
                        month = jsonData.getString("mese");
                        year = jsonData.getString("anno");
                        amount = jsonData.getString("importo");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textMonth.setText(month);
                    textYear.setText(year);
                    textAmount.setText(amount);
                }
                return convertView;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outgoing, container, false);
        listView = (AbsListView) view.findViewById(android.R.id.list);
        listView.setAdapter(jsonAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject item = (JSONObject) listView.getItemAtPosition(position);
        MainActivity activity = (MainActivity) getActivity();
        try {
            activity.loadMonthlyOutgoings(item.getInt("anno"), item.getInt("numMese"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
