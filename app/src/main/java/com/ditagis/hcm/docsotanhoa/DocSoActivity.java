package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocSoActivity extends AppCompatActivity {
    String mlt;
    String mMlt[];
    String mDB[];
    final HoaDonDB hoaDonDB = new HoaDonDB();
    Spinner spinDB = null;
    DocSoActivity.ItemClickHandle itemClickHandle = new ItemClickHandle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> llt_mlt = extras.getStringArrayList("mlt");
        int llt_chkposition[] = extras.getIntArray("chkPosition");
        mMlt = new String[extras.getInt("sum_mlt")];

        int j = 0;
        for (int i = 0; i < llt_mlt.size(); i++) {
            if (llt_chkposition[i] == 1)
                mMlt[j++] = llt_mlt.get(i);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mMlt);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        Spinner spinMLT = (Spinner) findViewById(R.id.spin_ds_mlt);
        spinDB = (Spinner) findViewById(R.id.spin_ds_db);
        spinMLT.setAdapter(adapter);

        spinMLT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mlt = mMlt[position];


                List<String> result = null;
                try {
                    result = hoaDonDB.get_DanhBo_ByMLT(mlt);
                    Collections.sort(result);
                    mDB = new String[result.size()];
                    for (int i = 0; i < result.size(); i++)
                        mDB[i] = result.get(i);
                    ArrayAdapter<String> adapterDB = new ArrayAdapter<String>(DocSoActivity.this, android.R.layout.simple_spinner_item, mDB);
                    adapterDB.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinDB.setAdapter(adapterDB);
                    spinDB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            itemClickHandle.execute(spinDB.getSelectedItem().toString());
                            HoaDon hoaDon = hoaDonDB.getByDanhBo(spinDB.getSelectedItem().toString());

                            ((TextView) findViewById(R.id.txt_ds_tenKH)).setText(hoaDon.getTenKhachHang());
                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
                            ((TextView) findViewById(R.id.txt_ds_CSC)).setText(hoaDon.getChiSoCu());
                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
                            ((TextView) findViewById(R.id.txt_ds_giabieu)).setText(hoaDon.getGiaBieu());
// ((TextView)findViewById(R.id.txt_ds_tiennuoc)).setText(0);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public class ItemClickHandle extends AsyncTask<String, HoaDon, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(DocSoActivity.this, "Đang tải thông tin khách hàng", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(String... params) {
            HoaDon hoaDon = hoaDonDB.getByDanhBo(params[0]);
            publishProgress(hoaDon);
            return null;
        }

        @Override
        protected void onProgressUpdate(HoaDon... values) {
            super.onProgressUpdate(values);
            HoaDon hoaDon = values[0];

            ((TextView) findViewById(R.id.txt_ds_tenKH)).setText(hoaDon.getTenKhachHang());
            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
            ((TextView) findViewById(R.id.txt_ds_CSC)).setText(hoaDon.getChiSoCu());
            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
            ((TextView) findViewById(R.id.txt_ds_giabieu)).setText(hoaDon.getGiaBieu());
        }


    }

    public void doPrev(View v) {
        int i = spinDB.getSelectedItemPosition();
        spinDB.setSelection(i == 0 ? i : i - 1);
    }

    public void doNext(View v) {
        int i = spinDB.getSelectedItemPosition();
        Toast.makeText(DocSoActivity.this, spinDB.getCount() + "", Toast.LENGTH_SHORT).show();
        spinDB.setSelection(i == spinDB.getCount() - 1 ? i : i + 1);
    }

    public void doCamera(View v) {
        Intent intent = new Intent(DocSoActivity.this, DocSoChupAnhActivity.class);

        startActivity(intent);
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

