package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 12/10/2017.
 */

public class DanhBo_ChiSoMoi {
    private String danhBo;
    private String maLoTrinh;
    private String dot;
    private String tenKH;
    private String diaChi;
    private String sdt;
    private String giaBieu;
    private String code;
    private String chiSoCu;
    private String chiSoMoi;
    private String tieuThu;
    private String note;
    private String image;
    private int hasImage;// 1 là đã lưu, 0 là chưa lưu

    public DanhBo_ChiSoMoi(String danhBo, String maLoTrinh, String dot, String tenKH, String diaChi, String sdt, String giaBieu, String code, String chiSoCu, String chiSoMoi, String tieuThu, String note, String image, int hasImage) {
        this.danhBo = danhBo;
        this.maLoTrinh = maLoTrinh;
        this.dot = dot;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.giaBieu = giaBieu;
        this.code = code;
        this.chiSoCu = chiSoCu;
        this.chiSoMoi = chiSoMoi;
        this.tieuThu = tieuThu;
        this.note = note;
        this.image = image;
        this.hasImage = hasImage;
    }

    public String getTieuThu() {
        return tieuThu;
    }

    public void setTieuThu(String tieuThu) {
        this.tieuThu = tieuThu;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getDanhBo() {
        return danhBo;
    }

    public String getMaLoTrinh() {
        return maLoTrinh;
    }

    public String getTenKH() {
        return tenKH;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGiaBieu() {
        return giaBieu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChiSoCu() {
        return chiSoCu;
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

    public int getHasImage() {
        return hasImage;
    }
}