package com.ditagis.hcm.docsotanhoa.conectDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.utities.ImageFile;

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

public class Uploading implements IDB<HoaDon, Boolean, String> {
    private final String TABLE_NAME = "HOADON";
    private final String NEW_TABLE_NAME = "HoaDonMoi";
    private final String TABLE_NAME_DOCSO = "DocSo";
    private final String TABLE_NAME_DOCSO_LUUTRU = "DocSoLuuTru";
    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME_DOCSO + " SET CSMOI=?, CODEMoi=?, GhiChuDS=?, tieuthumoi =?, gioghi = ?, sdt = ? WHERE DANHBa=? and dot = ? and ky = ? and nam = ?";
    private final String SQL_INSERT_LUUTRU = "INSERT INTO " + TABLE_NAME_DOCSO_LUUTRU + " VALUES (?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?)";
    private final String TABLE_NAME_HINHDHN = "HinhDHN_DocSo";
    private final String SQL_INSERT_HINHDHN = "INSERT INTO " + TABLE_NAME_HINHDHN + " VALUES(?,?,?,?,?,?)";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE ClassId=?";
    private final String SQL_INSERT = "INSERT INTO " + NEW_TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?)";
    private Connection cnn = ConnectionDB.getInstance().getConnection();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String mDot, mKy, mNam;
    private Context mContext;

    public Uploading(int dot, int ky, int nam, Context context) {
        this.mDot = dot + "";
        if (dot < 10)
            this.mDot = "0" + dot;
        this.mKy = ky + "";
        if (ky < 10)
            this.mKy = "0" + ky;
        this.mNam = nam + "";
        mContext = context;
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
    public Boolean add(HoaDon hoaDon) {


//            int result = addDocSoLuuTru(hoaDon);

        boolean result = update(hoaDon);
        int result1 = 1;
        if (!hoaDon.getImage().equals("null"))
            result1 = addHinhDHN(hoaDon);
        return result && result1 > 0;
//return result > 0;


    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(HoaDon hoaDon) {
        String sql = this.SQL_UPDATE;

        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return false;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, hoaDon.getChiSoMoi());
            st.setString(2, hoaDon.getCodeMoi());
            st.setString(3, hoaDon.getGhiChu());
            st.setString(4, hoaDon.getTieuThuMoi());
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
            st.setString(6, hoaDon.getSdt());
            st.setString(7, hoaDon.getDanhBo());
            st.setString(8, hoaDon.getDot());
            st.setString(9, this.mKy);
            st.setString(10, this.mNam);
            int result1 = st.executeUpdate();
            st.close();


            return result1 > 0;

        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int addDocSoLuuTru(HoaDon hoaDon) {
        String sql = this.SQL_INSERT_LUUTRU;

        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, this.mNam + this.mKy + hoaDon.getDanhBo());
            st.setString(2, hoaDon.getDanhBo());
            st.setString(3, hoaDon.getMaLoTrinh());
            st.setString(4, hoaDon.getMaLoTrinh());
            st.setString(5, hoaDon.getSoNha());
            st.setString(6, "");
            st.setString(7, hoaDon.getDuong());
            st.setString(8, hoaDon.getSdt());
            st.setString(9, hoaDon.getGiaBieu());
            st.setString(10, hoaDon.getDinhMuc());
            st.setString(11, this.mNam);
            st.setString(12, this.mKy);
            st.setString(13, this.mDot);
            st.setString(14, hoaDon.getMaLoTrinh().substring(2, 4));
            st.setString(15, "");
            st.setString(16, "");
            st.setString(17, hoaDon.getChiSoCu());
            st.setString(18, hoaDon.getChiSoMoi());
            st.setString(19, hoaDon.getCode_CSC_SanLuong().getCode1());
            st.setString(20, hoaDon.getCodeMoi());
            st.setString(21, "");
            st.setString(22, "");
            st.setString(23, hoaDon.getCode_CSC_SanLuong().getSanLuong1());
            int tieuThuMoi = Integer.parseInt(hoaDon.getChiSoMoi()) - Integer.parseInt(hoaDon.getChiSoCu());
            st.setString(24, tieuThuMoi + "");
            for (int i = 25; i <= 55; i++)
                st.setString(i, "");
            st.setString(56, hoaDon.getGhiChu());
            for (int i = 57; i <= 62; i++)
                st.setString(i, "");
            int result = st.executeUpdate();
            st.close();
            return result;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return 0;
    }

    private int addHinhDHN(HoaDon hoaDon) {
        String sqlInsert_HinhDHN = this.SQL_INSERT_HINHDHN;
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            PreparedStatement st1 = cnn.prepareStatement(sqlInsert_HinhDHN, Statement.RETURN_GENERATED_KEYS);

            st1.setString(1, hoaDon.getDanhBo());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap bit = BitmapFactory.decodeFile(hoaDon.getImage());
            bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            st1.setBytes(2, outputStream.toByteArray());

            st1.setString(3, "0.0");
            st1.setString(4, "0.0");
            st1.setString(5, "0");
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st1.setTimestamp(6, new java.sql.Timestamp(date.getTime()));
            int result = st1.executeUpdate();
            if (result > 0) {
                File f = ImageFile.getFile(hoaDon.getThoiGian(), mContext, hoaDon.getDanhBo());
                f.delete();
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public HoaDon find(String s) {
        return null;
    }

    @Override
    public List<HoaDon> getAll() {
        return null;
    }

}
