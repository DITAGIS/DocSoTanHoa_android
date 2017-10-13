package com.ditagis.hcm.docsotanhoa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewQuanLyDocSoAdapter;
import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class QuanLyDocSoActivity extends AppCompatActivity {
    EditText editTextSearch;
    GridView gridView;
    private ArrayList<DanhBo_ChiSoMoi> danhBo_chiSoMois;
    private HashMap<String, Integer> m_MLT_TongDanhBo;
    private GridViewQuanLyDocSoAdapter da;
    LocalDatabase localDatabase;
    private int mSumDanhBo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localDatabase = new LocalDatabase(this);
        setContentView(R.layout.activity_quan_ly_doc_so);

        editTextSearch = (EditText) findViewById(R.id.etxt_qlds_search);
        gridView = (GridView) findViewById(R.id.grid_qlds_danhSachDocSo);
        danhBo_chiSoMois = localDatabase.getAllDanhBo_CSM();

        editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //TODO search danh bộ

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Gán DataSource vào ArrayAdapter

        da = new GridViewQuanLyDocSoAdapter(QuanLyDocSoActivity.this, new ArrayList<GridViewQuanLyDocSoAdapter.Item>());

        for (DanhBo_ChiSoMoi danhBo_chiSoMoi : this.danhBo_chiSoMois) {
            da.add(new GridViewQuanLyDocSoAdapter.Item(
                    danhBo_chiSoMoi.getMaLoTrinh(),
                    danhBo_chiSoMoi.getDanhBo(),
                    danhBo_chiSoMoi.getChiSoCu(),
                    danhBo_chiSoMoi.getChiSoMoi()));
        }

        //gán Datasource vào GridView

        gridView.setAdapter(da);
        registerForContextMenu(QuanLyDocSoActivity.this.gridView);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String danhBo = ((TextView) view.findViewById(R.id.row_qlds_txt_danhBo)).getText().toString();
                DanhBo_ChiSoMoi danhBo_CSM = QuanLyDocSoActivity.this.localDatabase.getDanhBo_CSM(danhBo);

                //--------------------
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDocSoActivity.this);
                builder.setTitle("Danh bộ: " + danhBo);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("Chỉnh sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.layout_view_thongtin_docso, null);


                dialog.setView(dialogLayout);

                dialog.show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(danhBo_CSM.getImage(), options);

                ImageView image = (ImageView) dialog.findViewById(R.id.imgView_qlds);
                BitmapDrawable resizedDialogImage = new BitmapDrawable(QuanLyDocSoActivity.this.getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));

                image.setBackground(resizedDialogImage);

                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_MLT)).setText(danhBo_CSM.getMaLoTrinh());
//                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_DanhBo)).setText(danhBo_CSM.getDanhBo());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_tenKH)).setText(danhBo_CSM.getTenKH());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_diaChi)).setText(danhBo_CSM.getDiaChi());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_SDT)).setText(danhBo_CSM.getSdt());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_CSC)).setText(danhBo_CSM.getChiSoCu());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_CSM)).setText(danhBo_CSM.getChiSoMoi());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_code)).setText(danhBo_CSM.getCode());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_giaBieu)).setText(QuanLyDocSoActivity.this.localDatabase.getHoaDon(danhBo_CSM.getDanhBo()).getGiaBieu());
                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_tienNuoc)).setText("");//TODO tien nuoc

                ((TextView) dialog.findViewById(R.id.txt_layout_qlds_ghiChu)).setText(danhBo_CSM.getNote());
                return false;


            }
        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView txt_row_MLT = (TextView) view.findViewById(R.id.row_xltdt_txt_malotrinh);
//                TextView txt_row_danhbo = (TextView) view.findViewById(R.id.row_xltdt_txt_tongDanhBo);
//                ImageView imgCheck = (ImageView) view.findViewById(R.id.row_xltdt_img_Check);
//                LinearLayout row_layout = (LinearLayout) view.findViewById(R.id.row_xltdt_layout);
//                String mlt = txt_row_MLT.getText().toString();
//                String danhbo = txt_row_danhbo.getText().toString();
//                if (da.getItem(mlt).getCheckpos()) {
//                    da.getItem(mlt).setCheckpos(false);
//                    QuanLyDocSoActivity.this.m_MLT_TongDanhBo.remove(mlt);
//                    imgCheck.setImageResource(0);
//                    row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.color_row_uncheck));
//                    QuanLyDocSoActivity.this.mSumDanhBo -= Integer.parseInt(danhbo);
//
//                } else {
//                    da.getItem(mlt).setCheckpos(true);
//                    QuanLyDocSoActivity.this.m_MLT_TongDanhBo.put(mlt, Integer.parseInt(danhbo));
//                    imgCheck.setImageResource(R.drawable.checked);
//                    row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.color_row_check));
//
//                    QuanLyDocSoActivity.this.mSumDanhBo += Integer.parseInt(danhbo);
//
//                }
//
//            }
//
//        });


    }

    public void doDownloadMLT(View v) {
        finish();
    }

    public void doDocSo(View v) {
        if (this.m_MLT_TongDanhBo.size() == 0)
            Toast.makeText(this, "Chưa có lộ trình!!!", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(QuanLyDocSoActivity.this, DocSoActivity.class);
            Bundle extras = new Bundle();

            String[] mltArr = new String[this.m_MLT_TongDanhBo.size()];
            int j = 0;
            for (String mlt : this.m_MLT_TongDanhBo.keySet())
                mltArr[j++] = mlt;
            extras.putStringArray("mMltArr", mltArr);
//            extras.putStringArrayList("mMlt", LayLoTrinhActivity.this.m_mlt);
//            extras.putBooleanArray("chkPosition", LayLoTrinhActivity.this.m_MLT_TongDanhBo);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    public void doQuanLyDocSo(View v) {
        Intent intent = new Intent(QuanLyDocSoActivity.this, QuanLyDocSoActivity.class);

        startActivity(intent);
    }
}
