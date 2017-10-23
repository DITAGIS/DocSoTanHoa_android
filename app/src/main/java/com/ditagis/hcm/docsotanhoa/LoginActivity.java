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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.ChangePasswordDB;
import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.User;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnChangePassword;
    private ImageButton mImgBtnViewPassword;

    private LoginAsync mLoginAsync;
    private String mUsername;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ID_WRITE_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        this.mImgBtnViewPassword = (ImageButton) findViewById(R.id.imgBtn_login_viewPassword);
        requestPermissonCamera();
        requestPermissonWriteFile();
        this.mImgBtnViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.this.txtPassword.getTransformationMethod() == null) {
                    ((ImageButton) v).setImageResource(R.drawable.un_view_password);
                    LoginActivity.this.txtPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    ((ImageButton) v).setImageResource(R.drawable.view_password);
                    LoginActivity.this.txtPassword.setTransformationMethod(null);
                }
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.mUsername = txtUsername.getText().toString();
                if (txtUsername.getText().toString().length() == 0 || txtPassword.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không được để trống!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isOnline()) {
                    mLoginAsync = new LoginAsync();
                    mLoginAsync.execute(txtUsername.getText().toString(), txtPassword.getText().toString());

                } else if (txtPassword.getText().toString().equals(loadPreferences(txtUsername.getText().toString()))) {
                    Toast.makeText(LoginActivity.this, "Đang đăng nhập với tài khoản trước...", Toast.LENGTH_LONG).show();
                    doLayLoTrinh();
                } else {
                    Toast.makeText(LoginActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Đổi mật khẩu");
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.layout_change_password, null);
                dialog.setView(dialogLayout);
                dialog.show();


                final TextView txtChangePwUsername = (TextView) dialog.findViewById(R.id.txt_changePw_userName);
                final TextView txtChangePwOldPw = (TextView) dialog.findViewById(R.id.txt_changePw_oldPassword);
                final TextView txtChangePwNewPw = (TextView) dialog.findViewById(R.id.txt_changePw_newPw);
                final TextView txtChangePwConfirmPw = (TextView) dialog.findViewById(R.id.txt_changePw_confirmPw);
                final TextView txtAlertNotCorrect = (TextView) dialog.findViewById(R.id.txt_changePassword_Alert_NotCorrect);

                final EditText etxtChangePwUsername = (EditText) dialog.findViewById(R.id.etxt_changePw_userName);
                final EditText etxtChangePwOldPw = (EditText) dialog.findViewById(R.id.etxt_changePw_oldPw);
                final EditText etxtChangePwNewPw = (EditText) dialog.findViewById(R.id.etxt_changePw_NewPw);
                final EditText etxtChangePwConfirmPw = (EditText) dialog.findViewById(R.id.etxt_changePw_ConfirmPw);


                etxtChangePwUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            txtChangePwUsername.setVisibility(View.VISIBLE);
                            etxtChangePwUsername.setHint("");
                        } else {
                            txtChangePwUsername.setVisibility(View.INVISIBLE);
                            etxtChangePwUsername.setHint("Tên đăng nhập");
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
                            etxtChangePwOldPw.setHint("Mật khẩu cũ");
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
                            etxtChangePwNewPw.setHint("Mật khẩu mới");
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
                            etxtChangePwConfirmPw.setHint("Nhập lại mật khẩu mới");
                        }
                    }
                });


                ((ImageButton) dialog.findViewById(R.id.imgBtn_changePassword_viewOldPassword)).

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

                ((ImageButton) dialog.findViewById(R.id.imgBtn_changePassword_viewNewPassword)).

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

                ((ImageButton) dialog.findViewById(R.id.imgBtn_changePassword_viewConfirmPassword)).

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

                ((Button) dialog.findViewById(R.id.btn_changePassword_Dismiss)).

                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                final ChangePasswordDB changePasswordHandling = new ChangePasswordDB(etxtChangePwUsername.getText().toString(),
                        etxtChangePwOldPw.getText().toString(), etxtChangePwNewPw.getText().toString(), etxtChangePwConfirmPw.getText().toString());
                final Button btnOK = (Button) dialog.findViewById(R.id.btn_changePassword_OK);
                btnOK.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        changePasswordHandling.changePassword();
                    }
                });
                etxtChangePwConfirmPw.addTextChangedListener(new

                                                                     TextWatcher() {
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
        });
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            hideKeyboard();
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
                doLayLoTrinh();
            } else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public void doLayLoTrinh() {
        Intent intent = new Intent(LoginActivity.this, LayLoTrinhActivity.class);
        intent.putExtra("mayds", LoginActivity.this.mUsername);
        startActivity(intent);
    }

    public boolean requestPermissonCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
}
