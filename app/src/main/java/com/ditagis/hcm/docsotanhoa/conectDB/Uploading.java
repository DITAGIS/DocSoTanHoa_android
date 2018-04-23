package com.ditagis.hcm.docsotanhoa.conectDB;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.Location;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;
import com.ditagis.hcm.docsotanhoa.utities.CONSTANT;
import com.ditagis.hcm.docsotanhoa.utities.Calculate_TienNuoc;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ThanLe on 15/10/2017.
 */

public class Uploading implements IDB<HoaDon, Boolean, String> {
    private final String TABLE_NAME = "HOADON";
    private final String NEW_TABLE_NAME = "HoaDonMoi";
    private String TABLE_NAME_DOCSO = "DocSo1";
    private final String TABLE_NAME_DOCSO1 = "DocSo20180215";
    private final String TABLE_NAME_KH = "KhachHang";
    private final String TABLE_NAME_DOCSO_LUUTRU = "DocSoLuuTru";
    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;

    private String SQL_CREATE_TEMP_TABLE = "if OBJECT_ID('tempdb.dbo.#docsotemp') is not null drop table #docsotemp; " +
            "select docsoid, csmoi, codemoi, ghichuds, tieuthumoi, gioghi, sdt, vitrimoi, tiennuoc, bvmt, thue, " +
            "tongtien, ttdhnmoi,sonhamoi,duong into #docsotemp from docso where 1 = 0;";

    private String SQL_INSERT_TEMP_TABLE = "insert into #docsotemp (csmoi, codemoi, ghichuds, tieuthumoi, gioghi, sdt, vitrimoi, tiennuoc, bvmt, thue, tongtien, ttdhnmoi,sonhamoi,duong,docsoid) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private String SQL_UPDATE_TEMP_TABLE = "update ds " +
            "set ds.csmoi = temp.csmoi, ds.codemoi = temp.codemoi, ds.ghichuds = temp.ghichuds, ds.tieuthumoi = temp.tieuthumoi," +
            "ds.gioghi = temp.gioghi, ds.sdt = temp.sdt, ds.vitrimoi = temp.vitrimoi, ds.tiennuoc = temp.tiennuoc, ds.bvmt = temp.bvmt," +
            "ds.thue = temp.thue, ds.tongtien = temp.tongtien, ds.ttdhnmoi = temp.ttdhnmoi from " + TABLE_NAME_DOCSO + " ds" +
            " inner join #docsotemp temp on ds.DocSoID = temp.DocSoID;" +
            "update kh set kh.somoi = temp.sonhamoi, kh.duong = temp.duong from " + TABLE_NAME_KH + " kh inner join #docsotemp temp on kh.danhba = right(temp.docsoid,11)";

    private String SQL_UPDATE = "UPDATE top (1)" + TABLE_NAME_DOCSO + " SET CSMOI=?, CODEMoi=?, GhiChuDS=?, tieuthumoi =?, gioghi = ?, sdt = ?, vitrimoi = ?,tiennuoc = ?, bvmt = ?, thue = ?, tongtien = ?, ttdhnmoi=? WHERE docsoId = ? ";

    private final String SQL_UPDATE_KH = "UPDATE " + TABLE_NAME_KH + " SET somoi =?, duong = ? WHERE DANHBa=? ";
    private final String SQL_SELECT_KH = "SELECT so from " + TABLE_NAME_KH + " WHERE DANHBa=? ";
    private final String SQL_INSERT_LUUTRU = "INSERT INTO " + TABLE_NAME_DOCSO_LUUTRU + " VALUES (?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?)";

    private final String TABLE_NAME_GIAM_SAT = "GiamSatHanhTrinh";
    private final String SQL_INSERT_GIAM_SAT = "insert into " + TABLE_NAME_GIAM_SAT + " (id, nhanvien, longtitude, latitude, thoigian) values(?,?,?,?,?)";
    private final String SQL_UPDATE_GIAM_SAT = "update " + TABLE_NAME_GIAM_SAT + " set longtitude =?, latitude =?, thoigian =? where id =?";
    private final String TABLE_NAME_HINHDHN = "DocsoTh_Hinh..HinhDHN";//(Danhbo, Image, Latitude, Longitude, CreateBy, CreateDate)
    private final String SQL_INSERT_HINHDHN = " INSERT INTO " + TABLE_NAME_HINHDHN + " VALUES(?,?,?,?)  ";
    private final String SQL_UPDATE_HINHDHN = " update " + TABLE_NAME_HINHDHN + " set Image = ?, CreateDate =?  " +
            "    where hinhdhnid = ?";

    private final String SQL_DELETE = "if exists (select danhbo from " + TABLE_NAME_HINHDHN + " where hinhdhnid = ?)" +
            " delete from " + TABLE_NAME_HINHDHN + " where hinhdhnid = ?";
    private final String SQL_INSERT = "INSERT INTO " + NEW_TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?)";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat formatCalculateDate = new SimpleDateFormat("dd MM yyyy");
    private Connection cnn = ConnectionDB.getInstance().getConnection();
    private String mKy, mNam;
    private Context mContext;


    public String getmKy() {
        return mKy;
    }

    public void setmKy(int mKy) {
        this.mKy = mKy + "";
        if (mKy < 10)
            this.mKy = "0" + mKy;
    }

    public String getmNam() {
        return mNam;
    }

    public void setmNam(int mNam) {
        this.mNam = mNam + "";
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Uploading() {

    }

    public Uploading(int ky, int nam, Context context) {
        this.mKy = ky + "";
        if (ky < 10)
            this.mKy = "0" + ky;
        this.mNam = nam + "";
        mContext = context;
    }

    @NonNull
    private Boolean createTable() {

        return true;
    }

    public Boolean update(List<HoaDon> hoaDons) {
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            Statement stmt = cnn.createStatement();
            cnn.setAutoCommit(false);
            stmt.addBatch(this.SQL_CREATE_TEMP_TABLE);
            stmt.executeBatch();
            cnn.commit();

            PreparedStatement st = cnn.prepareStatement(this.SQL_INSERT_TEMP_TABLE);
            cnn.setAutoCommit(false);
            for (HoaDon hoaDon : hoaDons) {
                final double tienNuoc = Calculate_TienNuoc.getInstance().calculate(Integer.parseInt(hoaDon.getTieuThuMoi()), hoaDon.getGiaBieu(),
                        hoaDon.getDinhMuc(), hoaDon.getSh(), hoaDon.getSx(), hoaDon.getDv(), hoaDon.getHc());

                st.setString(1, hoaDon.getChiSoMoi());
                String codeMoi = hoaDon.getCodeMoi();
                String codeCu = hoaDon.getCode_CSC_SanLuong().getCode1();
                if (hoaDon.getCodeMoi().startsWith("4")) {
                    if (codeCu.startsWith("F") || codeCu.equals("K")
                            || codeCu.equals("N") || codeCu.startsWith("6")) {
                        if (!(5 + codeCu.substring(0, 1)).equals("54"))
                            codeMoi = 5 + codeCu.substring(0, 1);
//                } else if (codeCu.startsWith("6")) {
//                    codeMoi = "56";
                    } else if (codeCu.equals("M0")) {
                        try {
                            PreparedStatement statement = cnn.prepareStatement("select top 1 ngaykiem from thongbao where danhba = '" + hoaDon.getDanhBo() + "' order by ngaykiem desc");
                            ResultSet resultSet = statement.executeQuery();
                            if (resultSet.next()) {
                                Date ngayKiem = resultSet.getDate(1);
                                long date = (Calendar.getInstance().getTimeInMillis() - ngayKiem.getTime()) / (1000 * 60 * 60 * 24);
                                if (date < 32)
                                    codeMoi = "M1";
                                else if (date < 62)
                                    codeMoi = "M2";
                                else
                                    codeMoi = "M3";

                                resultSet.close();
                                statement.close();
                            }
                        } catch (Exception e) {

                        }
                    } else ;
                    //do nothing;
                }
//            if (Integer.parseInt(hoaDon.getTieuThuMoi()) < 0) {
//                codeMoi = "N1";
//                hoaDon.setTieuThuMoi("0");
//            }
                st.setString(2, codeMoi);
                st.setString(3, hoaDon.getGhiChu());
                st.setString(4, hoaDon.getTieuThuMoi());
                String stringDate = hoaDon.getThoiGian();
                Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
                st.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
                st.setString(6, hoaDon.getSdt());
                st.setString(7, hoaDon.getViTri());
                st.setDouble(8, tienNuoc);
                double BVMT = 0, VAT = tienNuoc / 20;
                if (!hoaDon.getGiaBieu().equals("52"))
                    BVMT = tienNuoc / 10;
                st.setDouble(9, BVMT);
                st.setDouble(10, VAT);
                st.setDouble(11, tienNuoc + BVMT + VAT);
                st.setString(12, LocalDatabase.getInstance(mContext).getTTDHN(hoaDon.getCodeMoi()));
                st.setString(13, hoaDon.getSoNha());
                st.setString(14, hoaDon.getDuong());
                st.setString(15, this.mNam + this.mKy + hoaDon.getDanhBo());
                st.addBatch();
            }
            int[] count = st.executeBatch();
            cnn.commit();

            cnn.setAutoCommit(false);
            stmt.addBatch(this.SQL_UPDATE_TEMP_TABLE);
            stmt.executeBatch();
            cnn.commit();
            return true;
        } catch (SQLException e) {
            Log.i("", e.toString());
        } catch (ParseException e) {
            Log.i("", e.toString());
        }
        return false;
    }

    public int updateLocation(HoaDon hoaDon) {
        String sql = this.SQL_UPDATE_GIAM_SAT;

        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            PreparedStatement st = cnn.prepareStatement(sql);
            Location location = LocalDatabase.getInstance(mContext).getLocation(hoaDon.getId());
            st.setDouble(1, location.getLongtitue());
            st.setDouble(2, location.getLatitude());
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
            st.setString(4, hoaDon.getId());
            int result1 = st.executeUpdate();


            st.close();


            return result1;

        } catch (Exception e1) {
            Log.i("Lỗi update hình", e1.toString());

        }
        return 0;
    }

    public int addLocation(HoaDon hoaDon) {
        String sql = this.SQL_INSERT_GIAM_SAT;
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;

            PreparedStatement st1 = cnn.prepareStatement(sql);
            st1.setString(1, hoaDon.getId());
            st1.setString(2, hoaDon.getMaLoTrinh().substring(2,4));
            Location location = LocalDatabase.getInstance(mContext).getLocation(hoaDon.getId());
            st1.setDouble(3, location.getLongtitue());
            st1.setDouble(4, location.getLatitude());
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st1.setTimestamp(5, new java.sql.Timestamp(date.getTime()));

            int result = st1.executeUpdate();

            return result;

        } catch (Exception e) {
            Log.i("Lỗi: ", e.toString());
        }
        return 0;
    }

    @Override
    public Boolean add(HoaDon hoaDon) {
        String path = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(path, mContext.getString(R.string.path_saveImage));
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
        boolean resultUpdateHoaDon = false;
        int resultAddImage = 0;
        try {

            if (hoaDon.getImage_byteArray().length > CONSTANT.MIN_IMAGE_QUATITY)
                resultAddImage = addHinhDHN(hoaDon);
            if (resultAddImage <= 0) {
                resultAddImage = updateHinhDHN(hoaDon);
            }
//            if (resultAddImage > 0) {
//                resultUpdateHoaDon = update(hoaDon);
//            }
        } catch (Exception e) {
        }
        return resultAddImage > 0;
    }

    public int updateHinhDHN(HoaDon hoaDon) {
        String sql = this.SQL_UPDATE_HINHDHN;

        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setBytes(1, hoaDon.getImage_byteArray());
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
            st.setString(3, hoaDon.getId());
            int result1 = st.executeUpdate();


            st.close();


            return result1;

        } catch (Exception e1) {
            Log.i("Lỗi update hình", e1.toString());

        }
        return 0;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(HoaDon hoaDon) {
//        TABLE_NAME_DOCSO += mNam + mKy + mDot;
        String sql = this.SQL_UPDATE;
        final double tienNuoc = Calculate_TienNuoc.getInstance().calculate(Integer.parseInt(hoaDon.getTieuThuMoi()), hoaDon.getGiaBieu(),
                hoaDon.getDinhMuc(), hoaDon.getSh(), hoaDon.getSx(), hoaDon.getDv(), hoaDon.getHc());
        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return false;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, hoaDon.getChiSoMoi());
            String codeMoi = hoaDon.getCodeMoi();
            String codeCu = hoaDon.getCode_CSC_SanLuong().getCode1();
            if (hoaDon.getCodeMoi().startsWith("4")) {
                if (codeCu.startsWith("F") || codeCu.equals("K")
                        || codeCu.equals("N") || codeCu.startsWith("6")) {
                    if (!(5 + codeCu.substring(0, 1)).equals("54"))
                        codeMoi = 5 + codeCu.substring(0, 1);
//                } else if (codeCu.startsWith("6")) {
//                    codeMoi = "56";
                } else if (codeCu.equals("M0")) {
                    try {
                        PreparedStatement statement = cnn.prepareStatement("select top 1 ngaykiem from thongbao where danhba = '" + hoaDon.getDanhBo() + "' order by ngaykiem desc");
                        ResultSet resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            Date ngayKiem = resultSet.getDate(1);
                            long date = (Calendar.getInstance().getTimeInMillis() - ngayKiem.getTime()) / (1000 * 60 * 60 * 24);
                            if (date < 32)
                                codeMoi = "M1";
                            else if (date < 62)
                                codeMoi = "M2";
                            else
                                codeMoi = "M3";

                            resultSet.close();
                            statement.close();
                        }
                    } catch (Exception e) {

                    }
                } else ;
                //do nothing;
            }
//            if (Integer.parseInt(hoaDon.getTieuThuMoi()) < 0) {
//                codeMoi = "N1";
//                hoaDon.setTieuThuMoi("0");
//            }
            st.setString(2, codeMoi);
            st.setString(3, hoaDon.getGhiChu());
            st.setString(4, hoaDon.getTieuThuMoi());
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
            st.setString(6, hoaDon.getSdt());
            st.setString(7, hoaDon.getViTri());
            st.setDouble(8, tienNuoc);
            double BVMT = 0, VAT = tienNuoc / 20;
            if (!hoaDon.getGiaBieu().equals("52"))
                BVMT = tienNuoc / 10;
            st.setDouble(9, BVMT);
            st.setDouble(10, VAT);
            st.setDouble(11, tienNuoc + BVMT + VAT);
            st.setString(12, LocalDatabase.getInstance(mContext).getTTDHN(hoaDon.getCodeMoi()));
            st.setString(13, this.mNam + this.mKy + hoaDon.getDanhBo());

            int result1 = st.executeUpdate();
            String sqlKH = this.SQL_SELECT_KH;
            PreparedStatement stselectKH = cnn.prepareStatement(sqlKH);
            stselectKH.setString(1, hoaDon.getDanhBo());
            ResultSet rs = stselectKH.executeQuery();

            while (rs.next()) {
                if (!rs.getString(1).equals(hoaDon.getSoNha())) {
                    sqlKH = this.SQL_UPDATE_KH;
                    PreparedStatement stUpdateKH = cnn.prepareStatement(sqlKH);
                    stUpdateKH.setString(1, hoaDon.getSoNha());
                    stUpdateKH.setString(2, hoaDon.getDuong());
                    stUpdateKH.setString(3, hoaDon.getDanhBo());
                    stUpdateKH.executeUpdate();
                    stUpdateKH.close();
                }
            }
            stselectKH.close();

            st.close();


            return result1 > 0;

        } catch (SQLException e1) {
        } catch (ParseException e) {
        }
        return false;
    }

    public int addHinhDHN(HoaDon hoaDon) {
        String sqlInsert_HinhDHN = this.SQL_INSERT_HINHDHN;
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            //Lấy danh sách id
            String query = "select HinhDHNID from " + TABLE_NAME_HINHDHN + " where danhbo = '" + hoaDon.getDanhBo() + "'  order by CreateDate desc";
            ResultSet rs = null;
            List<String> hoadonIDList = new ArrayList<>();
            Statement st = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
//            st1.setString(1, hoaDon.getDanhBo());
//            st1.setString(2, hoaDon.getDanhBo());
            rs = st.executeQuery(query);
            while (rs.next()) {
                hoadonIDList.add(rs.getString(1));
            }
            rs.close();
            st.close();
            //Kiểm tra nếu số lượng lớn hơn 12 thì xóa

            PreparedStatement st1;
            if (hoadonIDList.size() >= 12) {
                query = SQL_DELETE;
                st1 = cnn.prepareStatement(query);
                st1.setString(1, hoadonIDList.get(hoadonIDList.size() - 1));
                st1.setString(2, hoadonIDList.get(hoadonIDList.size() - 1));
                st1.executeUpdate();
            }

            st1 = cnn.prepareStatement(sqlInsert_HinhDHN);
            st1.setString(1, hoaDon.getId());
            st1.setString(2, hoaDon.getDanhBo());

//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Bitmap bit = BitmapFactory.decodeFile(hoaDon.getImage());
//            bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            st1.setBytes(3, hoaDon.getImage_byteArray());

            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
            st1.setTimestamp(4, new java.sql.Timestamp(date.getTime()));
            Location location = LocalDatabase.getInstance(mContext).getLocation(hoaDon.getId());
//            st1.setDouble(5, location.getLongtitue());
//            st1.setDouble(6, location.getLatitude());
            int result = st1.executeUpdate();

//                path = path.substring(0, path.length() - 1).concat("1");

//            if (result > 0) {
//                File f = ImageFile.getFile(hoaDon.getThoiGian(), mContext, hoaDon.getDanhBo());
//                f.delete();
//            }
            return result;

        } catch (Exception e) {
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