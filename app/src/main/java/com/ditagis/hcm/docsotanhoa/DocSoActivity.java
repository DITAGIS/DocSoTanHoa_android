package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DocSoActivity extends AppCompatActivity {
    String mMlt;
    String mDanhBo;
    String mArrMlt[];
    List<String> mDB = new ArrayList<String>();
    EditText editTextCSM;
    TextView txtCSM;
    TextView txtCSC;
    private LocalDatabase m_databaseHelper;
    //    final HoaDonDB hoaDonDB = new HoaDonDB();
    Spinner spinDB = null;
    Spinner spinCode;
    ImageButton imgbtn_Save;
    private Intent intentCaptureImage;
    private Bitmap mBpImage;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;

//    DocSoActivity.ItemClickHandle itemClickHandle = new ItemClickHandle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);
        Calendar calendar = Calendar.getInstance();
        ((TextView) findViewById(R.id.txt_ds_ky)).setText(calendar.get(Calendar.MONTH) + 1+"");
        ((TextView) findViewById(R.id.txt_ds_dot)).setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
        m_databaseHelper = new LocalDatabase(this);
        editTextCSM = (EditText) findViewById(R.id.etxt_ds_CSM);
        imgbtn_Save = (ImageButton) findViewById(R.id.imgbtn_ds_Save);
        txtCSM = (TextView) findViewById(R.id.txt_ds_CSM);
        String[] codes = {"4", "5", "6", "8", "F", "K", "M", "N", "Q"};
        Bundle extras = getIntent().getExtras();
        mArrMlt = extras.getStringArray("mMltArr");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mArrMlt);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        ArrayAdapter<String> adapterCode = new ArrayAdapter<String>(this, R.layout.spinner_item, codes);
        adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCode = (Spinner) findViewById(R.id.spin_ds_code);
        spinCode.setAdapter(adapterCode);
        spinCode.setSelection(0);
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
//                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
                            txtCSC = (TextView) findViewById(R.id.txt_ds_CSC);
                            txtCSC.setText(hoaDon.getChiSoCu());
                            ((TextView) findViewById(R.id.txt_ds_giabieu)).setText(hoaDon.getGiaBieu());
                            ((TextView) findViewById(R.id.txt_ds_diachi)).setText(hoaDon.getDiaChi());
                            DocSoActivity.this.editTextCSM.setText("");
                            DocSoActivity.this.txtCSM.setText("");
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

        imgbtn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiểm tra hình ảnh
                File sdCardDirectory = Environment.getExternalStorageDirectory();
                File image = new File(sdCardDirectory, "DocSoTanHoa" + File.separator + mDanhBo + ".png");
                if (!image.exists()) {
                    Toast.makeText(DocSoActivity.this, "Thiếu hình ảnh!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(DocSoActivity.this, "Đã có hình ảnh!!!", Toast.LENGTH_SHORT).show();
                }
                // kiểm tra chỉ số mới
                int csc = 0;
                int csm = 0;
                if (txtCSM.getText().length() > 0) {
                    csm = Integer.parseInt(txtCSM.getText().toString());
                    csc = Integer.parseInt(txtCSC.getText().toString());
                    if (csm < csc) {
                        // kiểm tra code
                    }
                }
            }
        });
    }
    public void doScan(View v) {

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
        if (!requestPermissonCamera())
            return;
        // Tạo một Intent không tường minh,
        // để yêu cầu hệ thống mở Camera chuẩn bị chụp hình.
        this.intentCaptureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (this.intentCaptureImage.resolveActivity(getPackageManager()) != null)
            // Start Activity chụp hình, và chờ đợi kết quả trả về.
            this.startActivityForResult(this.intentCaptureImage, REQUEST_ID_IMAGE_CAPTURE);
    }

    public boolean requestPermissonCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Không cho phép bật CAMERA", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    // Khi activy chụp hình (Hoặc quay video) hoàn thành, phương thức này sẽ được gọi.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                mBpImage = (Bitmap) data.getExtras().get("data");
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Hủy chụp hình", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Lỗi khi chụp hình", Toast.LENGTH_LONG).show();
            }
        }
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

