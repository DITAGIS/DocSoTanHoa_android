package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.ConnectionDB;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView txtTongMLT;
    TextView txtTongDB;
    GridView gridView;
    ImageButton imgbtnCheck;
    HoaDonDB hoaDonDB = new HoaDonDB();
    int sum_mlt = 0;
    //Dùng mảng 1 chiều hoặc ArrayList để lưu một số dữ liệu
    private ArrayList<String> m_mlt;
    int tongDanhBo[];
    int checked_position[];
    LayLoTrinh m_layLoTrinh;
    private GridViewLayLoTrinhAdapter da;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);

        m_mlt = new ArrayList<String>();


        txtTongMLT = (TextView) findViewById(R.id.txt_llt_mlt);
        txtTongDB = (TextView) findViewById(R.id.txt_llt_db);
        imgbtnCheck = (ImageButton) findViewById(R.id.imgbtn_llt_check);
        gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);
        //Gán DataSource vào ArrayAdapter

        da = new GridViewLayLoTrinhAdapter(LayLoTrinhActivity.this, new ArrayList<GridViewLayLoTrinhAdapter.Item>());
        //gán Datasource vào GridView

        gridView.setAdapter(da);
        registerForContextMenu(LayLoTrinhActivity.this.gridView);
        m_layLoTrinh = new LayLoTrinh();
        m_layLoTrinh.execute();

//        //sort malotrinh
//        Collections.sort(m_mlt);
//        List<GridViewLayLoTrinhAdapter.Item> itemList = new ArrayList<GridViewLayLoTrinhAdapter.Item>() {
//        };
//        for(String mlt: m_mlt){
//            itemList.add(new GridViewLayLoTrinhAdapter.Item(mlt, 0, 0));
//        }
//        da = new GridViewLayLoTrinhAdapter(LayLoTrinhActivity.this, itemList);
//        //gán Datasource vào GridView
//
//        gridView.setAdapter(da);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                int[] abcxyzxxx = ((GridViewLayLoTrinhAdapter) gridView.getAdapter()).get_checked_position();
//                ImageView imgView = (ImageView) view.findViewById(R.id.row_llt_img_Check);
//                TextView txtMLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
//                TextView txtDanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
//                if (checked_position[position] == 0) {
//                    checked_position[position] = 1;
//                    imgView.setImageResource(R.drawable.checked);
//                } else {
//                    checked_position[position] = 0;
//                    imgView.setImageResource(0);
//                }
//                if (txtMLT.getText().toString().equals("0")) {
//                    int danhBo = hoaDonDB.getNum_DanhBo_ByMLT((String) txtMLT.getText());
//                    txtDanhBo.setText(danhBo + "");
//                    tongDanhBo[position] = danhBo;
//                }
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
        for (int i = 0; i < checked_position.length; i++)
            if (checked_position[i] == 1) {
                sum += tongDanhBo[i];
                sum_mlt++;
            }
        txtTongMLT.setText("Mã lộ trình: " + sum_mlt);
        txtTongDB.setText("Tổng danh bộ: " + sum);

    }


    public class LayLoTrinh extends AsyncTask<Void, Object, Void> {
        int[] checked_position, tongDanhBo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LayLoTrinhActivity.this, "Đang lấy danh sách mã lộ trình", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            List<String> result;
            ConnectionDB condb = new ConnectionDB();
            Connection cnn = condb.getConnect();
            try {
                Statement statement = cnn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT DISTINCT MLT FROM HOADON");
                while (rs.next()) {

                    String maLoTrinh = rs.getString(1);
                    publishProgress(maLoTrinh, 0, 0);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(1000);
                LayLoTrinhActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LayLoTrinhActivity.this, "Đã lấy xong mã lộ trình", Toast.LENGTH_SHORT).show();
                    }
                });
                Collections.sort(m_mlt);

                rs.close();
                statement.close();
                cnn.close();

            } catch (SQLException e) {

                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//                result = hoaDonDB.getAllMaLoTrinh();
//                Collections.sort(result);
//                int size = result.size();
//                mlt = new String[size];
//                tongDanhBo = new int[size];
//                checked_position = new int[size];
//                for (int i = 0; i < mlt.length; i++) {
//                    mlt[i] = result.get(i);
//                    checked_position[i] = 0;
//                }

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

    public void doDocSo(View v) {
        if (sum_mlt == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, DocSoActivity.class);
            Bundle extras = new Bundle();
            extras.putStringArrayList("mlt", m_mlt);
            extras.putIntArray("danhbo", tongDanhBo);
            extras.putIntArray("chkPosition", checked_position);
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
