package com.ditagis.hcm.docsotanhoa.conectDB;

import android.support.annotation.NonNull;

import com.ditagis.hcm.docsotanhoa.entities.EncodeMD5;
import com.ditagis.hcm.docsotanhoa.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ThanLe on 12/10/2017.
 */

public class LogInDB implements IDB<User, Boolean, String> {

    private final String TABLE_NAME = "MayDS1";
    private final String SQL_SELECT = "select NhanVienID, dienthoai, may from " + TABLE_NAME + " where may = ? and password = ?";
    private final String SQL_SELECT_BY_IMEI = "select NhanVienID, dienthoai, may from " + TABLE_NAME + " where may = ? and password = ? and IMEI = ?";
    private final String SQL_SELECT_MAY = "select  may from " + TABLE_NAME + " where IMEI = ?";
    private final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?,?)";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET password=? WHERE username=?";
    private final String SQL_UPDATE_ALL = "UPDATE " + TABLE_NAME + " SET password=?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE USERNAME=?";

    public static void main(String[] args) {
//        LogInDB logInDB = new LogInDB();
//
//        for (int i = 1; i <= 70; i++)
//            logInDB.setPassword(i, "54321");

        System.out.print(new EncodeMD5().encode("54321"));
    }

    @NonNull
    private Boolean createTable() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        Statement statement = null;
        try {
            statement = cnn.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME +
                    "(username VARCHAR(15) not NULL," +
                    "password VARCHAR(100) not NULL," +
                    "PRIMARY KEY ( username))";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Boolean add(User user) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_INSERT;

        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, user.getUserName());
            st.setString(2, (new EncodeMD5()).encode(user.getPassWord()));
            boolean result = st.execute();
            st.close();
            return result;

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
    public Boolean update(User user) {
        return null;
    }

    @Override
    public User find(String s) {
        return null;
    }

    public String getUserName(String IMEI) {
        Connection cnn = ConnectionDB.getInstance().getConnection(true);
        String sql = this.SQL_SELECT_MAY;
        String username = "";
        try {
            if (cnn == null)
                return null;
            PreparedStatement statement = cnn.prepareStatement(sql);
            statement.setString(1, IMEI);
//            statement.setString(2, (new EncodeMD5()).encode(user.getPassWord()));
//            statement.setString(2, IMEI);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                username = resultSet.getString(1);
            }
            resultSet.close();

            return username;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return username;
    }

    public Result logIn(User user) {

        Calendar calendar = Calendar.getInstance();
        String ky = "";
//        int ky  = 10; // lay ky 10
//        int dot = calendar.get(Calendar.DAY_OF_MONTH);
        String nam = "";
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_SELECT;

        try {
            if (cnn == null)
                return null;
            List<String> lstMay = new ArrayList<>();
            PreparedStatement statement = cnn.prepareStatement(sql);
            statement.setString(1, user.getUserName());
            statement.setString(2, (new EncodeMD5()).encode(user.getPassWord()));
            ResultSet resultSet = statement.executeQuery();
            String staffName = "";
            String staffPhone = "";
            String username = "";

            if (resultSet.next()) {
                staffName = resultSet.getString(1);
                staffPhone = resultSet.getString(2);
                username = resultSet.getString(3);
            }
            resultSet.close();
            statement = cnn.prepareStatement("select distinct top 1 DocSoID from docso  order by DocSoID desc");
            ResultSet rsNamKy = statement.executeQuery();
            String docSoID = "";
            String mDot = "";
            while (rsNamKy.next()) {
                docSoID = rsNamKy.getString(1);
                nam = docSoID.substring(0, 4);
                ky = docSoID.substring(4, 6);
                break;
            }
            rsNamKy.close();


            statement = cnn.prepareStatement("select distinct top 1 dot from docso where docsoid like '" + docSoID.substring(0, 6) + "%'" +
                    " and gioghi < '2017-12-31 00:00:00.000'" +

                    " order by dot desc " +
                    "");
            mDot = "01";
            ResultSet rsDot = statement.executeQuery();
            while ((rsDot.next())) {
                mDot = rsDot.getString(1);
            }
            rsDot.close();

            int may = Integer.parseInt(user.getUserName());
            if (99 <= may && may <= 104) {
                switch (may) {
                    case 99:
                    case 100:
                        statement = cnn.prepareStatement("select may from mayds1 where toid is not null order by toid asc");
                        break;
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                        statement = cnn.prepareStatement("select may from mayds1 where ToID ='"
                                + user.getUserName().substring(2) + "' and toid is not null order by toid asc");
                        break;
                }

                ResultSet rsMay = statement.executeQuery();
                while ((rsMay.next())) {
                    lstMay.add(rsMay.getString(1));
                }
            }
            statement.close();
//            rsDot.close();
            Result result = new Result(mDot, staffName, username, staffPhone, lstMay);
            result.setmNam(nam);
            result.setmKy(ky);

            return result;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    public Result logIn(User user, String IMEI) {

        Calendar calendar = Calendar.getInstance();
        String ky = "";
//        int ky  = 10; // lay ky 10
//        int dot = calendar.get(Calendar.DAY_OF_MONTH);
        String nam = "";
        Connection cnn = ConnectionDB.getInstance().getConnection(true);
        String sql = this.SQL_SELECT_BY_IMEI;

        try {
            if (cnn == null)
                return null;
            List<String> lstMay = new ArrayList<>();
            PreparedStatement statement = cnn.prepareStatement(sql);
            statement.setString(1, user.getUserName());
            statement.setString(2, (new EncodeMD5()).encode(user.getPassWord()));
            statement.setString(3, IMEI);
            ResultSet resultSet = statement.executeQuery();
            String staffName = "";
            String staffPhone = "";
            String username = "";

            if (resultSet.next()) {
                staffName = resultSet.getString(1);
                staffPhone = resultSet.getString(2);
                username = resultSet.getString(3);
            }
            resultSet.close();
            statement = cnn.prepareStatement("select distinct top 1 DocSoID from docso  order by DocSoID desc");
            ResultSet rsNamKy = statement.executeQuery();
            String docSoID = "";
            String mDot = null;
            while (rsNamKy.next()) {
                docSoID = rsNamKy.getString(1);
                nam = docSoID.substring(0, 4);
                ky = docSoID.substring(4, 6);
                break;
            }
            rsNamKy.close();
            statement = cnn.prepareStatement("select distinct top 1 dot from docso where docsoid like '" + docSoID.substring(0, 6) + "%'" +
                    " and gioghi < '2017-12-31 00:00:00.000'" +
                    " order by dot desc " +
                    "");
            mDot = "01";
            ResultSet rsDot = statement.executeQuery();
            while ((rsDot.next())) {
                mDot = rsDot.getString(1);
            }
            rsDot.close();
//            statement = cnn.prepareStatement("SELECT TOP 1 dot from DocSo where nam = "
//                    + nam + " and ky = " + ky + " order by dot desc");
//            ResultSet rsDot = statement.executeQuery();
//
//            if (rsDot.next()) {
//                mDot = rsDot.getString(1);
//            }
////            mDot = dot + "";
            int may = Integer.parseInt(user.getUserName());
            if (99 <= may && may <= 104) {
                switch (may) {
                    case 99:
                    case 100:
                        statement = cnn.prepareStatement("select may from mayds1 where toid is not null order by toid asc");
                        break;
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                        statement = cnn.prepareStatement("select may from mayds1 where ToID ='"
                                + user.getUserName().substring(2) + "' and toid is not null order by toid asc");
                        break;
                }

                ResultSet rsMay = statement.executeQuery();
                while ((rsMay.next())) {
                    lstMay.add(rsMay.getString(1));
                }
            }
            statement.close();
//            rsDot.close();
            Result result = new Result(mDot, staffName, username, staffPhone, lstMay);
            result.setmNam(nam);
            result.setmKy(ky);

            return result;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    public boolean setPassword(int i, String password) {


        String sql = this.SQL_UPDATE_ALL;
        try {
            String username = i + "";
            if (i < 10)
                username = "0" + i;
            PreparedStatement st = ConnectionDB.getInstance().getConnection().prepareStatement(sql);
            st.setString(1, (new EncodeMD5()).encode(password));
            int result = st.executeUpdate();
            if (result > 0) {
                st.close();
                return true;
            }
            st.close();


        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;

    }

    @Override
    public List<User> getAll() {
        List<User> result = new ArrayList<User>();
//        Connection cnn = ConnectionDB.getInstance().getConnection();
//        try {
//            CallableStatement cal = cnn.prepareCall(this.SQL_SELECT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            ResultSet rs = cal.executeQuery();
//            while (rs.next()) {
//                String username = rs.getString(1);
//                String pass = rs.getString(2);
//
//                User user = new User(username, pass);
//                result.add(user);
//            }
//            rs.close();
//            cal.close();
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
        return result;
    }

    public class Result {
        private String mNam;
        private String mKy;
        private String mDot;
        private String mStaffName;
        private String username;
        private String password;
        private String mStaffPhone;
        private List<String> mLstMay;

        public Result(String mDot, String mStaffName, String userName, String staffPhone, List<String> lstMay) {
            this.mDot = mDot;
            this.mStaffName = mStaffName;
            this.username = userName;
            this.mStaffPhone = staffPhone;
            this.mLstMay = lstMay;
        }

        public List<String> getmLstMay() {
            return mLstMay;
        }

        public void setmLstMay(List<String> mLstMay) {
            this.mLstMay = mLstMay;
        }

        public String getmStaffPhone() {
            return mStaffPhone;
        }

        public String getmKy() {
            return mKy;
        }

        public void setmKy(String mKy) {
            this.mKy = mKy;
        }

        public String getmNam() {
            return mNam;
        }

        public void setmNam(String mNam) {
            this.mNam = mNam;
        }

        public String getmDot() {
            return mDot;
        }

        public String getmStaffName() {
            return mStaffName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
