package com.ditagis.hcm.docsotanhoa.conectDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ThanLe on 15/10/2017.
 */

public class Uploading implements IDB<DanhBo_ChiSoMoi, Boolean, String> {
    private final String TABLE_NAME = "HOADON";
    private final String NEW_TABLE_NAME = "HoaDonMoi";
    private final String TABLE_NAME_DOCSO = "DocSo";
    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME_DOCSO + " SET CSMOI=?, CODEMoi=?, GhiChuDS=? WHERE DANHBa=? and dot = ? and ky = ? and nam = ?";
    private final String TABLE_NAME_HINHDHN = "HinhDHN";
    private final String SQL_INSERT_HINHDHN = "INSERT INTO " + TABLE_NAME_HINHDHN + " VALUES(?,?,?,?,?,?)";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE ClassId=?";
    private final String SQL_INSERT = "INSERT INTO " + NEW_TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?)";
    private Connection cnn = ConnectionDB.getInstance().getConnection();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String mDot, mKy, mNam;

    public Uploading(int dot, int ky, int nam) {
        this.mDot = dot + "";
        if (dot < 10)
            this.mDot = "0" + dot;
        this.mKy = ky + "";
        if (ky < 10)
            this.mKy = "0" + ky;
        this.mNam = nam + "";
    }

    public void setmDot(int dot) {
        this.mDot = dot + "";
        if (dot < 10)
            this.mDot = "0" + dot;
    }

    @NonNull
    private Boolean createTable() {
        Statement statement = null;
        try {
            statement = cnn.createStatement();
            String sql = "CREATE TABLE " + NEW_TABLE_NAME +//12
                    "(Danhbo VARCHAR(15) not NULL," +
                    "MaLT VARCHAR(5) not NULL," +
                    "TenKH VARCHAR(100) not NULL," +
                    "DiaChi VARCHAR(50) not NULL," +
                    "SDT VARCHAR(15) ," +
                    "CSCu VARCHAR(5) not NULL," +
                    "CSMoi VARCHAR(5) not NULL," +
                    "Code VARCHAR(2) not NULL," +
                    "GhiChu NVARCHAR(255) not NULL," +
                    "HinhAnh IMAGE not NULL," +
                    "PRIMARY KEY (Danhbo))";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Boolean add(DanhBo_ChiSoMoi danhBo_chiSoMoi) {
        String sql = this.SQL_INSERT;

        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, danhBo_chiSoMoi.getDanhBo());
            st.setString(2, danhBo_chiSoMoi.getMaLoTrinh());
            st.setString(3, danhBo_chiSoMoi.getTenKH());
            st.setString(4, danhBo_chiSoMoi.getDiaChi());
            st.setString(5, danhBo_chiSoMoi.getSdt());
            st.setString(6, danhBo_chiSoMoi.getChiSoCu());
            st.setString(7, danhBo_chiSoMoi.getChiSoMoi());
            st.setString(8, danhBo_chiSoMoi.getCode());
            st.setString(9, danhBo_chiSoMoi.getNote());


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Bitmap bit = BitmapFactory.decodeFile(danhBo_chiSoMoi.getImage());
            bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            st.setBytes(10, outputStream.toByteArray());

            String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
            File outFile = new File(path, "DocSoTanHoa");
            String fileName = danhBo_chiSoMoi.getImage().substring(outFile.getAbsolutePath().length() + 1).split("\\.")[0];
            st.setString(11, fileName);

            boolean result = st.execute();
            st.close();
            return result;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(DanhBo_ChiSoMoi danhBo_chiSoMoi) {
        String sql = this.SQL_UPDATE;
        String sqlInsert_HinhDHN = this.SQL_INSERT_HINHDHN;
        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, danhBo_chiSoMoi.getChiSoMoi());
            st.setString(2, danhBo_chiSoMoi.getCode());
            st.setString(3, danhBo_chiSoMoi.getNote());
            st.setString(4, danhBo_chiSoMoi.getDanhBo());
            st.setString(5, danhBo_chiSoMoi.getDot());
            st.setString(6, this.mKy);
            st.setString(7, this.mNam);
            int result1 = st.executeUpdate();
            st.close();


            PreparedStatement st1 = cnn.prepareStatement(sqlInsert_HinhDHN, Statement.RETURN_GENERATED_KEYS);

            st1.setString(1, danhBo_chiSoMoi.getDanhBo());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Bitmap bit = BitmapFactory.decodeFile(danhBo_chiSoMoi.getImage());
            bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            st1.setBytes(2, outputStream.toByteArray());

            st1.setString(3, "0.0");
            st1.setString(4, "0.0");
            st1.setString(5, "0");
            String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
            File outFile = new File(path, "DocSoTanHoa");
            String fileName = danhBo_chiSoMoi.getImage().substring(outFile.getAbsolutePath().length() + 1).split("\\.")[0];
            String stringDate = fileName.substring(0, 19);
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st1.setTimestamp(6, new java.sql.Timestamp(date.getTime()));
            int result2 = st1.executeUpdate();
            return result1 > 0 && result2 > 0;

        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public DanhBo_ChiSoMoi find(String s) {
        return null;
    }

    @Override
    public List<DanhBo_ChiSoMoi> getAll() {
        return null;
    }

}
