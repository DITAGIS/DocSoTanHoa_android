package com.ditagis.hcm.docsotanhoa.conectDB;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.EncodeMD5;
import com.ditagis.hcm.docsotanhoa.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ThanLe on 23/10/2017.
 */

public class ChangePasswordDB extends AsyncTask<String, Boolean, LogInDB.Result> implements IDB<User, Boolean, String> {
    private String userName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private final String TABLE_NAME = "MayDS";
    private final String SQL_SELECT = "select * from " + TABLE_NAME + " where may = ? and password = ?";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET password=? WHERE may=?";
    private ProgressDialog dialog;
    private Context mContext;

    public interface AsyncResponse {
        void processFinish(LogInDB.Result output);
    }

    public AsyncResponse delegate = null;


    public ChangePasswordDB(Context context, String userName, String oldPassword, String newPassword, String confirmPassword, AsyncResponse delegate) {
        this.userName = userName;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.mContext = context;
        this.dialog = new ProgressDialog(this.mContext);
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        hideKeyboard();
        dialog.setMessage("Đang đổi mật khẩu...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected LogInDB.Result doInBackground(String... params) {
        LogInDB.Result result = changePassword();
        return result;
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        boolean result = values[0];
        if (result) {
            Toast.makeText(this.mContext, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.mContext, "Đổi mật khẩu thất bại :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostExecute(LogInDB.Result staffName) {
        delegate.processFinish(staffName);
//        super.onPostExecute(aBoolean);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private String logIn() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_SELECT;
        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, userName);
            st.setString(2, (new EncodeMD5()).encode(oldPassword));
            ResultSet result = st.executeQuery();
            String staffName = "";
            if (result.next()) {

                staffName = result.getString(1);

            }
            result.close();
            st.close();
            return staffName;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private LogInDB.Result changePassword() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        LogInDB.Result result = new LogInDB().logIn(new User(userName,oldPassword));
        if (result.getmStaffName().length() > 0) {

            String sql = this.SQL_UPDATE;

            try {
                PreparedStatement st = cnn.prepareStatement(sql);
                st.setString(1, (new EncodeMD5()).encode(newPassword));
                st.setString(2, userName);
                int executeUpdate = st.executeUpdate();
                if (executeUpdate > 0) {
                    st.close();

                    return result;
                }
                st.close();


            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return null;
        } else
            return null;
    }

    @Override
    public Boolean add(User user) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(User user) {
        return null;
    }

    @Override
    public User find(String s) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
