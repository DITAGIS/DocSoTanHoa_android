package com.ditagis.hcm.docsotanhoa;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DocSoActivity extends AppCompatActivity {
    String mMlt;
    String mDanhBo;
    String mArrMlt[];
    List<String> mDBs = new ArrayList<String>();
    EditText editTextCSM;
    TextView txtCSM;
    TextView txtCSC;
    TextView txtComplete;
    TextView txtSaveState;
    private LocalDatabase mLocalDatabase;
    //    final HoaDonDB hoaDonDB = new HoaDonDB();
    Spinner spinMLT;
    Spinner spinDB = null;
    Spinner spinCode;
    ImageButton imgbtn_Save;
    private Bitmap mBpImage;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private int mSumDanhBo, mDanhBoHoanThanh;
    private boolean isThayMoiDongHo;
    private int mKy;
    private int mDot;
    private String mGhiChu;
    private final String SAVED = "Đã lưu";
    private final String UN_SAVED = "Chưa lưu";
//    DocSoActivity.ItemClickHandle itemClickHandle = new ItemClickHandle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);

        Calendar calendar = Calendar.getInstance();
        this.mKy = calendar.get(Calendar.MONTH) + 1;
        this.mDot = calendar.get(Calendar.DAY_OF_MONTH);
        ((TextView) findViewById(R.id.txt_ds_ky)).setText(this.mKy + "");
        ((TextView) findViewById(R.id.txt_ds_dot)).setText(this.mDot + "");
        this.txtSaveState = (TextView) findViewById(R.id.txt_ds_save);

        this.txtComplete = (TextView) findViewById(R.id.txt_ds_complete);
        mLocalDatabase = new LocalDatabase(this);
        editTextCSM = (EditText) findViewById(R.id.etxt_ds_CSM);
        imgbtn_Save = (ImageButton) findViewById(R.id.imgbtn_ds_Save);
        txtCSM = (TextView) findViewById(R.id.txt_ds_CSM);


        String[] codes = {"40", "41", "42", "54", "55", "56", "58", "5F", "5K",
                "60", "61", "62", "63", "64", "65", "66", "81", "82",
                "83", "F1", "F2", "F3", "F4", "M1", "M2", "M3", "N",
                "RT", "K", "Q"};


        Bundle extras = getIntent().getExtras();
        mArrMlt = extras.getStringArray("mMltArr");

        DocSoActivity.this.mSumDanhBo = 0;
        for (String mlt : mArrMlt)
            DocSoActivity.this.mSumDanhBo += this.mLocalDatabase.getAllHoaDonByMaLoTrinh(mlt).size();
        DocSoActivity.this.mDanhBoHoanThanh = 0;
        DocSoActivity.this.txtComplete.setText(DocSoActivity.this.mDanhBoHoanThanh + "/" + DocSoActivity.this.mSumDanhBo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, mArrMlt);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        ArrayAdapter<String> adapterCode = new ArrayAdapter<String>(this, R.layout.spinner_item, codes);
        adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCode = (Spinner) findViewById(R.id.spin_ds_code);
        spinCode.setAdapter(adapterCode);
        spinCode.setSelection(0);
        spinMLT = (Spinner) findViewById(R.id.spin_ds_mlt);
        spinDB = (Spinner) findViewById(R.id.spin_ds_db);
        spinMLT.setAdapter(adapter);

        spinMLT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMlt = DocSoActivity.this.spinMLT.getSelectedItem().toString();
                try {
                    List<HoaDon> hoaDonList = mLocalDatabase.getAllHoaDonByMaLoTrinh(mMlt);

                    mDBs.clear();
                    for (HoaDon hoaDon : hoaDonList) {
                        mDBs.add(hoaDon.getDanhBo());
                    }
                    Collections.sort(mDBs);
                    ArrayAdapter<String> adapterDB = new ArrayAdapter<String>(DocSoActivity.this, R.layout.spinner_item, mDBs);
                    adapterDB.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinDB.setAdapter(adapterDB);
                    spinDB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            DocSoActivity.this.mDanhBo = spinDB.getSelectedItem().toString();
                            DanhBo_ChiSoMoi danhBo_csm = DocSoActivity.this.mLocalDatabase.getDanhBo_CSM(DocSoActivity.this.mDanhBo);
                            HoaDon hoaDon = mLocalDatabase.getHoaDon(DocSoActivity.this.mDanhBo);
                            ((TextView) findViewById(R.id.txt_ds_tenKH)).setText(hoaDon.getTenKhachHang());
//                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
                            txtCSC = (TextView) findViewById(R.id.txt_ds_CSC);
                            txtCSC.setText(hoaDon.getChiSoCu());
                            ((TextView) findViewById(R.id.txt_ds_giabieu)).setText(hoaDon.getGiaBieu());
                            ((TextView) findViewById(R.id.txt_ds_diachi)).setText(hoaDon.getDiaChi());

                            if (danhBo_csm != null) {
                                DocSoActivity.this.editTextCSM.setText(danhBo_csm.getChiSoMoi());
                                DocSoActivity.this.txtCSM.setText(danhBo_csm.getChiSoMoi());
                                DocSoActivity.this.mGhiChu = danhBo_csm.getNote();

                                DocSoActivity.this.txtSaveState.setText(SAVED);
                                DocSoActivity.this.txtSaveState.setTextColor(ContextCompat.getColor(DocSoActivity.this,
                                        R.color.colorBlueLight));
                            } else {
                                DocSoActivity.this.mGhiChu = "";
                                DocSoActivity.this.editTextCSM.setText("");
                                DocSoActivity.this.txtCSM.setText("");

                                DocSoActivity.this.txtSaveState.setText(UN_SAVED);
                                DocSoActivity.this.txtSaveState.setTextColor(ContextCompat.getColor(DocSoActivity.this,
                                        R.color.colorAccent));
                            }
//                            if (DocSoActivity.this.mLocalDatabase.getStateDanhBo_CSM(DocSoActivity.this.mDanhBo)) {
//                                DocSoActivity.this.txtSaveState.setText(SAVED);
//                                DocSoActivity.this.txtSaveState.setTextColor(ContextCompat.getColor(DocSoActivity.this,
//                                        R.color.colorBlueLight));
//                            } else {
//                                DocSoActivity.this.txtSaveState.setText(UN_SAVED);
//                                DocSoActivity.this.txtSaveState.setTextColor(ContextCompat.getColor(DocSoActivity.this,
//                                        R.color.colorAccent));
//                            }
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
                if (!getImageFileName().exists()) {
                    Toast.makeText(DocSoActivity.this, "Chưa có hình ảnh!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // kiểm tra chỉ số mới
                int csc = 0;
                int csm = 0;
                if (txtCSM.getText().length() == 0) {
                    //kiem tra code
                } else {
                    csm = Integer.parseInt(txtCSM.getText().toString());
                    csc = Integer.parseInt(txtCSC.getText().toString());
                    if (csm < csc) {
                        if (alertCSM()) {
                            saveDB_CSM(getImageFileName().getAbsolutePath(), csc, csm);
                            //Xử lý lưu danh bộ
                        } else {
                            //TODO
                        }
                        DocSoActivity.this.isThayMoiDongHo = false;
                    } else {
                        saveDB_CSM(getImageFileName().getAbsolutePath(), csc, csm);
                    }
                }
            }
        });
    }

    private void saveDB_CSM(String image, int csc, int csm) {
        DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(DocSoActivity.this.mDanhBo,
                DocSoActivity.this.mMlt,
                ((TextView)findViewById(R.id.txt_ds_tenKH)).getText().toString(),
                ((TextView)findViewById(R.id.txt_ds_diachi)).getText().toString(),
                ((EditText)findViewById(R.id.etxt_ds_sdt)).getText().toString(),
                DocSoActivity.this.spinCode.getSelectedItem().toString(),
                csc + "",
                csm + "",
                this.mGhiChu,
                image,
                1);
        DocSoActivity.this.mLocalDatabase.saveDanhBo_CSM(danhBo_chiSoMoi);
        DocSoActivity.this.mDanhBoHoanThanh++;
        DocSoActivity.this.txtComplete.setText(DocSoActivity.this.mDanhBoHoanThanh + "/" + DocSoActivity.this.mSumDanhBo);
        DocSoActivity.this.txtSaveState.setText(SAVED);
        DocSoActivity.this.txtSaveState.setTextColor(ContextCompat.getColor(DocSoActivity.this,
                R.color.colorBlueLight));

        Toast.makeText(DocSoActivity.this, "Đã lưu chỉ số mới", Toast.LENGTH_SHORT).show();
    }

    private void showImage(File f) {
        //get bitmap from file
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
        //--------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Chụp lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                capture();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_imageview_docso, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Without this line there is a very small border around the image (1px)
        dialog.getWindow().setBackgroundDrawable(null);

        dialog.show();
        ImageView image = (ImageView) dialog.findViewById(R.id.imgView_docso);

        BitmapDrawable resizedDialogImage = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));

        image.setBackground(resizedDialogImage);

    }

    private boolean alertCSM() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DocSoActivity.this);
        builder.setTitle("Chỉ số mới nhỏ hơn chỉ số cũ");
        builder.setMessage("Kiểm tra danh bộ hiện tại thuộc diện thay mới đồng hồ?")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DocSoActivity.this.isThayMoiDongHo = true;
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return this.isThayMoiDongHo;
    }

    public void doPrev(View v) {
        int i = spinDB.getSelectedItemPosition();
        spinDB.setSelection(i == 0 ? i : i - 1);
    }

    public void doNext(View v) {
        int i = spinDB.getSelectedItemPosition();
        spinDB.setSelection(i == spinDB.getCount() - 1 ? i : i + 1);
    }

    public void doScan(View v) {

    }

    public void doNote(View v) {
        //--------------------
        final EditText input = new EditText(this);
        input.setMaxLines(5);
        input.setText(this.mGhiChu);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ghi chú");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocSoActivity.this.mGhiChu = input.getText().toString();
                dialog.dismiss();
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setView(input);
        dialog.show();
    }

    public void doCamera(View v) {
        if (!requestPermissonCamera())
            return;
        if (getImageFileName().exists()) {
            showImage(getImageFileName());


        } else {
            capture();
        }
    }

    private void capture() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, "new-photo-name.jpg");
        startActivityForResult(cameraIntent, REQUEST_ID_IMAGE_CAPTURE);
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

    public boolean requestPermissonWriteFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_ID_IMAGE_CAPTURE);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Không cho phép lưu ảnh!!!", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public File getImageFileName() {
        String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
        File outFile = new File(path, "DocSoTanHoa");

        if (!outFile.exists())
            outFile.mkdir();
        File f = new File(outFile, mDanhBo + ".jpeg");
        return f;
    }

    // Khi activy chụp hình (Hoặc quay video) hoàn thành, phương thức này sẽ được gọi.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (!requestPermissonWriteFile())
                    return;
                mBpImage = (Bitmap) data.getExtras().get("data");

                FileOutputStream fos = null;
                try {

                    fos = new FileOutputStream(getImageFileName());
                    mBpImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(this, "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

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
        Intent intent = new Intent(DocSoActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
//        Toast.makeText(this, "Chức năng đang được cập nhật", Toast.LENGTH_SHORT).show();
    }
}

