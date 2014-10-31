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

public class DetailOutgoingFragment extends Fragment implements AbsListView.OnItemClickListener {

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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.detail_outgoing, null);
                }
                TextView dateDetail =(TextView)convertView.findViewById(R.id.dateDetail);
                TextView amountDetail =(TextView)convertView.findViewById(R.id.amountDetail);
                TextView categoryDetail =(TextView)convertView.findViewById(R.id.categoryDetail);
                TextView noteDetail =(TextView)convertView.findViewById(R.id.noteDetail);

                JSONObject jsonData = getItem(position);
                if (null != jsonData ){
                    String date = "";
                    String amount = "";
                    String category = "";
                    String note = "";
                    try {
                        date = jsonData.getString("dataSpesaFormatted");
                        amount = jsonData.getString("importo");
                        category = jsonData.getString("categoria");
                        note = jsonData.getString("note");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dateDetail.setText("Data: " + date);
                    amountDetail.setText("Importo: " + amount + " €");
                    categoryDetail.setText("Categoria: " + category);
                    noteDetail.setText("Note: " + note);
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
