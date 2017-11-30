package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.User;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;
import com.ditagis.hcm.docsotanhoa.utities.CheckConnect;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button btnLogin;
    private ImageButton mImgBtnViewPassword;

    private LoginAsync mLoginAsync;
    private String mUsername, mPassword, mStaffName;
    private String mDot;
    private NetworkStateChangeReceiver mStateChangeReceiver;
    private IntentFilter mIntentFilter;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;

    private static final int REQUEST_ID_WRITE_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTheme(R.style.Theme_AppCompat_DayNight);
        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtUsername.setBackgroundResource(R.layout.edit_text_styles2);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);
        mTxtPassword.setBackgroundResource(R.layout.edit_text_styles2);
        this.mImgBtnViewPassword = (ImageButton) findViewById(R.id.imgBtn_login_viewPassword);
        requestPermisson();
//        requestPermissonWriteFile();
        this.mImgBtnViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.this.mTxtPassword.getTransformationMethod() == null) {
                    ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                    LoginActivity.this.mTxtPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    ((ImageButton) v).setImageResource(R.drawable.view_password);
                    LoginActivity.this.mTxtPassword.setTransformationMethod(null);
                }
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        this.mStateChangeReceiver = new NetworkStateChangeReceiver(btnLogin, LoginActivity.this);
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mIntentFilter.addAction("android.net.conn.WIFI_STATE_CHANGED");
        registerReceiver(mStateChangeReceiver, this.mIntentFilter);

        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Toast.makeText(LoginActivity.this, mngr.getDeviceId(), Toast.LENGTH_LONG);

    }

    @Override
    protected void onResumeFragments() {
        if (mStateChangeReceiver == null) {
            this.mStateChangeReceiver = new NetworkStateChangeReceiver(btnLogin, LoginActivity.this);
            this.mIntentFilter = new IntentFilter();
            this.mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.mIntentFilter.addAction("android.net.conn.WIFI_STATE_CHANGED");
            registerReceiver(mStateChangeReceiver, this.mIntentFilter);
        }
        super.onResumeFragments();
    }

    @Override
    protected void onPause() {
        if (mStateChangeReceiver != null) {
            LoginActivity.this.unregisterReceiver(mStateChangeReceiver);
            mStateChangeReceiver = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        LoginActivity.this.unregisterReceiver(mStateChangeReceiver);
        super.onDestroy();
    }


    private void login() {
//        LocalDatabase localDatabase = new LocalDatabase(this);
//        localDatabase.Upgrade();
        LoginActivity.this.mUsername = mTxtUsername.getText().toString();
        LoginActivity.this.mPassword = mTxtPassword.getText().toString();
        if (LoginActivity.this.mUsername.length() == 0 || LoginActivity.this.mPassword.length() == 0) {
            MySnackBar.make(btnLogin, R.string.not_null_username_password, true);
            return;
        } else if (CheckConnect.isOnline(LoginActivity.this)) {
            mLoginAsync = new LoginAsync();
            mLoginAsync.execute(LoginActivity.this.mUsername, LoginActivity.this.mPassword);
        }
//        } else if (mTxtPassword.getText().toString().equals(loadPreferences(mTxtUsername.getText().toString()))) {
//
//            mTxtPassword.setText("");
//            mTxtUsername.setText("");
//            Toast.makeText(LoginActivity.this, this.getString(R.string.login_with_saved_account), Toast.LENGTH_SHORT).show();
//            doLayLoTrinh();
//        }
    }


    class LoginAsync extends AsyncTask<String, LogInDB.Result, LogInDB.Result> {
        private LogInDB loginDB = new LogInDB();
        private ProgressDialog dialog;

        public LoginAsync() {
            this.dialog = new ProgressDialog(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HideKeyboard.hide(LoginActivity.this);
            dialog.setMessage(LoginActivity.this.getString(R.string.checking_login));
            dialog.setCancelable(false);

            dialog.show();

        }

        public ProgressDialog getDialog() {
            return dialog;
        }

        @Override
        protected LogInDB.Result doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            LogInDB.Result result = this.loginDB.logIn(new User(username, password));
            if (result == null)
                ;
            else if (result.getmStaffName() == null || result.getmStaffName().length() > 0) {
                LoginActivity.this.mPassword = result.getPassword();
                LoginActivity.this.mStaffName = result.getmStaffName();
                LoginActivity.this.mDot = result.getmDot();
            }
            publishProgress(result);
            return result;
        }

        @Override
        protected void onProgressUpdate(LogInDB.Result... values) {
            super.onProgressUpdate(values);
            LogInDB.Result result = values[0];
            if (result == null) {
//                AlertDialogDisConnect.show(btnLogin.getContext(), LoginActivity.this);
            } else if (result.getmStaffName() == null)
                MySnackBar.make(btnLogin, "Chưa khởi tạo máy " + result.getUsername(), true);
            else if (result.getmStaffName().length() > 0) {

                mTxtPassword.setText("");
                mTxtUsername.setText("");
                doLayLoTrinh();
            } else {
                MySnackBar.make(btnLogin, R.string.login_fail, true);
            }
        }


        @Override
        protected void onPostExecute(LogInDB.Result result) {
            super.onPostExecute(result);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public void doLayLoTrinh() {
//        CheckConnectRealTime.asyncTask.cancel(true);

        Calendar calendar = Calendar.getInstance();
        int ky = calendar.get(Calendar.MONTH) + 1;
        int nam = calendar.get(Calendar.YEAR);
        int dot = Integer.parseInt(mDot);
        new LayLoTrinh(LoginActivity.this, getLayoutInflater(), ky, nam, dot, mUsername, mStaffName, mPassword);
    }

    public boolean requestPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;
    }

    public boolean requestPermissonWriteFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_ID_WRITE_FILE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;
    }
//
//    public SharedPreferences getPreferences() {
//        return getSharedPreferences("LOGGED_IN", MODE_PRIVATE);
//    }
//
//    /**
//     * Method used to save Preferences
//     */
//    public void savePreferences(String key, String value) {
//        SharedPreferences sharedPreferences = getPreferences();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }

    /**
     * Method used to load Preferences
     */
//    public String loadPreferences(String key) {
//        try {
//            SharedPreferences sharedPreferences = getPreferences();
//            String strSavedMemo = sharedPreferences.getString(key, "");
//            return strSavedMemo;
//        } catch (NullPointerException nullPointerException) {
//            return null;
//        }
//    }

    /**
     * Method used to delete Preferences
     */

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setCancelable(true);

        builder.setTitle(this.getString(R.string.quit));
        builder.setPositiveButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

}
