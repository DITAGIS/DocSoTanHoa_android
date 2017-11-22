package com.ditagis.hcm.docsotanhoa;

import android.app.Activity;
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
        this.mEditTextSearch.addTextChangedListener(new TextWatcher() {
            List<String> mlts = new ArrayList<String>();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                for (HoaDon hoaDon : LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon(LayLoTrinh.this.mDot + LayLoTrinh.this.mUsername + "%")) {
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
        this.mLayLoTrinhAsync = new LayLoTrinhAsync(LayLoTrinh.this.hoaDonDB, LocalDatabase.getInstance(mRootView.getContext()), LayLoTrinh.this.mUsername, LayLoTrinh.this.mDot, LayLoTrinh.this.mKy, LayLoTrinh.this.mNam, mRootView.getContext(), this.mActivity
                , new LayLoTrinhAsync.AsyncResponse() {
            @Override
            public void processFinish(ResultLayLoTrinh output) {
                finishLayLoTrinh(output, mRootView);
            }
        });

        selectDot();


    }


    public int selectDot() {
        final int[] dot = {mDot};
        String[] dots = new String[mDot];
        String[] kys = new String[mKy];
        for (int i = 1; i <= mDot; i++)
            if (i < 10)
                dots[i - 1] = "0" + i;
            else
                dots[i - 1] = i + "";
        for (int i = 1; i <= mKy; i++) {
            if (i < 10)
                kys[i - 1] = "0" + i;
            else
                kys[i - 1] = i + "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Chọn đợt đọc chỉ số");
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(mRootView.getContext());
        View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_dot, null);

        final Spinner spinDot = (Spinner) dialogLayout.findViewById(R.id.spin_select_dot);
        final Spinner spinKy = (Spinner) dialogLayout.findViewById(R.id.spin_select_ky);
        ArrayAdapter<String> adapterDot = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_right, dots);
        adapterDot.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        ArrayAdapter<String> adapterKy = new ArrayAdapter<String>(mRootView.getContext(), R.layout.spinner_item_right, kys);
        adapterKy.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinKy.setAdapter(adapterKy);
        spinKy.setSelection(mKy - 1);
        spinDot.setAdapter(adapterDot);
        spinDot.setSelection(mDot - 1);
        builder.setView(dialogLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDot = Integer.parseInt(spinDot.getSelectedItem().toString());
                        mKy = Integer.parseInt(spinKy.getSelectedItem().toString());
                        dialog.dismiss();
                        LayLoTrinh.this.mLayLoTrinhAsync.setmDot(mDot);
                        LayLoTrinh.this.mLayLoTrinhAsync.setmKy(mKy);
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
        List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDon(dotString + this.mUsername + "%");
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
                    List<HoaDon> hoaDons = LocalDatabase.getInstance(mRootView.getContext()).getAllHoaDonByMaLoTrinh(mlt);
                    for (HoaDon hoaDon : hoaDons)
                        LayLoTrinh.this.mLayLoTrinhAdapter.add(new GridViewLayLoTrinhAdapter.Item(mlt, hoaDon.getDanhBo()));
                }

            }

        } else {
            //TODO
        }
    }

    private void finishLayLoTrinh(ResultLayLoTrinh output, View rootView) {
//        if (output.getCount() < output.getTotal()) {
//            MySnackBar.make(rootView, rootView.getContext().getString(R.string.load_danhbo_error), true);
//        } else {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra(mActivity.getString(R.string.extra_username), mUsername);
        intent.putExtra(mActivity.getString(R.string.extra_staffname), mStaffName);
        intent.putExtra(mActivity.getString(R.string.extra_dot), mDot);

        mActivity.startActivity(intent);
//        }
    }
}
