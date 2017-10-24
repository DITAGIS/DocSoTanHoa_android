package com.ditagis.hcm.docsotanhoa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.conectDB.LayLoTrinhAsync;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

/**
 * Created by ThanLe on 24/10/2017.
 */

public class LayLoTrinh extends Fragment {
    TextView m_txtTongMLT;
    TextView m_txtTongDB;
    EditText editTextSearch;
    GridView gridView;
    private GridViewLayLoTrinhAdapter da;
    private LocalDatabase mLocalDatabase;
    private int mSumDanhBo;
    private int mSumMLT;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mKy;
    private int mNam;
    private int mDot;
    private String mUsername;
    private HoaDonDB hoaDonDB;
    private LayLoTrinhAsync mLayLoTrinhAsync;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_lo_trinh_fragment, container, false);

        return rootView;
    }
}
