package com.ditagis.hcm.docsotanhoa.utities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ditagis.hcm.docsotanhoa.R;

/**
 * Created by ThanLe on 27/10/2017.
 */

public class AlertDialogDisConnect {

    public static void show(Context context, Activity activiy) {
        new AsyncCheckConnect(context, activiy).execute();
    }

    static class AsyncCheckConnect extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Activity activity;

        public AsyncCheckConnect(Context context, Activity activity) {
            this.dialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog.setTitle(activity.getString(R.string.disconnect_title));
            dialog.setMessage(activity.getString(R.string.disconnect_message));
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!CheckConnect.isOnline(activity)) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }
}
