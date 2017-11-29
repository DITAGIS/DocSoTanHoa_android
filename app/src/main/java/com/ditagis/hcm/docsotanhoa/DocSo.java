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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.CustomArrayAdapter;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.theme.ThemeUtils;
import com.ditagis.hcm.docsotanhoa.utities.CalculateCSM_TieuThu;
import com.ditagis.hcm.docsotanhoa.utities.Code;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;
import com.ditagis.hcm.docsotanhoa.utities.MyAlertByHardware;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;
import com.ditagis.hcm.docsotanhoa.utities.Note;

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
    private static final int MIN_SIZE = 500000;
    String mMlt;
    String mDanhBo;
    List<String> mDBs = new ArrayList<String>(), mTenKHs = new ArrayList<>(), mDiaChis = new ArrayList<>();
    EditText mEditTextCSM;
    TextView mTxtCSM;
    TextView mTxtCSC;
    TextView mTxtComplete;
    TextView mTxtTT, mTxtTT1, mTxtTT2, mTxtTT3;
    Spinner mSpinMLT;
    Spinner mSpinDB = null;
    Spinner mSpinCode;
    private Bitmap mBpImage;
    private HoaDon mHoaDon;

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private int mSumDanhBo, mDanhBoHoanThanh;
    private String mStaffName;
    private int mDot, mKy;
    private String mGhiChu;
    private Date currentTime;
    private ArrayAdapter<String> mAdapterDB;
    private ArrayAdapter<String> mAdapterCode;
    AutoCompleteTextView singleComplete;
    List<String> mMLTs;
    Uri mUri;
    private ArrayAdapter<String> mAdapterMLT;
    private View mRootView;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Activity mActivity;
    private String mSearchType;
    private boolean isAllowChangeSdt = false;
    private int mSelected_theme;
    private String mLike;

    public void setmSelected_theme(int mSelected_theme) {
        this.mSelected_theme = mSelected_theme;
    }

    public int getmSumDanhBo() {
        return mSumDanhBo;
    }


    public DocSo(Activity activity, LayoutInflater inflater, int mKy, int mDot, String mUsername, String staffName, int theme) {
        this.mActivity = activity;
        this.mStaffName = staffName;
        this.mDot = mDot;
        this.mKy = mKy;
        this.mSelected_theme = theme;
//        this.mLike = "__" + mUsername + "%";
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        this.mLike = dotString + mUsername + "%";
        mRootView = inflater.inflate(R.layout.doc_so_fragment, null);
        mTxtTT = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu);
        mTxtTT1 = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1);
        mTxtTT2 = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2);
        mTxtTT3 = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3);
        //for camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        singleComplete = (AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds);
        singleComplete.setAdapter(
                new CustomArrayAdapter
                        (
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mDBs
                        ));
        singleComplete.setBackgroundResource(R.layout.edit_text_styles);

        ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).setBackgroundResource(R.layout.edit_text_styles);
        ((TextView) mRootView.findViewById(R.id.txt_ds_ky)).setText(this.mKy + "");
        ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setText(this.mStaffName);

        mTxtComplete = (TextView) mRootView.findViewById(R.id.txt_ds_complete);


        mEditTextCSM = (EditText) mRootView.findViewById(R.id.etxt_ds_CSM);
//        mEditTextCSM.setEnabled(false);
        mTxtCSM = (TextView) mRootView.findViewById(R.id.txt_ds_CSM);


        mMLTs = new ArrayList<String>();

        for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
            mMLTs.add(hoaDon.getMaLoTrinh());
        }
        mSpinMLT = (Spinner) mRootView.findViewById(R.id.spin_ds_mlt);
        mSpinDB = (Spinner) mRootView.findViewById(R.id.spin_ds_db);
        ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSearch();
            }
        });

        mSearchType = mRootView.getContext().getString(R.string.search_mlt);
        singleComplete.setAdapter(new CustomArrayAdapter(
                mRootView.getContext(),
                android.R.layout.simple_list_item_1,
                mMLTs
        ));

        singleComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSearchType.equals(mRootView.getContext().getString(R.string.search_mlt))) {
                    for (int i = 0; i < mMLTs.size(); i++) {
                        if (s.toString().equals(mMLTs.get(i))) {
                            mSpinMLT.setSelection(i);
                        }
                    }
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                    for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
                        if (s.toString().equals(hoaDon.getDanhBo()))
                            for (int i = 0; i < mMLTs.size(); i++)
                                if (hoaDon.getMaLoTrinh().equals(mMLTs.get(i)))
                                    mSpinMLT.setSelection(i);
                    }
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                    for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
                        if (s.toString().equals(hoaDon.getTenKhachHang()))
                            for (int i = 0; i < mMLTs.size(); i++)
                                if (hoaDon.getMaLoTrinh().equals(mMLTs.get(i)))
                                    mSpinMLT.setSelection(i);
                    }
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                    for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
                        if (s.toString().equals(hoaDon.getDiaChi()))
                            for (int i = 0; i < mMLTs.size(); i++)
                                if (hoaDon.getMaLoTrinh().equals(mMLTs.get(i)))
                                    mSpinMLT.setSelection(i);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRootView.findViewById(R.id.layout_ds_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).getText().toString().trim();
                if (phone.length() == 0) {
                    MySnackBar.make(mRootView, mRootView.getContext().getString(R.string.call_error), true);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
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
        mEditTextCSM.setBackgroundResource(R.layout.edit_text_styles);
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
//                mTxtCSM.setText(s.toString());
//
//                if (mTxtCSM.getText().toString().length() > 0) {
//                    int sanLuong = Integer.parseInt(mTxtCSM.getText().toString()) - Integer.parseInt(mTxtCSC.getText().toString());
//                    mTxtTT.setText(sanLuong + "");
//                    if (checkCSMFluctuation()) {
//                        ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
//                    } else {
//                        ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
//                    }
//                }
                String code = mSpinCode.getSelectedItem().toString().substring(0, 2);
                ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setText(code);
                CalculateCSM_TieuThu csm_tieuThu = new CalculateCSM_TieuThu(code, mHoaDon.getCode_CSC_SanLuong(), Integer.parseInt(mTxtCSC.getText().toString()), mEditTextCSM.getText().toString());

                mTxtCSM.setText(csm_tieuThu.getCSM());
//                mEditTextCSM.setText(csm_tieuThu.getCSM());
                mTxtTT.setText(csm_tieuThu.getTieuThu());

                if (checkCSMFluctuation()) {
                    MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(false);
                    ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
                } else {
                    ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
                }
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


        mRootView.findViewById(R.id.layout_ds_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScan();
            }
        });
        mRootView.findViewById(R.id.layout_ds_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNote();
            }
        });
        mRootView.findViewById(R.id.layout_ds_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSave(v);
            }
        });
        ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((EditText) v).getText().toString().length() > 0)
                    if (!isAllowChangeSdt) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
                        builder.setTitle(mRootView.getContext().getString(R.string.alert_sdt_title));
                        builder.setCancelable(false);
                        builder.setMessage(mRootView.getContext().getString(R.string.alert_sdt_message));
                        builder.setPositiveButton(mRootView.getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isAllowChangeSdt = true;
                                dialog.dismiss();
                            }
                        }).setNegativeButton(mRootView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();

                    }
            }

        });
        mDBs.clear();
        for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
            mDBs.add(hoaDon.getDanhBo());
        }
        refresh();
    }

    private boolean checkCSMFluctuation() {
        int tieuThu = 0, sum = 0, avergare = 0, min = 0, max = 0;
        List<Integer> tieuThuList = new ArrayList<>();
        if (mTxtTT.getText().length() > 0) {
            tieuThu = Integer.parseInt(mTxtTT.getText().toString());
            if (mTxtTT1.getText().length() > 0)
                tieuThuList.add(Integer.parseInt(mTxtTT1.getText().toString()));
            if (mTxtTT2.getText().length() > 0)
                tieuThuList.add(Integer.parseInt(mTxtTT2.getText().toString()));
            if (mTxtTT3.getText().length() > 0)
                tieuThuList.add(Integer.parseInt(mTxtTT3.getText().toString()));
            for (Integer item : tieuThuList)
                sum += item;
            avergare = sum / tieuThuList.size();
            min = avergare / 2;
            max = 3 * avergare / 2;
            for (Integer item : tieuThuList)
                if (max < item)
                    max = item;
            if (min <= tieuThu && tieuThu <= max)
                return false;
            else
                return true;

        } else
            return false;
    }

    private void setTheme() {
        switch (mSelected_theme) {
            case ThemeUtils.THEME_DEFAULT:
                ((LinearLayout) mRootView.findViewById(R.id.layout_ds)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_ky_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_ky)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dot_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dot)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.editauto_ds_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundResource(R.layout.edit_text_styles);
                ((TextView) mRootView.findViewById(R.id.spin_ds_mlt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterMLT = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mMLTs);

                ((TextView) mRootView.findViewById(R.id.spin_ds_db_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterDB = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mDBs);

                ((ImageButton) mRootView.findViewById(R.id.btn_ds_prev)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((ImageButton) mRootView.findViewById(R.id.btn_ds_prev)).setImageResource(R.drawable.prev);
                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setImageResource(R.drawable.next);
                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.etxt_ds_sdt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

//            ((TableLayout) mRootView.findViewById(R.id.layout_ds_csm)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_csm_1));
                ((TextView) mRootView.findViewById(R.id.spin_ds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterCode = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, Code.getInstance().getCodes());

                ((TextView) mRootView.findViewById(R.id.etxt_ds_CSM_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                break;

            case ThemeUtils.THEME_DARK:
                ((LinearLayout) mRootView.findViewById(R.id.layout_ds)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_ky_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_ky)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dot_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dot)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.editauto_ds_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundResource(R.layout.edit_text_styles);
                ((TextView) mRootView.findViewById(R.id.spin_ds_mlt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterMLT = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mMLTs);
                ((TextView) mRootView.findViewById(R.id.spin_ds_db_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterDB = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mDBs);

                ((ImageButton) mRootView.findViewById(R.id.btn_ds_prev)).setImageResource(R.drawable.prev_light);
                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setImageResource(R.drawable.next_light);
                ((ImageButton) mRootView.findViewById(R.id.btn_ds_prev)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.etxt_ds_sdt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                ((TextView) mRootView.findViewById(R.id.spin_ds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterCode = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, Code.getInstance().getCodes());
                ((TextView) mRootView.findViewById(R.id.etxt_ds_CSM_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                break;
        }

        mAdapterMLT.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinMLT.setAdapter(mAdapterMLT);
        mSpinMLT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMLT(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinMLT.setSelection(0);
            }
        });

        mAdapterDB.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinDB.setAdapter(mAdapterDB);
        mSpinDB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDanhBo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinMLT.setSelection(0);
            }
        });
        mAdapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinCode = (Spinner) mRootView.findViewById(R.id.spin_ds_code);
        mSpinCode.setAdapter(mAdapterCode);
        mSpinCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (checkNull())
                    return;
                String code = mSpinCode.getItemAtPosition(position).toString().substring(0, 2);
                ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setText(code);
                HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo);
                CalculateCSM_TieuThu csm_tieuThu = new CalculateCSM_TieuThu(code, hoaDon.getCode_CSC_SanLuong(), Integer.parseInt(mTxtCSC.getText().toString()), mEditTextCSM.getText().toString());

                mTxtCSM.setText(csm_tieuThu.getCSM());
                mEditTextCSM.setText(csm_tieuThu.getCSM());
                mTxtTT.setText(csm_tieuThu.getTieuThu());

                if (checkCSMFluctuation()) {
                    MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(false);
                    ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
                } else {
                    ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinCode.setSelection(0);
            }
        });

    }

    private void optionSearch() {
//        String[] searchTypes = {mRootView.getContext().getString(R.string.search_mlt),
//                mRootView.getContext().getString(R.string.search_danhbo)};
//                mRootView.getContext().getString(R.string.search_tenKH)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Tùy chọn tìm kiếm");
        builder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_search_type, null);

//        final Spinner spinSearchType = (Spinner) dialogLayout.findViewById(R.id.spin_select_type_seach);
//        ArrayAdapter<String> adapterSearchType = new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_spinner_dropdown_item, searchTypes);
//        adapterSearchType.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        spinSearchType.setAdapter(adapterSearchType);

        final RadioGroup group = (RadioGroup) dialogLayout.findViewById(R.id.radioGroup_searchtype);

        if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_mlt)))
            group.check(R.id.radio_search_mlt);
        else if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_danhbo)))
            group.check(R.id.radio_search_danhbo);
        else if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_tenKH)))
            group.check(R.id.radio_search_tenKH);
        else if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_diaChi)))
            group.check(R.id.radio_search_diaChi);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int iChecked = group.getCheckedRadioButtonId();
                switch (iChecked) {
                    case R.id.radio_search_mlt:
                        mSearchType = mRootView.getContext().getString(R.string.search_mlt);
                        singleComplete.setText("");
                        break;
                    case R.id.radio_search_danhbo:
                        mSearchType = mRootView.getContext().getString(R.string.search_danhbo);
                        singleComplete.setText("");
                        break;
                    case R.id.radio_search_tenKH:
                        mSearchType = mRootView.getContext().getString(R.string.search_tenKH);
                        singleComplete.setText("");
                        break;
                    case R.id.radio_search_diaChi:
                        mSearchType = mRootView.getContext().getString(R.string.search_diaChi);
                        singleComplete.setText("");
                        break;
                }
//                mSearchType = spinSearchType.getSelectedItem().toString();
                singleComplete.setHint(mSearchType);

                if (mSearchType.equals(mRootView.getContext().getString(R.string.search_mlt))) {
                    singleComplete.setAdapter(new CustomArrayAdapter(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mMLTs
                    ));

                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                    mDBs.clear();
                    for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
                        mDBs.add(hoaDon.getDanhBo());
                    }
                    singleComplete.setAdapter(new CustomArrayAdapter(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mDBs
                    ));
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                    mTenKHs.clear();
                    for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
                        mTenKHs.add(hoaDon.getTenKhachHang());
                    }

                    singleComplete.setAdapter(new CustomArrayAdapter(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mTenKHs
                    ));
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                    mDiaChis.clear();
                    for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike)) {
                        mDiaChis.add(hoaDon.getDiaChi());
                    }

                    singleComplete.setAdapter(new CustomArrayAdapter(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mDiaChis
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

        setTextProgress(dotString);

        setTheme();
    }

    private void setTextProgress(String dotString) {
//        String dotString = mDot + "";
//        if (this.mDot < 10)
//            dotString = "0" + this.mDot;
        this.mSumDanhBo = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike).size();
        this.mDanhBoHoanThanh = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Read().size();
        this.mSumDanhBo += this.mDanhBoHoanThanh;
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

    }

    private void checkSave(View v) {
        // kiểm tra hình ảnh
        if (getImageFileName() == null) {
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
            if (checkCSMFluctuation()) {
                MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(true);
//                MyAlertByHardware.getInstance(mRootView.getContext()).playSound();
                alertCSMFluctuation(csc, csm);
            } else if (csm < csc) {
                alertCSM_lt_CSC(csc, csm);
            } else {
                save(getImageFileName().getAbsolutePath(), csc, csm);
            }
        }
    }

    public void selectMLT(int position) {
        mMlt = mMLTs.get(position);
        selectDanhBo(position);
    }

    private void selectDanhBo(int position) {
        if (checkNull())
            return;
        mSpinCode.setSelection(0);
        mSpinMLT.setSelection(position);
        mSpinDB.setSelection(position);
        HideKeyboard.hide(mActivity);
        mDanhBo = mDBs.get(position);

        HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo);
        mHoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo);
        mDot = Integer.parseInt(mHoaDon.getDot());
        ((TextView) mRootView.findViewById(R.id.txt_ds_dot)).setText(mHoaDon.getDot());
        ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).setText(mHoaDon.getTenKhachHang());
//                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());
        mTxtCSC = (TextView) mRootView.findViewById(R.id.txt_ds_CSC);
        mTxtCSC.setText(mHoaDon.getChiSoCu());

        ((TextView) mRootView.findViewById(R.id.txt_ds_code1)).setText(mHoaDon.getCode_CSC_SanLuong().getCode1());
        ((TextView) mRootView.findViewById(R.id.txt_ds_code2)).setText(mHoaDon.getCode_CSC_SanLuong().getCode2());
        ((TextView) mRootView.findViewById(R.id.txt_ds_code3)).setText(mHoaDon.getCode_CSC_SanLuong().getCode3());

        ((TextView) mRootView.findViewById(R.id.txt_ds_CSC2)).setText(mHoaDon.getCode_CSC_SanLuong().getCSC1());
        ((TextView) mRootView.findViewById(R.id.txt_ds_CSC3)).setText(mHoaDon.getCode_CSC_SanLuong().getCSC2());

        ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1)).setText(mHoaDon.getCode_CSC_SanLuong().getSanLuong1());
        ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2)).setText(mHoaDon.getCode_CSC_SanLuong().getSanLuong2());
        ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3)).setText(mHoaDon.getCode_CSC_SanLuong().getSanLuong3());

        ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setText(mHoaDon.getGiaBieu());
        ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).setText(mHoaDon.getDiaChi());
        ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).setText(mHoaDon.getSdt());
        ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setText(mHoaDon.getDinhMuc());
//                    refresh();
//        if (hoaDon != null) {
//            mEditTextCSM.setText(hoaDon.getChiSoMoi());
//            mTxtCSM.setText(hoaDon.getChiSoMoi());
//            mGhiChu = hoaDon.getGhiChu();
//
//        } else {
        mGhiChu = "";
        mEditTextCSM.setText("");
        mTxtCSM.setText("");

//        }
    }

    private boolean checkNull() {
        if (mMLTs.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle("Không có lộ trình").setMessage("Có thể đợt hiện tại đã đọc xong, hoặc chưa có dữ liệu. Vui lòng kiểm tra lại!!!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            }).setCancelable(false);
            AlertDialog dialog = builder.create();

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            return true;
        }
        return false;
    }

    private void save(String image, int csc, int csm) {
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo);
        hoaDon.setCodeMoi(this.mSpinCode.getSelectedItem().toString().substring(0, 2));
        hoaDon.setChiSoMoi(csm + "");
        hoaDon.setTieuThuMoi(((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).getText().toString());
        hoaDon.setGhiChu(mGhiChu);
        hoaDon.setImage(image);
//        DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(this.mDanhBo,
//                this.mMlt,
//                dotString,
//                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).getText().toString(),
//                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).getText().toString(),
//                ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).getText().toString(),
//                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).getText().toString(),
//                this.mSpinCode.getSelectedItem().toString().substring(0, 2),
//                csc + "",
//                csm + "",
//                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).getText().toString(),
//                this.mGhiChu,
//                image,
//                1);
        LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonUnRead(hoaDon);
//        LocalDatabase.getInstance(mRootView.getContext()).deleteHoaDon(danhBo_chiSoMoi.getDanhBo());
        mTxtTT.setText("");

//        this.mAdapterMLT.remove(danhBo_chiSoMoi.getMaLoTrinh());
        this.mMLTs.remove(hoaDon.getMaLoTrinh());
        this.mAdapterMLT.notifyDataSetChanged();
//        this.mAdapterDB.remove(danhBo_chiSoMoi.getDanhBo());
        this.mDBs.remove(hoaDon.getDanhBo());
        this.mAdapterDB.notifyDataSetChanged();

        int i = mSpinMLT.getSelectedItemPosition();
        selectMLT(i == mMLTs.size() ? i - 1 : i);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Chưa nhập chỉ số mới");
        builder.setCancelable(false);
        builder.setMessage("Kiểm tra code?")

                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        save(getImageFileName().getAbsolutePath(), csc, csm);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Kiểm tra lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        MyAlertByHardware.getInstance(mRootView.getContext()).stopSound();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void alertCSMFluctuation(final int csc, final int csm) {
//        LinearLayout layout = (LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0);
//        for (int i = 1; i <= 3; i++) {
//            try {
//                layout.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
//                Thread.sleep(100);
//                layout.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_csm_fluctuation_title));
        builder.setCancelable(false);
        builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_fluctuation_message))

                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        save(getImageFileName().getAbsolutePath(), csc, csm);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Kiểm tra lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private void alertCSM_lt_CSC(final int csc, final int csm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_csm_lt_csc_title));
        builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_lt_csc_message))
                .setCancelable(false)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        save(getImageFileName().getAbsolutePath(), csc, csm);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Kiểm tra lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

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
//        final EditText input = new EditText(mRootView.getContext());
//        input.setMaxLines(5);
//        input.setText(this.mGhiChu);
//        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Ghi chú");
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_ghichu, null);
        final Spinner spin_ghichu = (Spinner) dialogLayout.findViewById(R.id.spin_select_ghichu);
        final Spinner spin_ghichu_sub = (Spinner) dialogLayout.findViewById(R.id.spin_select_ghichu_sub);
        final EditText etxtghichu = (EditText) dialogLayout.findViewById(R.id.etxt_select_ghichu);
        etxtghichu.setBackgroundResource(R.layout.edit_text_styles);
        etxtghichu.setEnabled(false);
        LinearLayout layout_ghichu_sub = (LinearLayout) dialogLayout.findViewById(R.id.layout_select_ghichu_sub);

        ArrayAdapter<String> adapterNotes = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_note_left, Note.getInstance().getNotes());
        final ArrayAdapter<String> adapterNotes_sub_dutchi = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_note_left, Note.getInstance().getNotes_sub_dutchi());
        final ArrayAdapter<String> adapterNotes_sub_kinhdoanh = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_note_left, Note.getInstance().getNotes_sub_kinhdoanh());

        spin_ghichu.setAdapter(adapterNotes);
        spin_ghichu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        etxtghichu.setEnabled(true);
                        spin_ghichu_sub.setEnabled(false);
                        spin_ghichu_sub.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        etxtghichu.setEnabled(false);
                        etxtghichu.setText("");
                        spin_ghichu_sub.setAdapter(adapterNotes_sub_dutchi);
                        spin_ghichu_sub.setEnabled(true);
                        spin_ghichu_sub.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        etxtghichu.setEnabled(false);
                        etxtghichu.setText("");
                        spin_ghichu_sub.setAdapter(adapterNotes_sub_kinhdoanh);
                        spin_ghichu_sub.setEnabled(true);
                        spin_ghichu_sub.setVisibility(View.VISIBLE);
                        break;
                    default:
                        spin_ghichu_sub.setEnabled(false);
                        etxtghichu.setText("");
                        etxtghichu.setEnabled(false);
                        spin_ghichu_sub.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mGhiChu = input.getText().toString();
                switch (spin_ghichu.getSelectedItemPosition()) {
                    case 0:
                        mGhiChu = etxtghichu.getText().toString();
                        break;
                    case 1:case 2:
                        mGhiChu = spin_ghichu.getSelectedItem().toString() + ": " + spin_ghichu_sub.getSelectedItem().toString();
                        break;
                    default:
                        mGhiChu = spin_ghichu.getSelectedItem().toString();
                        break;
                }
                dialog.dismiss();
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
//        dialog.setView(input);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
//                            if (getImageFileName().length() < MIN_SIZE) {
//                                alertImageLowQuatity();
//                            }
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

    private void alertImageLowQuatity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        builder.setCancelable(false);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_imgLowQuatity_Title));
        builder.setMessage(mRootView.getContext().getString(R.string.alert_imgLowQuatity_Message));

        builder.setPositiveButton("CHỤP LẠI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                capture();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
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
