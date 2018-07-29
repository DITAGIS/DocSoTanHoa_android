package com.ditagis.hcm.docsotanhoa.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.entitiesDB.User;
import com.ditagis.hcm.docsotanhoa.entities.entitiesDB.UserDangNhap;
import com.ditagis.hcm.docsotanhoa.utities.Preference;

/**
 * Created by ThanLe on 24/10/2017.
 */

public class LoginAsync extends AsyncTask<String, Void, Void> {
    private ProgressDialog dialog;
    private Context mContext;
    private AsyncResponse mDelegate;

    public interface AsyncResponse {
        void processFinish(Void output);
    }

    public LoginAsync(Context context, AsyncResponse response) {
        this.mContext = context;
        this.mDelegate = response;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(mContext, android.R.style.Theme_Material_Dialog_Alert);

        dialog.setMessage(mContext.getString(R.string.connecting));
        dialog.setCancelable(false);

        dialog.show();

    }


    @Override
    protected Void doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        String IMEI =  params[2];
        User user = new User();
        user.setUserName(username);
        user.setPassWord(password);
        ConnectionDB.getInstance().getConnection();
        publishProgress(null);
        LogInDB logInDB = new LogInDB();
        UserDangNhap.getInstance().setUser(logInDB.logIn(user, IMEI));
        if (UserDangNhap.getInstance().getUser() == null)
            ;

        else if (UserDangNhap.getInstance().getUser().getStaffName() == null
                || !UserDangNhap.getInstance().getUser().getStaffName().isEmpty()) {

            Preference.getInstance().deletePreferences();
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_username),
                    username);
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_password),
                    password);
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_tenNV),
                    UserDangNhap.getInstance().getUser().getStaffName());
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_dot),
                    UserDangNhap.getInstance().getUser().getDot());
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_ky),
                    UserDangNhap.getInstance().getUser().getKy());
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_nam),
                    UserDangNhap.getInstance().getUser().getNam());
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_sdtNV),
                    UserDangNhap.getInstance().getUser().getStaffPhone());

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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            this.mDelegate.processFinish(aVoid);
        }
    }

}