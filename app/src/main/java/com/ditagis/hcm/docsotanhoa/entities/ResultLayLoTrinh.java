package com.ditagis.hcm.docsotanhoa.entities;

import android.content.Context;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewLayLoTrinhAdapter;

import java.util.ArrayList;

/**
 * Created by ThanLe on 24/10/2017.
 */

public class ResultLayLoTrinh {
    private int count;
    private int total;
    private String dot;
    private String staffName;
    private GridViewLayLoTrinhAdapter da;

    public ResultLayLoTrinh(Context context) {
        da = new GridViewLayLoTrinhAdapter(context, new ArrayList<GridViewLayLoTrinhAdapter.Item>());
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return da.getCount();
    }


    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public GridViewLayLoTrinhAdapter getDa() {
        return da;
    }

    public void setDa(GridViewLayLoTrinhAdapter da) {
        this.da = da;
    }

    public void addItemToDa(GridViewLayLoTrinhAdapter.Item item) {
        this.da.add(item);
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }


}
