package com.ditagis.hcm.docsotanhoa.conectDB;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ThanLe on 06/11/2017.
 */

public class SumDanhBoDB {
    private final String TABLE_NAME = "DocSo";
    private final String SQL_SELECT_SUM = "select count (danhba) as count from " + TABLE_NAME + " where nam =? and ky = ? and mlt2 like ?";
    private final String SQL_SELECT_SUM_SYNC = "select count (danhba) as count from " + TABLE_NAME + " where nam =? and ky = ? and mlt2 like ? and csmoi > 0";
    private final String SQL_SELECT_COMPLETE = "select count (danhba) as count from " + TABLE_NAME + " where nam =? and ky = ? and dot =? and (csmoi is not null and csmoi > 0)";
    private Connection cnn = ConnectionDB.getInstance().getConnection();

    public int getComplete(String ky, int nam, String dot) {
        int complete = 0;
        String sql = this.SQL_SELECT_COMPLETE;
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return complete;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setInt(1, nam);
            st.setString(2, ky);
            st.setString(3, dot);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResultSet result = st.executeQuery();

            if (result.next()) {
                complete = result.getInt("count");
            }
            st.close();
            result.close();
            return complete;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return complete;
    }

    public int getSumSynchronized(String ky, int nam, String like) {
        int sum = 0;
        String sql = this.SQL_SELECT_SUM_SYNC;
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return sum;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setInt(1, nam);
            st.setString(2, ky);
            st.setString(3, like);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResultSet result = st.executeQuery();

            if (result.next()) {
                sum = result.getInt("count");
            }
            st.close();
            result.close();
            return sum;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return sum;
    }

    public int getSum(String ky, int nam, String like) {
        int sum = 0;
        String sql = this.SQL_SELECT_SUM;
        try {
            cnn = ConnectionDB.getInstance().getConnection();
            if (cnn == null)
                return sum;
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setInt(1, nam);
            st.setString(2, ky);
            st.setString(3, like);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ResultSet result = st.executeQuery();

            if (result.next()) {
                sum = result.getInt("count");
            }
            st.close();
            result.close();
            return sum;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return sum;
    }


}
