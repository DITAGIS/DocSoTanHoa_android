package com.ditagis.hcm.docsotanhoa.receiver;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.view.View;

import com.ditagis.hcm.docsotanhoa.MainActivity;
import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStateChangeReceiver extends BroadcastReceiver {
    ProgressDialog dialog;
    private Activity mActivity;
    CountDownTimer timer;
    boolean mIsRunning = false;

    public NetworkStateChangeReceiver(View view, Activity activity) {
        dialog = new ProgressDialog(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        dialog.setCancelable(false);
        this.mActivity = activity;

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        timer = new CountDownTimer(120 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                dialog.setMessage(context.getString(R.string.disconnect_message) + "\nCần kết nối lại trong: " + millisUntilFinished / 1000 + " giây");
            }

            public void onFinish() {
                dialog.dismiss();
                mActivity.finish();
                dialog.setMessage(context.getString(R.string.disconnect_message));
                dialog.setTitle(context.getString(R.string.disconnect_title));
                dialog.show();

            }

        };
//        timer.start();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (dialog != null && dialog.isShowing()) {
                if (mIsRunning) {
                    timer.cancel();
                    mIsRunning = false;
                }
                dialog.dismiss();

            }

        } else {
            if (context instanceof MainActivity) {
                dialog.setTitle(context.getString(R.string.disconnect_title));
                dialog.show();
                mIsRunning = true;
                timer.start();
            } else {
                dialog.setMessage(context.getString(R.string.disconnect_message));
                dialog.setTitle(context.getString(R.string.disconnect_title
                ));
                dialog.show();
            }
            ConnectionDB.getInstance().reConnect();

        }
    }

}
