package com.ditagis.hcm.docsotanhoa.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
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
    private Activity mActivity;

    public interface AsyncResponse {
        void processFinish(ResultLayLoTrinh output);
    }

    public void setmDot(int mDot) {
        this.mDot = mDot;
    }

    public AsyncResponse mDelegate = null;

    public LayLoTrinhAsync(HoaDonDB hoaDonDB, LocalDatabase localDatabase, String username, int dot, int ky, int nam, Context context, Activity activity, AsyncResponse delegate) {
        this.mContext = context;
        this.mActivity = activity;
        this.dialog = new ProgressDialog(this.mContext, android.R.style.Theme_Material_Dialog_Alert);
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
        this.dialog.setTitle(mContext.getString(R.string.load_danhbo_title));
        this.dialog.setMessage(mContext.getString(R.string.load_danhbo_message));
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    @Override
    protected ResultLayLoTrinh doInBackground(Boolean... params) {
        boolean isOffline = !params[0];
        List<HoaDon> hoaDons = new ArrayList<>();
        ResultLayLoTrinh resultLayLoTrinh = new ResultLayLoTrinh(this.mContext);
//        if (isOffline) {
        String dotString = mDot + "";
        if(mDot < 10)
            dotString = "0" + mDot;
        hoaDons = this.mLocalDatabase.getAllHoaDon(dotString + mUsername + "%");
//        } else {
        if (_hoadonDB == null)
            _hoadonDB = new HoaDonDB();
        if (hoaDons.size() == 0)
            hoaDons = _hoadonDB.getAllByUserName(this.mUsername, this.mDot, this.mNam, this.mKy);
        if (hoaDons != null)
            resultLayLoTrinh.setDot(_hoadonDB.getmDot());

//        }
        if (hoaDons != null) {
            resultLayLoTrinh.setCount(hoaDons.size());
            for (HoaDon hoaDon : hoaDons) {
                resultLayLoTrinh.addItemToDa(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo()));
                this.mLocalDatabase.addHoaDon(hoaDon);
            }
            resultLayLoTrinh.setDot(this.mDot + "");
            return resultLayLoTrinh;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultLayLoTrinh resultLayLoTrinh) {
        mDelegate.processFinish(resultLayLoTrinh);

//        if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
//            LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (resultLayLoTrinh == null) {

        } else

            Toast.makeText(this.mContext, "Đã tải xong mã lộ trình!!!", Toast.LENGTH_LONG).show();
    }

}