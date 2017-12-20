package com.ditagis.hcm.docsotanhoa.conectDB;

import com.ditagis.hcm.docsotanhoa.entities.EncodeMD5;
import com.ditagis.hcm.docsotanhoa.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ThanLe on 23/10/2017.
 */

public class ChangePasswordDB implements IDB<User, Boolean, String> {

    private final String TABLE_NAME = "MayDS1";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET password=? WHERE may=?";

    public class Result {
        private String mDot;
        private String mStaffName;
        private String username;
        private String password;

        public Result() {
        }

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

    public ChangePasswordDB.Result changePassword(String userName, String oldPassword, String newPassword) {
        Connection cnn = ConnectionDB.getInstance().getConnection();
//        LogInDB.Result result = new LogInDB().logIn(new User(userName, oldPassword));
//        if (result == null) {
//            return null;
//        }
        ChangePasswordDB.Result result = new ChangePasswordDB.Result();


        String sql = this.SQL_UPDATE;

        try {
            if (cnn == null)
                return null;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, (new EncodeMD5()).encode(newPassword));
            st.setString(2, userName);
            int executeUpdate = st.executeUpdate();
            if (executeUpdate > 0) {
                st.close();
                result.setUsername(userName);
                result.setPassword(newPassword);
                return result;
            }
            st.close();


        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;

    }

    @Override
    public Boolean add(User user) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(User user) {
        return null;
    }

    @Override
    public User find(String s) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
