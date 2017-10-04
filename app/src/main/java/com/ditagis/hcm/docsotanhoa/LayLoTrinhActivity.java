package com.ditagis.hcm.docsotanhoa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.conectDB.HoaDonDB;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class LayLoTrinhActivity extends AppCompatActivity {
    TextView txtTongMLT;
    TextView txtTongDB;
    GridView gridView;
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


        HoaDonDB cdb = new HoaDonDB();
        List<String> result = null;
        try {
            Toast.makeText(LayLoTrinhActivity.this, "Đang lấy danh sách mã lộ trình", Toast.LENGTH_LONG).show();
            result = cdb.getAllMaLoTrinh();
            Collections.sort(result);
            int size = result.size();
            mlt = new String[size];
            for (int i = 0; i < mlt.length; i++)
                mlt[i] = result.get(i);

            txtTongMLT.setText("Mã lộ trình: " + mlt.length);
            //Tối tượng này dùng để hiển thị phần tử được chọn trong GridView
            gridView = (GridView) findViewById(R.id.grid_llt_danhSachLoTrinh);
            //Gán DataSource vào ArrayAdapter
            GridViewLayLoTrinhAdapter da = new GridViewLayLoTrinhAdapter(this, mlt);
            //gán Datasource vào GridView

            gridView.setAdapter(da);
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
