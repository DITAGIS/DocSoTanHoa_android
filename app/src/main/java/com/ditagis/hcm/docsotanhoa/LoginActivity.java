package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LayLoTrinhActivity.class);
                if (checkInfo())
                    startActivity(intent);
            }
        });
    }

    private boolean checkInfo() {

        String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();

        if (username.equals("") && password.equals("")) {
            return true;
        } else {
            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
