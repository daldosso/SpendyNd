package com.adaldosso.spendy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;

import javax.security.auth.x500.X500Principal;

public class Utils {

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

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

}
