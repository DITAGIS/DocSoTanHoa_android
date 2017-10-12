package com.ditagis.hcm.docsotanhoa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
    ImageButton mImgBtnViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUserName = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        loginAsync = new LoginAsync();
        this.mImgBtnViewPassword = (ImageButton) findViewById(R.id.imgBtn_login_viewPassword);


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


        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                spinner.setVisibility(View.GONE);
//                spinner.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                Toast.makeText(LoginActivity.this, "Đang kiểm tra thông tin đăng nhập...", Toast.LENGTH_SHORT).show();

                if (isOnline()) {
                    loginAsync.execute(txtUserName.getText().toString(), txtPassword.getText().toString());

                } else {
                    Toast.makeText(LoginActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
                }

                btnLogin.setEnabled(true);
            }
        });


    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    class LoginAsync extends AsyncTask<String, Boolean, Void> {
        private LogInDB loginDB = new LogInDB();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            boolean isValid = this.loginDB.logIn(new User(username, password));

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
                Intent intent1 = new Intent(LoginActivity.this, XemLoTrinhDaTaiActivity.class);
                startActivity(intent1);
            }
        }
    }
}
