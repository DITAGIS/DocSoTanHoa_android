package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;
import com.ditagis.hcm.docsotanhoa.entities.LoTrinh;
import com.ditagis.hcm.docsotanhoa.localdb.MyDatabaseHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView txtTongMLT;
    TextView txtTongDB;
    EditText editTextSearch;
    GridView gridView;
    ImageButton imgbtnCheck;
    HoaDonDB hoaDonDB = new HoaDonDB();
    int sum_mlt = 0;
    //Dùng mảng 1 chiều hoặc ArrayList để lưu một số dữ liệu
    private ArrayList<String> m_mlt;
    private int m_DanhBo[];
    private int m_checked_position[];
    LayLoTrinh m_layLoTrinh;
    private GridViewLayLoTrinhAdapter da;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);

        m_mlt = new ArrayList<String>();
//

        txtTongMLT = (TextView) findViewById(R.id.txt_llt_mlt);
        txtTongDB = (TextView) findViewById(R.id.txt_llt_db);
        editTextSearch = (EditText) findViewById(R.id.etxt_llt_search);
        imgbtnCheck = (ImageButton) findViewById(R.id.imgbtn_llt_check);
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
        m_layLoTrinh.execute();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt_row_MLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
                TextView txt_row_DanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
                ImageView img_row_View = (ImageView) view.findViewById(R.id.row_llt_img_Check);


                if (txt_row_DanhBo.getText().toString().equals("Chưa xác định")) {
                    AsyncTask<String, Object, String> execute = new ItemClickHandle(txt_row_DanhBo, img_row_View, position).execute(txt_row_MLT.getText().toString());
                }
            }

        });


//        List<String> result = null;
//        try {
//
//            result = hoaDonDB.getAllMaLoTrinh();
//            Collections.sort(result);
//            int size = result.size();
//            mlt = new String[size];
//            tongDanhBo = new int[size];
//            checked_position = new int[size];
//            for (int i = 0; i < mlt.length; i++) {
//                mlt[i] = result.get(i);
//                checked_position[i] = 0;
//            }
//
//            //Tối tượng này dùng để hiển thị phần tử được chọn trong GridView
//            gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);
//            //Gán DataSource vào ArrayAdapter
//
//            GridViewLayLoTrinhAdapter da = new GridViewLayLoTrinhAdapter(this, mlt, tongDanhBo, checked_position);
//            //gán Datasource vào GridView
//
//            gridView.setAdapter(da);
//            registerForContextMenu(this.gridView);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////                    ImageView imgView = (ImageView) view.findViewById(R.id.row_llt_img_Check);
////                    if (imgView.getDrawable() == null) {
////                        imgView.setImageResource(R.drawable.checked);
////                        checked_position.add(position);
////                    } else {
////                        imgView.setImageResource(0);
////                        checked_position.remove((Object) position);
////                    }
//                    ImageView imgView = (ImageView) view.findViewById(R.id.row_llt_img_Check);
//
//
//                    TextView txtMLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
//                    TextView txtDanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
//                    if (checked_position[position] == 0) {
//                        checked_position[position] = 1;
//                        imgView.setImageResource(R.drawable.checked);
//                    } else {
//                        checked_position[position] = 0;
//                        imgView.setImageResource(0);
//                    }
////                    if (txtMLT.getText().equals("0")) {
//                        int danhBo = hoaDonDB.getNum_DanhBo_ByMLT((String) txtMLT.getText());
//                        txtDanhBo.setText(danhBo + "");
//                        tongDanhBo[position] = danhBo;
////                    }
//                }
//
//
//            });
//
////            gridView.setOnTouchListener(new View.OnTouchListener() {
////                @Override
////                public boolean onTouch(View v, MotionEvent event) {
////                    return false;
////                }
////            });
////            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
////                @Override
////                public void onScrollStateChanged(AbsListView view, int scrollState) {
////
////                }
////
////                int myLastVisiblePos = gridView.getFirstVisiblePosition();
////
////                @Override
////                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//////                    int currentFirstVisPos = view.getFirstVisiblePosition();
//////                    if(currentFirstVisPos > myLastVisiblePos) {
//////                        //scroll down
//////                    }
//////                    if(currentFirstVisPos < myLastVisiblePos) {
//////                        //scroll up
//////                    }
//////                    myLastVisiblePos = currentFirstVisPos;
//////                    for(int i = firstVisibleItem; i < visibleItemCount + firstVisibleItem; i ++){
//////                        tongDanhBo[i] = hoaDonDB.getNum_DanhBo_ByMLT(mlt[i]);
//////                    }
////
////                }
////            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    public void doCheck(View v) {

        sum_mlt = 0;
        int sum = 0;
        for (int i = 0; i < LayLoTrinhActivity.this.m_checked_position.length; i++)
            if (LayLoTrinhActivity.this.m_checked_position[i] == 1) {
                sum += LayLoTrinhActivity.this.m_DanhBo[i];
                sum_mlt++;
            }
        txtTongMLT.setText("Mã lộ trình: " + sum_mlt);
        txtTongDB.setText("Tổng danh bộ: " + sum);

    }


    public class LayLoTrinh extends AsyncTask<Void, Object, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LayLoTrinhActivity.this, "Đang lấy danh sách mã lộ trình", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            ConnectionDB condb = new ConnectionDB();
            Connection cnn = condb.getConnect();
            try {
                Statement statement = cnn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT DISTINCT MLT FROM HOADON");
                while (rs.next()) {

                    String maLoTrinh = rs.getString(1);
                    publishProgress(maLoTrinh, 0, 0);
                }
                LayLoTrinhActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LayLoTrinhActivity.this, "Đã lấy xong mã lộ trình", Toast.LENGTH_SHORT).show();
                        int count = m_mlt.size();
                        LayLoTrinhActivity.this.m_checked_position = new int[count];
                        LayLoTrinhActivity.this.m_DanhBo = new int[count];
                        for (int i = 0; i < count; i++) {
                            LayLoTrinhActivity.this.m_checked_position[i] = 0;
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
        }


        @Override
        protected String doInBackground(String... params) {
            String mlt = params[0];
            MyDatabaseHelper databaseHelper = new MyDatabaseHelper(LayLoTrinhActivity.this);
            List<LoTrinh> allMaLoTrinh = databaseHelper.getAllMaLoTrinh();

            int danhBo = -1;
            for (LoTrinh lotrinh : allMaLoTrinh) {
                if (lotrinh.getMaLoTrinh().equals(mlt)) {
                    danhBo = lotrinh.getSoLuong();
                    break;
                }
            }
            if (danhBo == -1) {
                danhBo = hoaDonDB.getNum_DanhBo_ByMLT(mlt);
                databaseHelper.addLoTrinh(new LoTrinh(mlt, danhBo));
            }

            publishProgress(danhBo);
            LayLoTrinhActivity.this.m_DanhBo[this.pos] = danhBo;
            return danhBo + "";
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            int danhBo = (int) values[0];
            this.txt_row_DanhBo.setText(danhBo + "");
            int count = LayLoTrinhActivity.this.m_checked_position.length;
            if (LayLoTrinhActivity.this.m_checked_position[this.pos] == 0) {
                LayLoTrinhActivity.this.m_checked_position[this.pos] = 1;
                this.img_row_check.setImageResource(R.drawable.checked);
            } else {
                LayLoTrinhActivity.this.m_checked_position[this.pos] = 0;
                this.img_row_check.setImageResource(0);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w(s, s);
        }
    }

    public void doDocSo(View v) {
        if (sum_mlt == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, DocSoActivity.class);
            Bundle extras = new Bundle();
            extras.putStringArrayList("mlt", LayLoTrinhActivity.this.m_mlt);
            extras.putIntArray("danhbo", LayLoTrinhActivity.this.m_DanhBo);
            extras.putIntArray("chkPosition", LayLoTrinhActivity.this.m_checked_position);
            extras.putInt("sum_mlt", sum_mlt);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(LayLoTrinhActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
    }
}
