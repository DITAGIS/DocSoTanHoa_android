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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import java.util.Calendar;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView m_txtTongMLT;
    TextView m_txtTongDB;
    EditText editTextSearch;
    GridView gridView;
    //    ImageButton imgbtnCheck;
    HoaDonDB hoaDonDB = new HoaDonDB();
    private int m_DanhBo[];
    private GridViewLayLoTrinhAdapter da;
    private LocalDatabase mLocalDatabase;
    //    private List<HoaDon> mHoaDons;
    private int mSumDanhBo;
    private int mSumMLT;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mKy;
    private int mNam, mDot;
    private String mUsername;
    ConnectionDB condb = new ConnectionDB();
    Connection cnn = condb.getConnect();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);

        Calendar calendar = Calendar.getInstance();
        this.mKy = calendar.get(Calendar.MONTH) + 1;
        this.mNam = calendar.get(Calendar.YEAR);

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_llt_swipeRefreshLayout);
        mLocalDatabase = new LocalDatabase(this);
        mUsername = getIntent().getExtras().getString("mayds");
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
        List<HoaDon> hoaDons = this.mLocalDatabase.getAllHoaDon();
        for (HoaDon hoaDon : hoaDons)
            da.add(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(), true));
        gridView.setAdapter(da);
        registerForContextMenu(LayLoTrinhActivity.this.gridView);

        List<LoTrinh> loTrinhs = LayLoTrinhActivity.this.mLocalDatabase.getAllMaLoTrinh();
        LayLoTrinhActivity.this.mSumMLT = da.items.size();
        LayLoTrinhActivity.this.m_txtTongMLT.setText("Mã lộ trình: " + LayLoTrinhActivity.this.mSumMLT);
        LayLoTrinhActivity.this.mSumDanhBo = da.items.size();
        for (LoTrinh loTrinh : loTrinhs)
            LayLoTrinhActivity.this.mSumDanhBo += loTrinh.getSoLuong();

        LayLoTrinhActivity.this.m_txtTongDB.setText("Danh bộ: " + LayLoTrinhActivity.this.mSumDanhBo);
        ((Button) findViewById(R.id.btn_llt_loadMLT)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    new LayLoTrinh().execute();
                } else {
                    Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
                    //TODO
                }
            }
        });

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
        private ProgressDialog dialog;

        public LayLoTrinh() {
            this.dialog = new ProgressDialog(LayLoTrinhActivity.this);
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

            List<HoaDon> hoaDons = LayLoTrinhActivity.this.mLocalDatabase.getAllHoaDon();
            for (HoaDon hoaDon : hoaDons) {
                da.add(new GridViewLayLoTrinhAdapter.Item(hoaDon.getMaLoTrinh(), hoaDon.getDanhBo(), true));
            }
            if (LayLoTrinhActivity.this.mSwipeRefreshLayout.isRefreshing())
                LayLoTrinhActivity.this.mSwipeRefreshLayout.setRefreshing(false);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(LayLoTrinhActivity.this, "Đã tải xong mã lộ trình!!!", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

//            ConnectionDB condb = new ConnectionDB();
//            Connection cnn = condb.getConnect();
            String like = "";//LayLoTrinhActivity.this.mDot + LayLoTrinhActivity.this.mUsername + "%";
//            List<LoTrinh> loTrinhs = LayLoTrinhActivity.this.mLocalDatabase.getAllMaLoTrinh();
            try {
                Statement statement = cnn.createStatement(), sttm1;
                ResultSet rsDot = statement.executeQuery("SELECT TOP 1 dot from DocSo where nam = "
                        + LayLoTrinhActivity.this.mNam + " and ky = " + LayLoTrinhActivity.this.mKy + " order by dot desc");
                String dot = null;
                if (rsDot.next()) {
                    dot = rsDot.getString(1);
                }
                rsDot.close();

                like = dot + LayLoTrinhActivity.this.mUsername + "%";
                ResultSet rs = statement.executeQuery("SELECT * FROM DocSo where nam = "
                        + LayLoTrinhActivity.this.mNam + " and ky = " + LayLoTrinhActivity.this.mKy + " and mlt2 like '" + like + "'");
//                LayLoTrinhActivity.this.mHoaDons = new ArrayList<HoaDon>();
                while (rs.next()) {


//                    int id = rs.getInt(1); //TODO xem vụ tạo DocSoID
                    String khu = "";
                    String danhBo = rs.getString(2);
                    String cuLy = "";
                    String hopDong = "";
                    String tenKhachHang = "";
                    String maLoTrinh = rs.getString(4);
                    String soNha = rs.getString(5);
                    String duong = rs.getString(7);
                    String giaBieu = rs.getString(9);
                    String dinhMuc = rs.getString(10);
                    String nam = LayLoTrinhActivity.this.mNam + "";
                    String ky = LayLoTrinhActivity.this.mKy + "";
                    String chiSoCu = rs.getInt(17) + ""; // lấy chỉ số mới của kỳ trước
                    String chiSoMoi = "";
                    String code = "";
                    String codeFU = "";

                    String quan = "";
                    String phuong = "";


                    sttm1 = cnn.createStatement();
                    ResultSet rs1 = sttm1.executeQuery("SELECT HopDong, tenkh, quan, phuong FROM KhachHang where MLT2 = '" + maLoTrinh + "'");
                    if (rs1.next()) {
                        hopDong = rs1.getString(1);
                        tenKhachHang = rs1.getString(2);
                        quan = rs1.getString(3) == null ? "" : rs1.getString(3);
                        phuong = rs1.getString(4) == null ? "" : rs1.getString(4);

                    }
                    HoaDon hoaDon = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
//                    LayLoTrinhActivity.this.mHoaDons.add(hoaDon);
                    if (mLocalDatabase.addHoaDon(hoaDon)) ;
                    else {
                        //TODO
                    }
                    sttm1.close();
                    rs1.close();
                    publishProgress(maLoTrinh, 0, 0);
//                    mLocalDatabase.addLoTrinh(new LoTrinh(maLoTrinh, LayLoTrinhActivity.this.mHoaDons.size()));
                }


                rs.close();
                statement.close();


//                cnn.close();

            } catch (SQLException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

        }

    }

    //Menu
//    public class ItemClickHandle extends AsyncTask<String, Object, String> {
//        private String mlt;
////        private LinearLayout layout_row;
////        private int pos;
//
//        public ItemClickHandle() {
////            this.layout_row = layout_row;
////            this.pos = pos;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            Toast.makeText(LayLoTrinhActivity.this, "Đang tính tổng số danh bộ...", Toast.LENGTH_LONG).show();
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            mlt = params[0];
//            int danhbo;
////            LayLoTrinhActivity.this.mHoaDons = LayLoTrinhActivity.this.mLocalDatabase.getAllHoaDons();
////            danhbo = LayLoTrinhActivity.this.da.getItem(mlt).getDanhbo();
////            if (danhbo != 0) {
////                publishProgress(danhbo);
////                LayLoTrinhActivity.this.m_DanhBo[this.pos] = danhbo;
////                return danhbo + "";
////            }
//
////            LayLoTrinhActivity.this.mHoaDons = new ArrayList<HoaDon>();
//            Statement statement = null, sttm1;
//            try {
//                statement = cnn.createStatement();
//
//                ResultSet rs = statement.executeQuery("SELECT * FROM DocSo where nam = "
//                        + LayLoTrinhActivity.this.mNam + " and ky = " + LayLoTrinhActivity.this.mKy + " and MLT2 = '" + mlt + "'");
//                while (rs.next()) {
//                    int id = 0; //TODO xem vụ tạo DocSoID
//                    String khu = "";
//                    String dot = "";
//                    String danhBo = rs.getString(2);
//                    String cuLy = "";
//                    String hopDong = "";
//                    String tenKhachHang = "";
//                    String soNha = rs.getString(5);
//                    String duong = rs.getString(7);
//                    String giaBieu = rs.getString(9);
//                    String dinhMuc = rs.getString(10);
//                    String nam = LayLoTrinhActivity.this.mNam + "";
//                    String ky = LayLoTrinhActivity.this.mKy + "";
//                    String chiSoCu = rs.getInt(17) + ""; // lấy chỉ số mới của kỳ trước
//                    String chiSoMoi = "";
//                    String code = "";
//                    String codeFU = "";
//
//                    String quan = "";
//                    String phuong = "";
//                    String maLoTrinh = mlt;
//
//                    sttm1 = cnn.createStatement();
//                    ResultSet rs1 = sttm1.executeQuery("SELECT HopDong, tenkh, quan, phuong FROM KhachHang where MLT2 = '" + mlt + "'");
//                    if (rs1.next()) {
//                        hopDong = rs1.getString(1);
//                        tenKhachHang = rs1.getString(2);
//                        quan = rs1.getString(3) == null ? "" : rs1.getString(3);
//                        phuong = rs1.getString(4) == null ? "" : rs1.getString(4);
//
//                    }
//                    HoaDon hoaDon = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
////                    LayLoTrinhActivity.this.mHoaDons.add(hoaDon);
//                    if (mLocalDatabase.addHoaDon(hoaDon)) ;
//                    else {
//                        //TODO
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
////            publishProgress(LayLoTrinhActivity.this.mHoaDons.size());
////            mLocalDatabase.addLoTrinh(new LoTrinh(mlt, LayLoTrinhActivity.this.mHoaDons.size()));
//            da.removeItem(mlt);
//            m_mlt.remove(mlt);
////            LayLoTrinhActivity.this.m_DanhBo[this.pos] = LayLoTrinhActivity.this.mHoaDons.size();
////            return LayLoTrinhActivity.this.mHoaDons.size() + "";
//            return "";
//        }
//
//        @Override
//        protected void onProgressUpdate(Object... values) {
//            super.onProgressUpdate(values);
//            int danhBo = (int) values[0];
////            this.layout_row.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_check));
//            try {
//                Thread.sleep(500);
//                Button btnViewDownloadMLT = (Button) findViewById(R.id.btn_llt_viewdownloadMLT);
//                btnViewDownloadMLT.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_check));
//
//                Thread.sleep(500);
//                btnViewDownloadMLT.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.colorGreen));
//                Thread.sleep(500);
//                gridView.setAdapter(da);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
////            LayLoTrinhActivity.this.da.getItem(mlt).setDanhbo(danhBo);
////            this.txt_row_DanhBo.setText(danhBo + "");
////            int count = LayLoTrinhActivity.this.m_MLT_TongDanhBo.size();
////            if (!LayLoTrinhActivity.this.m_MLT_TongDanhBo.containsKey(mlt)) {
////                LayLoTrinhActivity.this.m_MLT_TongDanhBo.put(mlt, danhBo);
////                this.img_row_check.setImageResource(R.drawable.checked);
////                LayLoTrinhActivity.this.da.getItem(mlt).setCheckpos(true);
////                this.layout_row.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_check));
////            } else {
////                LayLoTrinhActivity.this.m_MLT_TongDanhBo.remove(mlt);
////                this.img_row_check.setImageResource(0);
////                LayLoTrinhActivity.this.da.getItem(mlt).setCheckpos(false);
////                this.layout_row.setBackgroundColor(ContextCompat.getColor(LayLoTrinhActivity.this, R.color.color_row_uncheck));
////            }
//            LayLoTrinhActivity.this.mSumMLT += 1;
//            LayLoTrinhActivity.this.m_txtTongMLT.setText("Mã lộ trình: " + LayLoTrinhActivity.this.mSumMLT);
////
////            int sum_db = 0;
////            for (Integer db : LayLoTrinhActivity.this.m_MLT_TongDanhBo.values())
////                sum_db += db;
//            LayLoTrinhActivity.this.mSumDanhBo += danhBo;
//            LayLoTrinhActivity.this.m_txtTongDB.setText("Danh bộ: " + LayLoTrinhActivity.this.mSumDanhBo);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.w(s, s);
//        }
//    }


    public void doViewDownloadMLT(View v) {
        int size = this.mLocalDatabase.getAllHoaDon().size();
        if (size == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, XemLoTrinhDaTaiActivity.class);
            startActivity(intent);
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
