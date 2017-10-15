package com.ditagis.hcm.docsotanhoa.conectDB;

import android.support.annotation.NonNull;

import com.ditagis.hcm.docsotanhoa.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 12/10/2017.
 */

public class LogInDB extends AbstractDB implements IDB<User, Boolean, String> {

    private final String TABLE_NAME = "USER_ACCOUNT";
    private final String SQL_SELECT = "select * from " + TABLE_NAME + " where username = ? and password = ?";
    private final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(?,?)";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET password=? WHERE username=?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE USERNAME=?";

    @NonNull
    private Boolean createTable() {
        Connection cnn = this.condb.getConnect();
        Statement statement = null;
        try {
            statement = cnn.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME +
                    "(username VARCHAR(15) not NULL," +
                    "password VARCHAR(100) not NULL," +
                    "PRIMARY KEY ( username))";
            statement.execute(sql);
            statement.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Boolean add(User user) {
        Connection cnn = this.condb.getConnect();
        String sql = this.SQL_INSERT;

        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, user.getUserName());
            st.setString(2, encodeMD5(user.getPassWord()));
            boolean result = st.execute();
            st.close();
            cnn.close();
            return result;

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(String k) {
        Connection cnn = this.condb.getConnect();
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

    public boolean logIn(User user) {
        Connection cnn = this.condb.getConnect();
        String sql = this.SQL_SELECT;

        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, user.getUserName());
            st.setString(2, encodeMD5(user.getPassWord()));
            ResultSet result = st.executeQuery();
            if (result.next()) {
                st.close();
                cnn.close();
                return true;
            }
            st.close();
            cnn.close();


        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(User user, String newPassword) {
        if (logIn(user)) {
            Connection cnn = this.condb.getConnect();
            String sql = this.SQL_UPDATE;

            try {
                PreparedStatement st = cnn.prepareStatement(sql);
                st.setString(1, encodeMD5(newPassword));
                st.setString(2, user.getUserName());
                ResultSet result = st.executeQuery();
                if (result.next()) {
                    st.close();
                    cnn.close();
                    return true;
                }
                st.close();
                cnn.close();


            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        } else
            return false;
    }

    @Override
    public List<User> getAll() {
        List<User> result = new ArrayList<User>();
        Connection cnn = this.condb.getConnect();
        try {
            CallableStatement cal = cnn.prepareCall(this.SQL_SELECT, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                String username = rs.getString(1);
                String pass = rs.getString(2);

                User user = new User(username, pass);
                result.add(user);
            }
            rs.close();
            cal.close();
            cnn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public String encodeMD5(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        LogInDB logInDB = new LogInDB();
//        String[] msnv = new String[]{
//                "016",
//                "024",
//                "044",
//                "068",
//                "102",
//                "103",
//                "117",
//                "127",
//                "139",
//                "143",
//                "145",
//                "146",
//                "163",
//                "167",
//                "168",
//                "170",
//                "207",
//                "225",
//                "226",
//                "275",
//                "313",
//                "326",
//                "333",
//                "334",
//                "335",
//                "336",
//                "342",
//                "343",
//                "344",
//                "355",
//                "358",
//                "361",
//                "370",
//                "387",
//                "388",
//                "390",
//                "391",
//                "398",
//                "399",
//                "408",
//                "410",
//                "411",
//                "412",
//                "421",
//        };
//        for (String maNV : msnv)
//            logInDB.add(new User(maNV, "54321"));

    }
}
