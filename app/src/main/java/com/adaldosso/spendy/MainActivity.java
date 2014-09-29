package com.adaldosso.spendy;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.activity_main, new RegistrationFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void login(View view) throws IOException, JSONException {
        EditText editEmail = (EditText) findViewById(R.id.editEmail);
        EditText editPassword = (EditText) findViewById(R.id.editPassword);
        HttpClient httpClient = new DefaultHttpClient();
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
                //Utils.showMessage(this, "Connesso");
            } else {
                //Utils.showMessage(this, "Non Connesso");
            }
            Utils.showMessage(this, "Email e/o password errate");
        } else {
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    public void registration(View view) throws IOException, JSONException {
        EditText editEmail = (EditText) findViewById(R.id.editEmailReg);
        String email = editEmail.getText().toString();
        EditText editPassword = (EditText) findViewById(R.id.editPasswordReg);
        String password = editPassword.getText().toString();
        EditText editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);
        String confirmPassword = editPasswordConfirm.getText().toString();
        if (!password.equals(confirmPassword)) {
            Utils.showMessage(this, "Password e conferma password devono coincidere");
            return;
        }

        HttpClient httpClient = new DefaultHttpClient();
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
            Utils.showMessage(this, "Grazie per la registrazione, il servizio sarà attivo al più presto");
        } else {
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

}
