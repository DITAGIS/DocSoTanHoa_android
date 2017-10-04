package com.ditagis.hcm.docsotanhoa;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView txtTongMLT;
    TextView txtTongDB;
    GridView gridView;
    ImageButton imgbtnCheck;
    HoaDonDB hoaDonDB = new HoaDonDB();
    List<Integer> checked_position = new ArrayList<Integer>() {
    };
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
            for (int i = 0; i < mlt.length; i++) {
                mlt[i] = result.get(i);

            }

            //Tối tượng này dùng để hiển thị phần tử được chọn trong GridView
            gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);
            //Gán DataSource vào ArrayAdapter

            GridViewLayLoTrinhAdapter da = new GridViewLayLoTrinhAdapter(this, mlt, tongDanhBo);
            //gán Datasource vào GridView

            gridView.setAdapter(da);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageView imgView = (ImageView) view.findViewById(R.id.row_llt_img_Check);
                    if (imgView.getDrawable() == null) {
                        imgView.setImageResource(R.drawable.checked);
                        checked_position.add(position);
                    } else {
                        imgView.setImageResource(0);
                        checked_position.remove((Object)position);
                    }
                    TextView txtMLT = (TextView) view.findViewById(R.id.row_llt_txt_malotrinh);
                    TextView txtDanhBo = (TextView) view.findViewById(R.id.row_llt_txt_tongDanhBo);

                    int danhBo = hoaDonDB.getNum_DanhBo_ByMLT((String) txtMLT.getText());
                    txtDanhBo.setText( danhBo+ "");
                    tongDanhBo[position] = danhBo;
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

        txtTongMLT.setText("Mã lộ trình: " + checked_position.size());
        int sum = 0;
        for(int i: checked_position)
            sum+= tongDanhBo[i];

        txtTongDB.setText("Tổng danh bộ: " + sum);
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
