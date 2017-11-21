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

    private final String TABLE_NAME = "MayDS";
    private final String SQL_SELECT = "select NhanVienID from " + TABLE_NAME + " where may = ? and password = ?";
    private final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?,?)";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET password=? WHERE username=?";
    private final String SQL_UPDATE_ALL = "UPDATE " + TABLE_NAME + " SET password=?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE USERNAME=?";


    public class Result {
        private String mDot;
        private String mStaffName;
        private String username;
        private String password;

        public Result(String mDot, String mStaffName, String userName) {
            this.mDot = mDot;
            this.mStaffName = mStaffName;
            this.username = userName;
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

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
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

    public Result logIn(User user) {

        Calendar calendar = Calendar.getInstance();
        int ky = calendar.get(Calendar.MONTH) + 1;
//        int ky  = 10; // lay ky 10
        int dot = calendar.get(Calendar.DAY_OF_MONTH);
        int nam = calendar.get(Calendar.YEAR);
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_SELECT;

        try {
            if (cnn == null)
                return null;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, user.getUserName());
            st.setString(2, (new EncodeMD5()).encode(user.getPassWord()));
            ResultSet resultSet = st.executeQuery();
            String staffName = "";
            if (resultSet.next()) {
                staffName = resultSet.getString(1);

            }
            resultSet.close();
            st.close();
            Statement statement = cnn.createStatement(), sttm1;
            ResultSet rsDot = statement.executeQuery("SELECT TOP 1 dot from DocSo where nam = "
                    + nam + " and ky = " + ky + " order by dot desc");
            String mDot = null;
            if (rsDot.next()) {
                mDot = rsDot.getString(1);
            }
//            mDot = dot + "";
            statement.close();
            rsDot.close();
            Result result = new Result(mDot, staffName,user.getUserName());
            return result;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    public boolean setPassword(Connection cnn, String password) {


        String sql = this.SQL_UPDATE_ALL;

        try {
            PreparedStatement st = cnn.prepareStatement(sql);
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


    public static void main(String[] args) {
        LogInDB logInDB = new LogInDB();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        for (int i = 1; i <= 70; i++)
            logInDB.setPassword(cnn, "54321");

    }
}
