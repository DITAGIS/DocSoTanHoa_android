package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.async.ChangePassswordAsync;
import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.User;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;
import com.ditagis.hcm.docsotanhoa.utities.CheckConnect;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private Button btnLogin;
    private Button btnChangePassword;
    private ImageButton mImgBtnViewPassword;

    private LoginAsync mLoginAsync;
    private String mUsername, mPassword;
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
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

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

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
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
        LocalDatabase localDatabase = new LocalDatabase(this);
        localDatabase.Upgrade();
        LoginActivity.this.mUsername = mTxtUsername.getText().toString();
        LoginActivity.this.mPassword = mTxtPassword.getText().toString();
        if (LoginActivity.this.mUsername.length() == 0 || LoginActivity.this.mPassword.length() == 0) {
            MySnackBar.make(btnLogin, R.string.not_null_username_password, true);
            return;
        } else if (CheckConnect.isOnline(LoginActivity.this)) {
            mLoginAsync = new LoginAsync();
            mLoginAsync.execute(LoginActivity.this.mUsername, LoginActivity.this.mPassword);

        } else if (mTxtPassword.getText().toString().equals(loadPreferences(mTxtUsername.getText().toString()))) {

            mTxtPassword.setText("");
            mTxtUsername.setText("");
            Toast.makeText(LoginActivity.this, this.getString(R.string.login_with_saved_account), Toast.LENGTH_SHORT).show();
            doLayLoTrinh();
        }
//        else {
////            AlertDialogDisConnect.show(btnLogin.getContext(), LoginActivity.this);
////            MySnackBar.make(btnLogin, R.string.no_connect, true);
//        }

    }


    private void changePassword() {
        if (!CheckConnect.isOnline(LoginActivity.this)) {
            MySnackBar.make(btnLogin, R.string.no_connect, true);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Đổi mật khẩu");
        builder.setCancelable(false);
        final AlertDialog dialogChangePw = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_change_password, null);
        dialogChangePw.setView(dialogLayout);
        dialogChangePw.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChangePw.show();


        final TextView txtChangePwUsername = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_userName);
        final TextView txtChangePwOldPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_oldPassword);
        final TextView txtChangePwNewPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_newPw);
        final TextView txtChangePwConfirmPw = (TextView) dialogChangePw.findViewById(R.id.txt_changePw_confirmPw);
        final TextView txtAlertNotCorrect = (TextView) dialogChangePw.findViewById(R.id.txt_changePassword_Alert_NotCorrect);

        final EditText etxtChangePwUsername = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_userName);
        final EditText etxtChangePwOldPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_oldPw);
        final EditText etxtChangePwNewPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_NewPw);
        final EditText etxtChangePwConfirmPw = (EditText) dialogChangePw.findViewById(R.id.etxt_changePw_ConfirmPw);


        etxtChangePwUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtChangePwUsername.setVisibility(View.VISIBLE);
                    etxtChangePwUsername.setHint("");
                } else {
                    txtChangePwUsername.setVisibility(View.INVISIBLE);
                    etxtChangePwUsername.setHint(LoginActivity.this.getString(R.string.hint_username));
                }
            }
        });
        etxtChangePwOldPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtChangePwOldPw.setVisibility(View.VISIBLE);
                    etxtChangePwOldPw.setHint("");
                } else {
                    txtChangePwOldPw.setVisibility(View.INVISIBLE);
                    etxtChangePwOldPw.setHint(LoginActivity.this.getString(R.string.hint_old_password));
                }
            }
        });
        etxtChangePwNewPw.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtChangePwNewPw.setVisibility(View.VISIBLE);
                    etxtChangePwNewPw.setHint("");
                } else {
                    txtChangePwNewPw.setVisibility(View.INVISIBLE);
                    etxtChangePwNewPw.setHint(LoginActivity.this.getString(R.string.hint_new_password));
                }
            }
        });
        etxtChangePwConfirmPw.setOnFocusChangeListener(new View.OnFocusChangeListener()

        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txtChangePwConfirmPw.setVisibility(View.VISIBLE);
                    etxtChangePwConfirmPw.setHint("");
                } else {
                    txtChangePwConfirmPw.setVisibility(View.INVISIBLE);
                    etxtChangePwConfirmPw.setHint(LoginActivity.this.getString(R.string.hint_confirm_password));
                }
            }
        });


        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewOldPassword)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etxtChangePwOldPw.getTransformationMethod() == null) {
                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                            etxtChangePwOldPw.setTransformationMethod(new PasswordTransformationMethod());
                        } else {
                            ((ImageButton) v).setImageResource(R.drawable.view_password);
                            etxtChangePwOldPw.setTransformationMethod(null);
                        }
                    }
                });

        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewNewPassword)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etxtChangePwNewPw.getTransformationMethod() == null) {
                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                            etxtChangePwNewPw.setTransformationMethod(new PasswordTransformationMethod());
                        } else {
                            ((ImageButton) v).setImageResource(R.drawable.view_password);
                            etxtChangePwNewPw.setTransformationMethod(null);
                        }
                    }
                });

        ((ImageButton) dialogChangePw.findViewById(R.id.imgBtn_changePassword_viewConfirmPassword)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etxtChangePwConfirmPw.getTransformationMethod() == null) {
                            ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                            etxtChangePwConfirmPw.setTransformationMethod(new PasswordTransformationMethod());
                        } else {
                            ((ImageButton) v).setImageResource(R.drawable.view_password);
                            etxtChangePwConfirmPw.setTransformationMethod(null);
                        }
                    }
                });

        ((Button) dialogChangePw.findViewById(R.id.btn_changePassword_Dismiss)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogChangePw.dismiss();
                    }
                });

        final Button btnOK = (Button) dialogChangePw.findViewById(R.id.btn_changePassword_OK);
        btnOK.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                ChangePassswordAsync changePassswordAsync = new ChangePassswordAsync(btnLogin, LoginActivity.this, LoginActivity.this,
                        etxtChangePwUsername.getText().toString(),
                        etxtChangePwOldPw.getText().toString(),
                        etxtChangePwNewPw.getText().toString(),
                        etxtChangePwConfirmPw.getText().toString(),
                        new ChangePassswordAsync.AsyncResponse() {
                            @Override
                            public void processFinish(final LogInDB.Result output) {

                                if (output == null) {
                                    ;
                                } else if (output.getmStaffName().length() > 0) {
                                    dialogChangePw.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                                    builder.setTitle(LoginActivity.this.getString(R.string.change_password_success));
                                    builder.setMessage(LoginActivity.this.getString(R.string.ques_login_this_account));
                                    builder.setCancelable(false);
                                    builder.setPositiveButton(LoginActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deletePreferences();
                                            mUsername = output.getUsername();
                                            mPassword = output.getPassword();
                                            savePreferences(output.getUsername(), output.getPassword());
                                            savePreferences(output.getPassword(), output.getmStaffName());
                                            savePreferences(output.getmStaffName(), output.getmDot());
                                            doLayLoTrinh();
                                        }
                                    }).setNegativeButton(LoginActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
                        });

                changePassswordAsync.execute();


            }
        });
        etxtChangePwConfirmPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etxtChangePwNewPw.getText().toString().equals(etxtChangePwConfirmPw.getText().toString())) {
                    txtAlertNotCorrect.setVisibility(View.INVISIBLE);
                    btnOK.setEnabled(true);

                } else {
                    txtAlertNotCorrect.setVisibility(View.VISIBLE);
                    btnOK.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                deletePreferences();
                savePreferences(username, password);
                savePreferences(password, result.getmStaffName());
                savePreferences(result.getmStaffName(), result.getmDot());
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
            } else if (result.getmStaffName().length() > 0) {

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
        new LayLoTrinh(LoginActivity.this, getLayoutInflater(), ky, nam, Integer.parseInt(loadPreferences(loadPreferences(LoginActivity.this.mPassword))), mUsername, loadPreferences(LoginActivity.this.mPassword));
    }

    public boolean requestPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    public boolean deletePreferences() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear().commit();
        return false;
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

}
