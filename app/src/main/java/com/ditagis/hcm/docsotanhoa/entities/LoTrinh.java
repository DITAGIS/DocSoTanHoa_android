package com.ditagis.hcm.docsotanhoa.entities;

import java.io.Serializable;

/**
 * Created by ThanLe on 05/10/2017.
 */

public class LoTrinh implements Serializable {
    private String maLoTrinh;
    private int soLuong;

    public LoTrinh() {
    }

    public LoTrinh(String maLoTrinh, int soLuong) {
        this.maLoTrinh = maLoTrinh;
        this.soLuong = soLuong;
    }

    public String getMaLoTrinh() {
        return maLoTrinh;
    }

    public void setMaLoTrinh(String maLoTrinh) {
        this.maLoTrinh = maLoTrinh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
