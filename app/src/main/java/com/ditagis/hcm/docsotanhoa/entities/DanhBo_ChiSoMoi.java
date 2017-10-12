package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 12/10/2017.
 */

public class DanhBo_ChiSoMoi {
    private String danhBo;
    private String maLotrinh;
    private String dot;
    private String ky;
    private String code;
    private String chiSoMoi;
    private int hasImage;// 1 là đã lưu, 0 là chưa lưu

    public DanhBo_ChiSoMoi(String danhBo, String maLotrinh, String dot, String ky, String code, String chiSoMoi, int hasImage) {
        this.danhBo = danhBo;
        this.maLotrinh = maLotrinh;
        this.dot = dot;
        this.ky = ky;
        this.code = code;
        this.chiSoMoi = chiSoMoi;
        this.hasImage = hasImage;
    }

    public String getDanhBo() {
        return danhBo;
    }

    public void setDanhBo(String danhBo) {
        this.danhBo = danhBo;
    }

    public String getMaLoTrinh() {
        return maLotrinh;
    }

    public void setMaLotrinh(String maLotrinh) {
        this.maLotrinh = maLotrinh;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChiSoMoi() {
        return chiSoMoi;
    }

    public void setChiSoMoi(String chiSoMoi) {
        this.chiSoMoi = chiSoMoi;
    }

    public int getHasImage() {
        return hasImage;
    }

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }
}
