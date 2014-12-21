package com.adaldosso.spendy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.security.auth.x500.X500Principal;

public class Utils {

    public static final String MONTH = "mese";
    public static final String YEAR = "anno";
    public static final String NUM_MONTH = "numMese";
    public static final String ID_CATEGORY = "idCategoria";
    public static final String CATEGORY = "categoria";

    public static final String LOGIN_URL = "http://www.adaldosso.com/quantospendi/srv/login.php";
    public static final String REGISTRATION_URL = "http://www.adaldosso.com/quantospendi/srv/registration-nd.php";
    public static final String OUTGOINGS_URL = "http://www.adaldosso.com/quantospendi/srv/spese.php";
    public static final String MONTHLY_OUTGOINGS_URL = "http://www.adaldosso.com/quantospendi/srv/totali-categorie.php";
    public static final String DETAIL_OUTGOINGS_URL = "http://www.adaldosso.com/quantospendi/srv/spese-dettagli.php";
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    private static HttpClient httpClient;

    public static void showMessage(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static boolean isDebuggable(Context ctx) {
        return  ( 0 != ( ctx.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
    }

    private static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean checkConnectivity(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (!isNetworkAvailable(connectivityManager)) {
            Utils.showMessage(context, context.getString(R.string.error_network));
            return false;
        }
        return true;
    }

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }

    public static boolean supportsGooglePlayServices(Context context) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) ==
                ConnectionResult.SUCCESS;
    }



}
