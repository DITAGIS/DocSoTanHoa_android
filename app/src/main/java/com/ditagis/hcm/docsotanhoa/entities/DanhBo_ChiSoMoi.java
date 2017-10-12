package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 12/10/2017.
 */

public class DanhBo_ChiSoMoi {
    private String danhBo;
    private String maLoTrinh;
    private String dot;
    private String ky;
    private String code;
    private String chiSoMoi;
    private String note;
    private String image;
    private int hasImage;// 1 là đã lưu, 0 là chưa lưu

    public DanhBo_ChiSoMoi(String danhBo, String maLoTrinh, String dot, String ky, String code, String chiSoMoi, String note, String image, int hasImage) {
        this.danhBo = danhBo;
        this.maLoTrinh = maLoTrinh;
        this.dot = dot;
        this.ky = ky;
        this.code = code;
        this.chiSoMoi = chiSoMoi;
        this.note = note;
        this.image = image;
        this.hasImage = hasImage;
    }

    public String getDanhBo() {
        return danhBo;
    }

    public void setDanhBo(String danhBo) {
        this.danhBo = danhBo;
    }

    public String getMaLoTrinh() {
        return maLoTrinh;
    }

    public void setMaLoTrinh(String maLoTrinh) {
        this.maLoTrinh = maLoTrinh;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getHasImage() {
        return hasImage;
    }

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }
}