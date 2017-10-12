package com.ditagis.hcm.docsotanhoa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.LogInDB;
import com.ditagis.hcm.docsotanhoa.entities.User;

public class LoginActivity extends AppCompatActivity {
    //    private ProgressBar spinner;
    EditText txtUserName;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUserName = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
//        spinner = (ProgressBar) findViewById(R.id.progessLogin);


//        new LocalDatabase(this).Upgrade();


        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                spinner.setVisibility(View.GONE);
//                spinner.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                Toast.makeText(LoginActivity.this, "Đang kiểm tra thông tin đăng nhập...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, LayLoTrinhActivity.class);
                if (isOnline()) {
                    if (checkInfo())
                        startActivity(intent);
                } else {
                    Intent intent1 = new Intent(LoginActivity.this, XemLoTrinhDaTaiActivity.class);
                    startActivity(intent1);
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

    private boolean checkInfo() {

        String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();

        if (username.length() == 0 | password.length() == 0) {
            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            return false;
        }
        LogInDB logInDB = new LogInDB();
        if (logInDB.logIn(new User(username, password))) {
//            spinner.setVisibility(View.INVISIBLE);
            return true;
        } else {
//            spinner.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
