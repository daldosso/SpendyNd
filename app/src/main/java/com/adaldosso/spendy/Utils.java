package com.adaldosso.spendy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils {

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

}
