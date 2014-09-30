package com.adaldosso.spendy;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONAdapter  extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;

    public JSONAdapter (Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        if (null == jsonArray)
            return 0;
        else
            return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.outgoing, null);
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
}
