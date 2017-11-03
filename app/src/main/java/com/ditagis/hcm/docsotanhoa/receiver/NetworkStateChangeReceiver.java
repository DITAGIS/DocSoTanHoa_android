package com.ditagis.hcm.docsotanhoa.receiver;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStateChangeReceiver extends BroadcastReceiver {
    ProgressDialog dialog;
    private Activity mActivity;

    public NetworkStateChangeReceiver(View view, Activity activity) {
        dialog = new ProgressDialog(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        dialog.setCancelable(false);
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

        } else {
//            if (context instanceof MainActivity) {
//                mActivity.finish();
//            }
            dialog.setMessage(context.getString(R.string.disconnect_message));
            dialog.setTitle(context.getString(R.string.disconnect_title
            ));
            dialog.show();

            ConnectionDB.getInstance().reConnect();
        }
    }

}
