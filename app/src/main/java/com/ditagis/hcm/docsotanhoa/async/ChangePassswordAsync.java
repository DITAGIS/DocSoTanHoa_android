package com.ditagis.hcm.docsotanhoa.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.ChangePasswordDB;
import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;

/**
 * Created by ThanLe on 27/10/2017.
 */

public class ChangePassswordAsync extends AsyncTask<String, LogInDB.Result, LogInDB.Result> {
    private String userName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private ProgressDialog dialog;
    private Context mContext;
    private Activity mActivity;
    private View view;
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(LogInDB.Result output);
    }

    public ChangePassswordAsync(View view, Activity activity, Context context, String userName, String oldPassword, String newPassword, String confirmPassword, AsyncResponse delegate) {
        this.userName = userName;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.mContext = context;
        this.mActivity = activity;
        this.view = view;
        this.delegate = delegate;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        HideKeyboard.hide(this.mActivity);
        dialog.setMessage("Đang đổi mật khẩu...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected LogInDB.Result doInBackground(String... params) {
        LogInDB.Result result = new ChangePasswordDB().changePassword(userName, oldPassword, newPassword);
        publishProgress(result);
        return result;
    }

    @Override
    protected void onProgressUpdate(LogInDB.Result... values) {
        super.onProgressUpdate(values);
        LogInDB.Result result = values[0];
        if (result == null) {
//            AlertDialogDisConnect.show(mContext, mActivity);
        } else if (result.getPassword().length() > 0) {
            Toast.makeText(this.mContext, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.mContext, "Đổi mật khẩu thất bại :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostExecute(LogInDB.Result result) {
        delegate.processFinish(result);
//        super.onPostExecute(aBoolean);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
