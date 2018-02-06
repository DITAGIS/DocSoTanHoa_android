package com.ditagis.hcm.docsotanhoa.conectDB;

import android.content.Context;

import com.ditagis.hcm.docsotanhoa.entities.TTDHN;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 2/6/2018.
 */

public class TTDHNDB {
    private final String TABLE_NAME = "TTDHN";
    private final String select = "SELECT code,ttdhn FROM " + TABLE_NAME;

    private Context context;

    public TTDHNDB(Context context) {
        this.context = context;
    }

    public void getTTDHN() {
        List<TTDHN> result = new ArrayList<TTDHN>();
        Connection cnn = ConnectionDB.getInstance().getConnection();
        try {
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
                result.add(new TTDHN(rs.getString(1), rs.getString(2)));
            }
            LocalDatabase.getInstance(this.context).addTTDHN(result);
            rs.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
