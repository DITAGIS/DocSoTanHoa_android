package com.ditagis.hcm.docsotanhoa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.CodeSpinnerAdapter;
import com.ditagis.hcm.docsotanhoa.adapter.CustomArrayAdapter;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewSelectFolderAdapter;
import com.ditagis.hcm.docsotanhoa.entities.Code_Describle;
import com.ditagis.hcm.docsotanhoa.entities.Codes;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.theme.ThemeUtils;
import com.ditagis.hcm.docsotanhoa.utities.CalculateCSM_TieuThu;
import com.ditagis.hcm.docsotanhoa.utities.Calculate_TienNuoc;
import com.ditagis.hcm.docsotanhoa.utities.Flag;
import com.ditagis.hcm.docsotanhoa.utities.HideKeyboard;
import com.ditagis.hcm.docsotanhoa.utities.ImageFile;
import com.ditagis.hcm.docsotanhoa.utities.MyAlertByHardware;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;
import com.ditagis.hcm.docsotanhoa.utities.Note;
import com.ditagis.hcm.docsotanhoa.utities.Printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ThanLe on 25/10/2017.
 */

public class DocSo extends Fragment {
    private static final int MIN_SIZE = 500000;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ID_SCAN = 2;
    String mMlt;
    String mDanhBo;
    List<String> mMLTs, mDBs = new ArrayList<>(), mTenKHs = new ArrayList<>(), mDiaChis = new ArrayList<>(), mSdts = new ArrayList<>();
    List<String> mMLTs_old = new ArrayList<>(), mDBs_old = new ArrayList<>(), mTenKHs_old = new ArrayList<>(), mDiaChis_old = new ArrayList<>();
    EditText mEditTextCSM, mEditTextViTri;
    TextView mTxtCSM;
    TextView mTxtCSC;
    TextView mTxtComplete;
    TextView mTxtTT, mTxtTT1, mTxtTT2, mTxtTT3;
    Spinner mSpinMLT;
    Spinner mSpinDB = null, mSpinTenKH, mSpinDiaChi;
    Spinner mSpinCode, mSpinDot, mSpinKy, mSpinNam, mSpinSdt;
    AutoCompleteTextView singleComplete;
    Uri mUri;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Bitmap mBpImage;
    private HoaDon mHoaDon;
    private String mCode, mSoNha, mDuong;
    private int mSumDanhBo, mDanhBoHoanThanh;
    private String mStaffName, mStaffPhone;
    private int mDot, mKy;
    private int mNam, mCSGo;
    private String mGhiChu;
    private Date currentTime;
    private ArrayAdapter<String> mAdapterDB, mAdapterTenKH, mAdapterDiaChi, mAdapterSdt;
    private CodeSpinnerAdapter mAdapterCode;
    private String mSdt;
    private ArrayAdapter<String> mAdapterMLT;
    private View mRootView;
    private Activity mActivity;
    private String mSearchType;
    private int mSelected_theme;
    private String mLike;
    private ArrayAdapter<String> mAdapterDot, mAdapterKy, mAdapterNam;
    private List<String> mDots = new ArrayList<>(), mKys = new ArrayList<>(), mNams = new ArrayList<>();
    private ViewPager mViewPager;
    private FrameLayout mFrameLayoutViewImage;
    private ImageView mImageViewFrame;
    private Button mBtnCloseViewImageFrame;
    private GridViewSelectFolderAdapter mSelectFolderAdapter;
    private String mUsername;

    @SuppressLint("ClickableViewAccessibility")
    public DocSo(Activity activity, final LayoutInflater inflater, int mKy, int nam, final int mDot, String mUsername, String staffName, String staffPhone, int theme, ViewPager viewPager) {
        this.mActivity = activity;
        this.mStaffName = staffName;
        this.mStaffPhone = staffPhone;
        this.mDot = mDot;
        this.mKy = mKy;
        this.mNam = nam;
        this.mUsername = mUsername;
        this.mSelected_theme = theme;
//        this.mLike = "__" + mUsername + "%";
        mViewPager = viewPager;
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        this.mLike = dotString + mUsername + "%";
        mRootView = inflater.inflate(R.layout.doc_so_fragment, null);
        mTxtTT = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu);
        mTxtTT1 = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1);
        mTxtTT2 = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2);
        mTxtTT3 = (TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3);

        mSpinNam = (Spinner) mRootView.findViewById(R.id.spin_ds_nam);
        mSpinKy = (Spinner) mRootView.findViewById(R.id.spin_ds_ky);
        mSpinDot = (Spinner) mRootView.findViewById(R.id.spin_ds_dot);
        mSpinTenKH = (Spinner) mRootView.findViewById(R.id.spin_ds_tenKH);
        mSpinDiaChi = (Spinner) mRootView.findViewById(R.id.spin_ds_diachi);
//        mDots.add(dotString);
        mTxtCSC = (TextView) mRootView.findViewById(R.id.txt_ds_CSC);
        mFrameLayoutViewImage = (FrameLayout) mRootView.findViewById(R.id.layout_ds_viewImage);
        mImageViewFrame = (ImageView) mRootView.findViewById(R.id.imgView_frame);
        mBtnCloseViewImageFrame = (Button) mRootView.findViewById(R.id.btn_ds_close_viewImage_frame);
        mBtnCloseViewImageFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrameLayoutViewImage.setVisibility(View.INVISIBLE);
            }
        });
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

//        mKys.add(mKy + "");
        mNams.add(mNam + "");
        ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setText(this.mStaffName);

        mTxtComplete = (TextView) mRootView.findViewById(R.id.txt_ds_complete);


        mEditTextCSM = (EditText) mRootView.findViewById(R.id.etxt_ds_CSM);
//        mEditTextCSM.setEnabled(false);
        mTxtCSM = (TextView) mRootView.findViewById(R.id.txt_ds_CSM);


        mMLTs = new ArrayList<String>();

        for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(mLike, mKy, false)) {
            mMLTs.add(spaceMLT(hoaDon.getMaLoTrinh()));
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (mSearchType.equals(mRootView.getContext().getString(R.string.search_mlt))) {
                        setMaxLenghtAutoCompleteTextView(13);
                        for (int i = 0; i < mMLTs.size(); i++) {
                            if (s.toString().equals(mMLTs.get(i))) {
                                mSpinMLT.setSelection(i);
                            }
                        }
                    } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                        setMaxLenghtAutoCompleteTextView(15);
                        for (int i = 0; i < mDBs.size(); i++) {
                            if (s.toString().equals(mDBs.get(i)))
                                mSpinMLT.setSelection(i);
                        }
                    } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                        setMaxLenghtAutoCompleteTextView(50);
                        for (int i = 0; i < mTenKHs.size(); i++) {
                            if (s.toString().equals(mTenKHs.get(i)))
                                mSpinMLT.setSelection(i);
                        }
                    } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                        setMaxLenghtAutoCompleteTextView(50);
                        for (int i = 0; i < mDiaChis.size(); i++) {
                            if (s.toString().equals(mDiaChis.get(i)))
                                mSpinMLT.setSelection(i);
                        }
                    }
                } catch (Exception e) {
                    Log.d("asd", e.toString());
                }
            }
        });
//        mRootView.findViewById(R.id.imgBtn_ds_call).
//
//                setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String regex = "^[0-9]+$";
//                        Matcher matcher = Pattern.compile(regex).matcher(mSdt);
//                        if (matcher.find()) {
//                            if (mSdt.length() == 0) {
//                                MySnackBar.make(mRootView, mRootView.getContext().getString(R.string.call_errorNotFind), true);
//                            } else {
//                                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                callIntent.setData(Uri.parse("tel:" + mSdt));
//                                startActivity(callIntent);
//                            }
//                        } else {
//                            MySnackBar.make(mRootView, mRootView.getContext().getString(R.string.call_errorNotMatch), true);
//                        }
//                    }
//                });

        mEditTextCSM.setBackgroundResource(R.layout.edit_text_styles);

        mEditTextCSM.setOnTouchListener(new View.OnTouchListener()

        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {

                    if (mHoaDon.getImage_byteArray().length < 1000) {
                        MySnackBar.make(mRootView, mRootView.getContext().getString(R.string.alert_captureBefore
                        ), false);
                        mEditTextCSM.setFocusable(false);
                        HideKeyboard.hide(mActivity);
                    } else {
                        mEditTextCSM.setFocusableInTouchMode(true);

                    }
                } catch (Exception e) {
                }
                return false;

            }
        });

        mEditTextCSM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mCode.equals("60") && !mCode.equals("62")) {


                    ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setText(mCode);
                    CalculateCSM_TieuThu csm_tieuThu = new CalculateCSM_TieuThu(mCode, mHoaDon.getCode_CSC_SanLuong(), Integer.parseInt(mTxtCSC.getText().toString()), mEditTextCSM.getText().toString(), mCSGo);

                    mTxtCSM.setText(csm_tieuThu.getCSM());
                    mTxtTT.setText(csm_tieuThu.getTieuThu());

                    if (checkCSMFluctuation() != 0) {
                        ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
                    } else {
                        ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
                    }
                } else {
                    mTxtCSM.setText(s);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRootView.findViewById(R.id.btn_ds_prev).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPrev();
                    }
                });
        mRootView.findViewById(R.id.btn_ds_next).

                setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doNext();
                    }
                }));
        mEditTextViTri = (EditText) mRootView.findViewById(R.id.etxt_ds_vi_tri);
        mEditTextViTri.setBackgroundResource(R.layout.edit_text_styles);
        mRootView.findViewById(R.id.imgBtn_ds_camera).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCamera();
                    }
                });


        mRootView.findViewById(R.id.layout_ds_scan).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doScan();
                    }
                });
        mRootView.findViewById(R.id.layout_ds_print).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPrint();
                    }
                });
        mRootView.findViewById(R.id.layout_ds_note).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doNote();
                    }
                });
        mRootView.findViewById(R.id.layout_ds_saveAll).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkSave(v);
                    }
                });

        mDBs.clear();
        for (
                HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).

                getAllHoaDon_UnRead(mLike, mKy, false))

        {
            mDBs.add(spaceDB(hoaDon.getDanhBo()));
            mTenKHs.add(hoaDon.getTenKhachHang());
            mDiaChis.add(hoaDon.getDiaChi());
        }
        ((Button) mRootView.findViewById(R.id.btn_ds_sort)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sort();
                    }
                });

        mSdts.add(" ");
        mSpinSdt = (Spinner) mRootView.findViewById(R.id.spin_ds_sdt);
        ((ImageButton) mRootView.findViewById(R.id.imgBtn_ds_add_sdt)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_sdt();
                    }
                });
        ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change_address();
                    }
                });

        setTheme();

    }

    public int getmDot() {
        return mDot;
    }

    private void checkPrint() {
        try {
            if (mHoaDon.getImage_byteArray().length > 1000) {

                int csc = Integer.parseInt(mTxtCSC.getText().toString());
                int csm = -1;
                if (mTxtCSM.getText().toString().trim().length() == 0 && !checkCode()) {

//                alertCSM_Null(csc, csm);
                    MySnackBar.make(mRootView.getRootView(), "Chưa nhập chỉ số mới", true);

                } else {
                    if (mTxtCSM.getText().toString().length() > 0)
                        csm = Integer.parseInt(mTxtCSM.getText().toString());
                    canhBaoCSM(csc, csm, true);

                }

            } else {
//            File f = ImageFile.getFile(currentTime, mRootView, mDanhBo);
//            if (f != null && f.exists()) {
//                int csc = Integer.parseInt(mTxtCSC.getText().toString());
//                int csm = -1;
//                if (mTxtCSM.getText().toString().length() == 0 && !checkCode()) {
//
////                alertCSM_Null(csc, csm);
//                    MySnackBar.make(mRootView.getRootView(), "Chưa nhập chỉ số mới", true);
//
//                } else {
//                    if (mTxtCSM.getText().toString().length() > 0)
//                        csm = Integer.parseInt(mTxtCSM.getText().toString());
//                    if (checkCSMFluctuation()) {
//                        MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(true);
////                MyAlertByHardware.getInstance(mRootView.getContext()).playSound();
//                        alertCSMFluctuation(csc, csm);
//                    } else if (csm < csc) {
//                        alertCSM_lt_CSC(csc, csm);
//
//                    } else {
//                        doPrint();
//                    }
//                }
//                save_without_csm();
//            } else {
                MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
                save_without_csm();
                return;
//            }
            }
        } catch (Exception e) {
            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
            save_without_csm();
            return;
        }
    }

    private void canhBaoCSM(int csc, int csm, boolean isPrint) {
        switch (checkCSMFluctuation()) {
            case 0:
                if (csm < csc) {
                    MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(true);
                    ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));

                    if (isPrint)
                        alertCSM_lt_CSCPrint(csc, csm);
                    else
                        alertCSM_lt_CSC_save(csc, csm);

                } else {
                    if (isPrint)
                        doPrint();
                    else
                        save(csc, csm);
                    ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));

                }
                break;
            case -1:
                MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(true);
                if (isPrint)
                    alertCSMFluctuationPrint(csc, csm, -1);
                else {
                    alertCSMFluctuation_save(csc, csm, -1);
                }
                break;

            case 1:
                MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(true);
                if (isPrint)
                    alertCSMFluctuationPrint(csc, csm, 1);
                else
                    alertCSMFluctuation_save(csc, csm, 1);
                break;

            default:
                save(csc, csm);
                ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));


                break;
        }
    }

    private void doPrint() {
        try {
            if (Printer.getInstance().getmBluetoothSocket() == null) {
                MySnackBar.make(mEditTextCSM, "Chưa kết nối với máy in", true);
                return;
            }
            if (this.currentTime == null)
                this.currentTime = Calendar.getInstance().getTime();
            String datetime = this.formatter.format(this.currentTime);
            mHoaDon.setThoiGian(datetime);
            mHoaDon.setTieuThuMoi(((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).getText().toString());
            mHoaDon.setChiSoMoi(mTxtCSM.getText().toString());
            Calculate_TienNuoc calculate_tienNuoc = new Calculate_TienNuoc(
                    Integer.parseInt(mHoaDon.getTieuThuMoi()), mHoaDon.getGiaBieu(),
                    mHoaDon.getDinhMuc(), mHoaDon.getSh(), mHoaDon.getSx(), mHoaDon.getDv(), mHoaDon.getHc());
            double tienNuoc = calculate_tienNuoc.getmTienNuoc();

            Printer.getInstance().setValue(mNam, mStaffName, mStaffPhone, mHoaDon, tienNuoc);
            if (Printer.getInstance().print())
                save(Integer.parseInt(mTxtCSC.getText().toString()), Integer.parseInt(mTxtCSM.getText().toString()));
        } catch (Exception e) {

        }
    }

    private void change_address() {
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());//getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_change_address, null);
        final EditText etxtAddr_num = (EditText) dialogLayout.findViewById(R.id.etxt_address_num);
        etxtAddr_num.setText(mHoaDon.getSoNha());
        final EditText etxtAddr_street = (EditText) dialogLayout.findViewById(R.id.etxt_address_street);
        etxtAddr_street.setText(mHoaDon.getDuong());
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Thay đổi địa chỉ");
        builder.setPositiveButton(mRootView.getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etxtAddr_num.getText().toString().trim().length() > 0)
                    mSoNha = etxtAddr_num.getText().toString();
                if (etxtAddr_street.getText().toString().trim().length() > 0)
                    mDuong = etxtAddr_street.getText().toString();
                mDiaChis.remove(mSpinDiaChi.getSelectedItemPosition());
                mDiaChis.add(mSpinDiaChi.getSelectedItemPosition(), mSoNha + " " + mDuong);
                mAdapterDiaChi.notifyDataSetChanged();
                mHoaDon.setSoNha(mSoNha);
                mHoaDon.setDuong(mDuong);

                LocalDatabase.getInstance(mRootView.getContext()).updateHoaDon_Address(mHoaDon.getDanhBo(), mSoNha, mDuong, Flag.UNREAD);

                dialog.dismiss();

            }
        }).setNegativeButton(mRootView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false);
        AlertDialog dialog = builder.create();

        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void setmSelected_theme(int mSelected_theme) {
        this.mSelected_theme = mSelected_theme;
        try {
            setTheme();
        } catch (Exception e) {

        }
    }

    public int getmSumDanhBo() {
        return mSumDanhBo;
    }

    private void add_sdt() {
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());//getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_add_sdt, null);
        final EditText etxtSdt = (EditText) dialogLayout.findViewById(R.id.etxt_add_sdt);
        etxtSdt.setText(mSdt);
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Thêm số điện thoại");
        builder.setPositiveButton(mRootView.getContext().getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (etxtSdt.getText().toString().trim().length() > 0) {
                    if (mSdts.contains(" "))
                        mSdts.clear();
                    for (String sdt : mSdts)
                        if (sdt.contains(etxtSdt.getText().toString().trim())) {
                            return;
                        }
                    mSdts.add(etxtSdt.getText().toString());
                    mAdapterSdt.notifyDataSetChanged();
                    mSdt = mSdts.get(0);
                    mSpinSdt.setSelection(0);
                    mHoaDon.setSdt(mSdt);
                }


            }
        }).setNegativeButton(mRootView.getContext().getString(R.string.edit), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (mSdts.size() > 0) {
                    if (mSdts.contains(" "))
                        mSdts.clear();
                    mSdts.remove(mSdt);
                    if (etxtSdt.getText().toString().length() > 0) {
                        mSdts.add(etxtSdt.getText().toString());

                    }
                    mAdapterSdt.notifyDataSetChanged();
                    mSdt = mSdts.get(0);
                    mSpinSdt.setSelection(0);
                    mHoaDon.setSdt(mSdt);
                }
            }
        }).setCancelable(true);
        AlertDialog dialog = builder.create();

        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    private String getSdtString() {
        String sdt = "";
        if (mSdt.trim().length() > 0) {
            for (int i = 0; i <= mSdts.size() - 1; i++) {
                if (i == mSdts.size() - 1)
                    sdt = sdt.concat(mSdts.get(i));
                else
                    sdt = sdt.concat(mSdts.get(i)).concat("-");
            }
        }
        return sdt;
    }


    private String spaceMLT(String mlt) {
        String output = "";
        output = (mlt.substring(0, 2)).concat("  ").concat(mlt.substring(2, 4)).concat("  ").concat(mlt.substring(4));
        return output;
    }

    private String spaceDB(String danhBo) {
        String output = "";
        output = (danhBo.substring(0, 4)).concat("  ").concat(danhBo.substring(4, 7)).concat("  ").concat(danhBo.substring(7));
        return output;
    }

    private void setMaxLenghtAutoCompleteTextView(int maxLenght) {
        if (singleComplete != null) {
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLenght);
            singleComplete.setFilters(FilterArray);
        }
    }

    private int checkCSMFluctuation() {
        int tieuThu = 0, sum = 0, TTTB = 0, min = 0, max = 0;
        List<Integer> tieuThuList = new ArrayList<>();
        try {
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

                TTTB = sum / tieuThuList.size();
                if ((double) tieuThu / TTTB >= 1.5)
                    return 1;
                else if ((double) TTTB / tieuThu >= 1.5)
                    return -1;
                else return 0;

            } else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private void setTheme() {
        switch (mSelected_theme) {
            case ThemeUtils.THEME_DEFAULT:
                ((LinearLayout) mRootView.findViewById(R.id.layout_ds)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_nam_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_ky_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dot_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                mAdapterNam = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mNams);
                mAdapterKy = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mKys);
                mAdapterDot = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mDots);
                ((TextView) mRootView.findViewById(R.id.editauto_ds_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setBackgroundResource(R.layout.edit_text_styles);
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundResource(R.layout.edit_text_styles);
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.spin_ds_mlt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterMLT = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mMLTs);
                ((Button) mRootView.findViewById(R.id.btn_ds_sort)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((Button) mRootView.findViewById(R.id.btn_ds_sort)).setBackgroundResource(R.layout.edit_text_styles);
                ((Button) mRootView.findViewById(R.id.btn_ds_sort)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_so_than_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_so_than)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_vi_tri_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mEditTextViTri.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_1));
                mEditTextViTri.setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_1));
                mEditTextViTri.setBackgroundResource(R.layout.edit_text_styles);
                ((TextView) mRootView.findViewById(R.id.txt_ds_hieu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_hieu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_co_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_co)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.spin_ds_db_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterDB = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mDBs);

//                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setImageResource(R.drawable.next);
//                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
//                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setBackgroundResource(R.layout.edit_text_styles);

//                mEditTextViTri.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                mAdapterTenKH = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mTenKHs);

                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).setBackgroundResource(R.layout.edit_text_styles);
                ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                mAdapterDiaChi = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_bold_left1, mDiaChis);
                ((TextView) mRootView.findViewById(R.id.etxt_ds_sdt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterSdt = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left1, mSdts);
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code1)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code2)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code3)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC3)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC2)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                mTxtCSC.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mTxtCSM.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
//            ((TableLayout) mRootView.findViewById(R.id.layout_ds_csm)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_csm_1));
                ((TextView) mRootView.findViewById(R.id.spin_ds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterCode = new CodeSpinnerAdapter(mRootView.getContext(), R.layout.spincode_dropdown, Codes.getInstance().getCodeDescribles_ds()) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView row_code = (TextView) view.findViewById(R.id.row_code);
                        TextView row_describle = (TextView) view.findViewById(R.id.row_describle);
                        if (position % 2 == 0) { // we're on an even row
                            view.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                            row_code.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                            row_describle.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                        } else {
                            view.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                            row_code.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                            row_describle.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                        }
                        return view;
                    }

                };

                ((TextView) mRootView.findViewById(R.id.etxt_ds_CSM_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setBackgroundResource(R.layout.edit_text_styles);
                break;
            case ThemeUtils.THEME_DARK:
                ((LinearLayout) mRootView.findViewById(R.id.layout_ds)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_complete)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_staffName)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_nam_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_ky_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dot_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                mAdapterNam = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mNams);
                mAdapterKy = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mKys);
                mAdapterDot = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mDots);
                ((TextView) mRootView.findViewById(R.id.editauto_ds_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_ds)).setBackgroundResource(R.layout.edit_text_styles2);
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setBackgroundResource(R.layout.edit_text_styles2);
                ((Button) mRootView.findViewById(R.id.btn_ds_optionSearch)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.spin_ds_mlt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterMLT = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mMLTs);
                ((Button) mRootView.findViewById(R.id.btn_ds_sort)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((Button) mRootView.findViewById(R.id.btn_ds_sort)).setBackgroundResource(R.layout.edit_text_styles2);
                ((Button) mRootView.findViewById(R.id.btn_ds_sort)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_so_than_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_so_than)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_vi_tri_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mEditTextViTri.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_2));
                mEditTextViTri.setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_2));
                mEditTextViTri.setBackgroundResource(R.layout.edit_text_styles2);
                ((TextView) mRootView.findViewById(R.id.txt_ds_hieu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_hieu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_co_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_co)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.spin_ds_db_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterDB = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mDBs);

//                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setImageResource(R.drawable.next);
//                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
//                ((ImageButton) mRootView.findViewById(R.id.btn_ds_next)).setBackgroundResource(R.layout.edit_text_styles);

//                mEditTextViTri.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                mAdapterTenKH = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mTenKHs);

                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).setBackgroundResource(R.layout.edit_text_styles2);
                ((Button) mRootView.findViewById(R.id.btn_ds_changeDiaChi)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                mAdapterDiaChi = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_bold_left2, mDiaChis);
                ((TextView) mRootView.findViewById(R.id.etxt_ds_sdt_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterSdt = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_left2, mSdts);
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorToolBar_Row_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code1)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code2)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code3)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC3)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC2)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSC)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                mTxtCSC.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mTxtCSM.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

//            ((TableLayout) mRootView.findViewById(R.id.layout_ds_csm)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_csm_2));
                ((TextView) mRootView.findViewById(R.id.spin_ds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterCode = new CodeSpinnerAdapter(mRootView.getContext(), R.layout.spincode_dropdown2, Codes.getInstance().getCodeDescribles_ds()) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView row_code = (TextView) view.findViewById(R.id.row_code);
                        TextView row_describle = (TextView) view.findViewById(R.id.row_describle);
                        if (position % 2 == 0) { // we're on an even row
                            view.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                            row_code.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                            row_describle.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                        } else {
                            view.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                            row_code.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                            row_describle.setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                        }
                        return view;
                    }
                };

                ((TextView) mRootView.findViewById(R.id.etxt_ds_CSM_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((EditText) mRootView.findViewById(R.id.etxt_ds_CSM)).setBackgroundResource(R.layout.edit_text_styles2);
                break;

        }
        mAdapterCode.setDropDownViewResource(R.layout.spincode_dropdown2);
        mSpinCode = (Spinner) mRootView.findViewById(R.id.spin_ds_code);

        mSpinCode.setAdapter(mAdapterCode);
        mAdapterSdt.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinSdt.setAdapter(mAdapterSdt);

        mSpinSdt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSdt = mSpinSdt.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAdapterNam.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinNam.setAdapter(mAdapterNam);
        mSpinNam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAdapterKy.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinKy.setAdapter(mAdapterKy);
        createKy();
        mSpinKy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectKy(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectKy(0);
            }
        });
        mAdapterDot.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinDot.setAdapter(mAdapterDot);
        createDot();
        mSpinDot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDot(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectDot(0);
            }
        });

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


        mAdapterTenKH.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinTenKH.setAdapter(mAdapterTenKH);
        mSpinTenKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMLT(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinMLT.setSelection(0);
            }
        });

        mAdapterDiaChi.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinDiaChi.setAdapter(mAdapterDiaChi);
        mSpinDiaChi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMLT(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinMLT.setSelection(0);
            }
        });


        mSpinCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCode((position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinCode.setSelection(0);
            }
        });
        setBackGroundTintModeSpinner(PorterDuff.Mode.LIGHTEN);
    }

    private void setBackGroundTintModeSpinner(PorterDuff.Mode mode) {
        mSpinNam.setBackgroundTintMode(mode);
        mSpinKy.setBackgroundTintMode(mode);
        mSpinDot.setBackgroundTintMode(mode);
        mSpinMLT.setBackgroundTintMode(mode);
        mSpinDB.setBackgroundTintMode(mode);
        mSpinDiaChi.setBackgroundTintMode(mode);
        mSpinTenKH.setBackgroundTintMode(mode);
        mSpinSdt.setBackgroundTintMode(mode);
        mSpinCode.setBackgroundTintMode(mode);
    }

    private void sort() {
//        if (mMLTs_old.size() == 0) {
//            mMLTs_old.addAll(mMLTs);
//            mDBs_old.addAll(mDBs);
//            mTenKHs_old.addAll(mTenKHs);
//            mDiaChis_old.addAll(mDiaChis);

        Collections.sort(mMLTs);
        mDBs.clear();
        mTenKHs.clear();
        mDiaChis.clear();

        List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getallhoadonDS(mLike, mKy);
        for (String mlt : mMLTs) {
            for (HoaDon hoaDon : hoaDons) {
                if (mlt.equals(spaceMLT(hoaDon.getMaLoTrinh()))) {
                    mDBs.add(spaceDB(hoaDon.getDanhBo()));
                    mTenKHs.add(hoaDon.getTenKhachHang());
                    mDiaChis.add(hoaDon.getDiaChi());
                }
            }
        }
        mAdapterMLT.notifyDataSetChanged();
        mAdapterDB.notifyDataSetChanged();
        mAdapterTenKH.notifyDataSetChanged();
        mAdapterDiaChi.notifyDataSetChanged();
        if (mMLTs.size() > 0)
            selectMLT(0);
//        } else {
//            mMLTs.clear();
//            mDBs.clear();
//            mTenKHs.clear();
//            mDiaChis.clear();
//            mMLTs.addAll(mMLTs_old);
//            mAdapterMLT.notifyDataSetChanged();
//
//            mDBs.addAll(mDBs_old);
//            mAdapterDB.notifyDataSetChanged();
//
//            mTenKHs.addAll(mTenKHs_old);
//            mAdapterTenKH.notifyDataSetChanged();
//
//            mDiaChis.addAll(mDiaChis_old);
//            mAdapterDiaChi.notifyDataSetChanged();
//
//            mMLTs_old.clear();
//            mDBs_old.clear();
//            mTenKHs_old.clear();
//            mDiaChis_old.clear();
//        }
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
                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mMLTs
                        ));
                        singleComplete.setText("");
                        break;
                    case R.id.radio_search_danhbo:
                        mSearchType = mRootView.getContext().getString(R.string.search_danhbo);
                        singleComplete.setText("");
                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mDBs
                        ));
                        break;
                    case R.id.radio_search_tenKH:
                        mSearchType = mRootView.getContext().getString(R.string.search_tenKH);
                        singleComplete.setText("");
                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mTenKHs
                        ));
                        break;
                    case R.id.radio_search_diaChi:
                        mSearchType = mRootView.getContext().getString(R.string.search_diaChi);
                        singleComplete.setText("");
                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mDiaChis
                        ));
                        break;
                }
                singleComplete.setHint(mSearchType);
                dialog.dismiss();
            }
        });
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        return mRootView;
    }

    public void refresh() {
        String dotString = mDot + "";
        if (this.mDot < 10)
            dotString = "0" + this.mDot;
//        ((TextView) mRootView.findViewById(R.id.spin_ds_dot)).setText(this.mDot + "");

        setTextProgress();
    }

    private void setTextProgress() {

        this.mSumDanhBo = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonSize(mLike, mKy, Flag.UNREAD, false);
        this.mDanhBoHoanThanh = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonSize(mLike, mKy, Flag.READ, false);
        this.mSumDanhBo += this.mDanhBoHoanThanh;
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

    }

    private void checkSave(View v) {
        try {
            if (mHoaDon.getImage_byteArray().length > 1000) {

                int csc = Integer.parseInt(mTxtCSC.getText().toString());
                int csm = -1;
                if (mTxtCSM.getText().toString().trim().length() == 0 && !checkCode()) {

//                alertCSM_Null(csc, csm);
                    MySnackBar.make(mRootView.getRootView(), "Chưa nhập chỉ số mới", true);

                } else {
                    if (mTxtCSM.getText().toString().length() > 0)
                        csm = Integer.parseInt(mTxtCSM.getText().toString());
                    canhBaoCSM(csc, csm, false);
                }
            } else {
                File f = ImageFile.getFile(currentTime, mRootView, mDanhBo);
                if (f != null && f.exists()) {
                    int csc = Integer.parseInt(mTxtCSC.getText().toString());
                    int csm = -1;
                    if (mTxtCSM.getText().toString().length() == 0 && !checkCode()) {

//                alertCSM_Null(csc, csm);
                        MySnackBar.make(mRootView.getRootView(), "Chưa nhập chỉ số mới", true);

                    } else {
                        if (mTxtCSM.getText().toString().length() > 0)
                            csm = Integer.parseInt(mTxtCSM.getText().toString());
                        canhBaoCSM(csc, csm, false);
                    }
                    save_without_csm();
                } else {
                    MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
                    save_without_csm();
                    return;
                }

            }
        } catch (Exception e) {
            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
            save_without_csm();
            return;
        }
//        if (ImageFile.getFile(currentTime, mRootView, mDanhBo) == null) {
//            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
//            return;
////                    save(csc, csm);
//        } else if (!ImageFile.getFile(currentTime, mRootView, mDanhBo).exists()) {
//            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
//            return;
////                    save(csc, csm);
//        }

    }

    private boolean checkCode() {
        switch (mCode) {
            case "54":
            case "55":
            case "56":
            case "58":
            case "60":
            case "62":
            case "5M":
            case "5Q":
            case "5N":
            case "81":
            case "82":
            case "83":
                return true;
            default:
                return false;
        }
    }

    private void createDot() {
        mAdapterDot.clear();
        getDotExist();
        mAdapterDot.notifyDataSetChanged();
        selectDot(0);
    }

    private void createKy() {
        mAdapterKy.clear();
        getKyExist();
        mAdapterKy.notifyDataSetChanged();
    }
//
//    private void createNam() {
//        getNamExist();
//        mAdapterKy.notifyDataSetChanged();
//    }

    private void getDotExist() {
        String like;
        int count = 0;
        for (int i = 20; i >= 0; i--) {
//            if (mSelectFolderAdapter != null)
//                for (GridViewSelectFolderAdapter.Item item : mSelectFolderAdapter.getItems()) {
//                    if (mKy == Integer.parseInt(item.getKy()) && i != Integer.parseInt(item.getDot()))
//                        break;
            if (count == 3)
                break;
            String dotExist = "";
            if (i < 10)
                dotExist = "0" + i;
            else dotExist = i + "";
            if (!mDots.contains(dotExist)) {
                like = dotExist.concat(mLike.substring(2, 4)).concat("%");
                if (LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(like, mKy, false).size() > 0) {
                    mDots.add(dotExist);
                }
            }
            if (mDots.size() > 0)
                count++;
        }
//        }
//        if (mDots.size() > 1) {
//            MyAlertDialog.show(mRootView.getContext(), false, mRootView.getContext().getString(R.string.dotExist_title), mRootView.getContext().getString(R.string.dotExist_message));
//        }
    }

    private void getKyExist() {
        int count = 0;
        String kyString = "";
        for (int i = 12; i >= 0; i--) {
            if (count == 2)
                break;
            kyString = i + "";
            if (i < 10)
                kyString = "0" + i;
            if (!mKys.contains(kyString)) {
                if (LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(i, false).size() > 0) {
                    mKys.add(kyString);
                }
            }
            if (mKys.size() > 0)
                count++;
        }
    }

//    private void getNamExist() {
//        int count = 0;
//        int nam = Calendar.getInstance().get(Calendar.YEAR);
//        for (int i = nam; i >= 0; i--) {
//            if (count == 2)
//                break;
//            if (!mNams.contains(nam)) {
//                if (LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(i).size() > 0) {
//                    mNams.add(nam + "");
//
//                }
//            }
//            if (mNams.size() > 0)
//                count++;
//        }
//    }

    public void selectDotFromDialog(GridViewSelectFolderAdapter adapter, String ky, String dot) {
        mSelectFolderAdapter = adapter;
        boolean isFound = false;

        for (int i = 0; i < mKys.size(); i++) {
            for (GridViewSelectFolderAdapter.Item item : mSelectFolderAdapter.getItems()) {
                if (item.getKy().equals(Integer.parseInt(mKys.get(i)) + "")) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                mKys.remove(mKys.get(i));
                i--;
            }
            isFound = false;
        }
        mAdapterKy.notifyDataSetChanged();
        int kyInt = Integer.parseInt(ky);
        String kyString = kyInt + "";
        if (kyInt < 10)
            kyString = "0" + kyInt;
        int dotInt = Integer.parseInt(dot);
        String dotString = dotInt + "";
        if (dotInt < 10)
            dotString = "0" + dotInt;

        for (int i = 0; i < mAdapterKy.getCount(); i++) {
            if (mAdapterKy.getItem(i).equals(kyString)) {
                mSpinKy.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < mAdapterDot.getCount(); i++) {
            if (mAdapterDot.getItem(i).equals(dotString)) {
                mSpinDot.setSelection(i);
                break;
            }
        }
    }

    public void selectDotFromOut(int dot) {
        for (int i = 0; i < mDots.size(); i++)
            if (Integer.parseInt(mDots.get(i)) == dot) {
                mSpinDot.setSelection(i);
                return;
            }
    }

    private void selectKy(int position) {
        mKy = Integer.parseInt(mKys.get(position));
        createDot();
    }

    private void selectDot(int position) {
        if (mDots.size() > 0) {
            String dotString = mDots.get(position);
            mDot = Integer.parseInt(dotString);

            mLike = dotString.concat(mLike.substring(2, 4)).concat("%");

            List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getallhoadonDS(mLike, mKy);
            mMLTs.clear();
            mDBs.clear();
            mDiaChis.clear();
            mTenKHs.clear();
            for (HoaDon hoaDon : hoaDons) {
                mMLTs.add(spaceMLT(hoaDon.getMaLoTrinh()));
                mDBs.add(spaceDB(hoaDon.getDanhBo()));
                mDiaChis.add(hoaDon.getDiaChi());
                mTenKHs.add(hoaDon.getTenKhachHang());
            }
            if (mMLTs.size() > 0)
                selectMLT(0);
            mAdapterMLT.notifyDataSetChanged();
            mAdapterDB.notifyDataSetChanged();
            mAdapterDiaChi.notifyDataSetChanged();
            mAdapterTenKH.notifyDataSetChanged();
            sort();
            setTextProgress();
        }
    }

    private void selectCode(int position) {
        if (checkNull())
            return;
        Code_Describle code_describle = (Code_Describle) mSpinCode.getItemAtPosition(position);
        mCode = code_describle.getCode();
        ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setText(mCode);
        HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo, false);
        if (hoaDon == null || hoaDon.getCode_CSC_SanLuong() == null)
            return;

        if (mCode.startsWith("8")) {
            if (mHoaDon.getCsgo() == -1) {
                MySnackBar.make(mTxtCSM, "Chưa có dữ liệu báo thay", true);
                mCSGo = -1;
//                mSpinCode.setSelection(0);
            } else
                mCSGo = mHoaDon.getCsgo();
        }
        CalculateCSM_TieuThu csm_tieuThu = new CalculateCSM_TieuThu(mCode, hoaDon.getCode_CSC_SanLuong(), Integer.parseInt(mTxtCSC.getText().toString()), mEditTextCSM.getText().toString(), mCSGo);

        mTxtCSM.setText(csm_tieuThu.getCSM());
        mEditTextCSM.setText(csm_tieuThu.getCSM());
        mTxtTT.setText(csm_tieuThu.getTieuThu());

        if (checkCSMFluctuation() != 0) {
//                    MyAlertByHardware.getInstance(mRootView.getContext()).vibrate(false);
            ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
        } else {
            ((LinearLayout) mRootView.findViewById(R.id.layout_ds_CSC_SL0)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
        }
    }

    private void selectMLT(int position) {
        mMlt = mMLTs.get(position).replace(" ", "");

        mSpinTenKH.setSelection(position);
        mSpinDiaChi.setSelection(position);
        selectDanhBo(position);
    }

    private void showImageViewInFrame(byte[] image) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);

            double scale = bitmap.getHeight() / mFrameLayoutViewImage.getHeight();
            BitmapDrawable resizedDialogImage = new BitmapDrawable(this.getResources(),
                    Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale), false));

            mImageViewFrame.setBackground(resizedDialogImage);
        } catch (Exception e) {

        }
    }

    private void selectDanhBo(int position) {
        if (checkNull())
            return;
        mSpinMLT.setSelection(position);
        mSpinDB.setSelection(position);
        HideKeyboard.hide(mActivity);
        mDanhBo = mDBs.get(position).replace(" ", "");
        mHoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo, true);

        for (int i = 0; i < Codes.getInstance().getCodeDescribles_ds().length; i++) {

            if (Codes.getInstance().getCodeDescribles_ds()[i].getCode().equals(mHoaDon.getCodeMoi())) {
                mSpinCode.setSelection(i);
                mCode = Codes.getInstance().getCodeDescribles_ds()[i].getCode();
                break;
            }
        }
//        if (mHoaDon.getCode_CSC_SanLuong().getCode1().startsWith("F")) {
//            for (int i = 0; i < Codes.getInstance().getCodeDescribles_ds().length; i++) {
//                if (Codes.getInstance().getCodeDescribles_ds()[i].getCode().equals("5F")) {
//                    mSpinCode.setSelection(i);
//                    mCode = Codes.getInstance().getCodeDescribles_ds()[i].getCode();
//                    break;
//                }
//            }
//        } else if (mHoaDon.getCode_CSC_SanLuong().getCode1().startsWith("K")) {
//            for (int i = 0; i < Codes.getInstance().getCodeDescribles_ds().length; i++) {
//                if (Codes.getInstance().getCodeDescribles_ds()[i].getCode().equals("5K")) {
//                    mSpinCode.setSelection(i);
//                    mCode = Codes.getInstance().getCodeDescribles_ds()[i].getCode();
//                    break;
//                }
//            }
//        } else

        {
            mSpinCode.setSelection(0);
            mCode = Codes.getInstance().getCodeDescribles_ds()[0].getCode();
        }
        mFrameLayoutViewImage.setVisibility(View.INVISIBLE);
        try {
            if (mHoaDon.getImage_byteArray().length > 1000) {
                showImageViewInFrame(mHoaDon.getImage_byteArray());
                mFrameLayoutViewImage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
        mSoNha = mHoaDon.getSoNha();
        mDuong = mHoaDon.getDuong();
        //xử lý sđt
        mSdts.clear();
        mSdts.add(" ");
        mAdapterSdt.notifyDataSetChanged();
        if (mHoaDon.getSdt().length() > 0) {
            mSdts.clear();
            String[] sdts = mHoaDon.getSdt().split("-| ");
            for (String sdt : sdts) {
                mSdts.add(sdt.trim());
            }

            mAdapterSdt.notifyDataSetChanged();
        }
        mSdt = mSdts.get(0);
        mSpinSdt.setSelection(0);
        mDot = Integer.parseInt(mHoaDon.getDot());
//        for (int i = 0; i < mDots.size(); i++) {
//            if (mDots.get(i).equals(mHoaDon.getDot()))
//                mSpinDot.setSelection(i);
//        }

//                            ((TextView) findViewById(R.id.txt_ds_dinhmuc)).setText(hoaDon.getDinhMuc());

        mTxtCSC.setText(mHoaDon.getChiSoCu());
        mTxtCSM.setText("");
        mEditTextCSM.setText("");
        ((TextView) mRootView.findViewById(R.id.txt_ds_so_than)).setText(mHoaDon.getSoThan());
        ((TextView) mRootView.findViewById(R.id.txt_ds_hieu)).setText(mHoaDon.getHieu());
        ((TextView) mRootView.findViewById(R.id.txt_ds_co)).setText(mHoaDon.getCo());
        ((TextView) mRootView.findViewById(R.id.etxt_ds_vi_tri)).setText(mHoaDon.getViTri());
        ((TextView) mRootView.findViewById(R.id.txt_ds_code1)).setText(mHoaDon.getCode_CSC_SanLuong().getCode1());
        ((TextView) mRootView.findViewById(R.id.txt_ds_code2)).setText(mHoaDon.getCode_CSC_SanLuong().getCode2());
        ((TextView) mRootView.findViewById(R.id.txt_ds_code3)).setText(mHoaDon.getCode_CSC_SanLuong().getCode3());

        ((TextView) mRootView.findViewById(R.id.txt_ds_CSC2)).setText(mHoaDon.getCode_CSC_SanLuong().getCSC1());
        ((TextView) mRootView.findViewById(R.id.txt_ds_CSC3)).setText(mHoaDon.getCode_CSC_SanLuong().getCSC2());

        ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1)).setText(mHoaDon.getCode_CSC_SanLuong().getSanLuong1());
        ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2)).setText(mHoaDon.getCode_CSC_SanLuong().getSanLuong2());
        ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3)).setText(mHoaDon.getCode_CSC_SanLuong().getSanLuong3());

        ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setText(mHoaDon.getGiaBieu());
        ;

        ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setText(mHoaDon.getDinhMuc());
//                    refresh();
//        if (hoaDon != null) {
//            mEditTextCSM.setText(hoaDon.getChiSoMoi());
//            mTxtCSM.setText(hoaDon.getChiSoMoi());
//            mGhiChu = hoaDon.getGhiChu();
//
//        } else {
        mGhiChu = "";
        mCode = "40";


//        }
    }

    private boolean checkNull() {

        if (mMLTs.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle("Không có lộ trình").setMessage("Đợt hiện tại đã đọc xong, hoặc chưa có dữ liệu. Vui lòng kiểm tra lại!!!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            }).setNegativeButton("Quản lý đọc số", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callQuanLyDocSoFragment();
                }
            }).setCancelable(false);
            AlertDialog dialog = builder.create();

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            return true;
        }
        return false;
    }

    //    private void save(int csc, int csm) {
//        String dotString = mDot + "";
//        if (mDot < 10)
//            dotString = "0" + mDot;
//        HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo);
//        hoaDon.setCodeMoi(mCode);
//        hoaDon.setChiSoMoi(csm + "");
//        hoaDon.setTieuThuMoi(((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).getText().toString());
//        hoaDon.setGhiChu(mGhiChu);
//        hoaDon.setSdt(getSdtString());
//        if (mEditTextViTri.getText().toString().length() > 0)
//            hoaDon.setViTri(mEditTextViTri.getText().toString());
//        this.currentTime = Calendar.getInstance().getTime();
//        String datetime = this.formatter.format(this.currentTime);
//        hoaDon.setThoiGian(datetime);
////        DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(this.mDanhBo,
////                this.mMlt,
////                dotString,
////                ((TextView) mRootView.findViewById(R.id.txt_ds_tenKH)).getText().toString(),
////                ((TextView) mRootView.findViewById(R.id.txt_ds_diachi)).getText().toString(),
////                ((EditText) mRootView.findViewById(R.id.etxt_ds_sdt)).getText().toString(),
////                ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).getText().toString(),
////                this.mSpinCode.getSelectedItem().toString().substring(0, 2),
////                csc + "",
////                csm + "",
////                ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).getText().toString(),
////                this.mGhiChu,
////                image,
////                1);
//        LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonUnRead(hoaDon);
////        LocalDatabase.getInstance(mRootView.getContext()).deleteHoaDon(danhBo_chiSoMoi.getDanhBo());
//        mTxtTT.setText("");
//
////        this.mAdapterMLT.remove(danhBo_chiSoMoi.getMaLoTrinh());
//        notifyDataSetChange(hoaDon);
//        int i = mSpinMLT.getSelectedItemPosition();
//        if (mMLTs.size() != 0)
//            selectMLT(i == mMLTs.size() ? i - 1 : i);
//        this.mDanhBoHoanThanh++;
//        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);
//
//        Toast.makeText(mRootView.getContext(), "Đã lưu chỉ số mới", Toast.LENGTH_SHORT).show();
//
//        handleFinishADot();
//
//    }
    private void save_without_csm() {
        HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo, false);
        hoaDon.setCodeMoi(mCode);
        hoaDon.setGhiChu(mGhiChu);
        hoaDon.setSoNha(mSoNha);
        hoaDon.setDuong(mDuong);
        if (mEditTextViTri.getText().toString().length() > 0)
            hoaDon.setViTri(mEditTextViTri.getText().toString());
//        if (ImageFile.getFile(currentTime, mRootView, mDanhBo) == null) {
//
//        } else if (!ImageFile.getFile(currentTime, mRootView, mDanhBo).exists()) {
//
//        } else {
//            hoaDon.setImage(ImageFile.getFile(currentTime, mRootView, mDanhBo).getAbsolutePath());
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Bitmap bit = BitmapFactory.decodeFile(hoaDon.getImage());
//            bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//
//
//            hoaDon.setImage_byteArray(outputStream.toByteArray());
//        }

        hoaDon.setSdt(getSdtString());
        mHoaDon = hoaDon;

        LocalDatabase.getInstance(mRootView.getContext()).updateHoaDon_without_csm(hoaDon, Flag.UNREAD);
        Toast.makeText(mRootView.getContext(), "Đã lưu", Toast.LENGTH_LONG).show();
    }

    private void save(int csc, int csm) {
        try {
            String dotString = mDot + "";
            if (mDot < 10)
                dotString = "0" + mDot;
//        HoaDon mHoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_UnRead(mDanhBo);
            mHoaDon.setCodeMoi(mCode);
            if (csm >= 0)
                mHoaDon.setChiSoMoi(csm + "");
            else mHoaDon.setChiSoMoi("");
            mHoaDon.setTieuThuMoi(((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).getText().toString());
            mHoaDon.setGhiChu(mGhiChu);
//            if (!mHoaDon.getImage().equals("null")) {
//                // do nothing
//                this.currentTime = Calendar.getInstance().getTime();
//            } else {
//
//                File f = ImageFile.getFile(currentTime, mRootView, mDanhBo);
//                if (f != null && f.exists()) {
//                    mHoaDon.setImage(f.getAbsolutePath());
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    Bitmap bit = BitmapFactory.decodeFile(mHoaDon.getImage());
//                    bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                    mHoaDon.setImage_byteArray(outputStream.toByteArray());
//                }
//            }
            mHoaDon.setSdt(getSdtString());
            mHoaDon.setSoNha(mSoNha);
            mHoaDon.setDuong(mDuong);
            if (mEditTextViTri.getText().toString().length() > 0)
                mHoaDon.setViTri(mEditTextViTri.getText().toString());
            if (this.currentTime == null)
                this.currentTime = Calendar.getInstance().getTime();
            String datetime = this.formatter.format(this.currentTime);
            mHoaDon.setThoiGian(datetime);


            LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonUnRead(mHoaDon);
            mTxtTT.setText("");
            notifyDataSetChange(mHoaDon);

            int i = mSpinMLT.getSelectedItemPosition();
            if (mMLTs.size() != 0)
                selectMLT(i == mMLTs.size() ? i - 1 : i);
            this.mDanhBoHoanThanh++;
            this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);
            handleFinishADot();
            Toast.makeText(mRootView.getContext(), "Đã lưu chỉ số mới", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            MySnackBar.make(mRootView.getRootView(), "Lỗi khi lưu", false);
        }
    }

    private void notifyDataSetChange(HoaDon hoaDon) {
        this.mMLTs.remove(spaceMLT(hoaDon.getMaLoTrinh()));
        this.mAdapterMLT.notifyDataSetChanged();
//        this.mAdapterDB.remove(danhBo_chiSoMoi.getDanhBo());
        this.mDBs.remove(spaceDB(hoaDon.getDanhBo()));
        this.mAdapterDB.notifyDataSetChanged();
        this.mTenKHs.remove(hoaDon.getTenKhachHang());
        this.mAdapterTenKH.notifyDataSetChanged();

        this.mDiaChis.remove(hoaDon.getDiaChi());
        this.mAdapterDiaChi.notifyDataSetChanged();

        this.mMLTs_old.remove(spaceMLT(hoaDon.getMaLoTrinh()));
//        this.mAdapterDB.remove(danhBo_chiSoMoi.getDanhBo());
        this.mDBs_old.remove(spaceDB(hoaDon.getDanhBo()));
        this.mTenKHs_old.remove(hoaDon.getTenKhachHang());

        this.mDiaChis_old.remove(hoaDon.getDiaChi());

    }

    public boolean checkDotExist() {
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        String like = dotString.concat(mLike.substring(2, 4)).concat("%");

        List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(like, mKy, false);
        if (hoaDons.size() > 0)
            return true;
        return false;
    }

    private void handleFinishADot() {
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        String like = dotString.concat(mLike.substring(2, 4)).concat("%");

        List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_UnRead(like, mKy, false);
        if (hoaDons.size() > 0)
            return;
        else {
            //bỏ đợt hiện tại khỏi spinner
            mDots.remove(dotString);

            mAdapterDot.notifyDataSetChanged();
            ((TextView) mRootView.findViewById(R.id.txt_ds_so_than)).setText("");
            ((TextView) mRootView.findViewById(R.id.etxt_ds_vi_tri)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_hieu)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_co)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_giabieu)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_dinhmuc)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_code)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_code1)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_code2)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_code3)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_CSM)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_CSC)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_CSC2)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_CSC3)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu1)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu2)).setText("");
            ((TextView) mRootView.findViewById(R.id.txt_ds_tieuThu3)).setText("");
            mSdts.clear();
            mAdapterSdt.notifyDataSetChanged();

            mAdapterCode.clear();
            mAdapterCode.notifyDataSetChanged();
            if (mDots.size() > 0)
                mSpinDot.setSelection(0);
            else {
                callQuanLyDocSoFragment();
            }
        }
    }

    private void callQuanLyDocSoFragment() {
        mViewPager.setCurrentItem(1, true);
    }

    private void showImage(File f) {
        //get bitmap from file
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            Bitmap bitmap = BitmapFactory.decodeByteArray(mHoaDon.getImage_byteArray(), 0, mHoaDon.getImage_byteArray().length, options);
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
        } catch (Exception e) {
            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
            return;
        }

    }

    private void showImage(String imageStr) {
        //get bitmap from file
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imageStr, options);
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
                        if (ImageFile.getFile(currentTime, mRootView, mDanhBo) == null) {
                            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
//                            save(csc, csm);
                        } else if (!ImageFile.getFile(currentTime, mRootView, mDanhBo).exists()) {
                            MySnackBar.make(mRootView, "Chưa có hình ảnh", false);
//                            save(csc, csm);
                        } else {
                            save(csc, csm);
                        }
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

    private void alertCSMFluctuation_save(final int csc, final int csm, int canhBao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_csm_fluctuation_title));
        builder.setCancelable(false);
        if (canhBao == -1)
            builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_fluctuation_message_low));
        else
            builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_fluctuation_message_high));
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                save(csc, csm);
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

    private void alertCSMFluctuationPrint(final int csc, final int csm, int canhBao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_csm_fluctuation_title));
        builder.setCancelable(false);
        if (canhBao == -1)
            builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_fluctuation_message_low));
        else
            builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_fluctuation_message_high));
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doPrint();
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

    private void alertCSM_lt_CSC_save(final int csc, final int csm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_csm_lt_csc_title));
        builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_lt_csc_message))
                .setCancelable(false)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        save(csc, csm);
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

    private void alertCSM_lt_CSCPrint(final int csc, final int csm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(mRootView.getContext().getString(R.string.alert_csm_lt_csc_title));
        builder.setMessage(mRootView.getContext().getString(R.string.alert_csm_lt_csc_message))
                .setCancelable(false)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doPrint();
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
        Intent scannerIntent = new Intent(mActivity, ScannerActivity.class);
        startActivityForResult(scannerIntent, REQUEST_ID_SCAN);
    }

    private void doNote() {
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
        int positionNote = 0, positionNoteSub = 0;
        for (String note : Note.getInstance().getNotes()) {
            if (positionNote == Note.getInstance().getNotes().length - 1) {
                if (!mHoaDon.getGhiChu().equals("null"))
                    etxtghichu.setText(mHoaDon.getGhiChu());
            } else if (mHoaDon.getGhiChu().contains(note)) {
                spin_ghichu.setSelection(positionNote);
                switch (positionNote) {
                    case 1:
                        spin_ghichu.setSelection(1);
                        for (String noteSub : Note.getInstance().getNotes_sub_dutchi()) {
                            if (mHoaDon.getGhiChu().contains(noteSub)) {
                                spin_ghichu_sub.setSelection(positionNoteSub);
                                if (mHoaDon.getGhiChu().contains("_")) {
                                    String[] ghiChus = mHoaDon.getGhiChu().split("_");
                                    etxtghichu.setText(ghiChus[1]);
                                }
                                break;
                            }
                            positionNoteSub++;
                        }
                        break;
                    case 2:
                        spin_ghichu.setSelection(2);
                        for (String noteSub : Note.getInstance().getNotes_sub_kinhdoanh()) {
                            if (mHoaDon.getGhiChu().contains(noteSub)) {
                                spin_ghichu_sub.setSelection(positionNoteSub);
                                if (mHoaDon.getGhiChu().contains("_")) {
                                    String[] ghiChus = mHoaDon.getGhiChu().split("_");
                                    etxtghichu.setText(ghiChus[1]);
                                }
                                break;
                            }
                            positionNoteSub++;
                        }
                        break;
                    default:
                        if (mHoaDon.getGhiChu().contains("_")) {
                            String[] ghiChus = mHoaDon.getGhiChu().split("_");
                            etxtghichu.setText(ghiChus[1]);
                        }
                        spin_ghichu.setSelection(positionNote);
                        break;
                }
                break;
            }
            positionNote++;
        }

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
                        etxtghichu.setEnabled(true);
//                        etxtghichu.setText("");
                        spin_ghichu_sub.setAdapter(adapterNotes_sub_dutchi);
                        spin_ghichu_sub.setEnabled(true);
                        spin_ghichu_sub.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        etxtghichu.setEnabled(true);
//                        etxtghichu.setText("");
                        spin_ghichu_sub.setAdapter(adapterNotes_sub_kinhdoanh);
                        spin_ghichu_sub.setEnabled(true);
                        spin_ghichu_sub.setVisibility(View.VISIBLE);
                        break;
                    default:
                        spin_ghichu_sub.setEnabled(false);
//                        etxtghichu.setText("");
                        etxtghichu.setEnabled(true);
                        spin_ghichu_sub.setVisibility(View.VISIBLE);
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
                    case 1:
                    case 2:
                        mGhiChu = spin_ghichu.getSelectedItem().toString() + ": " + spin_ghichu_sub.getSelectedItem().toString();
                        if (etxtghichu.getText().toString().trim().length() > 0)
                            mGhiChu = mGhiChu.concat("_").concat(etxtghichu.getText().toString());
                        break;
                    default:

                        mGhiChu = spin_ghichu.getSelectedItem().toString();
                        if (etxtghichu.getText().toString().trim().length() > 0)
                            mGhiChu = mGhiChu.concat("_").concat(etxtghichu.getText().toString());
                        break;
                }
                mHoaDon.setGhiChu(mGhiChu);
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
        try {
            if (mHoaDon.getImage_byteArray().length > 100) {

                showImage(ImageFile.getFile(currentTime, mRootView, mDanhBo));
            } else
                capture();
        } catch (Exception e) {
            capture();
        }
    }

    private void capture() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());

        this.currentTime = Calendar.getInstance().getTime();
        File photo = ImageFile.getFile(currentTime, mRootView, mDanhBo);
//        this.mUri= FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", photo);
        this.mUri = Uri.fromFile(photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.mUri);
//        this.mUri = Uri.fromFile(photo);
        startActivityForResult(cameraIntent, REQUEST_ID_IMAGE_CAPTURE);
    }


    //    public File getImageFileName() {
//        if (this.currentTime == null)
//            return null;
//        String path = Environment.getExternalStorageDirectory().getPath();
////                path = path.substring(0, path.length() - 1).concat("1");
//        File outFile = new File(path, mRootView.getContext().getString(R.string.path_saveImage));
//
//        if (!outFile.exists())
//            outFile.mkdir();
//        String datetime = this.formatter.format(this.currentTime);
//        File f = new File(outFile, datetime + "_" + this.mDanhBo + ".jpeg");
//        return f;
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID_SCAN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ScannerActivity upc = new ScannerActivity();
                Toast.makeText(mActivity, ScannerActivity.upcCodeValue, Toast.LENGTH_LONG).show();
                for (int i = 0; i < mDBs.size(); i++)
                    if (mDBs.get(i).equals(spaceDB(ScannerActivity.upcCodeValue)))
                        selectMLT(i);
            }
//                overOnSubmit(upc.upcCodeValue);


        } else if (requestCode == REQUEST_ID_IMAGE_CAPTURE)

        {
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
//                    FileOutputStream fos = null;
                    try {
                        if (mBpImage != null) {
//                            fos = new FileOutputStream(ImageFile.getFile(currentTime, mRootView, mDanhBo));

                            Matrix matrix = new Matrix();

                            matrix.postRotate(90);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(mBpImage, 0, 0, mBpImage.getWidth(), mBpImage.getHeight(), matrix, true);
//                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                            //
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            byte[] image = outputStream.toByteArray();
                            mHoaDon.setImage_byteArray(image);
                            LocalDatabase.getInstance(mRootView.getContext()).updateHoaDon_Image(mHoaDon, Flag.UNREAD);

//                            if (getImageFileName().length() < MIN_SIZE) {
//                                alertImageLowQuatity();
//                            }
//                            fos.flush();
//                            fos.close();
                            Toast.makeText(mRootView.getContext(), "Đã lưu ảnh", Toast.LENGTH_SHORT).show();
                            setNextFocusEdittextCSM();

//                            mEditTextCSM.setFocusableInTouchMode(true);
                            mFrameLayoutViewImage.setVisibility(View.VISIBLE);
                            showImageViewInFrame(image);

//                            mEditTextCSM.requestFocus();

                        }
                    } catch (Exception e) {
                        Toast.makeText(mRootView.getContext(), "Chưa lưu được ảnh", Toast.LENGTH_SHORT).show();
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

    private void setNextFocusEdittextCSM() {
//        mEditTextCSM.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        mEditTextCSM.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(mEditTextCSM, InputMethodManager.SHOW_FORCED);
    }
}
