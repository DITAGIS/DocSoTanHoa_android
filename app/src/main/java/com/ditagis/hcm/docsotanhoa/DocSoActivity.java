package com.ditagis.hcm.docsotanhoa;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DocSoActivity extends AppCompatActivity {
    String mMlt;
    String mDanhBo;
    List<String> mDBs = new ArrayList<String>();
    EditText editTextCSM;
    TextView txtCSM;
    TextView txtCSC;
    TextView txtComplete;

    //    final HoaDonDB hoaDonDB = new HoaDonDB();
    Spinner spinMLT;
    Spinner spinDB = null;
    Spinner spinCode;
    ImageButton imgbtn_Save;
    private Bitmap mBpImage;

    private LocalDatabase mLocalDatabase;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private int mSumDanhBo, mDanhBoHoanThanh;
    private String mUsername;
    private int mDot;
    private String mGhiChu;
    private Date currentTime;
    private ArrayAdapter<String> adapterDB;
    AutoCompleteTextView singleComplete;
    List<String> mMLTs;
    Uri uri;
    private ArrayAdapter<String> adapterMLT;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//    DocSoActivity.ItemClickHandle itemClickHandle = new ItemClickHandle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        singleComplete = (AutoCompleteTextView) findViewById(R.id.editauto);
        singleComplete.setAdapter(
                new ArrayAdapter<String>
                        (
                                this,
                                android.R.layout.simple_list_item_1,
                                mDBs
                        ));

        if (getIntent().getExtras().getInt("ky") > 0)
            ((TextView) findViewById(R.id.txt_ds_ky)).setText(getIntent().getExtras().getInt("ky") + "");
        if (getIntent().getExtras().getInt("dot") > 0)
            this.mDot = getIntent().getExtras().getInt("dot");

        ((TextView) findViewById(R.id.txt_ds_dot)).setText(this.mDot + "");
        if (getIntent().getExtras().getString("username") != null)
            this.mUsername = getIntent().getExtras().getString("username");
        this.txtComplete = (TextView) findViewById(R.id.txt_ds_complete);

        mLocalDatabase = new LocalDatabase(this);

        editTextCSM = (EditText) findViewById(R.id.etxt_ds_CSM);
        editTextCSM.setEnabled(false);
        imgbtn_Save = (ImageButton)

                findViewById(R.id.imgbtn_ds_Save);

        txtCSM = (TextView)

                findViewById(R.id.txt_ds_CSM);


        String[] codes = {"40", "41", "42", "54", "55", "56", "58", "5F", "5K",
                "60", "61", "62", "63", "64", "65", "66", "81", "82",
                "83", "F1", "F2", "F3", "F4", "M1", "M2", "M3", "N",
                "RT", "K", "Q"};


        List<HoaDon> hoaDons = this.mLocalDatabase.getAllHoaDon(mDot + mUsername + "%");
        mMLTs = new ArrayList<String>();
        int i = 0;
        for (HoaDon hoaDon : hoaDons) {
            mMLTs.add(hoaDon.getMaLoTrinh());
        }
        DocSoActivity.this.mSumDanhBo = 0;
        for (String mlt : mMLTs)
            DocSoActivity.this.mSumDanhBo += this.mLocalDatabase.getAllHoaDonByMaLoTrinh(mlt).size();
        DocSoActivity.this.mDanhBoHoanThanh = DocSoActivity.this.mLocalDatabase.getAllDanhBo_CSM().size();
DocSoActivity.this.mSumDanhBo += DocSoActivity.this.mDanhBoHoanThanh;
        DocSoActivity.this.txtComplete.setText(DocSoActivity.this.mDanhBoHoanThanh + "/" + DocSoActivity.this.mSumDanhBo);
        adapterMLT = new ArrayAdapter<String>(this, R.layout.spinner_item, mMLTs);
        adapterMLT.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        ArrayAdapter<String> adapterCode = new ArrayAdapter<String>(this, R.layout.spinner_item, codes);
        adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCode = (Spinner)

                findViewById(R.id.spin_ds_code);
        spinCode.setAdapter(adapterCode);
        spinCode.setSelection(0);
        spinMLT = (Spinner) findViewById(R.id.spin_ds_mlt);
        spinDB = (Spinner) findViewById(R.id.spin_ds_db);
        spinMLT.setAdapter(adapterMLT);
        spinMLT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMLT(DocSoActivity.this.mMLTs.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        singleComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                for (int i = 0; i < DocSoActivity.this.mDBs.size(); i++) {
                    if (s.toString().equals(DocSoActivity.this.mDBs.get(i))) {
                        DocSoActivity.this.spinDB.setSelection(i);
                    }
                }
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

        imgbtn_Save.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                // kiểm tra hình ảnh
                if (!getImageFileName().exists()) {
                    Toast.makeText(DocSoActivity.this, "Chưa có hình ảnh!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // kiểm tra chỉ số mới
                int csc = Integer.parseInt(txtCSC.getText().toString());
                int csm = 0;
                if (txtCSM.getText().toString().length() == 0) {
                    alertCSM_Null(csc, csm);
                } else {
                    csm = Integer.parseInt(txtCSM.getText().toString());

                    if (csm < csc) {
                        alertCSM(csc, csm);
                    } else {
                        saveDB_CSM(getImageFileName().getAbsolutePath(), csc, csm);
                    }
                }
            }
        });
    }

    public void selectMLT(String mlt) {
        mMlt = mlt;
        try {
            List<HoaDon> hoaDonList = mLocalDatabase.getAllHoaDonByMaLoTrinh(mMlt);

            mDBs.clear();
            for (HoaDon hoaDon : hoaDonList) {
                mDBs.add(hoaDon.getDanhBo());
            }
            DocSoActivity.this.adapterDB = new ArrayAdapter<String>(DocSoActivity.this, R.layout.spinner_item, mDBs);
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

                    } else {
                        DocSoActivity.this.mGhiChu = "";
                        DocSoActivity.this.editTextCSM.setText("");
                        DocSoActivity.this.txtCSM.setText("");

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDB_CSM(String image, int csc, int csm) {
        DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(DocSoActivity.this.mDanhBo,
                DocSoActivity.this.mMlt,
                ((TextView) findViewById(R.id.txt_ds_tenKH)).getText().toString(),
                ((TextView) findViewById(R.id.txt_ds_diachi)).getText().toString(),
                ((EditText) findViewById(R.id.etxt_ds_sdt)).getText().toString(),
                ((TextView) findViewById(R.id.txt_ds_giabieu)).getText().toString(),
                DocSoActivity.this.spinCode.getSelectedItem().toString(),
                csc + "",
                csm + "",
                this.mGhiChu,
                image,
                1);
        DocSoActivity.this.mLocalDatabase.saveDanhBo_CSM(danhBo_chiSoMoi);
        DocSoActivity.this.mLocalDatabase.deleteHoaDon(danhBo_chiSoMoi.getDanhBo());

        int i = spinMLT.getSelectedItemPosition();
        DocSoActivity.this.adapterMLT.remove(danhBo_chiSoMoi.getMaLoTrinh());
        DocSoActivity.this.mMLTs.remove(danhBo_chiSoMoi.getMaLoTrinh());
        selectMLT(DocSoActivity.this.mMLTs.get(i == DocSoActivity.this.mMLTs.size() ? i - 1 : i));
//
//        spinMLT.setSelection(i == spinMLT.getCount() - 1 ? i : i + 1);
//        spinMLT.setSelection(i+1);
//        spinMLT.setSelection(i);
        DocSoActivity.this.mDanhBoHoanThanh++;
        DocSoActivity.this.txtComplete.setText(DocSoActivity.this.mDanhBoHoanThanh + "/" + DocSoActivity.this.mSumDanhBo );

        Toast.makeText(DocSoActivity.this, "Đã lưu chỉ số mới", Toast.LENGTH_SHORT).show();

        //select next danhbo
//        String danhBo = DocSoActivity.this.mDanhBo;
//
//        int i = spinDB.getSelectedItemPosition();
//        spinDB.setSelection(i == spinDB.getCount() - 1 ? i : i + 1);

//        DocSoActivity.this.adapterDB.remove(danhBo);
//
//        i = spinDB.getSelectedItemPosition();
//        spinDB.setSelection(i == 0 ? i : i - 1);
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

    private void alertCSM_Null(final int csc, final int csm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DocSoActivity.this);
        builder.setTitle("Chưa nhập chỉ số mới");
        builder.setMessage("Kiểm tra code?")
                .setCancelable(true)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveDB_CSM(getImageFileName().getAbsolutePath(), csc, csm);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Kiểm tra lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void alertCSM(final int csc, final int csm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DocSoActivity.this);
        builder.setTitle("Chỉ số mới nhỏ hơn chỉ số cũ");
        builder.setMessage("Kiểm tra danh bộ hiện tại thuộc diện thay mới đồng hồ?")
                .setCancelable(true)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveDB_CSM(getImageFileName().getAbsolutePath(), csc, csm);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Kiểm tra lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void doPrev(View v) {
        int i = spinMLT.getSelectedItemPosition();
        spinMLT.setSelection(i == 0 ? i : i - 1);
    }

    public void doNext(View v) {
        int i = spinMLT.getSelectedItemPosition();
        spinMLT.setSelection(i == spinMLT.getCount() - 1 ? i : i + 1);
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
        File f = getImageFileName();
        if (f != null && f.exists()) {
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

        this.currentTime = Calendar.getInstance().getTime();
        File photo = getImageFileName();
//        this.uri= FileProvider.getUriForFile(DocSoActivity.this, DocSoActivity.this.getApplicationContext().getPackageName() + ".my.package.name.provider", photo);
        this.uri = Uri.fromFile(photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.uri);
//        this.uri = Uri.fromFile(photo);
        startActivityForResult(cameraIntent, REQUEST_ID_IMAGE_CAPTURE);
    }


    public File getImageFileName() {
        if (DocSoActivity.this.currentTime == null)
            return null;
        String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
        File outFile = new File(path, "DocSoTanHoa");

        if (!outFile.exists())
            outFile.mkdir();
        String datetime = DocSoActivity.this.formatter.format(DocSoActivity.this.currentTime);
        File f = new File(outFile, datetime + "_" + this.mDanhBo + ".jpeg");
        return f;
    }

    private String pathFromUri(Uri imageUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, filePathColumn,
                null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        return filePath;
    }    // Khi activy chụp hình (Hoặc quay video) hoàn thành, phương thức này sẽ được gọi.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                mBpImage = BitmapFactory.decodeFile(pathFromUri(DocSoActivity.this.uri), options);
                if (DocSoActivity.this.uri != null) {
                    Uri selectedImage = DocSoActivity.this.uri;
                    getContentResolver().notifyChange(selectedImage, null);
                    mBpImage = getBitmap(DocSoActivity.this.uri.getPath());
//                    mBpImage = (Bitmap) data.getExtras().get("data");
//                    this.currentTime = Calendar.getInstance().getTime();
                    FileOutputStream fos = null;
                    try {
                        if (mBpImage != null) {
                            fos = new FileOutputStream(getImageFileName());

                            Matrix matrix = new Matrix();

                            matrix.postRotate(90);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(mBpImage, 0, 0, mBpImage.getWidth(), mBpImage.getHeight(), matrix, true);
                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            Toast.makeText(this, "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                            DocSoActivity.this.editTextCSM.setEnabled(true);
                        }
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Hủy chụp hình", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Lỗi khi chụp hình", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Nullable
    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    public void doLayLoTrinh(View v) {
        Intent intent = new Intent(DocSoActivity.this, LayLoTrinhActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(DocSoActivity.this, QuanLyDocSoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("dot", mDot);
        intent.putExtra("username", mUsername);
        startActivity(intent);
//        Toast.makeText(this, "Chức năng đang được cập nhật", Toast.LENGTH_SHORT).show();
    }
}