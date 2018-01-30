package com.ditagis.hcm.docsotanhoa.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.ResultLayLoTrinh;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.utities.Flag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 24/10/2017.
 */

public class LayLoTrinhAsync extends AsyncTask<Boolean, List<HoaDon>, ResultLayLoTrinh> {
    public AsyncResponse mDelegate = null;
    private ProgressDialog dialog;
    private HoaDonDB _hoadonDB;
    private Context mContext;
    private LocalDatabase mLocalDatabase;
    private String mUsername;
    private int mKy;
    private int mNam;
    private int mDot;
    private int count;
    private Activity mActivity;
    private boolean mIsLoading;

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
        this.count = 0;
        this.mIsLoading = true;
    }

    public boolean ismIsLoading() {
        return mIsLoading;
    }

    public void setmIsLoading(boolean mIsLoading) {
        this.mIsLoading = mIsLoading;
    }

    public void setmKy(int ky) {
        this.mKy = ky;
    }

    public void setmDot(int mDot) {
        this.mDot = mDot;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog.setTitle(mContext.getString(R.string.load_danhbo_title));
        this.dialog.setMessage(mContext.getString(R.string.load_danhbo_message));
        this.dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setmIsLoading(false);
            }
        });
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
        if (mDot < 10)
            dotString = "0" + mDot;
        String like = dotString + mUsername + "%";
//        int size = 0;
//        size = this.mLocalDatabase.getAllHoaDonSize(like, mKy,Flag.UNREAD, false);
//        size += this.mLocalDatabase.getAllHoaDonSize(like,mKy,Flag.READ,false);
//        hoaDons = this.mLocalDatabase.getAllHoaDon_UnRead(like,mKy,true);
//        hoaDons.addAll(this.mLocalDatabase.getAllHoaDon_Read(like, mKy, true));
        hoaDons = this.mLocalDatabase.getAllHoaDonLayLoTrinh(like, mKy);
//        } else {


        boolean isFound = false;
        if (_hoadonDB == null)
            _hoadonDB = new HoaDonDB();
        List<String> DBs = _hoadonDB.getCountHoaDon(this.mUsername, this.mDot, this.mNam, this.mKy);
        count = DBs.size();
        if (hoaDons.size() == count) {
            isFound = true;
        }
        if (!isFound) {
            for (HoaDon hoaDon : hoaDons)
                if (DBs.contains(hoaDon.getDanhBo()))
                    DBs.remove(hoaDon.getDanhBo());

            for (String danhBo : DBs) {
                if (mIsLoading) {
                    HoaDon hoaDon = _hoadonDB.getHoaDonByUserName(this.mUsername, danhBo, this.mDot, this.mNam, this.mKy);
                    if (hoaDon == null)
                        continue;
                    if (hoaDon.getCodeMoi() != null)
                        if (hoaDon.getCodeMoi().contains("F")) {
                            LocalDatabase.getInstance(mContext).deleteHoaDon(hoaDon.getDanhBo(), Flag.SYNCHRONIZED);
                        }
                    if (hoaDon == null) {
                        _hoadonDB.closeStatement();
                        return null;
                    }

                    hoaDons.add(hoaDon);
                    resultLayLoTrinh.addItemToDa(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo()));
                    this.mLocalDatabase.addHoaDon(hoaDon,false);
                    publishProgress(hoaDons);
                } else
                    break;
            }
            _hoadonDB.closeStatement();
        }
//        }
        if (hoaDons != null) {
            resultLayLoTrinh.setDot(dotString);
            resultLayLoTrinh.setTotal(count);

            resultLayLoTrinh.setDot(this.mDot + "");
            return resultLayLoTrinh;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List<HoaDon>... values) {
        super.onProgressUpdate(values);
        String title = String.format("%s %d/%d (%d%%)", mContext.getString(R.string.load_danhbo_title), values[0].size(), this.count, Math.round(values[0].size() * 100 / this.count));
        dialog.setTitle(title);
    }

    @Override
    protected void onPostExecute(ResultLayLoTrinh resultLayLoTrinh) {
        mDelegate.processFinish(resultLayLoTrinh);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (resultLayLoTrinh == null) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else
            Toast.makeText(this.mContext, mContext.getString(R.string.load_danhbo_complete), Toast.LENGTH_LONG).show();
    }

    public interface AsyncResponse {
        void processFinish(ResultLayLoTrinh output);
    }

}