package com.adaldosso.spendy;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private AddOutgoingFragment addOutgoingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            viewOutgoings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                viewOutgoingAdder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewOutgoingAdder() {
        addOutgoingFragment = new AddOutgoingFragment();
        getFragmentManager()
            .beginTransaction()
            .replace(R.id.activity_main, addOutgoingFragment)
            .addToBackStack(null)
            .commit();
    }

    private void viewOutgoings() throws IOException, JSONException {
        JSONArray rows = getRows(Utils.OUTGOINGS_URL, null);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        OutgoingFragment outgoingFragment = new OutgoingFragment();
        outgoingFragment.setJsonArray(rows);
        transaction.replace(R.id.activity_main, outgoingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private JSONArray getRows(String url, List<NameValuePair> params) {
        if (params != null) {
            String paramString = "?" + URLEncodedUtils.format(params, "utf-8");
            url += paramString;
        }
        JSONArray rows = null;
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = Utils.getHttpClient().execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                String responseString = out.toString();
                JSONObject json = new JSONObject(responseString);
                if (json.getBoolean("success")) {
                    rows = json.getJSONArray("rows");
                } else {
                    Utils.showMessage(this, "Errore");
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public void loadMonthlyOutgoings(int year, int month) {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair(Utils.MONTH, String.valueOf(month)));
        params.add(new BasicNameValuePair(Utils.YEAR, String.valueOf(year)));
        JSONArray rows = getRows(Utils.MONTHLY_OUTGOINGS_URL, params);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        MonthlyOutgoingFragment monthlyOutgoingFragment = new MonthlyOutgoingFragment();
        monthlyOutgoingFragment.setJsonArray(rows);
        transaction.replace(R.id.activity_main, monthlyOutgoingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadDetailOutgoings(int year, int month, int category) {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair(Utils.MONTH, String.valueOf(month)));
        params.add(new BasicNameValuePair(Utils.YEAR, String.valueOf(year)));
        params.add(new BasicNameValuePair(Utils.CATEGORY, String.valueOf(category)));
        JSONArray rows = getRows(Utils.DETAIL_OUTGOINGS_URL, params);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        DetailOutgoingFragment detailOutgoingFragment = new DetailOutgoingFragment();
        detailOutgoingFragment.setJsonArray(rows);
        transaction.replace(R.id.activity_main, detailOutgoingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void setSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        addOutgoingFragment.setSelectedDate(year, monthOfYear, dayOfMonth);
    }
}
