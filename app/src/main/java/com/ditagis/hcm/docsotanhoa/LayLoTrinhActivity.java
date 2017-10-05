package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;

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
    String mlt[] = {"Ipad", "Iphone", "New Ipad",
            "SamSung", "Nokia", "Sony Ericson",
            "Ipad", "Iphone", "New Ipad",
            "SamSung", "Nokia", "Sony Ericson",
            "Ipad", "Iphone", "New Ipad",
            "SamSung", "Nokia", "Sony Ericson",
            "Ipad", "Iphone", "New Ipad",
            "SamSung", "Nokia", "Sony Ericson",
            "Ipad", "Iphone", "New Ipad",
            "SamSung", "Nokia", "Sony Ericson",
    };
    int tongDanhBo[] = {0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
            0, 1, 50,
    };
    int checked_position[];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_lo_trinh);
        txtTongMLT = (TextView) findViewById(R.id.txt_llt_mlt);
        txtTongDB = (TextView) findViewById(R.id.txt_llt_db);
        imgbtnCheck = (ImageButton) findViewById(R.id.imgBtn_ChupAnh);

        List<String> result = null;
        try {
            Toast.makeText(LayLoTrinhActivity.this, "Đang lấy danh sách mã lộ trình", Toast.LENGTH_SHORT).show();
            result = hoaDonDB.getAllMaLoTrinh();
            Collections.sort(result);
            int size = result.size();
            mlt = new String[size];
            tongDanhBo = new int[size];
            checked_position = new int[size];
            for (int i = 0; i < mlt.length; i++) {
                mlt[i] = result.get(i);
                checked_position[i] = 0;
            }

            //Tối tượng này dùng để hiển thị phần tử được chọn trong GridView
            gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);
            //Gán DataSource vào ArrayAdapter

            GridViewLayLoTrinhAdapter da = new GridViewLayLoTrinhAdapter(this, mlt, tongDanhBo, checked_position);
            //gán Datasource vào GridView

            gridView.setAdapter(da);
            registerForContextMenu(this.gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    ImageView imgView = (ImageView) view.findViewById(R.id.row_llt_img_Check);
//                    if (imgView.getDrawable() == null) {
//                        imgView.setImageResource(R.drawable.checked);
//                        checked_position.add(position);
//                    } else {
//                        imgView.setImageResource(0);
//                        checked_position.remove((Object) position);
//                    }
                    ImageView imgView = (ImageView) view.findViewById(R.id.row_llt_img_Check);


                    TextView txtMLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
                    TextView txtDanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);
                    if (checked_position[position] == 0) {
                        checked_position[position] = 1;
                        imgView.setImageResource(R.drawable.checked);
                    } else {
                        checked_position[position] = 0;
                        imgView.setImageResource(0);
                    }
//                    if (txtMLT.getText().equals("0")) {
                        int danhBo = hoaDonDB.getNum_DanhBo_ByMLT((String) txtMLT.getText());
                        txtDanhBo.setText(danhBo + "");
                        tongDanhBo[position] = danhBo;
//                    }
                }


            });

//            gridView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return false;
//                }
//            });
//            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//                }
//
//                int myLastVisiblePos = gridView.getFirstVisiblePosition();
//
//                @Override
//                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////                    int currentFirstVisPos = view.getFirstVisiblePosition();
////                    if(currentFirstVisPos > myLastVisiblePos) {
////                        //scroll down
////                    }
////                    if(currentFirstVisPos < myLastVisiblePos) {
////                        //scroll up
////                    }
////                    myLastVisiblePos = currentFirstVisPos;
////                    for(int i = firstVisibleItem; i < visibleItemCount + firstVisibleItem; i ++){
////                        tongDanhBo[i] = hoaDonDB.getNum_DanhBo_ByMLT(mlt[i]);
////                    }
//
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }


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

    //Menu

    public void doDocSo(View v) {
        if (sum_mlt == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(LayLoTrinhActivity.this, DocSoActivity.class);
            Bundle extras = new Bundle();
            extras.putStringArray("mlt", mlt);
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

//    public class LayLoTrinh extends AsyncTask<String,String,String[]>{
//        @Override
//        protected String[] doInBackground(String... params) {
//            HoaDonDB cdb = new HoaDonDB();
//            List<String> result = null;
//            try {
//                result = cdb.getAllMaLoTrinh();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            mlt = new String[result.size()];
//            for (int i = 0; i < mlt.length; i++)
//                mlt[i] = result.get(i);
//            return mlt;
//        }
//
//        @Override
//        protected void onPostExecute(String[] strings) {
//            super.onPostExecute(strings);
//        }
//    }
}
