package com.ditagis.hcm.docsotanhoa.conectDB;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.ResultLayLoTrinh;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 24/10/2017.
 */

public class LayLoTrinhAsync extends AsyncTask<Boolean, List<HoaDon>, ResultLayLoTrinh> {
    private ProgressDialog dialog;
    private HoaDonDB _hoadonDB;
    private Context mContext;
    private LocalDatabase mLocalDatabase;
    private String mUsername;
    private int mKy;
    private int mNam;
    private int mDot;


    public interface AsyncResponse {
        void processFinish(ResultLayLoTrinh output);
    }

    public AsyncResponse mDelegate = null;

    public LayLoTrinhAsync(HoaDonDB hoaDonDB, LocalDatabase localDatabase, String username, int dot, int ky, int nam, Context context, AsyncResponse delegate) {
        this.mContext = context;
        this.dialog = new ProgressDialog(this.mContext);
        this._hoadonDB = hoaDonDB;
        this.mLocalDatabase = localDatabase;
        this.mUsername = username;
        this.mDot = dot;
        this.mKy = ky;
        this.mNam = nam;
        this.mDelegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog.setMessage("Đang tải danh bộ...");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    @Override
    protected ResultLayLoTrinh doInBackground(Boolean... params) {
        boolean isOffline = !params[0];
        List<HoaDon> hoaDons = new ArrayList<>();
        ResultLayLoTrinh resultLayLoTrinh = new ResultLayLoTrinh(this.mContext);
        if (isOffline) {
            hoaDons = this.mLocalDatabase.getAllHoaDon(mDot + mUsername + "%");
        } else {
            if (_hoadonDB == null)
                _hoadonDB = new HoaDonDB();
            hoaDons = _hoadonDB.getAllByUserName(this.mUsername, this.mDot, this.mNam, this.mKy);
            resultLayLoTrinh.setDot(_hoadonDB.getmDot());

        }
        resultLayLoTrinh.setCount(hoaDons.size());
        for (HoaDon hoaDon : hoaDons) {
            resultLayLoTrinh.addItemToDa(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo()));
            this.mLocalDatabase.addHoaDon(hoaDon);
        }
        resultLayLoTrinh.setDot(this.mDot + "");
        return resultLayLoTrinh;
    }

    @Override
    protected void onPostExecute(ResultLayLoTrinh resultLayLoTrinh) {
        mDelegate.processFinish(resultLayLoTrinh);

//        if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
//            LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(this.mContext, "Đã tải xong mã lộ trình!!!", Toast.LENGTH_LONG).show();
    }

}