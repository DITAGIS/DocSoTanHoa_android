package com.ditagis.hcm.docsotanhoa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.CodeSpinnerAdapter;
import com.ditagis.hcm.docsotanhoa.adapter.CustomArrayAdapter;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewQuanLyDocSoAdapter;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewSelectFolderAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.Uploading;
import com.ditagis.hcm.docsotanhoa.entities.Code_Describle;
import com.ditagis.hcm.docsotanhoa.entities.Codes;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.theme.ThemeUtils;
import com.ditagis.hcm.docsotanhoa.utities.CONSTANT;
import com.ditagis.hcm.docsotanhoa.utities.CalculateCSM_TieuThu;
import com.ditagis.hcm.docsotanhoa.utities.Calculate_TienNuoc;
import com.ditagis.hcm.docsotanhoa.utities.CheckConnect;
import com.ditagis.hcm.docsotanhoa.utities.Flag;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;
import com.ditagis.hcm.docsotanhoa.utities.Note;
import com.ditagis.hcm.docsotanhoa.utities.Preference;
import com.ditagis.hcm.docsotanhoa.utities.Printer1;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by ThanLe on 25/10/2017.
 */

public class QuanLyDocSo extends Fragment {
    GridView mGridView;
    TextView mTxtComplete;
    AutoCompleteTextView singleComplete;
    List<String> mDBs = new ArrayList<String>(), mMlts = new ArrayList<>(), mTenKHs = new ArrayList<>(), mDiaChis = new ArrayList<>(), mDots = new ArrayList<>(), mSdts = new ArrayList<>(),
            mKys = new ArrayList<>(), mNams = new ArrayList<>();
    Spinner mSpinCode;
    private List<HoaDon> mHoaDons;
    private int mSelected_theme;
    private GridViewQuanLyDocSoAdapter mQuanLyDocSoAdapter;
    private int mSumDanhBo = 0, mDanhBoHoanThanh;
    private int mDot, mKy, mNam, mCSGo;
    private String mUsername;
    private Uploading mUploading;
    private View mRootView;
    private String mStaffName, mStaffPhone;
    private String mSdt;
    private Spinner mSpinSdt;
    private String mLike;
    private String mSearchType;
    private CodeSpinnerAdapter mAdapterCode;
    private String mCode;
    private String mKyString;
    private ArrayAdapter<String> mAdapterDot, mAdapterSdt, mAdapterKy, mAdapterNam;
    private GridViewSelectFolderAdapter mSelectFolderAdapter;
    private Spinner mSpinDot, mSpinKy, mSpinNam;

    @SuppressLint("ValidFragment")
    public QuanLyDocSo(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.quan_ly_doc_so_fragment, null);
        Preference.getInstance().setContext(mRootView.getContext());
        this.mStaffName = Preference.getInstance().loadPreference(mRootView.getContext().getString(R.string.preference_tenNV));
        this.mStaffPhone = Preference.getInstance().loadPreference(mRootView.getContext().getString(R.string.preference_sdtNV));
        mDot = Integer.parseInt(Preference.getInstance().loadPreference(mRootView.getContext().getString(R.string.preference_dot)));
        ;
        mKy = Integer.parseInt(Preference.getInstance().loadPreference(mRootView.getContext().getString(R.string.preference_ky)));
        ;
        mNam = Integer.parseInt(Preference.getInstance().loadPreference(mRootView.getContext().getString(R.string.preference_nam)));
        mUsername = Preference.getInstance().loadPreference(mRootView.getContext().getString(R.string.preference_username));
        this.mSelected_theme = ThemeUtils.THEME_DEFAULT;
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        this.mLike = dotString + mUsername + "%";


        mAdapterCode = new CodeSpinnerAdapter(mRootView.getContext(), R.layout.spinner_item_left1, Codes.getInstance().getCodeDescribles_qlds());
        mAdapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinCode = (Spinner) mRootView.findViewById(R.id.spin_qlds_filter);
        mSpinCode.setAdapter(mAdapterCode);

        mTxtComplete = (TextView) mRootView.findViewById(R.id.txt_qlds_complete);
        mUploading = new Uploading(mKy, mNam, mRootView.getContext());
        mGridView = (GridView) mRootView.findViewById(R.id.grid_qlds_danhSachDocSo);
        mKyString = mKy + "";
        if (mKy < 10)
            mKyString = "0" + mKy;
        mKys.add(mKyString);
        mNams.add(mNam + "");
//        mSumDanhBo = mSumDanhBoDB.getSum(mKyString, mNam, mLike);
//        this.mDanhBoHoanThanh = mSumDanhBoDB.getSumSynchronized(mKyString, mNam, mLike);
        //Gán DataSource vào ArrayAdapter
        mQuanLyDocSoAdapter = new GridViewQuanLyDocSoAdapter(mRootView.getContext(), new ArrayList<GridViewQuanLyDocSoAdapter.Item>());

        //gán Datasource vào GridView

        mGridView.setAdapter(mQuanLyDocSoAdapter);
        registerForContextMenu(mGridView);

//        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                showMoreInfo(view);
//                return false;
//            }
//        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMoreInfo(view);
            }
        });
        setDynamicWidth(mGridView);

        final ImageButton imgBtnUpload = (ImageButton) mRootView.findViewById(R.id.imgBtn_qlds_upload);
        imgBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpLoad();

            }
        });
//        imgBtnUpload.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorImgBtnUpload_1));
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        v.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorPrimary_1));
//                        if (LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Read(mLike, mKy, true).size() == 0) {
//                            MySnackBar.make(mGridView, "Chưa có danh bộ!!!", false);
//                        } else if (isOnline()) {
//                            doUpLoad();
//                        } else {
//                            MySnackBar.make(mGridView, "Kiểm tra kết nối Internet và thử lại", false);
//                        }
//                        return true;
//                }
//                return false;
//            }
//        });
//        mHoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Read(mLike, mKy, true);
//        mHoaDons.addAll(LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Synchronized(mLike, mKy, true));
//        for (HoaDon hoaDon : this.mHoaDons) {
//            mDBs.add(hoaDon.getDanhBo());
//        }
        mSearchType = mRootView.getContext().getString(R.string.search_danhbo);
        singleComplete = (AutoCompleteTextView) mRootView.findViewById(R.id.editauto_qlds);
        singleComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    return;
                if (mSearchType.equals(mRootView.getContext().getString(R.string.search_mlt))) {
                    mQuanLyDocSoAdapter.clear();
                    int countTieuThu = 0;
                    for (HoaDon hoaDon : mHoaDons) {
                        if (hoaDon.getMaLoTrinh().contains(s.toString())) {
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                                    hoaDon.getMaLoTrinh(),
                                    hoaDon.getDanhBo(),
                                    hoaDon.getChiSoCu(),
                                    hoaDon.getChiSoMoi(),
                                    hoaDon.getCodeMoi(),
                                    hoaDon.getDiaChi(),
                                    hoaDon.getThoiGian(),
                                    hoaDon.getFlag()
                            ));
                            try {
                                countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                            } catch (Exception e) {
                                Log.e("", e.toString());
                            }
                        }
                    }
                    notifyDataSetGridViewChange();
                    ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));

                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                    if (s.length() == 0)
                        return;
                    mQuanLyDocSoAdapter.clear();
                    int countTieuThu = 0;
                    for (HoaDon hoaDon : mHoaDons) {
                        if (hoaDon.getDanhBo().contains(s.toString())) {
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                                    hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(),
                                    hoaDon.getChiSoCu(),
                                    hoaDon.getChiSoMoi(),
                                    hoaDon.getCodeMoi(),
                                    hoaDon.getDiaChi(),
                                    hoaDon.getThoiGian(),
                                    hoaDon.getFlag()
                            ));
                            try {
                                countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                            } catch (Exception e) {
                                Log.e("", e.toString());
                            }
                        }
                    }
                    notifyDataSetGridViewChange();
                    ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));

                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                    if (s.length() == 0)
                        return;
                    mQuanLyDocSoAdapter.clear();
                    int countTieuThu = 0;
                    for (HoaDon hoaDon : mHoaDons) {
                        if (hoaDon.getTenKhachHang().toLowerCase().contains(s.toString().toLowerCase())) {
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                                    hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(),
                                    hoaDon.getChiSoCu(),
                                    hoaDon.getChiSoMoi(),
                                    hoaDon.getCodeMoi(),
                                    hoaDon.getDiaChi(),
                                    hoaDon.getThoiGian(),
                                    hoaDon.getFlag()));
                            try {
                                countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                            } catch (Exception e) {
                                Log.e("", e.toString());
                            }
                        }
                    }
                    notifyDataSetGridViewChange();
                    ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                    if (s.length() == 0)
                        return;
                    mQuanLyDocSoAdapter.clear();
                    int countTieuThu = 0;
                    for (HoaDon hoaDon : mHoaDons) {
                        if (hoaDon.getDiaChi().toLowerCase().contains(s.toString().toLowerCase())) {
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                                    hoaDon.getMaLoTrinh(),
                                    hoaDon.getDanhBo(),
                                    hoaDon.getChiSoCu(),
                                    hoaDon.getChiSoMoi(),
                                    hoaDon.getCodeMoi(), hoaDon.getDiaChi(), hoaDon.getThoiGian(), hoaDon.getFlag()));
                            try {
                                countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                            } catch (Exception e) {
                                Log.e("", e.toString());
                            }
                        }
                    }
                    notifyDataSetGridViewChange();
                    ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        (mRootView.findViewById(R.id.btn_qlds_optionSearch)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionSearch();
                    }
                });

        mSpinCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQuanLyDocSoAdapter.clear();
                Code_Describle code_describle = (Code_Describle) mSpinCode.getItemAtPosition(position);
                String code = code_describle.getCode();
                //xử lý trường hợp lọc tất cả
                if (code.equals(Codes.getInstance().getCodeDescribles_qlds()[0].getCode()))
                    code = "";
                int countTieuThu = 0;
                for (HoaDon hoaDon : mHoaDons) {
                    if (hoaDon.getCodeMoi() == null)
                        continue;

                    if (hoaDon.getCodeMoi().contains(code)) {
                        mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                                hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(),
                                hoaDon.getChiSoCu(),
                                hoaDon.getChiSoMoi(),
                                hoaDon.getCodeMoi(), hoaDon.getDiaChi(), hoaDon.getThoiGian(), hoaDon.getFlag()));
                        try {
                            countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                        } catch (Exception e) {
                            Log.e("", e.toString());
                        }
                    }
                }
                notifyDataSetGridViewChange();
                ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinDot = (Spinner) mRootView.findViewById(R.id.spin_qlds_dot);
        mSpinKy = (Spinner) mRootView.findViewById(R.id.spin_qlds_ky);
        mSpinNam = (Spinner) mRootView.findViewById(R.id.spin_qlds_nam);
        mSpinDot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDot(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        mDots.add(dotString);


//        createDot();
//        mAdapterKy = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mKys);
//        mAdapterKy.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        mSpinKy.setAdapter(mAdapterKy);
//        mAdapterNam = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mNams);
//        mAdapterNam.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        mSpinNam.setAdapter(mAdapterNam);

        setTheme();
    }


    public int getmDot() {
        return mDot;
    }

    public int getmKy() {
        return mKy;
    }

    public void setmDot(int mDot) {
        this.mDot = mDot;
    }

    public void setmSelected_theme(int mSelected_theme) {
        this.mSelected_theme = mSelected_theme;
        try {
            setTheme();
        } catch (Exception e) {
            Log.e("", e.toString());
        }
    }

    public void selectDotFromDialog(GridViewSelectFolderAdapter adapter, String ky, String dot) {
        mSelectFolderAdapter = adapter;
        mKys.clear();
        for (GridViewSelectFolderAdapter.Item item : mSelectFolderAdapter.getItems()) {
            @SuppressLint("DefaultLocale") String iKy = String.format("%02d", Integer.parseInt(item.getKy()));
            if (!mKys.contains(iKy))
                mKys.add(iKy);
        }
        mAdapterKy.notifyDataSetChanged();
        @SuppressLint("DefaultLocale") String kyString = String.format("%02d", Integer.parseInt(ky));
        int dotInt = Integer.parseInt(dot);
        String dotString = dotInt + "";
        if (dotInt < 10)
            dotString = "0" + dotInt;

        for (int i = 0; i < mAdapterKy.getCount(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.requireNonNull(mAdapterKy.getItem(i)).equals(kyString)) {
                    mSpinKy.setSelection(i);
                    break;
                }
            }
        }
        for (int i = 0; i < mAdapterDot.getCount(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.requireNonNull(mAdapterDot.getItem(i)).equals(dotString)) {
                    mSpinDot.setSelection(i);
                    break;
                }
            }
        }
    }

    private void notifyDataSetGridViewChange() {
        mQuanLyDocSoAdapter.notifyDataSetChanged();
//        mQuanLyDocSoAdapter.sort(new Comparator<GridViewQuanLyDocSoAdapter.Item>() {
//            public int compare(GridViewQuanLyDocSoAdapter.Item item1, GridViewQuanLyDocSoAdapter.Item item2) {
//                return item2.getThoiGian().compareTo(item1.getThoiGian());
//            }
//        });
        mQuanLyDocSoAdapter.sort(new Comparator<GridViewQuanLyDocSoAdapter.Item>() {
            public int compare(GridViewQuanLyDocSoAdapter.Item item1, GridViewQuanLyDocSoAdapter.Item item2) {
                return item1.getMlt().compareTo(item2.getMlt());
            }
        });
    }

    private void add_sdt() {
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());//getLayoutInflater();
        @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.layout_edittext, null);
        final EditText etxtSdt = (EditText) dialogLayout.findViewById(R.id.edit_layout_edittext);
        etxtSdt.setText(mSdt);
        AlertDialog.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(builder).setTitle("Thêm số điện thoại");

            Objects.requireNonNull(builder).setPositiveButton(mRootView.getContext().getString(R.string.add), new DialogInterface.OnClickListener() {
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
                    }
                }
            }).setCancelable(true);
            AlertDialog dialog = builder.create();
            dialog.setView(dialogLayout);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        refresh();
        return mRootView;
    }

    private void setTheme() {
        switch (mSelected_theme) {
            case ThemeUtils.THEME_DEFAULT:
                (mRootView.findViewById(R.id.layout_qlds)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_qlds)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_qlds)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                (mRootView.findViewById(R.id.editauto_qlds)).setBackgroundResource(R.drawable.edit_text_styles);
                (mRootView.findViewById(R.id.btn_qlds_optionSearch)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_1));
                (mRootView.findViewById(R.id.btn_qlds_optionSearch)).setBackgroundResource(R.drawable.edit_text_styles);
                ((Button) mRootView.findViewById(R.id.btn_qlds_optionSearch)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.editauto_qlds_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                ((TextView) mRootView.findViewById(R.id.txt_qlds_filter_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                ((TextView) mRootView.findViewById(R.id.txt_qlds_nam_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_ky_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_dot_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));

                mAdapterNam = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mNams);
                mAdapterKy = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mKys);
                mAdapterDot = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mDots);

                (mRootView.findViewById(R.id.layout_qlds_gridview_title)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorGridviewTitle_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_danhbo_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_diachi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_csc_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_csmoi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_tieuthu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_thoigian_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_trangthai_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                mAdapterCode = new CodeSpinnerAdapter(mRootView.getContext(), R.layout.spincode_dropdown, Codes.getInstance().getCodeDescribles_qlds()) {
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
                (mRootView.findViewById(R.id.layout_qlds_summary)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorSummary_1));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_1));
                break;
            case ThemeUtils.THEME_DARK:
                (mRootView.findViewById(R.id.layout_qlds)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_qlds)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((AutoCompleteTextView) mRootView.findViewById(R.id.editauto_qlds)).setHintTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                (mRootView.findViewById(R.id.editauto_qlds)).setBackgroundResource(R.drawable.edit_text_styles2);
                (mRootView.findViewById(R.id.btn_qlds_optionSearch)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorBackground_2));
                (mRootView.findViewById(R.id.btn_qlds_optionSearch)).setBackgroundResource(R.drawable.edit_text_styles2);
                ((Button) mRootView.findViewById(R.id.btn_qlds_optionSearch)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.editauto_qlds_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_filter_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                ((TextView) mRootView.findViewById(R.id.txt_qlds_nam_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_ky_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_dot_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                mAdapterNam = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left2, mNams);
                mAdapterKy = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left2, mKys);
                mAdapterDot = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left2, mDots);

                (mRootView.findViewById(R.id.layout_qlds_gridview_title)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorGridviewTitle_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_danhbo_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_diachi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_csc_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_csmoi_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_tieuthu_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_code_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_thoigian_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_trangthai_title)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));
                mAdapterCode = new CodeSpinnerAdapter(mRootView.getContext(), R.layout.spincode_dropdown2, Codes.getInstance().getCodeDescribles_qlds()) {
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

                (mRootView.findViewById(R.id.layout_qlds_summary)).setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorSummary_2));
                ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setTextColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorTextColor_2));

                break;

        }
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

            }
        });

        mAdapterCode.setDropDownViewResource(R.layout.spincode_dropdown2);

        mSpinCode.setAdapter(mAdapterCode);
        mSpinCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQuanLyDocSoAdapter.clear();
                Code_Describle code_describle = (Code_Describle) mSpinCode.getItemAtPosition(position);
                String code = code_describle.getCode();
                //xử lý trường hợp lọc tất cả
                if (code.equals(Codes.getInstance().getCodeDescribles_qlds()[0].getCode()))
                    code = "";
                int countTieuThu = 0;
                for (HoaDon hoaDon : mHoaDons) {
                    if (hoaDon.getCodeMoi() == null)
                        continue;
                    if (hoaDon.getCodeMoi().contains(code)) {
                        mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                                hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(),
                                hoaDon.getChiSoCu(),
                                hoaDon.getChiSoMoi(),
                                hoaDon.getCodeMoi(), hoaDon.getDiaChi(), hoaDon.getThoiGian(), hoaDon.getFlag()));
                        try {
                            countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                        } catch (Exception e) {
                            Log.e("", e.toString());
                        }
                    }
                }

                notifyDataSetGridViewChange();
                ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinCode.setSelection(0);
            }
        });


        setBackGroundTintModeSpinner(PorterDuff.Mode.LIGHTEN);
    }

    private void setBackGroundTintModeSpinner(PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSpinNam.setBackgroundTintMode(mode);
            mSpinKy.setBackgroundTintMode(mode);
            mSpinDot.setBackgroundTintMode(mode);

            mSpinCode.setBackgroundTintMode(mode);

        }
    }

    private void setDynamicWidth(GridView gridView) {

        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            return;
        }
        if (gridViewAdapter.getCount() == 0)
            return;
        int totalWidth;
        int items = gridViewAdapter.getCount();
        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalWidth = listItem.getMeasuredWidth();
        totalWidth = totalWidth * items;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = totalWidth;
        gridView.setLayoutParams(params);

    }

    public void refresh() {
//        createKy();
        mHoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonQLDS(mLike, mKy);
        setTextProgress();
        mQuanLyDocSoAdapter.clear();
        int countTieuThu = 0;
        for (HoaDon hoaDon : this.mHoaDons) {
            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                    hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                    hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(),
                    hoaDon.getChiSoCu(),
                    hoaDon.getChiSoMoi(),
                    hoaDon.getCodeMoi(),
                    hoaDon.getDiaChi(),
                    hoaDon.getThoiGian(),
                    hoaDon.getFlag()));
            try {
                countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
            } catch (Exception e) {
                Log.e("", e.toString());
            }
        }
        mSpinCode.setSelection(0);

        notifyDataSetGridViewChange();

        setDynamicWidth(mGridView);
        ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).

                setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.

                        size(), countTieuThu));
    }

    private void selectKy(int position) {
        try {
            //todo?
            mKy = Integer.parseInt(mKys.get(position));
            createDot();
        } catch (Exception e) {
            Log.e("", e.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setTextProgress() {
//        this.mDanhBoHoanThanh = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Synchronized(mLike).size();
        this.mDanhBoHoanThanh = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonSize(mLike, mKy, Flag.SYNCHRONIZED, false) +
                LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonSize(mLike, mKy, Flag.CODE_F_SYNCHRONIZED, false);
        this.mSumDanhBo = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonSumSize(mLike, mKy);
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

    }


    private void selectDot(int position) {
        if (mDots.size() > 0) {
            String dotString = mDots.get(position);
            mDot = Integer.parseInt(dotString);

            mLike = dotString.concat(mLike.substring(2, 4)).concat("%");
            List<HoaDon> hoaDons = LocalDatabase.getInstance((mRootView.getContext())).getAllHoaDonQLDS(mLike, mKy);
//            List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Read(mLike, mKy, false);
//            hoaDons.addAll(LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Synchronized(mLike, mKy, false));
            if (mHoaDons == null)
                mHoaDons = new ArrayList<>();
            mHoaDons.clear();
            mHoaDons.addAll(hoaDons);
            mQuanLyDocSoAdapter.clear();
            int countTieuThu = 0;
            for (HoaDon hoaDon : hoaDons) {
                mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                        hoaDon.getTieuThuMoi() == null ? "" : hoaDon.getTieuThuMoi(),
                        hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(),
                        hoaDon.getChiSoCu(),
                        hoaDon.getChiSoMoi(),
                        hoaDon.getCodeMoi(), hoaDon.getDiaChi(), hoaDon.getThoiGian(),
                        hoaDon.getFlag()));
                try {
                    countTieuThu += Integer.parseInt(hoaDon.getTieuThuMoi());
                } catch (Exception e) {
                    Log.e("", e.toString());
                }
            }
            mSpinCode.setSelection(0);
            notifyDataSetGridViewChange();
            ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));
            setTextProgress();
        }
    }


    private void createDot() {
        mAdapterDot.clear();
        getDotExist();
//        mAdapterDot = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mDots);
//        mAdapterDot.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        mSpinDot.setAdapter(mAdapterDot);
        mAdapterDot.notifyDataSetChanged();
        selectDot(0);
    }

    private void getDotExist() {
        int count = 0;
        for (int i = 20; i >= 1; i--) {
//            if(mSelectFolderAdapter!= null)
//            for (GridViewSelectFolderAdapter.Item item : mSelectFolderAdapter.getItems()) {
//                if (mKy == Integer.parseInt(item.getKy()) && i != Integer.parseInt(item.getDot()))
//                    break;
            if (count == CONSTANT.MAX_DOT)
                break;
            String dotString = i + "";
            if (i < 10)
                dotString = "0" + i;
//            List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon(mUsername, dotString, mKy + "", false);
            boolean isExistHoaDon = LocalDatabase.getInstance(mRootView.getContext()).getExistHoaDon(mUsername, dotString, mKy + "");
            if (isExistHoaDon && !mDots.contains(dotString))
                mDots.add(dotString);
            if (mDots.size() > 0)
                count++;

//            }
        }
//        if (mDots.size() > 1) {
//            MyAlertDialog.show(mRootView.getContext(), false, mRootView.getContext().getString(R.string.dotExist_title), mRootView.getContext().getString(R.string.dotExist_message));
//        }
        deleteHoaDon();
    }

    private void deleteHoaDon() {
        List<HoaDon> hoaDonLst = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon(false);
        for (HoaDon hoaDon : hoaDonLst) {
            if (Integer.parseInt(hoaDon.getDot()) < mDot - 3 && hoaDon.getFlag() == Flag.SYNCHRONIZED
                    ) {
                LocalDatabase.getInstance(mRootView.getContext()).deleteHoaDon(hoaDon.getDanhBo(), Flag.SYNCHRONIZED);
            }
        }
    }

    private void optionSearch() {
//        String[] searchTypes = {mRootView.getContext().getString(R.string.search_mlt),
//                mRootView.getContext().getString(R.string.search_danhbo)};
//                mRootView.getContext().getString(R.string.search_tenKH)};

        AlertDialog.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(builder).setTitle("Tùy chọn tìm kiếm");
            builder.setCancelable(true);
            LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
            @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_search_type, null);

            final RadioGroup group = (RadioGroup) dialogLayout.findViewById(R.id.radioGroup_searchtype);
            if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_mlt)))
                group.check(R.id.radio_search_mlt);
            else if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                group.check(R.id.radio_search_danhbo);
            } else if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                group.check(R.id.radio_search_tenKH);
            } else if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                group.check(R.id.radio_search_diaChi);

            }
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
                        mMlts.clear();
                        for (HoaDon hoaDon : mHoaDons) {
                            mMlts.add(hoaDon.getMaLoTrinh());
                        }
                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mMlts
                        ));
                    } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                        mDBs.clear();
                        for (HoaDon hoaDon : mHoaDons) {
                            mDBs.add(hoaDon.getDanhBo());
                        }
                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mDBs
                        ));
                    } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                        mTenKHs.clear();
                        for (HoaDon hoaDon : mHoaDons) {
                            mTenKHs.add(hoaDon.getTenKhachHang());
                        }

                        singleComplete.setAdapter(new CustomArrayAdapter(
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mTenKHs
                        ));
                    } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                        mDiaChis.clear();
                        for (HoaDon hoaDon : mHoaDons) {
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
    }


    @SuppressLint({"NewApi", "SetTextI18n"})
    private void showMoreInfo(final View view) {
        String danhBo = ((TextView) view.findViewById(R.id.row_qlds_txt_danhBo)).getText().toString();
        HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_Read(danhBo, true);
        if (hoaDon == null) {
            hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_Synchronized(danhBo, true);
            if (hoaDon == null)
                return;
        }
        final double tienNuoc = Calculate_TienNuoc.getInstance().calculate(Integer.parseInt(hoaDon.getTieuThuMoi()), hoaDon.getGiaBieu(),
                hoaDon.getDinhMuc(), hoaDon.getSh(), hoaDon.getSx(), hoaDon.getDv(), hoaDon.getHc());
        //--------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        Objects.requireNonNull(builder).setTitle("Danh bộ: " + danhBo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (hoaDon.getFlag() == Flag.READ) {

            final HoaDon finalHoaDon = hoaDon;
            builder.setNegativeButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    edit_info(view);
                }
            }).setPositiveButton("In GBTN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
//                    if (Printer.getInstance().getmBluetoothSocket() == null) {
//                        MySnackBar.make(mGridView, "Chưa kết nối với máy in", true);
//                        return;
//                    }
//
                    Printer1.getInstance().setValue(mNam, mStaffName, mStaffPhone, finalHoaDon, tienNuoc);
                    Printer1.getInstance().print();


                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.layout_view_thongtin_docso, null);


        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        try {
            if (hoaDon.getImage_byteArray().length > CONSTANT.MIN_IMAGE_QUATITY) {
                ImageView image = (ImageView) dialog.findViewById(R.id.imgView_qlds);
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(hoaDon.getImage_byteArray(), 0, hoaDon.getImage_byteArray().length, options);


                    BitmapDrawable resizedDialogImage = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.7), (int) (bitmap.getHeight() * 0.7), false));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(image).setBackground(resizedDialogImage);
                    }
                } catch (Exception e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        MySnackBar.make(Objects.requireNonNull(image).getRootView(), "Không tìm thấy hình ảnh", false);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("", e.toString());
        }
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_SoThan))).setText(hoaDon.getSoThan());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_MLT))).setText(hoaDon.getMaLoTrinh());
//                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_DanhBo)).setText(danhBo_CSM.getDanhBo());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_tenKH))).setText(hoaDon.getTenKhachHang());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_diaChi))).setText(hoaDon.getDiaChi());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_SDT))).setText(hoaDon.getSdt());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_code))).setText(hoaDon.getCodeMoi());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_CSC))).setText(hoaDon.getChiSoCu());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_CSM))).setText(hoaDon.getChiSoMoi());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_tieuThu))).setText(hoaDon.getTieuThuMoi());
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_giaBieu))).setText(hoaDon.getGiaBieu());
        double BVMT = 0, VAT = tienNuoc / 20;
        if (!hoaDon.getGiaBieu().equals("52"))
            BVMT = tienNuoc / 10;
        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_tienNuoc))).setText(NumberFormat.getNumberInstance(Locale.US).format(tienNuoc + BVMT + VAT) + " VNĐ");//TODO tien nuoc

        ((TextView) Objects.requireNonNull(dialog.findViewById(R.id.txt_layout_qlds_ghiChu))).setText(hoaDon.getGhiChu());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void edit_info(View view) {
        try {
            final String danhBo = ((TextView) view.findViewById(R.id.row_qlds_txt_danhBo)).getText().toString();
            final HoaDon hoaDon = LocalDatabase.getInstance(mRootView.getContext()).getHoaDon_Read(danhBo, true);

            AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle("Cập nhật thông tin chỉ số");
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
            @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.layout_edit_thongtin_docso, null);
            final EditText etxtAddress = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_address_number);
            etxtAddress.setText(hoaDon.getSoNha());
            mSdts.add(" ");
            if (hoaDon.getSdt().length() > 0) {
                mSdts.clear();
                String[] sdts = hoaDon.getSdt().split("-");
                for (String sdt : sdts) {
                    mSdts.add(sdt.trim());
                }
            }
            mAdapterSdt = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_left1, mSdts);
            mAdapterSdt.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            mSpinSdt = (Spinner) dialogLayout.findViewById(R.id.spin_qlds_sdt);
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
//        mSdt = mSdts.get(0);
            mSpinSdt.setSelection(0);
            (dialogLayout.findViewById(R.id.imgBtn_qlds_add_sdt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_sdt();
                }
            });
            try {
                if (hoaDon.getImage_byteArray().length > CONSTANT.MIN_IMAGE_QUATITY) {
                    ImageView image = (ImageView) dialogLayout.findViewById(R.id.imgView_edit);
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(hoaDon.getImage_byteArray(), 0, hoaDon.getImage_byteArray().length, options);

                        BitmapDrawable resizedDialogImage = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.7), (int) (bitmap.getHeight() * 0.7), false));

                        image.setBackground(resizedDialogImage);
                    } catch (Exception e) {
                        Log.e("", e.toString());
                    }
                }
            } catch (Exception e) {
                Log.e("", e.toString());
            }
            ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_MLT)).setText(hoaDon.getMaLoTrinh());
//                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_DanhBo)).setText(danhBo_CSM.getDanhBo());
            ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_tenKH)).setText(hoaDon.getTenKhachHang());
            ((TextView) dialogLayout.findViewById(R.id.txt_layout_qlds_SoThan)).setText(hoaDon.getSoThan());
            final EditText etxtAddressStreet = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_address_street);
            etxtAddressStreet.setText(hoaDon.getDuong());
            ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_CSC)).setText(hoaDon.getChiSoCu());
            final TextView txtTT = (TextView) dialogLayout.findViewById(R.id.txt_layout_edit_tieuThu);
            txtTT.setText(hoaDon.getTieuThuMoi());
            final EditText etxtCSM = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_CSM);

            etxtCSM.setText(hoaDon.getChiSoMoi());

//        final EditText etxtSDT = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_SDT);
//        etxtSDT.setText(hoaDon.getSdt());

            final TextView txtNote = (TextView) dialogLayout.findViewById(R.id.txt_layout_edit_ghiChu);
            txtNote.setText(hoaDon.getGhiChu());
            (dialogLayout.findViewById(R.id.btn_layout_edit_ghiChu)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
                    builder.setTitle("Ghi chú");
                    LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
                    @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_ghichu, null);
                    final Spinner spin_ghichu = (Spinner) dialogLayout.findViewById(R.id.spin_select_ghichu);
                    final Spinner spin_ghichu_sub = (Spinner) dialogLayout.findViewById(R.id.spin_select_ghichu_sub);
                    final EditText etxtghichu = (EditText) dialogLayout.findViewById(R.id.etxt_select_ghichu);
                    etxtghichu.setBackgroundResource(R.drawable.edit_text_styles);
                    etxtghichu.setEnabled(false);
                    ArrayAdapter<String> adapterNotes = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_note_left, Note.getInstance().getNotes());
                    final ArrayAdapter<String> adapterNotes_sub_dutchi = new ArrayAdapter<>(mRootView.getContext(), R.layout.spinner_item_note_left, Note.getInstance().getNotes_sub_dutchi());
                    final ArrayAdapter<String> adapterNotes_sub_kinhdoanh = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_note_left, Note.getInstance().getNotes_sub_kinhdoanh());

                    spin_ghichu.setAdapter(adapterNotes);
                    int positionNote = 0, positionNoteSub = 0;
                    for (String note : Note.getInstance().getNotes()) {
                        if (positionNote == Note.getInstance().getNotes().length - 1) {
                            if (!hoaDon.getGhiChu().equals("null"))
                                etxtghichu.setText(hoaDon.getGhiChu());
                        } else if (hoaDon.getGhiChu().contains(note)) {
                            spin_ghichu.setSelection(positionNote);
                            switch (positionNote) {
                                case 1:
                                    spin_ghichu.setSelection(1);
                                    for (String noteSub : Note.getInstance().getNotes_sub_dutchi()) {
                                        if (hoaDon.getGhiChu().contains(noteSub)) {
                                            spin_ghichu_sub.setSelection(positionNoteSub);
                                            if (hoaDon.getGhiChu().contains("_")) {
                                                String[] ghiChus = hoaDon.getGhiChu().split("_");
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
                                        if (hoaDon.getGhiChu().contains(noteSub)) {
                                            spin_ghichu_sub.setSelection(positionNoteSub);
                                            if (hoaDon.getGhiChu().contains("_")) {
                                                String[] ghiChus = hoaDon.getGhiChu().split("_");
                                                etxtghichu.setText(ghiChus[1]);
                                            }
                                            break;
                                        }
                                        positionNoteSub++;
                                    }
                                    break;
                                default:
                                    if (hoaDon.getGhiChu().contains("_")) {
                                        String[] ghiChus = hoaDon.getGhiChu().split("_");
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
//                                etxtghichu.setText("");
                                    spin_ghichu_sub.setAdapter(adapterNotes_sub_dutchi);
                                    spin_ghichu_sub.setEnabled(true);
                                    spin_ghichu_sub.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    etxtghichu.setEnabled(true);
//                                etxtghichu.setText("");
                                    spin_ghichu_sub.setAdapter(adapterNotes_sub_kinhdoanh);
                                    spin_ghichu_sub.setEnabled(true);
                                    spin_ghichu_sub.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    spin_ghichu_sub.setEnabled(false);
//                                etxtghichu.setText("");
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
                            String ghiChu = "";
//                mGhiChu = input.getText().toString();
                            switch (spin_ghichu.getSelectedItemPosition()) {
                                case 0:
                                    txtNote.setText(etxtghichu.getText().toString());
                                    break;
                                case 1:
                                case 2:
                                    ghiChu = spin_ghichu.getSelectedItem().toString() + ": " + spin_ghichu_sub.getSelectedItem().toString();

                                    if (etxtghichu.getText().toString().trim().length() > 0)
                                        ghiChu = ghiChu.concat("_").concat(etxtghichu.getText().toString());
                                    txtNote.setText(ghiChu);
                                    break;
                                default:
                                    ghiChu = spin_ghichu.getSelectedItem().toString();
                                    if (etxtghichu.getText().toString().trim().length() > 0)
                                        ghiChu = ghiChu.concat("_").concat(etxtghichu.getText().toString());
                                    txtNote.setText(ghiChu);
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
            });

            final Spinner spinCode = (Spinner) dialogLayout.findViewById(R.id.spin_edit_code);
            CodeSpinnerAdapter adapterCode = new CodeSpinnerAdapter(mRootView.getContext(), R.layout.spincode_dropdown2, Codes.getInstance().getCodeDescribles_ds()) {
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
            adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spinCode.setAdapter(adapterCode);
            for (int i = 0; i < Codes.getInstance().getCodeDescribles_ds().length; i++)
                if (Codes.getInstance().getCodeDescribles_ds()[i].getCode().equals(hoaDon.getCodeMoi())) {
                    spinCode.setSelection(i);
                    break;
                }
            spinCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                Code_Describle code_describle = (Code_Describle) spinCode.getItemAtPosition(position);
                    Code_Describle code_describle = Codes.getInstance().getCodeDescribles_ds()[position];
                    mCode = code_describle.getCode();
                    if (mCode.startsWith("8"))
                        mCSGo = hoaDon.getCsgo();
                    if (mCSGo == -1) {
                        MySnackBar.make(mTxtComplete, "Chưa có dữ liệu báo thay", true);
                        mSpinCode.setSelection(0);
                    }
                    try {
                        CalculateCSM_TieuThu csm_tieuThu = new CalculateCSM_TieuThu(mCode, hoaDon.getCode_CSC_SanLuong(), Integer.parseInt(hoaDon.getChiSoCu()), hoaDon.getChiSoMoi(), hoaDon.getCsgo(), hoaDon.getCsganmoi());

                        etxtCSM.setText(csm_tieuThu.getCSM());
                        txtTT.setText(csm_tieuThu.getTieuThu());

                        hoaDon.setChiSoMoi(csm_tieuThu.getCSM());
                        hoaDon.setTieuThuMoi(csm_tieuThu.getTieuThu());
                    } catch (Exception e) {

                    }
                    if (CalculateCSM_TieuThu.checkCSMFluctuation(hoaDon.getTieuThuMoi(), hoaDon.getCode_CSC_SanLuong().getSanLuong1(),
                            hoaDon.getCode_CSC_SanLuong().getSanLuong2(), hoaDon.getCode_CSC_SanLuong().getSanLuong3())) {
                        etxtCSM.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
                    } else {
                        etxtCSM.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            etxtCSM.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        CalculateCSM_TieuThu csm_tieuThu = new CalculateCSM_TieuThu(mCode, hoaDon.getCode_CSC_SanLuong(), Integer.parseInt(hoaDon.getChiSoCu()), s.toString(), hoaDon.getCsgo(), hoaDon.getCsganmoi());

                        txtTT.setText(csm_tieuThu.getTieuThu());
                        hoaDon.setChiSoMoi(s.toString());
                        hoaDon.setTieuThuMoi(csm_tieuThu.getTieuThu());
                        if (CalculateCSM_TieuThu.checkCSMFluctuation(csm_tieuThu.getTieuThu(), hoaDon.getCode_CSC_SanLuong().getSanLuong1(),
                                hoaDon.getCode_CSC_SanLuong().getSanLuong2(), hoaDon.getCode_CSC_SanLuong().getSanLuong3())) {
                            etxtCSM.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorAlertWrong_1));
                        } else {
                            etxtCSM.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorCSC_SL_0_1));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_giaBieu)).setText(hoaDon.getGiaBieu());
            ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_tienNuoc)).setText("");//TODO tien nuoc

//        ((TextView) dialogLayout.findViewById(R.id.etxt_layout_edit_ghiChu)).setText(hoaDon.getGhiChu());

            builder.setView(dialogLayout)
                    .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Lưu thay đổi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: lưu chỉnh sửa
                    if (etxtCSM.getText().toString().trim().length() == 0 || txtTT.getText().toString().trim().toString().length() == 0F)
                        return;
                    hoaDon.setChiSoMoi(etxtCSM.getText().toString());
                    hoaDon.setCodeMoi(mCode);
                    if (etxtAddress.getText().toString().trim().length() > 0)
                        hoaDon.setSoNha(etxtAddress.getText().toString());
                    if (etxtAddressStreet.getText().toString().trim().length() > 0)
                        hoaDon.setDuong(etxtAddressStreet.getText().toString());
//                hoaDon.setSdt(etxtSDT.getText().toString());
                    hoaDon.setGhiChu(txtNote.getText().toString());
                    hoaDon.setSdt(getSdtString());
                    LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonRead(hoaDon);
                    refresh();
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } catch (Exception e) {

        }

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

    private void doUpLoad() {
        if (!CheckConnect.isOnline(getActivity())) {
            MySnackBar.make(mTxtComplete, mRootView.getContext().getString(R.string.no_connect), true);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Đồng bộ danh bộ?");
        builder.setMessage("Dữ liệu sau khi đồng bộ sẽ không thể chỉnh sửa!")
                .setCancelable(true)
                .setPositiveButton("Đồng bộ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        upLoadData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    //TODO: progress bar
    private void upLoadData() {
        try {
            ConnectionDB.getInstance().reConnect();
            new UploadingAsync().execute();
        } catch (Exception e) {

        }
    }

    class UploadingAsync extends AsyncTask<String, Integer, Void> {
        private ProgressDialog dialog;
        private int countUpload = 0;
        private int sum = 0;

        public UploadingAsync() {
            this.dialog = new ProgressDialog(mRootView.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle(mRootView.getContext().getString(R.string.upload_danhbo_title));
            dialog.setMessage(mRootView.getContext().getString(R.string.upload_danhbo_message));
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            for (GridViewQuanLyDocSoAdapter.Item item : mQuanLyDocSoAdapter.getItems())
                if (item.getFlag() == Flag.READ || item.getFlag() == Flag.CODE_F)
                    countUpload++;
            sum = countUpload;
        }

        private Void dongBo(String... params) {
            Boolean isValid = false;


            mHoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Read(mLike, mKy, true);

            boolean isUpdate = mUploading.update(mHoaDons);
            if (!isUpdate) {
                publishProgress(0);
                return null;
            }

//              ConnectionDB.getInstance().reConnect();
            for (GridViewQuanLyDocSoAdapter.Item item : mQuanLyDocSoAdapter.getItems()) {
                for (HoaDon hoaDon : mHoaDons) {
                    if (item.getDanhbo().equals(hoaDon.getDanhBo()) && (
                            hoaDon.getFlag() == Flag.READ || hoaDon.getFlag() == Flag.CODE_F)) {
                        String codeMoi = hoaDon.getCodeMoi();
                        boolean success1 = mUploading.add(hoaDon);
                        if (success1) {
                            mHoaDons.remove(hoaDon);
                            mQuanLyDocSoAdapter.removeItem(hoaDon.getMaLoTrinh());
                            countUpload--;
                            LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonSynchronized(hoaDon);
                            publishProgress(countUpload);
                        }
                        break;
                    }
                }
            }
            if (countUpload == 0)
                isValid = true;

            return null;
        }

        private Void suaDongBoKhongLen(String... params) {
            mHoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon(mLike, mKy, Flag.SYNCHRONIZED, true);
            for (HoaDon hoaDon : mHoaDons) {
                if (hoaDon.getCodeMoi().startsWith("F"))
                    LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonFlag(hoaDon, Flag.CODE_F);
                else
                    LocalDatabase.getInstance(mRootView.getContext()).updateHoaDonFlag(hoaDon, Flag.READ);
            }
            return null;

        }

        @Override
        protected Void doInBackground(String... params) {
            return dongBo(params);
//            return suaDongBoKhongLen(params);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int count = values[0];
            String title = String.format("%s %d/%d (%d%%)", "Đang đồng bộ: ", sum - count, sum, Math.round((sum - count) * 100 / sum));
            dialog.setTitle(title);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            notifyDataSetGridViewChange();
//            ((TextView) mRootView.findViewById(R.id.txt_qlds_soLuong)).setText(String.format("Số lượng: %s/%s     Tiêu thụ: %s m3", mQuanLyDocSoAdapter.getCount(), mHoaDons.size(), countTieuThu));
            mDanhBoHoanThanh += LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon_Synchronized(mLike, mKy, false).size();
            setTextProgress();
            if (this.countUpload == 0) {
                Toast.makeText(mRootView.getContext(), "Đồng bộ thành công", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(mRootView.getContext(), "Đồng bộ thất bại. Kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
            }
            refresh();
        }
    }

}
