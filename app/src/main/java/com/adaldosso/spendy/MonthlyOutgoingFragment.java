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

public class MonthlyOutgoingFragment extends Fragment implements AbsListView.OnItemClickListener {

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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.monthly_outgoing, null);
                }
                TextView textCategory =(TextView)convertView.findViewById(R.id.category);
                TextView textAmount =(TextView)convertView.findViewById(R.id.amount);
                JSONObject jsonData = getItem(position);
                if (null != jsonData ){
                    String description = "";
                    String amount = "";
                    try {
                        description = jsonData.getString("descrizione");
                        amount = jsonData.getString("importo");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textCategory.setText(description);
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
    }

}
