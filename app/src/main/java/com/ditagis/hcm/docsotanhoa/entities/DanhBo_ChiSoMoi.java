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
    private String giaBieu;
    private String code;
    private String chiSoCu;
    private String chiSoMoi;
    private String note;
    private String image;
    private int hasImage;// 1 là đã lưu, 0 là chưa lưu

    public DanhBo_ChiSoMoi(String danhBo, String maLoTrinh, String tenKH, String diaChi, String sdt, String giaBieu, String code, String chiSoCu, String chiSoMoi, String note, String image, int hasImage) {
        this.danhBo = danhBo;
        this.maLoTrinh = maLoTrinh;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.giaBieu = giaBieu;
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

    public String getGiaBieu() {
        return giaBieu;
    }

    public String getCode() {
        return code;
    }

    public String getChiSoCu() {
        return chiSoCu;
    }

    public String getChiSoMoi() {
        return chiSoMoi;
    }

    public String getNote() {
        return note;
    }

    public String getImage() {
        return image;
    }

    public int getHasImage() {
        return hasImage;
    }
}