package com.ditagis.hcm.docsotanhoa.utities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Window;

/**
 * Created by ThanLe on 12/4/2017.
 */

public class MyAlertDialog {
    public static void show(Context context, Boolean cancelable, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setCancelable(cancelable);
        AlertDialog dialog = builder.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
