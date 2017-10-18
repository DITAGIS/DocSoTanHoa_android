package com.ditagis.hcm.docsotanhoa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewQuanLyDocSoAdapter;
import com.ditagis.hcm.docsotanhoa.conectDB.Uploading;
import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class QuanLyDocSoActivity extends AppCompatActivity {
    EditText editTextSearch;
    GridView gridView;
    TextView txtComplete;
    private ArrayList<DanhBo_ChiSoMoi> danhBo_chiSoMois;
    private HashMap<String, Integer> m_MLT_TongDanhBo;
    private GridViewQuanLyDocSoAdapter da;
    LocalDatabase localDatabase;
    private int mSumDanhBo = 0, mDanhBoHoanThanh;

    public static final String FILE_UPLOAD_URL = "http://103.74.117.51/AndroidFileUpload/fileUpload.php";
    private ProgressBar progressUploading;
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "DocSoTanHoa";
    private int progressStatus = 0;
    private TextView txtProgressStatus;
    private Uploading uploading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_doc_so);


        txtComplete = (TextView) findViewById(R.id.txt_qlds_tienTrinh);
        localDatabase = new LocalDatabase(this);
        QuanLyDocSoActivity.this.uploading = new Uploading();
        this.progressUploading = (ProgressBar) findViewById(R.id.progressUploading);
        this.txtProgressStatus = (TextView) findViewById(R.id.txtProgressStatus);
        editTextSearch = (EditText) findViewById(R.id.etxt_qlds_search);
        gridView = (GridView) findViewById(R.id.grid_qlds_danhSachDocSo);
        danhBo_chiSoMois = localDatabase.getAllDanhBo_CSM();
        this.mSumDanhBo = danhBo_chiSoMois.size();

        m_MLT_TongDanhBo = localDatabase.getAllMLT();
        for (HashMap.Entry<String, Integer> entry : this.m_MLT_TongDanhBo.entrySet()) {
            this.mSumDanhBo += entry.getValue();
        }
        QuanLyDocSoActivity.this.mDanhBoHoanThanh = danhBo_chiSoMois.size();
        QuanLyDocSoActivity.this.txtComplete.setText(QuanLyDocSoActivity.this.mDanhBoHoanThanh + "/" + QuanLyDocSoActivity.this.mSumDanhBo);
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


    }

    public void doLayLoTrinh(View v) {
        Intent intent = new Intent(QuanLyDocSoActivity.this, LayLoTrinhActivity.class);
        startActivity(intent);
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

    public void doUpLoad(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDocSoActivity.this);
        builder.setTitle("Đồng bộ " + this.mSumDanhBo + " danh bộ?");
        builder.setMessage("Dữ liệu sau khi đồng bộ sẽ không thể chỉnh sửa!")
                .setCancelable(true)
                .setPositiveButton("Đồng bộ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        upLoadData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //TODO: progress bar
    private void upLoadData() {
        new UploadingAsync().execute();
//        boolean isFinished = false;
//
//        while (!isFinished) {
//            if (uploading.isConnected()) {
//
//                final List<DanhBo_ChiSoMoi> danhBo_chiSoMois = QuanLyDocSoActivity.this.localDatabase.getAllDanhBo_CSM();
//                final int total = danhBo_chiSoMois.size();
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDocSoActivity.this);
//                builder.setTitle("Tải dữ liệu lên server");
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogLayout = inflater.inflate(R.layout.progress_horizontal_uploading, null);
//
//
//                dialog.setView(dialogLayout);
//
//                dialog.show();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        QuanLyDocSoActivity.this.progressUploading.setMax(total);
//
//                        for (DanhBo_ChiSoMoi danhBo_chiSoMoi : danhBo_chiSoMois) {
////                            isValid = uploading.update(danhBo_chiSoMoi);
//
//                            QuanLyDocSoActivity.this.progressUploading.setProgress(QuanLyDocSoActivity.this.progressStatus);
//
//                            QuanLyDocSoActivity.this.txtProgressStatus.setText(progressStatus + "/" + total);
//
//                            QuanLyDocSoActivity.this.progressStatus += 1;
//                            // refresh gridview
//
//                        }
//                        QuanLyDocSoActivity.this.progressStatus = 0;
//                        uploading.disConnect();
//                    }
//                });
//            }
//        }
    }


    class UploadingAsync extends AsyncTask<String, Boolean, Void> {
        private ProgressDialog dialog;

        public UploadingAsync() {
            this.dialog = new ProgressDialog(QuanLyDocSoActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Đang đồng bộ dữ liệu đọc số...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            Boolean isValid = false;
            QuanLyDocSoActivity.this.uploading.connect();

            for (int i = 0; i < QuanLyDocSoActivity.this.danhBo_chiSoMois.size(); i++) {
                DanhBo_ChiSoMoi danhBo_chiSoMoi = QuanLyDocSoActivity.this.danhBo_chiSoMois.get(i);
//                uploading.update(danhBo_chiSoMoi);
                uploading.add(danhBo_chiSoMoi);
                danhBo_chiSoMois.remove(danhBo_chiSoMoi);
                QuanLyDocSoActivity.this.da.removeItem(danhBo_chiSoMoi.getMaLoTrinh());
                i--;
                QuanLyDocSoActivity.this.localDatabase.deleteDanhBo_CSM(danhBo_chiSoMoi);
            }
            if (QuanLyDocSoActivity.this.localDatabase.getAllDanhBo_CSM().size() == 0)
                isValid = true;

            QuanLyDocSoActivity.this.uploading.disConnect();
            publishProgress(isValid);
            return null;

        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            boolean isValid = values[0];
            if (isValid) {
                QuanLyDocSoActivity.this.da.notifyDataSetChanged();
                Toast.makeText(QuanLyDocSoActivity.this, "Đồng bộ thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(QuanLyDocSoActivity.this, "Đồng bộ thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
