package com.ditagis.hcm.docsotanhoa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.CustomArrayAdapter;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewQuanLyDocSoAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.SumDanhBoDB;
import com.ditagis.hcm.docsotanhoa.conectDB.Uploading;
import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.utities.MySnackBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 25/10/2017.
 */

public class QuanLyDocSo extends Fragment {
    GridView mGridView;
    TextView mTxtComplete;
    private ArrayList<DanhBo_ChiSoMoi> mDanhBo_chiSoMois;
    private GridViewQuanLyDocSoAdapter mQuanLyDocSoAdapter;
    LocalDatabase mLocalDatabase;
    private int mSumDanhBo = 0, mDanhBoHoanThanh;
    private int mDot, mKy, mNam;
    private String mUsername;
    private Uploading mUploading;
    private View mRootView;
    private SumDanhBoDB mSumDanhBoDB;
    AutoCompleteTextView singleComplete;
    List<String> mDBs = new ArrayList<String>(), mTenKHs = new ArrayList<>(), mDiaChis = new ArrayList<>(), mMLTs = new ArrayList<>();
    private String mLike;
    private String mSearchType;

    public Uploading getmUploading() {
        return mUploading;
    }


    public void setmDanhBoHoanThanh(int mDanhBoHoanThanh) {
        this.mDanhBoHoanThanh = mSumDanhBo - mDanhBoHoanThanh;
    }

    public QuanLyDocSo(LayoutInflater inflater, int dot, int ky, int nam, String userName) {
        mRootView = inflater.inflate(R.layout.quan_ly_doc_so_fragment, null);

        mDot = dot;
        mKy = ky;
        mNam = nam;
        mUsername = userName;
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        this.mLike = dotString + mUsername + "%";
        mTxtComplete = (TextView) mRootView.findViewById(R.id.txt_qlds_tienTrinh);
        mLocalDatabase = new LocalDatabase(mRootView.getContext());
        mUploading = new Uploading(mDot, mKy, mNam);
        mGridView = (GridView) mRootView.findViewById(R.id.grid_qlds_danhSachDocSo);
        mSumDanhBoDB = new SumDanhBoDB();
        String kyString = mKy + "";
        if (mKy < 10)
            kyString += "0" + mKy;

        mSumDanhBo = mSumDanhBoDB.getSum(kyString, mNam, dotString + mUsername + "%");
        //Gán DataSource vào ArrayAdapter
        mQuanLyDocSoAdapter = new GridViewQuanLyDocSoAdapter(mRootView.getContext(), new ArrayList<GridViewQuanLyDocSoAdapter.Item>());

        //gán Datasource vào GridView

        mGridView.setAdapter(mQuanLyDocSoAdapter);
        registerForContextMenu(mGridView);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showMoreInfro(view);
                return false;
            }
        });
        final ImageButton imgBtnUpload = (ImageButton) mRootView.findViewById(R.id.imgBtn_qlds_upload);
        imgBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        imgBtnUpload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorImgBtnUpload_1));
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(ContextCompat.getColor(mRootView.getContext(), R.color.colorPrimary_1));
                        if (mLocalDatabase.getAllDanhBo_CSM().size() == 0) {
                            MySnackBar.make(mGridView, "Chưa có danh bộ!!!", false);
                        } else if (isOnline()) {
                            doUpLoad();
                        } else {
                            MySnackBar.make(mGridView, "Kiểm tra kết nối Internet và thử lại", false);
                        }
                        return true;
                }
                return false;
            }
        });
        mDanhBo_chiSoMois = mLocalDatabase.getAllDanhBo_CSM();
        for (DanhBo_ChiSoMoi danhBo_chiSoMoi : this.mDanhBo_chiSoMois) {
            mDBs.add(danhBo_chiSoMoi.getDanhBo());
        }
        mSearchType = mRootView.getContext().getString(R.string.search_danhbo);
        singleComplete = (AutoCompleteTextView) mRootView.findViewById(R.id.editauto_qlds);
        singleComplete.setAdapter(
                new CustomArrayAdapter
                        (
                                mRootView.getContext(),
                                android.R.layout.simple_list_item_1,
                                mDBs
                        ));
        singleComplete.setBackgroundResource(R.layout.edit_text_styles2);
        singleComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                    mQuanLyDocSoAdapter.clear();
                    for (DanhBo_ChiSoMoi danhBo_chiSoMoi : mDanhBo_chiSoMois) {
                        if (danhBo_chiSoMoi.getDanhBo().contains(s.toString()))
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    danhBo_chiSoMoi.getTieuThu(),
                                    danhBo_chiSoMoi.getDanhBo(),
                                    danhBo_chiSoMoi.getChiSoCu(),
                                    danhBo_chiSoMoi.getChiSoMoi()));
                    }
                    mQuanLyDocSoAdapter.notifyDataSetChanged();

                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                    mQuanLyDocSoAdapter.clear();
                    for (DanhBo_ChiSoMoi danhBo_chiSoMoi : mDanhBo_chiSoMois) {
                        if (danhBo_chiSoMoi.getTenKH().toLowerCase().contains(s.toString().toLowerCase()))
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    danhBo_chiSoMoi.getTieuThu(),
                                    danhBo_chiSoMoi.getDanhBo(),
                                    danhBo_chiSoMoi.getChiSoCu(),
                                    danhBo_chiSoMoi.getChiSoMoi()));
                    }
                    mQuanLyDocSoAdapter.notifyDataSetChanged();
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                    mQuanLyDocSoAdapter.clear();
                    for (DanhBo_ChiSoMoi danhBo_chiSoMoi : mDanhBo_chiSoMois) {
                        if (danhBo_chiSoMoi.getDiaChi().toLowerCase().contains(s.toString().toLowerCase()))
                            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                                    danhBo_chiSoMoi.getTieuThu(),
                                    danhBo_chiSoMoi.getDanhBo(),
                                    danhBo_chiSoMoi.getChiSoCu(),
                                    danhBo_chiSoMoi.getChiSoMoi()));
                    }
                    mQuanLyDocSoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((Button) mRootView.findViewById(R.id.btn_qlds_optionSearch)).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionSearch();
                    }
                });

    }

    public void setmDot(int mDot) {
        this.mDot = mDot;
    }

    public void setmKy(int mKy) {
        this.mKy = mKy;
    }

    public void setmNam(int mNam) {
        this.mNam = mNam;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        refresh();
        return mRootView;
    }

    public void refresh() {
        mDanhBo_chiSoMois = mLocalDatabase.getAllDanhBo_CSM();
        setTextProgress();


        mQuanLyDocSoAdapter.clear();
        for (DanhBo_ChiSoMoi danhBo_chiSoMoi : this.mDanhBo_chiSoMois) {
            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                    danhBo_chiSoMoi.getTieuThu(),
                    danhBo_chiSoMoi.getDanhBo(),
                    danhBo_chiSoMoi.getChiSoCu(),
                    danhBo_chiSoMoi.getChiSoMoi()));
        }
        mQuanLyDocSoAdapter.notifyDataSetChanged();
    }

    private void setTextProgress() {
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mRootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void optionSearch() {
//        String[] searchTypes = {mRootView.getContext().getString(R.string.search_mlt),
//                mRootView.getContext().getString(R.string.search_danhbo)};
//                mRootView.getContext().getString(R.string.search_tenKH)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Tùy chọn tìm kiếm");
        builder.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_search_type_qlds, null);

        final RadioGroup group = (RadioGroup) dialogLayout.findViewById(R.id.radioGroup_searchtype);

        if (singleComplete.getHint().equals(mRootView.getContext().getString(R.string.search_danhbo))) {
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

                if (mSearchType.equals(mRootView.getContext().getString(R.string.search_danhbo))) {
                    mDBs.clear();
                    for (DanhBo_ChiSoMoi danhBo_chiSoMoi : mDanhBo_chiSoMois) {
                        mDBs.add(danhBo_chiSoMoi.getDanhBo());
                    }
                    singleComplete.setAdapter(new CustomArrayAdapter(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mDBs
                    ));
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_tenKH))) {
                    mTenKHs.clear();
                    for (DanhBo_ChiSoMoi danhBo_chiSoMoi : mDanhBo_chiSoMois) {
                        mTenKHs.add(danhBo_chiSoMoi.getTenKH());
                    }

                    singleComplete.setAdapter(new CustomArrayAdapter(
                            mRootView.getContext(),
                            android.R.layout.simple_list_item_1,
                            mTenKHs
                    ));
                } else if (mSearchType.equals(mRootView.getContext().getString(R.string.search_diaChi))) {
                    mDiaChis.clear();
                    for (DanhBo_ChiSoMoi danhBo_chiSoMoi : mDanhBo_chiSoMois) {
                        mDiaChis.add(danhBo_chiSoMoi.getDiaChi());
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


    private void showMoreInfro(final View view) {
        String danhBo = ((TextView) view.findViewById(R.id.row_qlds_txt_danhBo)).getText().toString();
        DanhBo_ChiSoMoi danhBo_CSM = mLocalDatabase.getDanhBo_CSM(danhBo);

        //--------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Danh bộ: " + danhBo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
//        TODO chỉnh sửa khách hàng
                .setNegativeButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        edit_info(view);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_view_thongtin_docso, null);


        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(danhBo_CSM.getImage(), options);

        ImageView image = (ImageView) dialog.findViewById(R.id.imgView_qlds);
        BitmapDrawable resizedDialogImage = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));

        image.setBackground(resizedDialogImage);

        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_MLT)).setText(danhBo_CSM.getMaLoTrinh());
//                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_DanhBo)).setText(danhBo_CSM.getDanhBo());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_tenKH)).setText(danhBo_CSM.getTenKH());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_diaChi)).setText(danhBo_CSM.getDiaChi());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_SDT)).setText(danhBo_CSM.getSdt());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_CSC)).setText(danhBo_CSM.getChiSoCu());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_CSM)).setText(danhBo_CSM.getChiSoMoi());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_code)).setText(danhBo_CSM.getCode());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_giaBieu)).setText(danhBo_CSM.getGiaBieu());
        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_tienNuoc)).setText("");//TODO tien nuoc

        ((TextView) dialog.findViewById(R.id.txt_layout_qlds_ghiChu)).setText(danhBo_CSM.getNote());
    }

    private void edit_info(View view) {
        String[] codes = {"40", "41", "42", "54", "55", "56", "58", "5F", "5K",
                "60", "61", "62", "63", "64", "65", "66", "81", "82",
                "83", "F1", "F2", "F3", "F4", "M1", "M2", "M3", "N",
                "RT", "K", "Q"};
        final String danhBo = ((TextView) view.findViewById(R.id.row_qlds_txt_danhBo)).getText().toString();
        final DanhBo_ChiSoMoi danhBo_CSM = mLocalDatabase.getDanhBo_CSM(danhBo);
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Cập nhật thông tin chỉ số");

        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_edit_thongtin_docso, null);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(danhBo_CSM.getImage(), options);

        ImageView image = (ImageView) dialogLayout.findViewById(R.id.imgView_edit);
        BitmapDrawable resizedDialogImage = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));

        image.setBackground(resizedDialogImage);

        ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_MLT)).setText(danhBo_CSM.getMaLoTrinh());
//                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_DanhBo)).setText(danhBo_CSM.getDanhBo());
        ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_tenKH)).setText(danhBo_CSM.getTenKH());
        ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_diaChi)).setText(danhBo_CSM.getDiaChi());
        ((TextView) dialogLayout.findViewById(R.id.etxt_layout_edit_SDT)).setText(danhBo_CSM.getSdt());
        ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_CSC)).setText(danhBo_CSM.getChiSoCu());
        final EditText etxtCSM = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_CSM);
        etxtCSM.setText(danhBo_CSM.getChiSoMoi());

        final EditText etxtSDT = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_SDT);
        etxtSDT.setText(danhBo_CSM.getSdt());

        final EditText etxtNote = (EditText) dialogLayout.findViewById(R.id.etxt_layout_edit_ghiChu);
        etxtNote.setText(danhBo_CSM.getNote());

        final Spinner spinCode = (Spinner) dialogLayout.findViewById(R.id.spin_edit_code);
        ArrayAdapter<String> adapterCode = new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_spinner_dropdown_item, codes);
        adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCode.setAdapter(adapterCode);
        for (int i = 0; i < codes.length; i++)
            if (codes[i].equals(danhBo_CSM.getCode())) {
                spinCode.setSelection(i);
                break;
            }
        ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_giaBieu)).setText(danhBo_CSM.getGiaBieu());
        ((TextView) dialogLayout.findViewById(R.id.txt_layout_edit_tienNuoc)).setText("");//TODO tien nuoc

        ((TextView) dialogLayout.findViewById(R.id.etxt_layout_edit_ghiChu)).setText(danhBo_CSM.getNote());

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
                danhBo_CSM.setChiSoMoi(etxtCSM.getText().toString());
                danhBo_CSM.setCode(spinCode.getSelectedItem().toString());
                danhBo_CSM.setSdt(etxtSDT.getText().toString());
                danhBo_CSM.setNote(etxtNote.getText().toString());

                mLocalDatabase.updateDanhBo_CSM(danhBo_CSM);
                refresh();
                dialog.dismiss();


            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


    }

    private void doUpLoad() {
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
        new UploadingAsync().execute();

    }

    class UploadingAsync extends AsyncTask<String, Boolean, Void> {
        private ProgressDialog dialog;
        private int countUpload = 0;

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
            countUpload = mLocalDatabase.getAllDanhBo_CSM().size();
        }

        @Override
        protected Void doInBackground(String... params) {

            Boolean isValid = false;

            for (int i = 0; i < mLocalDatabase.getAllDanhBo_CSM().size(); i++) {
                DanhBo_ChiSoMoi danhBo_chiSoMoi = mDanhBo_chiSoMois.get(i);
//                mUploading.update(danhBo_chiSoMoi);

//                boolean success = mUploading.update(danhBo_chiSoMoi);
                boolean success1 = mUploading.add(danhBo_chiSoMoi);
                if (success1) {
                    mDanhBo_chiSoMois.remove(danhBo_chiSoMoi);
                    mQuanLyDocSoAdapter.removeItem(danhBo_chiSoMoi.getMaLoTrinh());
                    i--;
                    mLocalDatabase.deleteDanhBo_CSM(danhBo_chiSoMoi);
                }
            }
            if (mLocalDatabase.getAllDanhBo_CSM().size() == 0)
                isValid = true;

            publishProgress(isValid);
            return null;

        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            boolean isValid = values[0];
            if (isValid) {
                mQuanLyDocSoAdapter.notifyDataSetChanged();
                Toast.makeText(mRootView.getContext(), "Đồng bộ thành công", Toast.LENGTH_SHORT).show();
                mDanhBoHoanThanh += (countUpload - mLocalDatabase.getAllDanhBo_CSM().size());
                setTextProgress();

            } else {
                Toast.makeText(mRootView.getContext(), "Đồng bộ thất bại. Kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
            }
            refresh();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
