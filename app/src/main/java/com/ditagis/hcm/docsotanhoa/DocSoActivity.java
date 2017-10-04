package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DocSoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);
    }

    public void doLayLoTrinh(View v) {
        Intent intent = new Intent(DocSoActivity.this, LayLoTrinhActivity.class);

        startActivity(intent);
    }
    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(DocSoActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
    }
}
