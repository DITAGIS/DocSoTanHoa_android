package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocSoActivity extends AppCompatActivity {
    String mMlt;
    String mDanhBo;
    String mArrMlt[];
    List<String> mDB = new ArrayList<String>();
    EditText editTextCSM;
    TextView txtCSM;
    private MyDatabaseHelper m_databaseHelper;
    //    final HoaDonDB hoaDonDB = new HoaDonDB();
    Spinner spinDB = null;
//    DocSoActivity.ItemClickHandle itemClickHandle = new ItemClickHandle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);
        m_databaseHelper = new MyDatabaseHelper(this);
        editTextCSM = (EditText) findViewById(R.id.etxt_ds_CSM);
        txtCSM = (TextView) findViewById(R.id.txt_ds_CSM);

        Bundle extras = getIntent().getExtras();
        mArrMlt = extras.getStringArray("mMltArr");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mArrMlt);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        Spinner spinMLT = (Spinner) findViewById(R.id.spin_ds_mlt);
        spinDB = (Spinner) findViewById(R.id.spin_ds_db);
        spinMLT.setAdapter(adapter);

        spinMLT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMlt = mArrMlt[position];
                try {
                    List<HoaDon> hoaDonList = m_databaseHelper.getAllHoaDonByMaLoTrinh(mMlt);

                    for (HoaDon hoaDon : hoaDonList) {
                        mDB.add(hoaDon.getDanhBo());
                    }
                    Collections.sort(mDB);
                    ArrayAdapter<String> adapterDB = new ArrayAdapter<String>(DocSoActivity.this, R.layout.spinner_item, mDB);
                    adapterDB.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinDB.setAdapter(adapterDB);
                    spinDB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            DocSoActivity.this.mDanhBo = spinDB.getSelectedItem().toString();
                            HoaDon hoaDon = m_databaseHelper.getHoaDon(DocSoActivity.this.mDanhBo);
                            ((TextView) findViewById(R.id.txt_ds_tenKH)).setText(hoaDon.getTenKhachHang());
                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
                            ((TextView) findViewById(R.id.txt_ds_CSC)).setText(hoaDon.getChiSoCu());
                            ((TextView) findViewById(R.id.txt_ds_giabieu)).setText(hoaDon.getGiaBieu());
                            ((TextView) findViewById(R.id.txt_ds_diachi)).setText(hoaDon.getDiaChi());
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

        editTextCSM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtCSM.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void doPrev(View v) {
        int i = spinDB.getSelectedItemPosition();
        spinDB.setSelection(i == 0 ? i : i - 1);
    }

    public void doNext(View v) {
        int i = spinDB.getSelectedItemPosition();
        spinDB.setSelection(i == spinDB.getCount() - 1 ? i : i + 1);
    }

    public void doCamera(View v) {
        Intent intent = new Intent(DocSoActivity.this, DocSoChupAnhActivity.class);
        intent.putExtra("danhbo", this.mDanhBo);

        startActivity(intent);
    }

    public void doLayLoTrinh(View v) {
        finish();
    }

    public void doQuanLyDocSo(View v) {
//        Intent intent = new Intent(DocSoActivity.this, QuanLyDocSoActivity.class);
//
//        startActivity(intent);
        Toast.makeText(this, "Chức năng đang được cập nhật", Toast.LENGTH_SHORT).show();
    }
}

