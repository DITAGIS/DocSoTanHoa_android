package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;

import java.util.Collections;
import java.util.List;

public class DocSoActivity extends AppCompatActivity {
String mlt;
    String mMlt[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);

        Bundle extras = getIntent().getExtras();
        String llt_mlt[] = extras.getStringArray("mlt");
        int llt_chkposition[] = extras.getIntArray("chkPosition");
        mMlt = new String[extras.getInt("sum_mlt")];

        int j = 0;
        for (int i = 0; i < llt_mlt.length; i++) {
            if (llt_chkposition[i] == 1)
                mMlt[j++] = llt_mlt[i];
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mMlt);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        Spinner spin = (Spinner) findViewById(R.id.spin_ds_mlt);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new MyProcessEvent());

        HoaDonDB hoaDonDB = new HoaDonDB();
        List<String> result = null;
        try {
            result = hoaDonDB.get_DanhBo_ByMLT(mlt);
            Collections.sort(result);
            EditText txtDanhBo = (EditText)findViewById(R.id.txt_ds_db);
            txtDanhBo.setText(result.get(0));
            for(String s: result)
                System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Class tạo sự kiện
    private class MyProcessEvent implements AdapterView.OnItemSelectedListener {
        //Khi có chọn lựa thì vào hàm này
        public void onItemSelected(AdapterView<?> arg0,
                                   View arg1,
                                   int arg2,
                                   long arg3) {
            //arg2 là phần tử được chọn trong data source
            mlt = mMlt[arg2];
        }

        //Nếu không chọn gì cả
        public void onNothingSelected(AdapterView<?> arg0) {
//            selection.setText("");
        }

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
