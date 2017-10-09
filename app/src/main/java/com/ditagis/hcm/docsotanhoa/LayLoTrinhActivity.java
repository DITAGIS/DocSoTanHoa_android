package com.ditagis.hcm.docsotanhoa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.MyDatabaseHelper;

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
    private boolean m_checked_position[]; //TODO: cần chuyển thành int[] với mỗi phần tử là vị trí được check
    LayLoTrinh m_layLoTrinh;
    private int m_sum_mlt = 0;
    private int m_sum_db = 0;
    private GridViewLayLoTrinhAdapter da;
    private MyDatabaseHelper m_databaseHelper;
    private List<HoaDon> mHoaDons;
    private ProgressBar spinner;
    private Handler handler = new Handler();
    private int progressStatus = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lay_lo_trinh);

        m_mlt = new ArrayList<String>();
        m_databaseHelper = new MyDatabaseHelper(this);
        m_databaseHelper.Upgrade();
        m_databaseHelper.Create();
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
                if (!s.equals("")) {
//                    System.out.println(gridView.getAdapter().getItem(0).toString());
                    List<String> result = new ArrayList<String>();
                    //Lấy dữ liệu bắt đầu với text search
                    for (String mlt : m_mlt) {
                        if (mlt.startsWith(s.toString()))
                            result.add(mlt);
                    }
                    //Gán dữ liệu vào data source
                    if (da != null && result.size() > 0) {
                        da.clear();
                        for (String mlt : result)
                            da.add(new GridViewLayLoTrinhAdapter.Item(mlt, 0, 0));
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
        m_layLoTrinh = new LayLoTrinh();
        if (isOnline()) {
            //-------

//            textView = (TextView) findViewById(R.id.progressTextView);


            // set the drawable as progress drawable
//            initProgresBar();
            //-----------------

            m_layLoTrinh.execute();
        } else {
            Toast.makeText(this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
            //TODO
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isOnline()) {
                    TextView txt_row_MLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
                    TextView txt_row_DanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
                    ImageView img_row_View = (ImageView) view.findViewById(R.id.row_llt_img_Check);


                    AsyncTask<String, Object, String> execute = new ItemClickHandle(txt_row_DanhBo, img_row_View, position).execute(txt_row_MLT.getText().toString());
                } else {
                    Toast.makeText(LayLoTrinhActivity.this, "Kiểm tra kết nối Internet và thử lại", Toast.LENGTH_SHORT).show();
                    //TODO
                }

            }

        });


    }
    public void initProgresBar(){
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 1000) {
                    progressStatus += 1;
                    // Update the progress bar and display the

                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            spinner.setProgress(progressStatus);
//                            textView.setText(progressStatus+"/"+spinner.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.

                        //Just to display the progress slowly
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class LayLoTrinh extends AsyncTask<Void, Object, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LayLoTrinhActivity.this, "Đang lấy danh sách mã lộ trình", Toast.LENGTH_LONG).show();
            spinner = (ProgressBar) findViewById(R.id.myProgress);
            spinner.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... params) {

            ConnectionDB condb = new ConnectionDB();
            Connection cnn = condb.getConnect();
            try {
                Statement statement = cnn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT DISTINCT MLT FROM HOADON");
                LayLoTrinhActivity.this.mHoaDons = new ArrayList<HoaDon>();
                while (rs.next()) {

                    String maLoTrinh = rs.getString(1);

                    publishProgress(maLoTrinh, 0, 0);
                }
                LayLoTrinhActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LayLoTrinhActivity.this, "Đã lấy xong mã lộ trình", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.INVISIBLE);
                        int count = m_mlt.size();
                        LayLoTrinhActivity.this.m_checked_position = new boolean[count];
                        LayLoTrinhActivity.this.m_DanhBo = new int[count];
                        for (int i = 0; i < count; i++) {
                            LayLoTrinhActivity.this.m_checked_position[i] = false;
                            LayLoTrinhActivity.this.m_DanhBo[i] = 0;
                        }
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
                da.add(new GridViewLayLoTrinhAdapter.Item(mlt, 0, 0));
            }
        }

    }

    //Menu
    public class ItemClickHandle extends AsyncTask<String, Object, String> {

        private TextView txt_row_DanhBo;
        private ImageView img_row_check;
        private int pos;

        public ItemClickHandle(TextView txt_row_DanhBo, ImageView img_row_check, int pos) {
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
            String mlt = params[0];
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
                    m_databaseHelper.addHoaDon(hoaDon);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            publishProgress(LayLoTrinhActivity.this.mHoaDons.size());
            LayLoTrinhActivity.this.m_DanhBo[this.pos] = LayLoTrinhActivity.this.mHoaDons.size();
            return LayLoTrinhActivity.this.mHoaDons.size() + "";
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            int danhBo = (int) values[0];
            this.txt_row_DanhBo.setText(danhBo + "");
            int count = LayLoTrinhActivity.this.m_checked_position.length;
            if (!LayLoTrinhActivity.this.m_checked_position[this.pos]) {
                LayLoTrinhActivity.this.m_checked_position[this.pos] = true;
                this.img_row_check.setImageResource(R.drawable.checked);
                LayLoTrinhActivity.this.m_sum_mlt++;
                LayLoTrinhActivity.this.m_sum_db += danhBo;

            } else {
                LayLoTrinhActivity.this.m_checked_position[this.pos] = false;
                this.img_row_check.setImageResource(0);
                LayLoTrinhActivity.this.m_sum_mlt--;
                LayLoTrinhActivity.this.m_sum_db -= danhBo;
            }
            LayLoTrinhActivity.this.m_txtTongMLT.setText("Mã lộ trình: " + LayLoTrinhActivity.this.m_sum_mlt);
            LayLoTrinhActivity.this.m_txtTongDB.setText("Danh bộ: " + LayLoTrinhActivity.this.m_sum_db);
            spinner.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w(s, s);
        }
    }

    public void doDocSo(View v) {
        if (this.m_sum_mlt == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, DocSoActivity.class);
            Bundle extras = new Bundle();

            String[] mltArr = new String[this.m_sum_mlt];
            int j = 0;
            for (int i = 0; i < this.m_checked_position.length; i++)
                if (this.m_checked_position[i])
                    mltArr[j++] = this.m_mlt.get(i);
            extras.putStringArray("mMltArr", mltArr);
//            extras.putStringArrayList("mMlt", LayLoTrinhActivity.this.m_mlt);
//            extras.putBooleanArray("chkPosition", LayLoTrinhActivity.this.m_checked_position);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(LayLoTrinhActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
    }
}
