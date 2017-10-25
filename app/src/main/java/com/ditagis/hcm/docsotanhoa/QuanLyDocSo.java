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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewQuanLyDocSoAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.Uploading;
import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;

/**
 * Created by ThanLe on 25/10/2017.
 */

public class QuanLyDocSo extends Fragment {
    EditText mEditTextSearch;
    GridView mGridView;
    TextView mTxtComplete;
    private ArrayList<DanhBo_ChiSoMoi> mDanhBo_chiSoMois;
    private GridViewQuanLyDocSoAdapter mQuanLyDocSoAdapter;
    LocalDatabase mLocalDatabase;
    private int mSumDanhBo = 0, mDanhBoHoanThanh;
    private int mDot, mKy;
    private String mUsername;
    private Uploading mUploading;
    private View mRootView;

    public QuanLyDocSo(LayoutInflater inflater, int dot, String userName) {
        mRootView = inflater.inflate(R.layout.quan_ly_doc_so_fragment, null);

        mDot = dot;
        mUsername = userName;
        mTxtComplete = (TextView) mRootView.findViewById(R.id.txt_qlds_tienTrinh);
        mLocalDatabase = new LocalDatabase(mRootView.getContext());
        mUploading = new Uploading();
        mEditTextSearch = (EditText) mRootView.findViewById(R.id.etxt_qlds_search);
        mGridView = (GridView) mRootView.findViewById(R.id.grid_qlds_danhSachDocSo);
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
        ((ImageButton) mRootView.findViewById(R.id.imgBtn_qlds_upload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    doUpLoad();

                } else {
                    snackbarMake(mGridView, "Kiểm tra kết nối Internet và thử lại", false);
                }
            }
        });
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


        mEditTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //TODO search danh bộ

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mQuanLyDocSoAdapter.clear();
        for (DanhBo_ChiSoMoi danhBo_chiSoMoi : this.mDanhBo_chiSoMois) {
            mQuanLyDocSoAdapter.add(new GridViewQuanLyDocSoAdapter.Item(
                    danhBo_chiSoMoi.getMaLoTrinh(),
                    danhBo_chiSoMoi.getDanhBo(),
                    danhBo_chiSoMoi.getChiSoCu(),
                    danhBo_chiSoMoi.getChiSoMoi()));
        }
        mQuanLyDocSoAdapter.notifyDataSetChanged();
    }

    private void setTextProgress() {

        this.mSumDanhBo = mLocalDatabase.getAllHoaDon(this.mDot + this.mUsername + "%").size();
        this.mDanhBoHoanThanh = mQuanLyDocSoAdapter.getCount();
        this.mSumDanhBo += this.mDanhBoHoanThanh;
        this.mTxtComplete.setText(this.mDanhBoHoanThanh + "/" + this.mSumDanhBo);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mRootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void snackbarMake(View view, String text, boolean isLong) {
        int time = isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT;
        Snackbar.make(view, text, time)
                .setAction("Action", null).show();
    }

    private void showMoreInfro(View view) {
        String danhBo = ((TextView) view.findViewById(R.id.row_qlds_txt_danhBo)).getText().toString();
        DanhBo_ChiSoMoi danhBo_CSM = mLocalDatabase.getDanhBo_CSM(danhBo);

        //--------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
        builder.setTitle("Danh bộ: " + danhBo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //TODO chỉnh sửa khách hàng
//                });.setNegativeButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
        AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_view_thongtin_docso, null);


        dialog.setView(dialogLayout);

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

    private void doUpLoad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext());
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
        AlertDialog alert = builder.create();
        alert.show();
    }

    //TODO: progress bar
    private void upLoadData() {
        new UploadingAsync().execute();

    }

    class UploadingAsync extends AsyncTask<String, Boolean, Void> {
        private ProgressDialog dialog;

        public UploadingAsync() {
            this.dialog = new ProgressDialog(mRootView.getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Đang đồng bộ dữ liệu đọc số...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            Boolean isValid = false;

            for (int i = 0; i < mDanhBo_chiSoMois.size(); i++) {
                DanhBo_ChiSoMoi danhBo_chiSoMoi = mDanhBo_chiSoMois.get(i);
//                mUploading.update(danhBo_chiSoMoi);
                boolean success = mUploading.update(danhBo_chiSoMoi);
                if (success) {
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
            } else {
                Toast.makeText(mRootView.getContext(), "Đồng bộ thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            setTextProgress();
        }
    }

}
