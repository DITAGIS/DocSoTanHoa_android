package com.ditagis.hcm.docsotanhoa.conectDB;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDB implements IDB<HoaDon, Boolean, String> {
    private final String TABLE_NAME = "HOADON";
    private final String SQL_SELECT = "SELECT ID,KHU,DOT,DANHBO,CULY,HOPDONG,TENKH,SONHA,DUONG,GIABIEU,DINHMUC,KY,NAM,CODE,CODEFU,CSCU,CSMOI,QUAN,PHUONG,MLT FROM " + TABLE_NAME;
    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;
    private final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?)";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET CSC=? WHERE DANHBO=?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE ClassId=?";

    private String mDot;
    private String mStaffname;

    public String getmDot() {
        return mDot;
    }


    public String getmStaffname() {
        return mStaffname;
    }

    //    private final String SQL_FIND = "SELECT * FROM " + TABLE_NAME + " WHERE ID=?";

    @Override
    public Boolean add(HoaDon e) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_INSERT;

        try {
            Statement st = cnn.createStatement();
            int result = st.executeUpdate(sql);
            st.close();
            cnn.close();
            return result > 0;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(String k) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            PreparedStatement pst = cnn.prepareStatement(this.SQL_DELETE);
            pst.setString(1, k);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(HoaDon e) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            CallableStatement cbs = cnn.prepareCall(this.SQL_UPDATE);
            cbs.setString(1, e.getChiSoCu());
            cbs.setString(2, e.getChiSoMoi());
            return cbs.executeUpdate() > 0;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public HoaDon find(String k) {
//		Connection cnn = this.condb.getConnect();
//		HoaDon course = null;
//		try {
//			PreparedStatement pst = cnn.prepareStatement(this.SQL_FIND);
//			pst.setString(1, k);
//			ResultSet rs = pst.executeQuery();
//			if (rs.next()) {
//				String courseName = rs.getString(2);
//				course = new HoaDon(k, courseName);
//			}
//			rs.close();
//			pst.close();
//			cnn.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return course;
        return null;
    }

    @Override
    public List<HoaDon> getAll() {
        List<HoaDon> result = new ArrayList<HoaDon>();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            CallableStatement cal = cnn.prepareCall(this.SQL_SELECT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                String khu = rs.getString(2);
                String dot = rs.getString(3);
                String danhBo = rs.getString(4);
                String cuLy = rs.getString(5);
                String hopDong = rs.getString(6);
                String tenKhachHang = rs.getString(7);
                String soNha = rs.getString(8);
                String duong = rs.getString(9);
                String giaBieu = rs.getString(10);
                String dinhMuc = rs.getString(11);
                String ky = rs.getString(12);
                String nam = rs.getString(13);
                String code = rs.getString(14);
                String codeFU = rs.getString(15);
                String chiSoCu = rs.getString(16);
                String chiSoMoi = rs.getString(17);
                String quan = rs.getString(18);
                String phuong = rs.getString(19);
                String maLoTrinh = rs.getString(20);
                HoaDon course = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
                result.add(course);
            }
            rs.close();
            cal.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public List<String> getAllMaLoTrinh() throws SQLException {
        List<String> result = new ArrayList<String>();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT DISTINCT MLT FROM " + this.TABLE_NAME);
            while (rs.next()) {
                String maLoTrinh = rs.getString(1);
                result.add(maLoTrinh);
            }
            rs.close();
            statement.close();
            cnn.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public List<HoaDon> getByMaLoTrinh(String maLoTrinh) {
        List<HoaDon> result = new ArrayList<HoaDon>();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery(this.SQL_SELECT + " WHERE MLT = '" + maLoTrinh + "'");
            while (rs.next()) {
                int id = rs.getInt(1);
                String khu = rs.getString(2);
                String dot = rs.getString(3);
                String danhBo = rs.getString(4);
                String cuLy = rs.getString(5);
                String hopDong = rs.getString(6);
                String tenKhachHang = rs.getString(7);
                String soNha = rs.getString(8);
                String duong = rs.getString(9);
                String giaBieu = rs.getString(10);
                String dinhMuc = rs.getString(11);
                String ky = rs.getString(12);
                String nam = rs.getString(13);
                String code = rs.getString(14);
                String codeFU = rs.getString(15);
                String chiSoCu = rs.getString(16);
                String chiSoMoi = rs.getString(17);
                String quan = rs.getString(18);
                String phuong = rs.getString(19);

                HoaDon course = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
                result.add(course);
            }
            rs.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return result;
    }

    public List<HoaDon> getAllByUserName(String userName, int dot, int nam, int ky) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<HoaDon> hoaDons = null;
        try {
            if (cnn == null)
                return null;
            Statement statement = cnn.createStatement(), sttm1;
            hoaDons = new ArrayList<HoaDon>();
            String dotString = "";
            if (dot < 10)
                dotString = "0" + dot;
            else dotString = dot + "";
            String like = dotString + userName + "%";
            ResultSet rs = statement.executeQuery("SELECT top 3 * FROM DocSo where nam = "
                    + nam + " and ky = " + ky + " and mlt2 like '" + like + "' and (csmoi is null or csmoi = 0 )");
//                LayLoTrinhActivity.this.mHoaDons = new ArrayList<HoaDon>();
            while (rs.next()) {


//                    int id = rs.getInt(1); //TODO xem vụ tạo DocSoID
                String khu = "";
                String danhBo = rs.getString(2);
                String cuLy = "";
                String hopDong = "";
                String tenKhachHang = "";
                String maLoTrinh = rs.getString(4);
                String soNha = rs.getString(5);
                String duong = rs.getString(7);
                String giaBieu = rs.getString(9);
                String dinhMuc = rs.getString(10);
                String chiSoCu = rs.getInt(17) + ""; // lấy chỉ số mới của kỳ trước
                String chiSoMoi = "";
                String code = "";
                String codeFU = "";

                String quan = "";
                String phuong = "";


                sttm1 = cnn.createStatement();
                ResultSet rs1 = sttm1.executeQuery("SELECT HopDong, tenkh, quan, phuong FROM KhachHang where MLT2 = '" + maLoTrinh + "'");
                if (rs1.next()) {
                    hopDong = rs1.getString(1);
                    tenKhachHang = rs1.getString(2);
                    quan = rs1.getString(3) == null ? "" : rs1.getString(3);
                    phuong = rs1.getString(4) == null ? "" : rs1.getString(4);

                }
                HoaDon hoaDon = new HoaDon(khu, mDot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky + "", nam + "", code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
                hoaDons.add(hoaDon);
//                    LayLoTrinhActivity.this.mHoaDons.add(hoaDon);
//                if (mLocalDatabase.addHoaDon(hoaDon)) ;
//                else {
//                    //TODO
//                }
                sttm1.close();
                rs1.close();
//                publishProgress(maLoTrinh, 0, 0);
//                    mLocalDatabase.addLoTrinh(new LoTrinh(maLoTrinh, LayLoTrinhActivity.this.mHoaDons.size()));
            }


            rs.close();
            statement.close();


//                cnn.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return hoaDons;
    }

    public HoaDon getByDanhBo(String danhBo) {
        HoaDon hoaDon = null;
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery(this.SQL_SELECT + " WHERE DANHBO = '" + danhBo + "'");
            if (rs.next()) {
                int id = rs.getInt(1);
                String khu = rs.getString(2);
                String dot = rs.getString(3);
                String cuLy = rs.getString(5);
                String hopDong = rs.getString(6);
                String tenKhachHang = rs.getString(7);
                String soNha = rs.getString(8);
                String duong = rs.getString(9);
                String giaBieu = rs.getString(10);
                String dinhMuc = rs.getString(11);
                String ky = rs.getString(12);
                String nam = rs.getString(13);
                String code = rs.getString(14);
                String codeFU = rs.getString(15);
                String chiSoCu = rs.getString(16);
                String chiSoMoi = rs.getString(17);
                String quan = rs.getString(18);
                String phuong = rs.getString(19);
                String maLoTrinh = rs.getString(20);
                hoaDon = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);

            }
            rs.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return hoaDon;
    }

    public int getNum_DanhBo_ByMLT(String maLoTrinh) {
        int result = 0;
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(DANHBO) FROM " + TABLE_NAME + " WHERE MLT = '" + maLoTrinh + "'");
            if (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
            statement.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public List<String> get_DanhBo_ByMLT(String maLoTrinh) {
        List<String> result = new ArrayList<String>();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery(this.SQL_SELECT_DANHBO + " WHERE MLT = '" + maLoTrinh + "'");
            while (rs.next()) {
                result.add(rs.getString("DANHBO"));
            }
            rs.close();
            statement.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        HoaDonDB cdb = new HoaDonDB();
        List<String> result = null;
        try {
            result = cdb.get_DanhBo_ByMLT("00005");
            for (String s : result)
                System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
//		List<HoaDon> results = cdb.getByMaLoTrinh("22800");
//		for (HoaDon c : results) {
//			System.out.println(c);
//		}

    }

}
