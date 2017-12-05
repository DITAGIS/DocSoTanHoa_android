package com.ditagis.hcm.docsotanhoa.conectDB;

import android.os.StrictMode;

import com.ditagis.hcm.docsotanhoa.entities.Code_CSC_SanLuong;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.utities.Flag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDB implements IDB<HoaDon, Boolean, String> {
    private final String TABLE_NAME = "DocSo";
    private final String SQL_SELECT = "SELECT ID,KHU,DOT,DANHBO,CULY,HOPDONG,TENKH,SONHA,DUONG,GIABIEU,DINHMUC,KY,NAM,CODE,CODEFU,CSCU,CSMOI,QUAN,PHUONG,MLT FROM " + TABLE_NAME;
    private final String SQL_SELECT_GETALL_BY_USERNAME = "SELECT gb,dm,CSCU, MLT2, soThanCu FROM " + TABLE_NAME;
    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;
    private final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?)";
    //    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET CSC=? WHERE DANHBO=?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE ClassId=?";

    private String mDot;
    private String mStaffname;
    PreparedStatement mStatement;

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
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        try {
//            CallableStatement cbs = cnn.prepareCall(this.SQL_UPDATE);
//            cbs.setString(1, e.getChiSoCu());
//            return cbs.executeUpdate() > 0;
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//        }
//        return false;
        return null;
    }

    @Override
    public HoaDon find(String k) {
        return null;
    }

    @Override
    public List<HoaDon> getAll() {
//        List<HoaDon> result = new ArrayList<HoaDon>();
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        try {
//            CallableStatement cal = cnn.prepareCall(this.SQL_SELECT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            ResultSet rs = cal.executeQuery();
//            while (rs.next()) {
//                String khu = rs.getString(2);
//                String dot = rs.getString(3);
//                String danhBo = rs.getString(4);
//                String cuLy = rs.getString(5);
//                String hopDong = rs.getString(6);
//                String tenKhachHang = rs.getString(7);
//                String soNha = rs.getString(8);
//                String duong = rs.getString(9);
//                String giaBieu = rs.getString(10);
//                String dinhMuc = rs.getString(11);
//                String ky = rs.getString(12);
//                String nam = rs.getString(13);
//                String code = rs.getString(14);
//                String codeFU = rs.getString(15);
//                String chiSoCu = rs.getString(16);
//                String chiSoMoi = rs.getString(17);
//                String quan = rs.getString(18);
//                String phuong = rs.getString(19);
//                String maLoTrinh = rs.getString(20);
//                HoaDon course = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
//                result.add(course);
//            }
//            rs.close();
//            cal.close();
//            cnn.close();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//        return result;
        return null;
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

    //    public List<HoaDon> getByMaLoTrinh(String maLoTrinh) {
//        List<HoaDon> result = new ArrayList<HoaDon>();
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        try {
//            Statement mStatement = cnn.createStatement();
//            ResultSet rs = mStatement.executeQuery(this.SQL_SELECT + " WHERE MLT = '" + maLoTrinh + "'");
//            while (rs.next()) {
//                int id = rs.getInt(1);
//                String khu = rs.getString(2);
//                String dot = rs.getString(3);
//                String danhBo = rs.getString(4);
//                String cuLy = rs.getString(5);
//                String hopDong = rs.getString(6);
//                String tenKhachHang = rs.getString(7);
//                String soNha = rs.getString(8);
//                String duong = rs.getString(9);
//                String giaBieu = rs.getString(10);
//                String dinhMuc = rs.getString(11);
//                String ky = rs.getString(12);
//                String nam = rs.getString(13);
//                String code = rs.getString(14);
//                String codeFU = rs.getString(15);
//                String chiSoCu = rs.getString(16);
//                String chiSoMoi = rs.getString(17);
//                String quan = rs.getString(18);
//                String phuong = rs.getString(19);
//
//                HoaDon course = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
//                result.add(course);
//            }
//            rs.close();
//            cnn.close();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//
//        return result;
//    }
    public List<String> getCountHoaDon(String userName, int dot, int nam, int ky) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        List<String> DBs = new ArrayList<>();
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Statement statement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String dotString = "", kyString = "";
            if (dot < 10)
                dotString = "0" + dot;
            else dotString = dot + "";
            if (ky < 10)
                kyString = "0" + ky;
            else kyString = ky + "";
            String like = dotString + userName + "%";
            final ResultSet rs = statement.executeQuery("select danhba from docso where nam = "
                    + nam + " and ky = " + kyString + " and mlt2 like '" + like + "' and (csmoi is null or csmoi = 0 )");

            while (rs.next()) {
                DBs.add(rs.getString(1));
            }
            rs.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return DBs;
    }

    public HoaDon getHoaDonByUserName(String userName, String danhBo, int dot, int nam, int ky) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        HoaDon hoaDon = null;
        String sqlCode_CSC_SanLuong = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where danhba = ? and ky = ? and nam =?";
        String sqlCode_CSC_SanLuong3Ky = "SELECT cscu, csmoi, codemoi, tieuthumoi  FROM Docso where danhba = ? and (ky = ? or ky =? or ky = ?) and nam =? order by ky desc";
        try {
            if (cnn == null)
                return null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String dotString = "", kyString = "";
            if (dot < 10)
                dotString = "0" + dot;
            else dotString = dot + "";
            if (ky < 10)
                kyString = "0" + ky;
            else kyString = ky + "";
            String like = dotString + userName + "%";
            mStatement = cnn.prepareStatement(SQL_SELECT_GETALL_BY_USERNAME + " where danhba = ? and  nam = ?" +
                            " and ky = ? and mlt2 like ? and (csmoi is null or csmoi = 0 )", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            mStatement.setString(1, danhBo);
            mStatement.setInt(2, nam);
            mStatement.setString(3, kyString);
            mStatement.setString(4, like);
            ResultSet rs = mStatement.executeQuery();

            while (rs.next()) {
                String giaBieu = rs.getString(1);
                String dinhMuc = rs.getString(2);
                String chiSoCu = rs.getInt(3) + "";
                String maLoTrinh = rs.getString(4);
                String soThan = rs.getString(5);
                String soNha = "";
                String duong = "";
                String tenKhachHang = "";
                String sdt = "";
                String CSC1 = "";
                String CSC2 = "";
                String CSC3 = "";
                String code1 = "";
                String code2 = "";
                String code3 = "";
                String sanLuong_3 = "";
                String sanLuong_2 = "";
                String sanLuong_1 = "";
                mStatement = cnn.prepareStatement("SELECT tenkh,so, duong, sdt FROM KhachHang where MLT2 = ?");
                mStatement.setString(1, maLoTrinh);
                ResultSet rs1 = mStatement.executeQuery();
                if (rs1.next()) {
                    tenKhachHang = rs1.getString(1);
                    soNha = rs1.getString(2) == null ? "" : rs1.getString(2);
                    duong = rs1.getString(3) == null ? "" : rs1.getString(3);
                    sdt = rs1.getString(4) == null ? "" : rs1.getString(4);

                }
                List<Integer> kyNamList = get3Ky(ky, nam);
                //neu cung nam
                if (kyNamList.get(1) == kyNamList.get(3) && kyNamList.get(3) == kyNamList.get(5)) {
                    mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong3Ky);
                    mStatement.setString(1, danhBo);
                    mStatement.setInt(2, kyNamList.get(0));
                    mStatement.setInt(2, kyNamList.get(2));
                    mStatement.setInt(2, kyNamList.get(4));
                    mStatement.setInt(3, kyNamList.get(1));
                    ResultSet rs2 = mStatement.executeQuery();
                    if (rs2.next()) {
                        CSC1 = rs2.getString(1);
                        code1 = rs2.getString(3);
                        sanLuong_1 = rs2.getString(4);
                    }
                    if (rs2.next()) {
                        CSC2 = rs2.getString(1);
                        code2 = rs2.getString(3);
                        sanLuong_2 = rs2.getString(4);
                    }
                    if (rs2.next()) {
                        CSC3 = rs2.getString(1);
                        code3 = rs2.getString(3);
                        sanLuong_3 = rs2.getString(4);
                    }
                } else {

                    mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong);
                    mStatement.setString(1, danhBo);
                    mStatement.setInt(2, kyNamList.get(0));
                    mStatement.setInt(3, kyNamList.get(1));
                    ResultSet rs2 = mStatement.executeQuery();
                    if (rs2.next()) {
                        CSC1 = rs2.getString(1);
                        code1 = rs2.getString(3);
                        sanLuong_1 = rs2.getString(4);
                    }

//                mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong);
//                mStatement.setString(1, danhBo);
                    mStatement.setInt(2, kyNamList.get(2));
                    mStatement.setInt(3, kyNamList.get(3));
                    ResultSet rs3 = mStatement.executeQuery();
                    if (rs3.next()) {
                        CSC2 = rs3.getString(1);
                        code2 = rs3.getString(3);
                        sanLuong_2 = rs3.getString(4);
                    }

//                mStatement = cnn.prepareStatement(sqlCode_CSC_SanLuong);
//                mStatement.setString(1, danhBo);
                    mStatement.setInt(2, kyNamList.get(4));
                    mStatement.setInt(3, kyNamList.get(5));
                    ResultSet rs4 = mStatement.executeQuery();
                    if (rs4.next()) {
                        CSC3 = rs4.getString(1);
                        code3 = rs4.getString(3);
                        sanLuong_3 = rs4.getString(4);
                    }
                    rs1.close();
                    rs2.close();
                    rs3.close();
                    rs4.close();
                }
                hoaDon = new HoaDon(dotString, danhBo, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky + "", chiSoCu, maLoTrinh,
                        sdt, Flag.UNREAD);
                Code_CSC_SanLuong code_csu_sanLuong = new Code_CSC_SanLuong(code1, code2, code3,
                        CSC1, CSC2, CSC3,
                        sanLuong_1, sanLuong_2, sanLuong_3);
                hoaDon.setCode_CSC_SanLuong(code_csu_sanLuong);
                hoaDon.setSoThan(soThan);
                rs.close();


            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return hoaDon;
    }

    public void closeStatement() {
        if (mStatement != null)
            try {
                mStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

//    public List<HoaDon> getAllByUserName(String userName, int dot, int nam, int ky) {
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        List<HoaDon> hoaDons = null;
//        try {
//            if (cnn == null)
//                return null;
//            Statement statement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY), sttm1;
//            hoaDons = new ArrayList<HoaDon>();
//            String dotString = "", kyString = "";
//            if (dot < 10)
//                dotString = "0" + dot;
//            else dotString = dot + "";
//            if (ky < 10)
//                kyString = "0" + ky;
//            else kyString = ky + "";
//            String like = dotString + userName + "%";
//            final ResultSet rs = statement.executeQuery(SQL_SELECT_GETALL_BY_USERNAME + " where nam = "
//                    + nam + " and ky = " + kyString + " and mlt2 like '" + like + "' and (csmoi is null or csmoi = 0 )");
//            rs.last();
//            final int count = rs.getRow();
//            rs.first();
//
//            while (rs.next()) {
//
////                    int id = rs.getInt(1); //TODO xem vụ tạo DocSoID
//                String danhBo = rs.getString(1);
//
//                String giaBieu = rs.getString(2);
//                String dinhMuc = rs.getString(3);
//                String chiSoCu = rs.getInt(4) + "";
//                String maLoTrinh = rs.getString(5);
//                String code = rs.getString(6);
//                String soNha = "";
//                String duong = "";
//                String tenKhachHang = "";
//                String sdt = "";
//                String sanLuong_3 = "";
//                String sanLuong_2 = "";
//                String sanLuong_1 = "";
//
//                sttm1 = cnn.createStatement();
//                ResultSet rs1 = sttm1.executeQuery("SELECT tenkh,so, duong, sdt FROM KhachHang where MLT2 = '" + maLoTrinh + "'");
//                if (rs1.next()) {
//                    tenKhachHang = rs1.getString(1);
//                    soNha = rs1.getString(2) == null ? "" : rs1.getString(2);
//                    duong = rs1.getString(3) == null ? "" : rs1.getString(3);
//                    sdt = rs1.getString(4) == null ? "" : rs1.getString(4);
//
//                }
//                List<Integer> kyNamList = get3Ky(ky, nam);
//                //nếu cùng năm
//                if (kyNamList.get(1) == kyNamList.get(3) && kyNamList.get(3) == kyNamList.get(5))
//                    ;
//                else {
//
//                    PreparedStatement st = cnn.prepareStatement("SELECT cscu, csmoi FROM Docso where danhba = ? and ky = ? and nam =?");
//                    st.setString(1, danhBo);
//                    st.setInt(2, kyNamList.get(0));
//                    st.setInt(3, kyNamList.get(1));
//                    ResultSet rs2 = st.executeQuery();
//                    if (rs2.next()) {
//                        sanLuong_1 = (Integer.parseInt(rs2.getString(2)) - Integer.parseInt(rs2.getString(1))) + "";
//                    }
//
//                    int[] kyNam1 = getKyNam(ky, nam, 2);
//                    PreparedStatement st1 = cnn.prepareStatement("SELECT cscu, csmoi FROM Docso where danhba = ? and ky = ? and nam =?");
//                    st1.setString(1, danhBo);
//                    st1.setInt(2, kyNamList.get(2));
//                    st1.setInt(3, kyNam1[1]);
//                    ResultSet rs3 = st1.executeQuery();
//                    if (rs3.next()) {
//                        sanLuong_2 = (Integer.parseInt(rs2.getString(2)) - Integer.parseInt(rs2.getString(1))) + "";
//                    }
//
//                    int[] kyNam2 = getKyNam(ky, nam, 3);
//                    PreparedStatement st2 = cnn.prepareStatement("SELECT cscu, csmoi FROM Docso where danhba = ? and ky = ? and nam =?");
//                    st2.setString(1, danhBo);
//                    st2.setInt(2, kyNam[0]);
//                    st2.setInt(3, kyNam[1]);
//                    ResultSet rs4 = st2.executeQuery();
//                    if (rs4.next()) {
//                        sanLuong_3 = (Integer.parseInt(rs2.getString(2)) - Integer.parseInt(rs2.getString(1))) + "";
//                    }
//                }
//                HoaDon hoaDon = new HoaDon(dotString, danhBo, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky + "", chiSoCu, maLoTrinh,
//                        sdt);
//                Code_CSC_SanLuong code_csu_sanLuong = new Code_CSC_SanLuong("code1", "code2", "code3",
//                        "csc1", "csc2", "csc3",
//                        "", "", "");
//                hoaDon.setCode_CSC_SanLuong(code_csu_sanLuong);
//                hoaDons.add(hoaDon);
//
//                sttm1.close();
//                rs1.close();
////                st1.close();
////                st2.close();
////                st.close();
////                rs2.close();
////                rs3.close();
////                rs4.close();
//            }
//            rs.close();
//            statement.close();
//
//
////                cnn.close();
//
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//        return hoaDons;
//    }

    private List<Integer> get3Ky(int ky, int nam) {
        List<Integer> result = new ArrayList<>();
        int[] kyNam = new int[2];
        for (int i = 1; i <= 3; i++) {
            kyNam = getKyNam(ky, nam, i);
            result.add(kyNam[0]);
            result.add(kyNam[1]);
        }
        return result;
    }

    private int[] getKyNam(int ky, int nam, int prev) {
        int[] result = new int[2];
        int delta = ky - prev;
        if (delta > 0) {
            result[0] = ky - prev;
            result[1] = nam;
        } else if (delta == 0) {
            result[0] = 12;
            result[1] = nam - 1;
        } else if (delta == -1) {
            result[0] = 11;
            result[1] = nam - 1;
        } else if (delta == -2) {
            result[0] = 10;
            result[1] = nam - 1;
        }
        return result;
    }

//    public HoaDon getByDanhBo(String danhBo) {
//        HoaDon hoaDon = null;
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        try {
//            Statement mStatement = cnn.createStatement();
//            ResultSet rs = mStatement.executeQuery(this.SQL_SELECT + " WHERE DANHBO = '" + danhBo + "'");
//            if (rs.next()) {
//                int id = rs.getInt(1);
//                String khu = rs.getString(2);
//                String dot = rs.getString(3);
//                String cuLy = rs.getString(5);
//                String hopDong = rs.getString(6);
//                String tenKhachHang = rs.getString(7);
//                String soNha = rs.getString(8);
//                String duong = rs.getString(9);
//                String giaBieu = rs.getString(10);
//                String dinhMuc = rs.getString(11);
//                String ky = rs.getString(12);
//                String nam = rs.getString(13);
//                String code = rs.getString(14);
//                String codeFU = rs.getString(15);
//                String chiSoCu = rs.getString(16);
//                String chiSoMoi = rs.getString(17);
//                String quan = rs.getString(18);
//                String phuong = rs.getString(19);
//                String maLoTrinh = rs.getString(20);
//                hoaDon = new HoaDon(khu, dot, danhBo, cuLy, hopDong, tenKhachHang, soNha, duong, giaBieu, dinhMuc, ky, nam, code, codeFU, chiSoCu, chiSoMoi, quan, phuong, maLoTrinh);
//
//            }
//            rs.close();
//            cnn.close();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
//        return hoaDon;
//    }

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
