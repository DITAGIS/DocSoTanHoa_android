package com.ditagis.hcm.docsotanhoa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;

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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ThanLe on 25/10/2017.
 */

public class DocSo extends Fragment {
    String mMlt;
    String mDanhBo;
    List<String> mDBs = new ArrayList<String>();
    EditText mEditTextCSM;
    TextView mTxtCSM;
    TextView mTxtCSC;
    TextView mTxtComplete;

    //    final HoaDonDB hoaDonDB = new HoaDonDB();
    Spinner mSpinMLT;
    Spinner mSpinDB = null;
    Spinner mSpinCode;
    ImageButton mImgbtn_Save;
    private Bitmap mBpImage;

    private LocalDatabase mLocalDatabase;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private int mSumDanhBo, mDanhBoHoanThanh;
    private String mUsername, mStaffName;
    private int mDot, mKy;
    private String mGhiChu;
    private Date currentTime;
    private ArrayAdapter<String> mAdapterDB;
    AutoCompleteTextView singleComplete;
    List<String> mMLTs;
    Uri mUri;
    private ArrayAdapter<String> mAdapterMLT;
    private View mRootView;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Activity mActivity;
    private NetworkStateChangeReceiver mStateChangeReceiver;
    private String mSearchType;

    public int getmSumDanhBo() {
        return mSumDanhBo;
    }

    public int getmDanhBoHoanThanh() {
        return mDanhBoHoanThanh;
    }

    public void setmDot(int mDot) {
        this.mDot = mDot;
    }

    public DocSo(Activity activity, LayoutInflater inflater, int mKy, int mDot, String mUsername, String staffName) {
        this.mActivity = activity;
        this.mUsername = mUsername;
        this.mStaffName = staffName;
        this.mDot = mDot;
        this.mKy = mKy;

        mRootView = inflater.inflate(R.layout.doc_so_fragment, null);

        //for camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        singleComplete = (AutoCompleteTextView) mRootView.findViewById(R.id.editauto);
        singleComplete.setAdapter(
                new ArrayAdapter<String>
                        (
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mDBs
                        ));

        ((TextView) mRootView.findViewById(R.id.txt_ds_ky)).setText(this.mKy + "");
        ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setText(this.mStaffName);

        mTxtComplete = (TextView) mRootView.findViewById(R.id.txt_ds_complete);

        mLocalDatabase = new LocalDatabase(mRootView.getContext());

        mEditTextCSM = (EditText) mRootView.findViewById(R.id.etxt_ds_CSM);
//        mEditTextCSM.setEnabled(false);
        mImgbtn_Save = (ImageButton) mRootView.findViewById(R.id.imgbtn_ds_Save);
        mTxtCSM = (TextView) mRootView.findViewById(R.id.txt_ds_CSM);
        String[] codes = {"40", "41", "42", "54", "55", "56", "58", "5F", "5K",
                "60", "61", "62", "63", "64", "65", "66", "81", "82",
                "83", "F1", "F2", "F3", "F4", "M1", "M2", "M3", "N",
                "RT", "K", "Q"};
        mMLTs = new ArrayList<String>();
        mAdapterMLT = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item, mMLTs);
        mAdapterMLT.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        ArrayAdapter<String> adapterCode = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item, codes);
        adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinCode = (Spinner) mRootView.findViewById(R.id.spin_ds_code);
        mSpinCode.setAdapter(adapterCode);
        mSpinCode.setSelection(0);
        mSpinMLT = (Spinner) mRootView.findViewById(R.id.spin_ds_mlt);
        mSpinDB = (Spinner) mRootView.findViewById(R.id.spin_ds_db);
        mSpinMLT.setAdapter(mAdapterMLT);
        mSpinMLT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMLT(mMLTs.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSearch();
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

                for (int i = 0; i < mDBs.size(); i++) {
                    if (s.toString().equals(mDBs.get(i))) {
                        mSpinDB.setSelection(i);
                    }
                }
            }
        });
        mEditTextCSM.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getImageFileName() == null) {
                    MySnackBar.make(mEditTextCSM, R.string.alert_captureBefore, true);
                    HideKeyboard.hide(mActivity);
                    return true;
                } else if (!getImageFileName().exists()) {
                    MySnackBar.make(mEditTextCSM, R.string.alert_captureBefore, true);
                    HideKeyboard.hide(mActivity);
                    return true;
                }
                return false;
            }
        });
//        mEditTextCSM.setEnabled(false);
//        mEditTextCSM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getImageFileName() == null)
//                    MySnackBar.make(mEditTextCSM, R.string.alert_captureBefore, true);
//                else if (!getImageFileName().exists()) {
//                    MySnackBar.make(mEditTextCSM, R.string.alert_captureBefore, true);
//
//                }
//            }
//        });
        mEditTextCSM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTxtCSM.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRootView.findViewById(R.id.btn_ds_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrev();
            }
        });
        mRootView.findViewById(R.id.btn_ds_next).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNext();
            }
        }));
        mRootView.findViewById(R.id.imgBtn_ds_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCamera();
            }
        });
        mRootView.findViewById(R.id.imgBtn_ds_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNote();
            }
        });
        mImgbtn_Save.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                saveImage(v);
            }
        });

    }

    private void optionSearch() {
        String[] searchTypes = {mRootView.getContext().getString(R.string.search_mlt),
                mRootView.getContext().getString(R.string.search_danhbo)};
//                mRootView.getContext().getString(R.string.search_tenKH)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Tùy chọn tìm kiếm");
        builder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_search_type, null);

        final Spinner spinSearchType = (Spinner) dialogLayout.findViewById(R.id.spin_select_type_seach);
        ArrayAdapter<String> adapterSearchType = new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_spinner_dropdown_item, searchTypes);
        adapterSearchType.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinSearchType.setAdapter(adapterSearchType);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSearchType = spinSearchType.getSelectedItem().toString();
                singleComplete.setHint(mSearchType);

                if (mSearchType.equals(mRootView.getContext().getString(R.string.search_mlt))) {
                    singleComplete.setAdapter(new ArrayAdapter<String>(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mMLTs
                    ));

                } else {
                    singleComplete.setAdapter(new ArrayAdapter<String>(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mDBs
                    ));
                }
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return mRootView;
    }

    public void refresh() {
        String dotString = mDot + "";
        if (this.mDot < 10)
            dotString = "0" + this.mDot;
        ((TextView) mRootView.findViewById(R.id.txt_ds_dot)).setText(this.mDot + "");
        for (HoaDon hoaDon : mLocalDatabase.getAllHoaDon(dotString + this.mUsername + "%")) {
            mMLTs.add(hoaDon.getMaLoTrinh());
        }
        mAdapterMLT.notifyDataSetChanged();
        setTextProgress();
    }

    private void setTextProgress() {
        String dotString = mDot + "";
        if (this.mDot < 10)
            dotString = "0" + this.mDot;
        this.mSumDanhBo = mLocalDatabase.getAllHoaDon(dotString + this.mUsername + "%").size();
        this.mDanhBoHoanThanh = mLocalDatabase.getAllDanhBo_CSM().size();
        this.mSumDanhBo += this.mDanhBoHoanThanh;
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

    }

    private void saveImage(View v) {
        // kiểm tra hình ảnh
        if(getImageFileName() == null){
            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
            return;
        }
        if (!getImageFileName().exists()) {
            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
            return;
        }
        // kiểm tra chỉ số mới
        int csc = Integer.parseInt(mTxtCSC.getText().toString());
        int csm = 0;
        if (mTxtCSM.getText().toString().length() == 0) {
            alertCSM_Null(csc, csm);
        } else {
            csm = Integer.parseInt(mTxtCSM.getText().toString());

            if (csm < csc) {
                alertCSM(csc, csm);
            } else {
                saveDB_CSM(getImageFileName().getAbsolutePath(), csc, csm);
            }
        }
    }

    public void selectMLT(String mlt) {
        mMlt = mlt;
        try {
            List<HoaDon> hoaDonList = mLocalDatabase.getAllHoaDonByMaLoTrinh(mMlt);

            mDBs.clear();
            for (HoaDon hoaDon : hoaDonList) {
                mDBs.add(hoaDon.getDanhBo());
            }
            this.mAdapterDB = new ArrayAdapter<String>(this.mRootView.getContext(), R.layout.spinner_item, mDBs);
            mAdapterDB.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            mSpinDB.setAdapter(mAdapterDB);
            mSpinDB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mDanhBo = mSpinDB.getSelectedItem().toString();
                    DanhBo_ChiSoMoi danhBo_csm = mLocalDatabase.getDanhBo_CSM(mDanhBo);
                    HoaDon hoaDon = mLocalDatabase.getHoaDon(mDanhBo);
                    ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).setText(hoaDon.getTenKhachHang());
//                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
                    mTxtCSC = (TextView) mRootView.findViewById(R.id.txt_ds_CSC);
                    mTxtCSC.setText(hoaDon.getChiSoCu());
                    ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setText(hoaDon.getGiaBieu());
                    ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).setText(hoaDon.getDiaChi());

                    if (danhBo_csm != null) {
                        mEditTextCSM.setText(danhBo_csm.getChiSoMoi());
                        mTxtCSM.setText(danhBo_csm.getChiSoMoi());
                        mGhiChu = danhBo_csm.getNote();

                    } else {
                        mGhiChu = "";
                        mEditTextCSM.setText("");
                        mTxtCSM.setText("");

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
        DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(this.mDanhBo,
                this.mMlt,
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).getText().toString(),
                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).getText().toString(),
                ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).getText().toString(),
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).getText().toString(),
                this.mSpinCode.getSelectedItem().toString(),
                csc + "",
                csm + "",
                this.mGhiChu,
                image,
                1);
        this.mLocalDatabase.saveDanhBo_CSM(danhBo_chiSoMoi);
        this.mLocalDatabase.deleteHoaDon(danhBo_chiSoMoi.getDanhBo());

        int i = mSpinMLT.getSelectedItemPosition();
        this.mAdapterMLT.remove(danhBo_chiSoMoi.getMaLoTrinh());
        this.mMLTs.remove(danhBo_chiSoMoi.getMaLoTrinh());
        selectMLT(mMLTs.get(i == mMLTs.size() ? i - 1 : i));
        this.mDanhBoHoanThanh++;
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

        Toast.makeText(mRootView.getContext(), "Đã lưu chỉ số mới", Toast.LENGTH_SHORT).show();
    }

    private void showImage(File f) {
        //get bitmap from file
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
        //--------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
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
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());//getLayoutInflater();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
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

    private void doPrev() {
        int i = mSpinMLT.getSelectedItemPosition();
        mSpinMLT.setSelection(i == 0 ? i : i - 1);
    }

    private void doNext() {
        int i = mSpinMLT.getSelectedItemPosition();
        mSpinMLT.setSelection(i == mSpinMLT.getCount() - 1 ? i : i + 1);
    }

    private void doScan() {

    }

    private void doNote() {
        //--------------------
        final EditText input = new EditText(mRootView.getContext());
        input.setMaxLines(5);
        input.setText(this.mGhiChu);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
        builder.setTitle("Ghi chú");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGhiChu = input.getText().toString();
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

    private void doCamera() {
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
//        this.mUri= FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", photo);
        this.mUri = Uri.fromFile(photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.mUri);
//        this.mUri = Uri.fromFile(photo);
        startActivityForResult(cameraIntent, REQUEST_ID_IMAGE_CAPTURE);
    }


    public File getImageFileName() {
        if (this.currentTime == null)
            return null;
        String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
        File outFile = new File(path, "DocSoTanHoa");

        if (!outFile.exists())
            outFile.mkdir();
        String datetime = this.formatter.format(this.currentTime);
        File f = new File(outFile, datetime + "_" + this.mDanhBo + ".jpeg");
        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                mBpImage = BitmapFactory.decodeFile(pathFromUri(this.mUri), options);
                if (this.mUri != null) {
                    Uri selectedImage = this.mUri;
                    mRootView.getContext().getContentResolver().notifyChange(selectedImage, null);
                    mBpImage = getBitmap(mUri.getPath());
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
                            Toast.makeText(mRootView.getContext(), "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                            this.mEditTextCSM.setEnabled(true);
                        }
                    } catch (FileNotFoundException e) {
                        MySnackBar.make(mRootView, "Lỗi khi lưu ảnh", false);
                        e.printStackTrace();
                    } catch (IOException e) {
                        MySnackBar.make(mRootView, "Lỗi khi lưu ảnh", false);
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                MySnackBar.make(mRootView, "Hủy chụp ảnh", false);
            } else {
                MySnackBar.make(mRootView, "Lỗi khi chụp ảnh", false);
            }
        }
    }


    @Nullable
    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = mRootView.getContext().getContentResolver().openInputStream(uri);

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
            in = mRootView.getContext().getContentResolver().openInputStream(uri);
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

}
