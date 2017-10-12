package com.ditagis.hcm.docsotanhoa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.LoTrinh;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView m_txtTongMLT;
    TextView m_txtTongDB;
    EditText editTextSearch;
    GridView gridView;
    //    ImageButton imgbtnCheck;
    HoaDonDB hoaDonDB = new HoaDonDB();
    //Dùng mảng 1 chiều hoặc ArrayList để lưu một số dữ liệu
    private ArrayList<String> m_mlt;
    private int m_DanhBo[];
    private GridViewLayLoTrinhAdapter da;
    private LocalDatabase mLocalDatabase;
    private List<HoaDon> mHoaDons;
    private ProgressBar spinner;
    private int mSumDanhBo;
    private int mSumMLT;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_llt_swipeRefreshLayout);
        m_mlt = new ArrayList<String>();
        mLocalDatabase = new LocalDatabase(this);
//        mLocalDatabase.Upgrade();
//        mLocalDatabase.Create();
        m_txtTongMLT = (TextView) findViewById(R.id.txt_llt_mlt);
        m_txtTongDB = (TextView) findViewById(R.id.txt_llt_db);
        editTextSearch = (EditText) findViewById(R.id.etxt_llt_search);
        gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);

        editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
//                    System.out.println(gridView.getAdapter().getItem(0).toString());
                    List<String> result = new ArrayList<String>();
                    //Lấy dữ liệu bắt đầu với text search
                    for (String mlt : m_mlt) {
                        if (mlt.contains(s.toString()))
                            result.add(mlt);
                    }
                    //Gán dữ liệu vào data source
                    if (da != null && result.size() > 0) {
                        da.clear();
                        for (String mlt : result)
                            da.add(new GridViewLayLoTrinhAdapter.Item(mlt, 0, false));
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

        gridView.setAdapter(da);
        registerForContextMenu(LayLoTrinhActivity.this.gridView);
        if (isOnline()) {
            //-------

//            textView = (TextView) findViewById(R.id.progressTextView);


            // set the drawable as progress drawable
//            initProgresBar();
            //-----------------

            new LayLoTrinh().execute();
        } else {
            Toast.makeText(this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
            //TODO
        }
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount <= totalItemCount) {

                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isOnline()) {
                    LinearLayout layout_row = (LinearLayout) view.findViewById(R.id.row_llt_layout);
                    TextView txt_row_MLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
                    TextView txt_row_DanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
                    ImageView img_row_View = (ImageView) view.findViewById(R.id.row_llt_img_Check);


                    AsyncTask<String, Object, String> execute = new ItemClickHandle(layout_row, txt_row_DanhBo, img_row_View, position).execute(txt_row_MLT.getText().toString());
                } else {
                    Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
                    //TODO
                }

            }

        });
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                da.clear();
                new LayLoTrinh().execute();

            }
        });

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class LayLoTrinh extends AsyncTask<Void, Object, Void> {
        List<LoTrinh> loTrinhs;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LayLoTrinhActivity.this, "Đang lấy danh sách mã lộ trình", Toast.LENGTH_LONG).show();
            loTrinhs = LayLoTrinhActivity.this.mLocalDatabase.getAllMaLoTrinh();
            LayLoTrinhActivity.this.mSumMLT = loTrinhs.size();
            LayLoTrinhActivity.this.m_txtTongMLT.setText("Mã lộ trình: " + LayLoTrinhActivity.this.mSumMLT);
            LayLoTrinhActivity.this.mSumDanhBo = 0;
            for (LoTrinh loTrinh : loTrinhs)
                LayLoTrinhActivity.this.mSumDanhBo += loTrinh.getSoLuong();

            LayLoTrinhActivity.this.m_txtTongDB.setText("Danh bộ: " + LayLoTrinhActivity.this.mSumDanhBo);
            spinner = (ProgressBar) findViewById(R.id.myProgress);
            if (!LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing()) {
                spinner.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
                LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ConnectionDB condb = new ConnectionDB();
            Connection cnn = condb.getConnect();
            List<LoTrinh> loTrinhs = LayLoTrinhActivity.this.mLocalDatabase.getAllMaLoTrinh();
            try {
                Statement statement = cnn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT DISTINCT MLT FROM HOADON");
//                LayLoTrinhActivity.this.mHoaDons = new ArrayList<HoaDon>();
                while (rs.next()) {

                    String maLoTrinh = rs.getString(1);
                    boolean isFound = false;

                    //Bỏ những lộ trình đã tải về
                    for (LoTrinh loTrinh : loTrinhs)
                        if (loTrinh.getMaLoTrinh().equals(maLoTrinh)) {
                            isFound = true;
                            break;
                        }

                    if (isFound)
                        continue;
                    publishProgress(maLoTrinh, 0, 0);
                }
                LayLoTrinhActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LayLoTrinhActivity.this, "Đã lấy xong mã lộ trình", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.INVISIBLE);
                        int count = m_mlt.size();
                        LayLoTrinhActivity.this.m_DanhBo = new int[count];
                    }
                });

                rs.close();
                statement.close();
                cnn.close();

            } catch (SQLException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            String mlt = (String) values[0];
            if (da != null) {
                m_mlt.add(mlt);
                da.add(new GridViewLayLoTrinhAdapter.Item(mlt, 0, false));
            }
        }

    }

    //Menu
    public class ItemClickHandle extends AsyncTask<String, Object, String> {
        private String mlt;
        private LinearLayout layout_row;
        private TextView txt_row_DanhBo;
        private ImageView img_row_check;
        private int pos;

        public ItemClickHandle(LinearLayout layout_row, TextView txt_row_DanhBo, ImageView img_row_check, int pos) {
            this.layout_row = layout_row;
            this.txt_row_DanhBo = txt_row_DanhBo;
            this.img_row_check = img_row_check;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LayLoTrinhActivity.this, "Đang tính tổng số danh bộ...", Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {
            mlt = params[0];
int danhbo;
//            LayLoTrinhActivity.this.mHoaDons = LayLoTrinhActivity.this.mLocalDatabase.getAllHoaDons();
            danhbo = LayLoTrinhActivity.this.da.getItem(mlt).getDanhbo();
            if (danhbo != 0) {
                publishProgress(danhbo);
                LayLoTrinhActivity.this.m_DanhBo[this.pos] = danhbo;
                return danhbo+ "";
            }
            ConnectionDB condb = new ConnectionDB();
            Connection cnn = condb.getConnect();
            LayLoTrinhActivity.this.mHoaDons = new ArrayList<HoaDon>();
            Statement statement = null;
            try {
                statement = cnn.createStatement();

                ResultSet rs = statement.executeQuery("SELECT * FROM HOADON WHERE MLT = '" + mlt + "'");
                while (rs.next()) {
                    int id = rs.getInt(1);

                    String khu = rs.getString(2);
                    String dot = rs.getString(3);
                    String danhBo = rs.getString(4);
                    String cuLy = rs.getString(5);
                    String hopDong = rs.getString(6);
                    String tenKhachHang = rs.getString(7);
                    String soNha = rs.getString(8);
                    String duong = rs.getString(9);
                    String giaBieu = rs.getString(10);
                    String dinhMuc = rs.getString(11);
                    String ky = rs.getString(12);
                    String nam = rs.getString(13);
                    String code = rs.getString(14);
                    String codeFU = rs.getString(15);
                    String chiSoCu = rs.getString(16);
                    String chiSoMoi = rs.getString(17);
                    String quan = rs.getString(18);
                    String phuong = rs.getString(19);
                    String maLoTrinh = rs.getString(23);
                    HoaDon hoaDon = new HoaDon(id, khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
                    LayLoTrinhActivity.this.mHoaDons.add(hoaDon);
                    if(mLocalDatabase.addHoaDon(hoaDon));
                    else{
                        //TODO
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            publishProgress(LayLoTrinhActivity.this.mHoaDons.size());
            mLocalDatabase.addLoTrinh(new LoTrinh(mlt, LayLoTrinhActivity.this.mHoaDons.size()));
            da.removeItem(mlt);
            LayLoTrinhActivity.this.m_DanhBo[this.pos] = LayLoTrinhActivity.this.mHoaDons.size();
            return LayLoTrinhActivity.this.mHoaDons.size() + "";
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            int danhBo = (int) values[0];
            this.layout_row.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_check));
            try {
                Thread.sleep(500);
                Button btnViewDownloadMLT = (Button) findViewById(R.id.btn_llt_viewdownloadMLT);
                btnViewDownloadMLT.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_check));

                Thread.sleep(500);
                btnViewDownloadMLT.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.colorGreen));
                Thread.sleep(500);
                gridView.setAdapter(da);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            LayLoTrinhActivity.this.da.getItem(mlt).setDanhbo(danhBo);
//            this.txt_row_DanhBo.setText(danhBo + "");
//            int count = LayLoTrinhActivity.this.m_MLT_TongDanhBo.size();
//            if (!LayLoTrinhActivity.this.m_MLT_TongDanhBo.containsKey(mlt)) {
//                LayLoTrinhActivity.this.m_MLT_TongDanhBo.put(mlt, danhBo);
//                this.img_row_check.setImageResource(R.drawable.checked);
//                LayLoTrinhActivity.this.da.getItem(mlt).setCheckpos(true);
//                this.layout_row.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_check));
//            } else {
//                LayLoTrinhActivity.this.m_MLT_TongDanhBo.remove(mlt);
//                this.img_row_check.setImageResource(0);
//                LayLoTrinhActivity.this.da.getItem(mlt).setCheckpos(false);
//                this.layout_row.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_uncheck));
//            }
            LayLoTrinhActivity.this.mSumMLT += 1;
            LayLoTrinhActivity.this.m_txtTongMLT.setText("Mã lộ trình: " + LayLoTrinhActivity.this.mSumMLT);
//
//            int sum_db = 0;
//            for (Integer db : LayLoTrinhActivity.this.m_MLT_TongDanhBo.values())
//                sum_db += db;
            LayLoTrinhActivity.this.mSumDanhBo += danhBo;
            LayLoTrinhActivity.this.m_txtTongDB.setText("Danh bộ: " + LayLoTrinhActivity.this.mSumDanhBo);
            spinner.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w(s, s);
        }
    }


    public void doViewDownloadMLT(View v) {
        int size = this.mLocalDatabase.getAllMLT().size();
        if (size == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, XemLoTrinhDaTaiActivity.class);
            startActivity(intent);
        }
    }

    public void doDocSo(View v) {
        doViewDownloadMLT(v);
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(LayLoTrinhActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
    }
}
