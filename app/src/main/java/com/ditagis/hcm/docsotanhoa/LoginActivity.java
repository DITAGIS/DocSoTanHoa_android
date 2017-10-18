package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.User;

public class LoginActivity extends AppCompatActivity {
    //    private ProgressBar spinner;
    EditText txtUserName;
    EditText txtPassword;
    LoginAsync loginAsync;
    private Button btnLogin;
    private Button btnChangePassword;
    ImageButton mImgBtnViewPassword;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUserName = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        this.mImgBtnViewPassword = (ImageButton) findViewById(R.id.imgBtn_login_viewPassword);
        requestPermissonCamera();
        requestPermissonWriteFile();
//        spinner = (ProgressBar) findViewById(R.id.progessLogin);


//        new LocalDatabase(this).Upgrade();
        this.mImgBtnViewPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginActivity.this.mImgBtnViewPassword.setImageResource(R.drawable.view_password);
                    LoginActivity.this.txtPassword.setTransformationMethod(null);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginActivity.this.mImgBtnViewPassword.setImageResource(R.drawable.un_view_password);
                    LoginActivity.this.txtPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
                return false;
            }
        });


        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                spinner.setVisibility(View.GONE);
//                spinner.setVisibility(View.VISIBLE);
                if (txtUserName.getText().toString().length() == 0 || txtPassword.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không được để trống!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
//                setEnable(false);
                if (isOnline()) {
                    loginAsync = new LoginAsync();
                    loginAsync.execute(txtUserName.getText().toString(), txtPassword.getText().toString());

                } else if (txtPassword.getText().toString().equals(loadPreferences(txtUserName.getText().toString()))) {
                    Toast.makeText(LoginActivity.this, "Đang đăng nhập với tài khoản trước...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, LayLoTrinhActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Tính năng đang cập nhật", Toast.LENGTH_SHORT).show();
                return;

//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                builder.setTitle("Đổi mật khẩu");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogLayout = inflater.inflate(R.layout.layout_change_password, null);
//                final ImageButton viewPassword = (ImageButton) findViewById(R.id.imgBtn_changePassword_viewPassword);
//                ImageButton viewNewPassword = (ImageButton) findViewById(R.id.imgBtn_changePassword_viewNewPassword);
//                ImageButton viewNewPasswordAgain = (ImageButton) findViewById(R.id.imgBtn_changePassword_viewNewPasswordAgain);
//                viewPassword.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            viewPassword.setImageResource(R.drawable.view_password);
//                            LoginActivity.this.txtPassword.setTransformationMethod(null);
//                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            LoginActivity.this.mImgBtnViewPassword.setImageResource(R.drawable.un_view_password);
//                            LoginActivity.this.txtPassword.setTransformationMethod(new PasswordTransformationMethod());
//                        }
//                        return false;
//                    }
//                });
//
//
//                dialog.setView(dialogLayout);
//
//                dialog.show();


            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void setEnable(boolean enable) {
        LoginActivity.this.txtPassword.setEnabled(enable);
        LoginActivity.this.txtUserName.setEnabled(enable);
        LoginActivity.this.btnLogin.setEnabled(enable);
        LoginActivity.this.btnChangePassword.setEnabled(enable);
        LoginActivity.this.mImgBtnViewPassword.setEnabled(enable);
    }

    class LoginAsync extends AsyncTask<String, Boolean, Void> {
        private LogInDB loginDB = new LogInDB();
        private ProgressDialog dialog;

        public LoginAsync() {
            this.dialog = new ProgressDialog(LoginActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Đang kiểm tra thông tin đăng nhập...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            boolean isValid = this.loginDB.logIn(new User(username, password));
            if (isValid) {
                for (int i = 1; i <= 70; i++) {
                    if (i < 10) {
                        deletePreferences("0" + i);
                    } else
                        deletePreferences(i + "");
                }
                savePreferences(username, password);
            }
            publishProgress(isValid);
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            boolean isValid = values[0];
            if (isValid) {
                Intent intent = new Intent(LoginActivity.this, LayLoTrinhActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(LoginActivity.this, XemLoTrinhDaTaiActivity.class);
//                startActivity(intent1);
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            setEnable(true);
        }
    }

    public boolean requestPermissonCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Không cho phép bật CAMERA", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public boolean requestPermissonWriteFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Không cho phép lưu ảnh!!!", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
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

    /**
     * Method used to load Preferences
     */
    public String loadPreferences(String key) {
        try {
            SharedPreferences sharedPreferences = getPreferences();
            String strSavedMemo = sharedPreferences.getString(key, "");
            return strSavedMemo;
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    /**
     * Method used to delete Preferences
     */
    public boolean deletePreferences(String key) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(key).commit();
        return false;
    }
}
