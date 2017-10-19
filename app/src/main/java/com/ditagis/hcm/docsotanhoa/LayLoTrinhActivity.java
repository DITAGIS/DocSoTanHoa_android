package com.ditagis.hcm.docsotanhoa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView m_txtTongMLT;
    TextView m_txtTongDB;
    EditText editTextSearch;
    GridView gridView;
    //    ImageButton imgbtnCheck;
    private int m_DanhBo[];
    private GridViewLayLoTrinhAdapter da;
    private LocalDatabase mLocalDatabase;
    //    private List<HoaDon> mHoaDons;
    private int mSumDanhBo;
    private int mSumMLT;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mKy;
    private int mNam, mDot;
    private static String mUsername;
    private HoaDonDB hoaDonDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);

        Calendar calendar = Calendar.getInstance();
        this.mKy = calendar.get(Calendar.MONTH) + 1;
        this.mNam = calendar.get(Calendar.YEAR);

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_llt_swipeRefreshLayout);
        mLocalDatabase = new LocalDatabase(this);
        if (getIntent().getExtras().getString("mayds") != null)
            LayLoTrinhActivity.mUsername = getIntent().getExtras().getString("mayds");
//        mLocalDatabase.Upgrade();
//        mLocalDatabase.Create();
        m_txtTongMLT = (TextView) findViewById(R.id.txt_llt_mlt);
        m_txtTongDB = (TextView) findViewById(R.id.txt_llt_db);
        editTextSearch = (EditText) findViewById(R.id.etxt_llt_search);
        gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            List<String> mlts = new ArrayList<String>();


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                for (HoaDon hoaDon : mLocalDatabase.getAllHoaDon()) {
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
                                da.add(new GridViewLayLoTrinhAdapter.Item(mlt, hoaDon.getDanhBo(), true));
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

        da = new GridViewLayLoTrinhAdapter(LayLoTrinhActivity.this, new ArrayList<GridViewLayLoTrinhAdapter.Item>());
        //gán Datasource vào GridView
//        List<HoaDon> hoaDons = this.mLocalDatabase.getAllHoaDon();
//        for (HoaDon hoaDon : hoaDons)
//            da.add(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(), true));
        gridView.setAdapter(da);
        registerForContextMenu(LayLoTrinhActivity.this.gridView);

//        List<LoTrinh> loTrinhs = LayLoTrinhActivity.this.mLocalDatabase.getAllMaLoTrinh();

//        ((Button) findViewById(R.id.btn_llt_loadMLT)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
        new LayLoTrinh(hoaDonDB).execute(isOnline());

//        if (isOnline()) {
//
//        } else {
//            Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
//            //TODO
//        }

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (isOnline()) {
//                    LinearLayout layout_row = (LinearLayout) view.findViewById(R.id.row_llt_layout);
//                    TextView txt_row_MLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
//                    TextView txt_row_DanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
//                    ImageView img_row_View = (ImageView) view.findViewById(R.id.row_llt_img_Check);
//
//
////                    AsyncTask<String, Object, String> execute = new ItemClickHandle(layout_row, txt_row_DanhBo, img_row_View, position).execute(txt_row_MLT.getText().toString());
//                } else {
//                    Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
//                    //TODO
//                }
//
//            }
//
//        });
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isOnline()) {
                    da.clear();
                    new LayLoTrinh(hoaDonDB).execute(true);
                } else {
                    Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại!!!", Toast.LENGTH_LONG).show();
                    if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
                        LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class LayLoTrinh extends AsyncTask<Boolean, List<HoaDon>, Void> {
        private ProgressDialog dialog;
        private HoaDonDB _hoadonDB;

        public LayLoTrinh(HoaDonDB hoaDonDB) {
            this.dialog = new ProgressDialog(LayLoTrinhActivity.this);
            this._hoadonDB = hoaDonDB;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Đang tải danh bộ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            LayLoTrinhActivity.this.mSumMLT = da.items.size();
            LayLoTrinhActivity.this.m_txtTongMLT.setText("Mã lộ trình: " + LayLoTrinhActivity.this.mSumMLT);
            LayLoTrinhActivity.this.mSumDanhBo = da.items.size();
//        for (LoTrinh loTrinh : loTrinhs)
//            LayLoTrinhActivity.this.mSumDanhBo += loTrinh.getSoLuong();

            LayLoTrinhActivity.this.m_txtTongDB.setText("Danh bộ: " + LayLoTrinhActivity.this.mSumDanhBo);
            if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
                LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(LayLoTrinhActivity.this, "Đã tải xong mã lộ trình!!!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            boolean isOffline = !params[0];
            List<HoaDon> hoaDons = new ArrayList<>();
            if (isOffline) {
                hoaDons = LayLoTrinhActivity.this.mLocalDatabase.getAllHoaDon();
            } else {
                if (_hoadonDB == null)
                    _hoadonDB = new HoaDonDB();
                hoaDons = _hoadonDB.getAllByUserName(LayLoTrinhActivity.this.mUsername, LayLoTrinhActivity.this.mNam, LayLoTrinhActivity.this.mKy);

            }
            publishProgress(hoaDons);
            return null;
        }

        @Override
        protected void onProgressUpdate(List<HoaDon>... values) {
            super.onProgressUpdate(values);
            List<HoaDon> hoaDons = values[0];
            for (HoaDon hoaDon : hoaDons) {
                da.add(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(), true));
                LayLoTrinhActivity.this.mLocalDatabase.addHoaDon(hoaDon);
            }
        }

    }


    public void doDocSo(View v) {
        int size = this.mLocalDatabase.getAllHoaDon().size();
        if (size == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, DocSoActivity.class);

            startActivity(intent);
        }
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(LayLoTrinhActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
    }
}
