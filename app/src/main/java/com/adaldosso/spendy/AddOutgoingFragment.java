package com.adaldosso.spendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddOutgoingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_outgoing, container, false);

        // http://www.mkyong.com/android/android-spinner-drop-down-list-example/
        Spinner categoryList = (Spinner) view.findViewById(R.id.categoryList);
        List<Category> list = new ArrayList<>();
        JSONArray rows = getCategories(Utils.CATEGORIES_URL);
        for (int i=0; i<rows.length(); i++) {
            JSONObject row = rows.optJSONObject(i);
            String description = "";
            Integer idCategory = null;
            try {
                description = row.getString("descrizione");
                idCategory = row.getInt("idCategoria");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(new Category(idCategory, description));
        }
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryList.setAdapter(dataAdapter);
        return view;
    }

    private JSONArray getCategories(String url) {
        JSONArray rows = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = Utils.getHttpClient().execute(httpGet);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String responseString = out.toString();
            JSONObject json = new JSONObject(responseString);
            rows = json.getJSONArray("rows");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public void setSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        EditText selectedDate = (EditText) getView().findViewById(R.id.dateEdit);
        selectedDate.setText(String.format("%02d/%02d/%04d",  dayOfMonth, (monthOfYear + 1),  year));
    }
}
