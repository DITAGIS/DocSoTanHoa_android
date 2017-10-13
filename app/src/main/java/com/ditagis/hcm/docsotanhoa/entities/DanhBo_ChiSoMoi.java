package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 12/10/2017.
 */

public class DanhBo_ChiSoMoi {
    private String danhBo;
    private String maLoTrinh;
    private String tenKH;
    private String diaChi;
    private String sdt;
    private String code;
    private String chiSoCu;
    private String chiSoMoi;
    private String note;
    private String image;
    private int hasImage;// 1 là đã lưu, 0 là chưa lưu

    public DanhBo_ChiSoMoi(String danhBo, String maLoTrinh, String tenKH, String diaChi, String sdt, String code, String chiSoCu, String chiSoMoi, String note, String image, int hasImage) {
        this.danhBo = danhBo;
        this.maLoTrinh = maLoTrinh;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.code = code;
        this.chiSoCu = chiSoCu;
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

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
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

    public void setChiSoCu(String chiSoCu) {
        this.chiSoCu = chiSoCu;
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