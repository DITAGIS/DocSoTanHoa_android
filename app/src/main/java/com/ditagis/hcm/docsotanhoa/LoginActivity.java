package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.User;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;
import com.ditagis.hcm.docsotanhoa.utities.CheckConnect;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;
import com.ditagis.hcm.docsotanhoa.utities.LocationHelper;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ID_WRITE_FILE = 2;
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button btnLogin;
    private ImageButton mImgBtnViewPassword;
    private LoginAsync mLoginAsync;
    private GetUserNameAsync mGetUserNameAsync;
    private String mUsername, mPassword, mStaffName, mStaffPhone;
    private String mDot, mKy, mNam;
    private NetworkStateChangeReceiver mStateChangeReceiver;
    private IntentFilter mIntentFilter;
    private String IMEI = "";
    LocationHelper locationHelper;
    double latitude, longtitude;
    Location mLastLocation;

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
//                loginWithIMEI();
            }
        });

    }

    @Override
    protected void onResumeFragments() {
//        if (mStateChangeReceiver == null) {
//            this.mStateChangeReceiver = new NetworkStateChangeReceiver(btnLogin, LoginActivity.this);
//            this.mIntentFilter = new IntentFilter();
//            this.mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//            this.mIntentFilter.addAction("android.net.conn.WIFI_STATE_CHANGED");
//            registerReceiver(mStateChangeReceiver, this.mIntentFilter);
//        }
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

    public void getAddress() {
        Address locationAddress;

        locationAddress = locationHelper.getAddress(latitude, longtitude);

        if (locationAddress != null) {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

            }

        }

    }

    private void login() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LoginActivity.this.mUsername = mTxtUsername.getText().toString();
        LoginActivity.this.mPassword = mTxtPassword.getText().toString();
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

        if (LoginActivity.this.mUsername.length() == 0 || LoginActivity.this.mPassword.length() == 0) {
            MySnackBar.make(btnLogin, R.string.not_null_username_password, true);
            return;
        } else if (CheckConnect.isOnline(LoginActivity.this)) {
            mLoginAsync = new LoginAsync();
            mLoginAsync.execute(LoginActivity.this.mUsername, LoginActivity.this.mPassword);
        } else if (mTxtPassword.getText().toString().equals(loadPreference(mTxtUsername.getText().toString()))) {
            LoginActivity.this.mPassword = mTxtPassword.getText().toString();
            LoginActivity.this.mStaffName = loadPreference(getString(R.string.preference_tenNV));
            LoginActivity.this.mKy = loadPreference(getString(R.string.preference_ky));
            LoginActivity.this.mDot = loadPreference(getString(R.string.preference_dot));
            LoginActivity.this.mNam = loadPreference(getString(R.string.preference_nam));
            LoginActivity.this.mStaffPhone = loadPreference(getString(R.string.preference_sdtNV));


            if (mStaffName == null)
                MySnackBar.make(btnLogin, "Chưa khởi tạo máy " + mTxtUsername.getText().toString(), true);
            else if (mStaffName.length() > 0) {

                mTxtPassword.setText("");
                mTxtUsername.setText("");
                doLayLoTrinh();
            } else {
                MySnackBar.make(btnLogin, R.string.login_fail, true);
            }
        }
    }

    private void loginWithIMEI() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        IMEI = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        LoginActivity.this.mUsername = mTxtUsername.getText().toString();
        LoginActivity.this.mPassword = mTxtPassword.getText().toString();
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

        if (LoginActivity.this.mUsername.length() == 0 || LoginActivity.this.mPassword.length() == 0) {
            MySnackBar.make(btnLogin, R.string.not_null_username_password, true);
            return;
        } else if (CheckConnect.isOnline(LoginActivity.this)) {
            mLoginAsync = new LoginAsync();
            mLoginAsync.execute(LoginActivity.this.mUsername, LoginActivity.this.mPassword, IMEI);
        } else if (mTxtPassword.getText().toString().equals(loadPreference(mTxtUsername.getText().toString()))) {
            LoginActivity.this.mPassword = mTxtPassword.getText().toString();
            LoginActivity.this.mStaffName = loadPreference(getString(R.string.preference_tenNV));
            LoginActivity.this.mKy = loadPreference(getString(R.string.preference_ky));
            LoginActivity.this.mDot = loadPreference(getString(R.string.preference_dot));
            LoginActivity.this.mNam = loadPreference(getString(R.string.preference_nam));
            LoginActivity.this.mStaffPhone = loadPreference(getString(R.string.preference_sdtNV));


            if (mStaffName == null)
                MySnackBar.make(btnLogin, "Chưa khởi tạo máy " + mTxtUsername.getText().toString(), true);
            else if (mStaffName.length() > 0) {

                mTxtPassword.setText("");
                mTxtUsername.setText("");
                doLayLoTrinh();
            } else {
                MySnackBar.make(btnLogin, R.string.login_fail, true);
            }
        }
    }

    public void doLayLoTrinh() {
//        CheckConnectRealTime.asyncTask.cancel(true);
        try {
            Calendar calendar = Calendar.getInstance();
            int ky = Integer.parseInt(mKy);
            int nam = Integer.parseInt(mNam);
            int dot = Integer.parseInt(mDot);
            new LayLoTrinh(LoginActivity.this, btnLogin, getLayoutInflater(), ky, nam, dot, mUsername, mStaffName, mPassword, mStaffPhone);

        } catch (Exception e) {

        }
    }

    public boolean requestPermisson() {
//        LocationHelper mLocationHelper = new LocationHelper(this);
//        mLocationHelper.checkpermission();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
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

    public SharedPreferences getPreferences() {
        return getSharedPreferences("LOGGED_IN", MODE_PRIVATE);
    }

    /**
     * Method used to save Preferences
     */
    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, Set<String> values) {
        SharedPreferences sharedPreferences = getPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public boolean deletePreferences(String key) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(key).commit();
        return false;
    }

    public boolean deletePreferences() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear().commit();
        return false;
    }

    /**
     * Method used to load Preferences
     */
    public String loadPreference(String key) {
        try {
            SharedPreferences sharedPreferences = getPreferences();
            String strSavedMemo = sharedPreferences.getString(key, "");
            return strSavedMemo;
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    public Set<String> loadPreferences(String key) {
        try {
            SharedPreferences sharedPreferences = getPreferences();
            Set<String> strSavedMemo = sharedPreferences.getStringSet(key, null);
            return strSavedMemo;
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = locationHelper.getLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            dialog.setMessage(LoginActivity.this.getString(R.string.connecting));
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
            String IMEI = "";
            if (params.length > 2)
                IMEI = params[2];
            LogInDB.Result result = null;
            ConnectionDB.getInstance().getConnection(true);
            publishProgress(null);

            if (IMEI.equals(""))
                result = this.loginDB.logIn(new User(username, password));
            else result = this.loginDB.logIn(new User(username, password), IMEI);
            if (result == null)
                ;

            else if (result.getmStaffName() == null || result.getmStaffName().length() > 0) {

                deletePreferences();
                savePreferences(username, password);
                savePreferences(getString(R.string.preference_tenNV), result.getmStaffName());
                savePreferences(getString(R.string.preference_dot), result.getmDot());
                savePreferences(getString(R.string.preference_ky), result.getmKy());
                savePreferences(getString(R.string.preference_nam), result.getmNam());
                savePreferences(getString(R.string.preference_sdtNV), result.getmStaffPhone());

                LoginActivity.this.mPassword = result.getPassword();
                LoginActivity.this.mStaffName = result.getmStaffName();
                LoginActivity.this.mKy = result.getmKy();
                LoginActivity.this.mDot = result.getmDot();
                LoginActivity.this.mNam = result.getmNam();
                LoginActivity.this.mStaffPhone = result.getmStaffPhone();
            }


            publishProgress(result);
            return result;
        }

        @Override
        protected void onProgressUpdate(LogInDB.Result... values) {
            super.onProgressUpdate(values);
            if (values == null) {
                dialog.setMessage(LoginActivity.this.getString(R.string.checking_login));
                return;
            }
            LogInDB.Result result = values[0];
            if (result == null) {

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


    class GetUserNameAsync extends AsyncTask<String, String, String> {
        private LogInDB loginDB = new LogInDB();
        private ProgressDialog dialog;

        public GetUserNameAsync() {
            this.dialog = new ProgressDialog(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HideKeyboard.hide(LoginActivity.this);
//            dialog.setMessage("");
            dialog.setCancelable(false);

            dialog.show();

        }

        public ProgressDialog getDialog() {
            return dialog;
        }

        @Override
        protected String doInBackground(String... params) {
            String IMEI = params[0];
            String userName = this.loginDB.getUserName(IMEI);

            publishProgress(userName);
            return userName;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String userName = values[0];
            if (userName == "")
//                AlertDialogDisConnect.show(btnLogin.getContext(), LoginActivity.this);
                MySnackBar.make(btnLogin, "Chưa khởi tạo máy cho IMEI: " + userName, true);
            else if (userName == null) {
                ;
            } else if (userName.length() > 0) {

                mTxtUsername.setText(userName);
            } else {
                MySnackBar.make(btnLogin, R.string.login_fail, true);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            final String location = data.getStringExtra(getString(R.string.ket_qua_location_loc));
            final double longtitude = data.getDoubleExtra(getString(R.string.ket_qua_location_long), 0.0);
            final double latetitude = data.getDoubleExtra(getString(R.string.ket_qua_location_lat), 0.0);
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {

                }
            }
        } catch (Exception e) {
        }
    }
}
