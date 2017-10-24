package com.ditagis.hcm.docsotanhoa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.conectDB.LayLoTrinhAsync;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.ResultLayLoTrinh;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);


        Calendar calendar = Calendar.getInstance();
        this.mKy = calendar.get(Calendar.MONTH) + 1;
        this.mNam = calendar.get(Calendar.YEAR);

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_llt_swipeRefreshLayout);
        mLocalDatabase = new LocalDatabase(this);
        if (getIntent().getExtras().getString("mayds") != null)
            this.mUsername = getIntent().getExtras().getString("mayds");
        ((TextView) findViewById(R.id.txt_llt_may)).setText("Máy: " + this.mUsername);
        if (getIntent().getExtras().getString("staffname") != null)
            ((TextView) findViewById(R.id.txt_llt_tenNV)).setText("Nhân viên: " + getIntent().getExtras().getString("staffname"));
        if (getIntent().getExtras().getInt("dot") > 0)
            this.mDot = getIntent().getExtras().getInt("dot");
        m_txtTongMLT = (TextView) findViewById(R.id.txt_llt_mlt);
        m_txtTongDB = (TextView) findViewById(R.id.txt_llt_db);
        editTextSearch = (EditText) findViewById(R.id.etxt_llt_search);
        gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            List<String> mlts = new ArrayList<String>();


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                for (HoaDon hoaDon : mLocalDatabase.getAllHoaDon(mDot + mUsername + "%")) {
                    mlts.add((hoaDon.getMaLoTrinh()));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
//                    System.out.println(gridView.getAdapter().getItem(0).toString());
                    List<String> result = new ArrayList<String>();
                    //Lấy dữ liệu bắt đầu với text search
                    for (String mlt : mlts) {
                        if (mlt.contains(s.toString()))
                            result.add(mlt);
                    }
                    //Gán dữ liệu vào data source
                    if (da != null && result.size() > 0) {
                        da.clear();
                        for (String mlt : result) {
                            List<HoaDon> hoaDons = LayLoTrinhActivity.this.mLocalDatabase.getAllHoaDonByMaLoTrinh(mlt);
                            for (HoaDon hoaDon : hoaDons)
                                da.add(new GridViewLayLoTrinhAdapter.Item(mlt, hoaDon.getDanhBo()));
                        }

                    }

                } else {
                    //TODO
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Gán DataSource vào ArrayAdapter

//        da = new GridViewLayLoTrinhAdapter(LayLoTrinhActivity.this, new ArrayList<GridViewLayLoTrinhAdapter.Item>());

        mLayLoTrinhAsync = new LayLoTrinhAsync(hoaDonDB, mLocalDatabase, mUsername, mDot, mKy, mNam, LayLoTrinhActivity.this, new LayLoTrinhAsync.AsyncResponse() {
            @Override
            public void processFinish(ResultLayLoTrinh output) {
                LayLoTrinhActivity.this.mSumMLT = output.getCount();
                LayLoTrinhActivity.this.m_txtTongMLT.setText("Tổng mã lộ trình: " + LayLoTrinhActivity.this.mSumMLT);
                LayLoTrinhActivity.this.mSumDanhBo = output.getCount();
                LayLoTrinhActivity.this.m_txtTongDB.setText("Tổng danh bộ: " + LayLoTrinhActivity.this.mSumDanhBo);
                LayLoTrinhActivity.this.da = output.getDa();

                LayLoTrinhActivity.this.gridView.setAdapter(da);
                registerForContextMenu(LayLoTrinhActivity.this.gridView);
                ((TextView) findViewById(R.id.txt_llt_dot)).setText("Đợt: " + output.getDot());
            }
        });
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isOnline()) {
                    da.clear();
                    mLayLoTrinhAsync.execute(true);
//                    new LayLoTrinh(hoaDonDB).execute(true);
                } else {
                    Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại!!!", Toast.LENGTH_LONG).show();
                    if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
                        LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mLayLoTrinhAsync.execute(isOnline());
//        new LayLoTrinh(hoaDonDB).execute(isOnline());
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    public void doDocSo(View v) {
        int size = this.mLocalDatabase.getAllHoaDon(mDot + mUsername + "%").size();
        if (size == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, DocSoActivity.class);
            intent.putExtra("ky", mKy);
            intent.putExtra("dot", mDot);
            intent.putExtra("username", mUsername);
            startActivity(intent);
        }
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(LayLoTrinhActivity.this, QuanLyDocSoActivity.class);
        intent.putExtra("dot", mDot);
        intent.putExtra("username", mUsername);
        startActivity(intent);
    }
}
