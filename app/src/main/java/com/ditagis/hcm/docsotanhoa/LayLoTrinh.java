package com.ditagis.hcm.docsotanhoa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.async.LayLoTrinhAsync;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.ResultLayLoTrinh;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.receiver.NetworkStateChangeReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 24/10/2017.
 */

public class LayLoTrinh {

    TextView m_txtTongMLT;
    TextView m_txtTongDB;
    EditText mEditTextSearch;
    GridView mGridView;
    private GridViewLayLoTrinhAdapter mLayLoTrinhAdapter;
    private LocalDatabase mLocalDatabase;
    private int mSumDanhBo;
    private int mSumMLT;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mKy;
    private int mNam;
    private int mDot;
    private String mUsername, mStaffName;
    private HoaDonDB hoaDonDB;
    private LayLoTrinhAsync mLayLoTrinhAsync;
    private View mRootView;
    private Activity mActivity;
    private NetworkStateChangeReceiver mStateChangeReceiver;

    public int getmDot() {
        return mDot;
    }

    public LayLoTrinh(Activity activity, LayoutInflater inflater, int mKy, int mNam, int mDot, String mUsername, String mStaffName) {
        this.mActivity = activity;
        this.mKy = mKy;
        this.mNam = mNam;
        this.mDot = mDot;
        this.mUsername = mUsername;
        this.mStaffName = mStaffName;

        mRootView = inflater.inflate(R.layout.lay_lo_trinh_fragment, null);

        ((TextView) mRootView.findViewById(R.id.txt_llt_may)).setText("Máy: " + this.mUsername);
        ((TextView) mRootView.findViewById(R.id.txt_llt_tenNV)).setText("Nhân viên: " + this.mStaffName);
        this.m_txtTongMLT = (TextView) mRootView.findViewById(R.id.txt_llt_mlt);
        this.m_txtTongDB = (TextView) mRootView.findViewById(R.id.txt_llt_db);
        this.mEditTextSearch = (EditText) mRootView.findViewById(R.id.etxt_llt_search);
        this.mGridView = (GridView) mRootView.findViewById(R.id.grid_llt_danhSachLoTrinh);
        this.mLocalDatabase = new LocalDatabase(mRootView.getContext());
        this.mEditTextSearch.addTextChangedListener(new TextWatcher() {
            List<String> mlts = new ArrayList<String>();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                for (HoaDon hoaDon : mLocalDatabase.getAllHoaDon(LayLoTrinh.this.mDot + LayLoTrinh.this.mUsername + "%")) {
                    mlts.add((hoaDon.getMaLoTrinh()));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s, mlts);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.mLayLoTrinhAsync = new LayLoTrinhAsync(LayLoTrinh.this.hoaDonDB, LayLoTrinh.this.mLocalDatabase, LayLoTrinh.this.mUsername, LayLoTrinh.this.mDot, LayLoTrinh.this.mKy, LayLoTrinh.this.mNam, mRootView.getContext(), this.mActivity
                , new LayLoTrinhAsync.AsyncResponse() {
            @Override
            public void processFinish(ResultLayLoTrinh output) {
                finishLayLoTrinh(output, mRootView);
            }
        });

        selectDot();


    }

    private void loadLoTrinh(boolean isOnline, int dot, HoaDonDB hoaDonDB, LocalDatabase mLocalDatabase, String mUsername, int mDot, int mKy, int mNam, Context context, Activity mActivity) {
        ProgressDialog dialog = new ProgressDialog(mRootView.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        dialog.setTitle(mRootView.getContext().getString(R.string.load_danhbo_title));
        dialog.setMessage(mRootView.getContext().getString(R.string.load_danhbo_message));
        dialog.setCancelable(false);
        dialog.show();

        List<HoaDon> hoaDons = new ArrayList<>();
        ResultLayLoTrinh output = new ResultLayLoTrinh(mRootView.getContext());
//        if (isOffline) {
        String dotString = mDot + "";
        if (mDot < 10)
            dotString = "0" + mDot;
        hoaDons = this.mLocalDatabase.getAllHoaDon();//dotString + mUsername + "%");
//        } else {
        if (hoaDonDB == null)
            hoaDonDB = new HoaDonDB();

        boolean isFound = false;
        if (hoaDons.size() > 0)
            for (HoaDon hoaDon : hoaDons) {
                if (hoaDon.getDot().equals(dotString))
                    isFound = true;
            }
        if (!isFound) {
            List<String> DBs = hoaDonDB.getCountHoaDon(mUsername, mDot, mNam, mKy);
            int i = 0;
            for (String danhBo : DBs) {
                hoaDons.addAll(hoaDonDB.getHoaDonByUserName(mUsername, danhBo, mDot, mNam, mKy));
                dialog.setTitle(mRootView.getContext().getString(R.string.load_danhbo_title) + ++i + "/" + DBs.size());
            }
        }
        if (hoaDons != null)
            output.setDot(hoaDonDB.getmDot());

//        }
        if (hoaDons != null) {
            output.setCount(hoaDons.size());
            for (HoaDon hoaDon : hoaDons) {
                output.addItemToDa(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo()));
                this.mLocalDatabase.addHoaDon(hoaDon);
            }
            output.setDot(this.mDot + "");
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (output == null) {

        } else
            Toast.makeText(mRootView.getContext(), "Đã tải xong mã lộ trình!!!", Toast.LENGTH_LONG).show();


        finishLayLoTrinh(output, mRootView);
    }


    public int selectDot() {
        final int[] dot = {mDot};
        String[] dots = new String[mDot];

        for (int i = 1; i <= mDot; i++)
            if (i < 10)
                dots[i - 1] = "0" + i;
            else
                dots[i - 1] = i + "";
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Chọn đợt đọc chỉ số");
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_dot, null);

        final Spinner spinCode = (Spinner) dialogLayout.findViewById(R.id.spin_select_dot);
        ArrayAdapter<String> adapterCode = new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_spinner_dropdown_item, dots);
        adapterCode.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinCode.setAdapter(adapterCode);
        spinCode.setSelection(mDot - 1);
        builder.setView(dialogLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dot[0] = Integer.parseInt(spinCode.getSelectedItem().toString());
                        mDot = dot[0];
                        dialog.dismiss();
//                        loadLoTrinh(isOnline(mRootView), mDot, LayLoTrinh.this.hoaDonDB, LayLoTrinh.this.mLocalDatabase, LayLoTrinh.this.mUsername, LayLoTrinh.this.mDot, LayLoTrinh.this.mKy, LayLoTrinh.this.mNam, mRootView.getContext(), mActivity);
                        LayLoTrinh.this.mLayLoTrinhAsync.setmDot(mDot);
                        LayLoTrinh.this.mLayLoTrinhAsync.execute(isOnline(mRootView));

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        return dot[0];
    }

    public void setTextProgress() {

        String dotString = mDot + "";
        if (this.mDot < 10)
            dotString = "0" + this.mDot;
        List<HoaDon> hoaDons = mLocalDatabase.getAllHoaDon(dotString + this.mUsername + "%");
        LayLoTrinh.this.mSumMLT = hoaDons.size();
        LayLoTrinh.this.m_txtTongMLT.setText("Tổng mã lộ trình: " + mSumMLT);
        LayLoTrinh.this.mSumDanhBo = mSumMLT;
        LayLoTrinh.this.m_txtTongDB.setText("Tổng danh bộ: " + mSumDanhBo);

        mLayLoTrinhAdapter.clear();
        for (HoaDon hoaDon : hoaDons)
            mLayLoTrinhAdapter.add(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo()));
        mLayLoTrinhAdapter.notifyDataSetChanged();

    }

    private boolean isOnline(View rootView) {
        ConnectivityManager cm = (ConnectivityManager) rootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void search(CharSequence s, List<String> mlts) {
        if (s.length() != 0) {
//                    System.out.println(mGridView.getAdapter().getItem(0).toString());
            List<String> result = new ArrayList<String>();
            //Lấy dữ liệu bắt đầu với text search
            for (String mlt : mlts) {
                if (mlt.contains(s.toString()))
                    result.add(mlt);
            }
            //Gán dữ liệu vào data source
            if (LayLoTrinh.this.mLayLoTrinhAdapter != null && result.size() > 0) {
                LayLoTrinh.this.mLayLoTrinhAdapter.clear();
                for (String mlt : result) {
                    List<HoaDon> hoaDons = LayLoTrinh.this.mLocalDatabase.getAllHoaDonByMaLoTrinh(mlt);
                    for (HoaDon hoaDon : hoaDons)
                        LayLoTrinh.this.mLayLoTrinhAdapter.add(new GridViewLayLoTrinhAdapter.Item(mlt, hoaDon.getDanhBo()));
                }

            }

        } else {
            //TODO
        }
    }

    private void finishLayLoTrinh(ResultLayLoTrinh output, View rootView) {

        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra(mActivity.getString(R.string.extra_username), mUsername);
        intent.putExtra(mActivity.getString(R.string.extra_staffname), mStaffName);
        intent.putExtra(mActivity.getString(R.string.extra_dot), mDot);

        mActivity.startActivity(intent);


    }
}
