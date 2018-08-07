package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
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
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.async.LoginAsync;
import com.ditagis.hcm.docsotanhoa.entities.entitiesDB.User;
import com.ditagis.hcm.docsotanhoa.entities.entitiesDB.UserDangNhap;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;
import com.ditagis.hcm.docsotanhoa.utities.CheckConnect;
import com.ditagis.hcm.docsotanhoa.utities.LocationHelper;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;
import com.ditagis.hcm.docsotanhoa.utities.Preference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ID_WRITE_FILE = 2;
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button btnLogin;
    private ImageButton mImgBtnViewPassword;
    private String mUsername, mPassword, mStaffName, mStaffPhone;
    private List<String> mLstMay;
    private NetworkStateChangeReceiver mStateChangeReceiver;
    private IntentFilter mIntentFilter;
    LocationHelper locationHelper;
    double latitude, longtitude;
    Location mLastLocation;
    private boolean isLastLogin;
    private TextView mTxtValidation;
    private boolean mmIsIMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTheme(R.style.Theme_AppCompat_DayNight);
        this.mmIsIMEI = false;
        mLstMay = new ArrayList<>();
        mTxtUsername = findViewById(R.id.txtUsername);
//        mTxtUsername.setBackgroundResource(R.drawable.edit_text_styles2);
        mTxtPassword = findViewById(R.id.txtPassword);
//        mTxtPassword.setBackgroundResource(R.drawable.edit_text_styles2);
        this.mImgBtnViewPassword = findViewById(R.id.imgBtn_login_viewPassword);
        requestPermisson();
        mTxtPassword.setTransformationMethod(new PasswordTransformationMethod());
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
        mTxtValidation = findViewById(R.id.txt_login_validation);

        ((TextView) findViewById(R.id.txt_login_version)).setText("Phiên bản " + BuildConfig.VERSION_NAME);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        create();
    }

    private void create() {
        Preference.getInstance().setContext(this);
        String preference_userName = Preference.getInstance().loadPreference(getString(R.string.preference_username));

        //nếu chưa từng đăng nhập thành công trước đó
        //nhập username và password bình thường
        if (preference_userName == null || preference_userName.isEmpty()) {
            findViewById(R.id.layout_login_username).setVisibility(View.VISIBLE);
            isLastLogin = false;
        }
        //ngược lại
        //chỉ nhập pasword
        else {
            isLastLogin = true;
            findViewById(R.id.layout_login_username).setVisibility(View.GONE);
        }

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


    private void login(boolean isIMEI) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String IMEI = "";
        if (isIMEI)
            IMEI = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        mTxtValidation.setVisibility(View.GONE);

        if (isLastLogin)
            LoginActivity.this.mUsername = Preference.getInstance().loadPreference(getString(R.string.preference_username));
        else
            LoginActivity.this.mUsername = mTxtUsername.getText().toString().trim();
        LoginActivity.this.mPassword = mTxtPassword.getText().toString();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (LoginActivity.this.mUsername.length() == 0 && LoginActivity.this.mPassword.length() == 0) {
            handleInfoLoginEmpty();
        } else if (CheckConnect.isOnline(LoginActivity.this)) {
            LoginAsync loginAsync = new LoginAsync(this, new LoginAsync.AsyncResponse() {
                @Override
                public void processFinish(Void output) {
                    if (UserDangNhap.getInstance().getUser() == null) {
                        handleLoginFail();
                    } else {
                        handleLoginSuccess();
                    }
                }
            });
            loginAsync.execute(mUsername, mPassword, IMEI);

        } else if (mUsername.equals(Preference.getInstance().loadPreference(getString(R.string.preference_username))) &&
                mPassword.equals(Preference.getInstance().loadPreference(getString(R.string.preference_password)))) {
            User user = new User();
            user.setUserName(mUsername);
            user.setStaffName(Preference.getInstance().loadPreference(getString(R.string.preference_tenNV)));
            user.setStaffPhone(Preference.getInstance().loadPreference(getString(R.string.preference_sdtNV)));
            user.setNam(Preference.getInstance().loadPreference(getString(R.string.preference_nam)));
            user.setKy(Preference.getInstance().loadPreference(getString(R.string.preference_ky)));
            user.setDot(Preference.getInstance().loadPreference(getString(R.string.preference_dot)));
            UserDangNhap.getInstance().setUser(user);
            handleLoginSuccess();

//            LoginActivity.this.mPassword = mTxtPassword.getText().toString();
//            LoginActivity.this.mStaffName = Preference.getInstance().loadPreference(getString(R.string.preference_tenNV));
//            LoginActivity.this.mStaffPhone = Preference.getInstance().loadPreference(getString(R.string.preference_sdtNV));
//
//            int may = Integer.parseInt(mUsername);
//            if (99 <= may && may <= 104)
//                LoginActivity.this.mLstMay.add(Preference.getInstance().loadPreference(getString(R.string.preference_username)));
//
//            if (mStaffName == null) {
//                mTxtValidation.setText("Chưa khởi tạo máy " + mTxtUsername.getText().toString());
//                mTxtValidation.setVisibility(View.VISIBLE);
//            } else if (mStaffName.length() > 0) {
//
//                mTxtPassword.setText("");
//                mTxtUsername.setText("");
//                doLayLoTrinh();
//            } else {
//                mTxtValidation.setVisibility(View.VISIBLE);
//                mTxtValidation.setText(getString(R.string.login_fail));
//            }
        } else {
            handleLoginFail();
        }
    }

    private void handleLoginSuccess() {
        if (UserDangNhap.getInstance().getUser().getStaffName() == null)
            MySnackBar.make(btnLogin, "Chưa khởi tạo máy " + UserDangNhap.getInstance().getUser().getUserName(), true);
        else if (!UserDangNhap.getInstance().getUser().getStaffName().isEmpty()) {

            mTxtPassword.setText("");
            mTxtUsername.setText("");
            doLayLoTrinh();
        } else {
            MySnackBar.make(btnLogin, R.string.login_fail, true);
        }
    }

    private void handleInfoLoginEmpty() {
        mTxtValidation.setText(R.string.info_login_empty);
        mTxtValidation.setVisibility(View.VISIBLE);
    }

    private void handleLoginFail() {
        mTxtValidation.setText(R.string.validate_login_fail);
        mTxtValidation.setVisibility(View.VISIBLE);
    }

    public void doLayLoTrinh() {
//        CheckConnectRealTime.asyncTask.cancel(true);
        try {
            if (UserDangNhap.getInstance().getUser().getKy().equals(""))
                UserDangNhap.getInstance().getUser().setKy("0");
            if (UserDangNhap.getInstance().getUser().getNam().equals(""))
                UserDangNhap.getInstance().getUser().setNam("0");
            if (UserDangNhap.getInstance().getUser().getDot().equals(""))
                UserDangNhap.getInstance().getUser().setDot("0");
            int ky = Integer.parseInt(UserDangNhap.getInstance().getUser().getKy());
            int nam = Integer.parseInt(UserDangNhap.getInstance().getUser().getNam());
            int dot = Integer.parseInt(UserDangNhap.getInstance().getUser().getDot());
            new LayLoTrinh(LoginActivity.this, btnLogin, getLayoutInflater(), ky, nam, dot, mUsername, mStaffName, mPassword, mStaffPhone, mLstMay);

        } catch (Exception e) {

        }
    }

    public boolean requestPermisson() {
//        LocationHelper mLocationHelper = new LocationHelper(this);
//        mLocationHelper.checkpermission();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login(this.mmIsIMEI);
                break;
        }

    }

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
