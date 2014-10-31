package com.adaldosso.spendy;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    public static final String LOGIN_URL = "http://www.adaldosso.com/quantospendi/srv/login.php";
    private static final String REGISTRATION_URL = "http://www.adaldosso.com/quantospendi/srv/registration-nd.php";
    private static final String OUTGOINGS_URL = "http://www.adaldosso.com/quantospendi/srv/spese.php";
    private static final String MONTHLY_OUTGOINGS_URL = "http://www.adaldosso.com/quantospendi/srv/totali-categorie.php";
    private static final String DETAIL_OUTGOINGS_URL = "http://www.adaldosso.com/quantospendi/srv/spese-dettagli.php";

    private HttpClient httpClient = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getFragmentManager().beginTransaction()
                .add(R.id.activity_main, new LoginFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void switchRegistration(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        //transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out );
        transaction.replace(R.id.activity_main, new RegistrationFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void login(View view) throws IOException, JSONException {
        if (!checkConnectivity()) {
            return;
        }
        EditText editEmail = (EditText) findViewById(R.id.editEmail);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        URL url = new URL(LOGIN_URL);
        HttpResponse response;
        HttpPost httpPost = new HttpPost(String.valueOf(url));
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", editEmail.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password", editPassword.getText().toString()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        response = httpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String responseString = out.toString();
            JSONObject json = new JSONObject(responseString);
            if (json.getBoolean("success")) {
                viewOutgoings();
            } else {
                Utils.showMessage(this, getString(R.string.wrong_credentials));
            }
        } else {
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (!isNetworkAvailable(connectivityManager)) {
            Utils.showMessage(this, getString(R.string.error_network));
            return false;
        }
        return true;
    }

    private void viewOutgoings() throws IOException, JSONException {
        JSONArray rows = getRows(OUTGOINGS_URL, null);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
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
            HttpResponse response = httpClient.execute(httpGet);
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

    public void registration(View view) throws IOException, JSONException {
        EditText editEmail = (EditText) findViewById(R.id.editEmailReg);
        String email = editEmail.getText().toString();
        EditText editPassword = (EditText) findViewById(R.id.editPasswordReg);
        String password = editPassword.getText().toString();
        EditText editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);
        String confirmPassword = editPasswordConfirm.getText().toString();
        if (!password.equals(confirmPassword)) {
            Utils.showMessage(this, getString(R.string.bad_password));
            return;
        }

        URL url = new URL(REGISTRATION_URL);
        HttpResponse response;
        HttpPost httpPost = new HttpPost(String.valueOf(url));
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        response = httpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String responseString = out.toString();
            JSONObject json = new JSONObject(responseString);
            Utils.showMessage(this, getString(R.string.welcome_message));
        } else {
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    public void loadMonthlyOutgoings(int year, int month) {
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair(Utils.MONTH, String.valueOf(month)));
        params.add(new BasicNameValuePair(Utils.YEAR, String.valueOf(year)));
        JSONArray rows = getRows(MONTHLY_OUTGOINGS_URL, params);
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
        JSONArray rows = getRows(DETAIL_OUTGOINGS_URL, params);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        DetailOutgoingFragment detailOutgoingFragment = new DetailOutgoingFragment();
        detailOutgoingFragment.setJsonArray(rows);
        transaction.replace(R.id.activity_main, detailOutgoingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
