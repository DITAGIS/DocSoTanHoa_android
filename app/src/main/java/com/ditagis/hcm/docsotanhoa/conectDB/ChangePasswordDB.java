package com.ditagis.hcm.docsotanhoa.conectDB;

import com.ditagis.hcm.docsotanhoa.entities.EncodeMD5;
import com.ditagis.hcm.docsotanhoa.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ThanLe on 23/10/2017.
 */

public class ChangePasswordDB implements IDB<User, Boolean, String> {
    private String userName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private final String TABLE_NAME = "MayDS";
    private final String SQL_SELECT = "select * from " + TABLE_NAME + " where may = ? and password = ?";
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET password=? WHERE username=?";

    public ChangePasswordDB() {
    }

    public ChangePasswordDB(String userName, String oldPassword, String newPassword, String confirmPassword) {
        this.userName = userName;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }


    private boolean logIn() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        String sql = this.SQL_SELECT;
        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, userName);
            st.setString(2, (new EncodeMD5()).encode(oldPassword));
            ResultSet result = st.executeQuery();
            if (result.next()) {
                st.close();
                cnn.close();
                return true;
            }
            st.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public boolean changePassword() {
        Connection cnn = ConnectionDB.getInstance().getConnection();
        if (logIn()) {

            String sql = this.SQL_UPDATE;

            try {
                PreparedStatement st = cnn.prepareStatement(sql);
                st.setString(1, (new EncodeMD5()).encode(newPassword));
                st.setString(2, userName);
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
