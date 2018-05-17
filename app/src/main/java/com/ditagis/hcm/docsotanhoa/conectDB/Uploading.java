package com.ditagis.hcm.docsotanhoa.conectDB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
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
    @SuppressLint("SimpleDateFormat")
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private Connection cnn = ConnectionDB.getInstance().getConnection();
    private String mKy, mNam;
    private Context mContext;
//    public String getmKy() {
//        return mKy;
//    }
//
//    public void setmKy(int mKy) {
//        this.mKy = mKy + "";
//        if (mKy < 10)
//            this.mKy = "0" + mKy;
//    }

//    public String getmNam() {
//        return mNam;
//    }
//
//    public void setmNam(int mNam) {
//        this.mNam = mNam + "";
//    }
//
//    public Context getmContext() {
//        return mContext;
//    }
//
//    public void setmContext(Context mContext) {
//        this.mContext = mContext;
//    }

    public Uploading() {

    }

    public Uploading(int ky, int nam, Context context) {
        this.mKy = ky + "";
        if (ky < 10)
            this.mKy = "0" + ky;
        this.mNam = nam + "";
        mContext = context;
    }

    public Boolean update(List<HoaDon> hoaDons) {
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            Statement stmt = cnn.createStatement();
//            cnn.setAutoCommit(false);
//            stmt.addBatch(this.SQL_CREATE_TEMP_TABLE);
//            stmt.executeBatch();
//            cnn.commit();
            int isCreate = stmt.executeUpdate(mContext.getString(R.string.sql_create_temp_table));

            if (isCreate < 0)
                return false;
            PreparedStatement st = cnn.prepareStatement(mContext.getString(R.string.sql_insert_temp_table));
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
                            PreparedStatement statement = cnn.prepareStatement(mContext.getString(R.string.sql_getngaykiem_thongbao));
                            statement.setString(1, hoaDon.getDanhBo());
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
                        } catch (Exception ignored) {

                        }
                    }
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
            int[] result = st.executeBatch();
            int count = 0;
            for (Integer i : result)
                count += i;
            if (hoaDons.size() != count)
                return false;


            cnn.commit();


            cnn.setAutoCommit(true);
            int update = stmt.executeUpdate(mContext.getString(R.string.sql_update_temp_table));
//            int[] result1 = stmt.executeBatch();
//            cnn.commit();


            if (update != count)
                return false;

        } catch (SQLException e) {
            Log.i("", e.toString());
            return false;
        } catch (ParseException e) {
            Log.i("", e.toString());
            return false;
        }
        return true;
    }


    private int updateLocation(HoaDon hoaDon) {

        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            PreparedStatement st = cnn.prepareStatement(mContext.getString(R.string.sql_update_giamsat));
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

    private int addLocation(HoaDon hoaDon) {
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;

            PreparedStatement st1 = cnn.prepareStatement(mContext.getString(R.string.sql_insert_giamsat));
            st1.setString(1, hoaDon.getId());
            st1.setString(2, hoaDon.getMaLoTrinh().substring(2, 4));
            Location location = LocalDatabase.getInstance(mContext).getLocation(hoaDon.getId());
            st1.setDouble(3, location.getLongtitue());
            st1.setDouble(4, location.getLatitude());
            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate);
            st1.setTimestamp(5, new java.sql.Timestamp(date.getTime()));


//            Statement stmt = cnn.createStatement();
//            sql = String.format(Locale.US, "insert into " + TABLE_NAME_GIAM_SAT + " (id, nhanvien, longtitude, latitude, thoigian) values('%s','%s',%f,%f,'%s')",
//                    hoaDon.getId(), hoaDon.getMaLoTrinh().substring(2, 4), location.getLongtitue(), location.getLatitude(),
//                    stringDate);
            int result = st1.executeUpdate();
//            int result = st1.executeUpdate();
            st1.close();
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
            for (String aChildren : children)
                new File(dir, aChildren).delete();
        }
        try {
            if (addLocation(hoaDon) > 0 || updateLocation(hoaDon) > 0) {
                if (hoaDon.getImage_byteArray().length > CONSTANT.MIN_IMAGE_QUATITY)
                    if (addHinhDHN(hoaDon) > 0 || updateHinhDHN(hoaDon) > 0)
                        return true;
            }

//            if (resultAddImage > 0) {
//                resultUpdateHoaDon = update(hoaDon);
//            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private int updateHinhDHN(HoaDon hoaDon) {

        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            PreparedStatement st = cnn.prepareStatement(mContext.getString(R.string.sql_update_hinhdhn));
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
//        final double tienNuoc = Calculate_TienNuoc.getInstance().calculate(Integer.parseInt(hoaDon.getTieuThuMoi()), hoaDon.getGiaBieu(),
//                hoaDon.getDinhMuc(), hoaDon.getSh(), hoaDon.getSx(), hoaDon.getDv(), hoaDon.getHc());
//        //TODO: cập nhật chỉ số cũ = chỉ số mới
//        try {
//            cnn = ConnectionDB.getInstance().getConnection();
//            if (cnn == null)
//                return false;
//            PreparedStatement st = cnn.prepareStatement(mContext.getString(R.string.sql_update_docso));
//            st.setString(1, hoaDon.getChiSoMoi());
//            String codeMoi = hoaDon.getCodeMoi();
//            String codeCu = hoaDon.getCode_CSC_SanLuong().getCode1();
//            if (hoaDon.getCodeMoi().startsWith("4")) {
//                if (codeCu.startsWith("F") || codeCu.equals("K")
//                        || codeCu.equals("N") || codeCu.startsWith("6")) {
//                    if (!(5 + codeCu.substring(0, 1)).equals("54"))
//                        codeMoi = 5 + codeCu.substring(0, 1);
////                } else if (codeCu.startsWith("6")) {
////                    codeMoi = "56";
//                } else if (codeCu.equals("M0")) {
//                    try {
//                        PreparedStatement statement = cnn.prepareStatement("select top 1 ngaykiem from thongbao where danhba = '" + hoaDon.getDanhBo() + "' order by ngaykiem desc");
//                        ResultSet resultSet = statement.executeQuery();
//                        if (resultSet.next()) {
//                            Date ngayKiem = resultSet.getDate(1);
//                            long date = (Calendar.getInstance().getTimeInMillis() - ngayKiem.getTime()) / (1000 * 60 * 60 * 24);
//                            if (date < 32)
//                                codeMoi = "M1";
//                            else if (date < 62)
//                                codeMoi = "M2";
//                            else
//                                codeMoi = "M3";
//
//                            resultSet.close();
//                            statement.close();
//                        }
//                    } catch (Exception ignored) {
//
//                    }
//                }
//                //do nothing;
//            }
////            if (Integer.parseInt(hoaDon.getTieuThuMoi()) < 0) {
////                codeMoi = "N1";
////                hoaDon.setTieuThuMoi("0");
////            }
//            st.setString(2, codeMoi);
//            st.setString(3, hoaDon.getGhiChu());
//            st.setString(4, hoaDon.getTieuThuMoi());
//            String stringDate = hoaDon.getThoiGian();
//            Date date = Uploading.this.formatter.parse(stringDate); //TODO datetime
//            st.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
//            st.setString(6, hoaDon.getSdt());
//            st.setString(7, hoaDon.getViTri());
//            st.setDouble(8, tienNuoc);
//            double BVMT = 0, VAT = tienNuoc / 20;
//            if (!hoaDon.getGiaBieu().equals("52"))
//                BVMT = tienNuoc / 10;
//            st.setDouble(9, BVMT);
//            st.setDouble(10, VAT);
//            st.setDouble(11, tienNuoc + BVMT + VAT);
//            st.setString(12, LocalDatabase.getInstance(mContext).getTTDHN(hoaDon.getCodeMoi()));
//            st.setString(13, this.mNam + this.mKy + hoaDon.getDanhBo());
//
//            int result1 = st.executeUpdate();
//            String sqlKH = "SELECT so from " + TABLE_NAME_KH + " WHERE DANHBa=? ";
//            PreparedStatement stselectKH = cnn.prepareStatement(sqlKH);
//            stselectKH.setString(1, hoaDon.getDanhBo());
//            ResultSet rs = stselectKH.executeQuery();
//
//            while (rs.next()) {
//                if (!rs.getString(1).equals(hoaDon.getSoNha())) {
//                    sqlKH = "UPDATE " + TABLE_NAME_KH + " SET somoi =?, duong = ? WHERE DANHBa=? ";
//                    PreparedStatement stUpdateKH = cnn.prepareStatement(sqlKH);
//                    stUpdateKH.setString(1, hoaDon.getSoNha());
//                    stUpdateKH.setString(2, hoaDon.getDuong());
//                    stUpdateKH.setString(3, hoaDon.getDanhBo());
//                    stUpdateKH.executeUpdate();
//                    stUpdateKH.close();
//                }
//            }
//            stselectKH.close();
//
//            st.close();
//
//
//            return result1 > 0;
//
//        } catch (SQLException ignored) {
//        } catch (ParseException ignored) {
//        }
        return false;
    }

    private int addHinhDHN(HoaDon hoaDon) {
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return 0;
            //Lấy danh sách id
            ResultSet rs;
            List<String> hoadonIDList = new ArrayList<>();
            PreparedStatement st1 = cnn.prepareStatement(mContext.getString(R.string.sql_gethinhdhnid_hinhdhn));
            st1.setString(1, hoaDon.getDanhBo());
            rs = st1.executeQuery();
            while (rs.next()) {
                hoadonIDList.add(rs.getString(1));
            }
            rs.close();
            st1.close();
            //Kiểm tra nếu số lượng lớn hơn 4 thì xóa

            if (hoadonIDList.size() >= 4) {
                st1 = cnn.prepareStatement(mContext.getString(R.string.sql_delete_hinhdhn));
                st1.setString(1, hoadonIDList.get(hoadonIDList.size() - 1));
                st1.setString(2, hoadonIDList.get(hoadonIDList.size() - 1));
                st1.executeUpdate();
            }

            st1 = cnn.prepareStatement(mContext.getString(R.string.sql_insert_hinhdhn));
            st1.setString(1, hoaDon.getId());
            st1.setString(2, hoaDon.getDanhBo());

//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Bitmap bit = BitmapFactory.decodeFile(hoaDon.getImage());
//            bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            st1.setBytes(3, hoaDon.getImage_byteArray());

            String stringDate = hoaDon.getThoiGian();
            Date date = Uploading.this.formatter.parse(stringDate);
            st1.setTimestamp(4, new java.sql.Timestamp(date.getTime()));
//            Location location = LocalDatabase.getInstance(mContext).getLocation(hoaDon.getId());
//            st1.setDouble(5, location.getLongtitue());
//            st1.setDouble(6, location.getLatitude());

//                path = path.substring(0, path.length() - 1).concat("1");

//            if (result > 0) {
//                File f = ImageFile.getFile(hoaDon.getThoiGian(), mContext, hoaDon.getDanhBo());
//                f.delete();
//            }
            return st1.executeUpdate();

        } catch (Exception ignored) {
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