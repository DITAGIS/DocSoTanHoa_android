package com.ditagis.hcm.docsotanhoa.conectDB;

import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ThanLe on 15/10/2017.
 */

public class Uploading extends AbstractDB implements IDB<DanhBo_ChiSoMoi, Boolean, String> {
    private final String TABLE_NAME = "HOADON";
    private final String SQL_SELECT = "SELECT ID,KHU,DOT,DANHBO,CULY,HOPDONG,TENKH,SONHA,DUONG,GIABIEU,DINHMUC,KY,NAM,CODE,CODEFU,CSCU,CSMOI,QUAN,PHUONG,MLT FROM " + TABLE_NAME;
    private final String SQL_SELECT_DANHBO = "SELECT DANHBO FROM " + TABLE_NAME;
    private final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET CSMOI=?, CODE=? WHERE DANHBO=?";
    private final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE ClassId=?";

    @Override
    public Boolean add(DanhBo_ChiSoMoi danhBo_chiSoMoi) {
        return null;
    }

    @Override
    public Boolean delete(String s) {
        return null;
    }

    @Override
    public Boolean update(DanhBo_ChiSoMoi danhBo_chiSoMoi) {
        Connection cnn = this.condb.getConnect();
        String sql = this.SQL_UPDATE;
        //TODO: cập nhật chỉ số cũ = chỉ số mới
        try {
            PreparedStatement st = cnn.prepareStatement(sql);
            st.setString(1, danhBo_chiSoMoi.getChiSoMoi());
            st.setString(2, danhBo_chiSoMoi.getCode());
            st.setString(3, danhBo_chiSoMoi.getDanhBo());
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
    public DanhBo_ChiSoMoi find(String s) {
        return null;
    }

    @Override
    public List<DanhBo_ChiSoMoi> getAll() {
        return null;
    }
}
