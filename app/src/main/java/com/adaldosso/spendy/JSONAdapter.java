package com.adaldosso.spendy;

import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JSONAdapter extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;

    public JSONAdapter (Activity activity, JSONArray jsonArray) {
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
        if (null == jsonArray) {
            return null;
        } else {
            return jsonArray.optJSONObject(position);
        }
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);
        return jsonObject.optLong("id");
    }

}
