package com.ditagis.hcm.docsotanhoa.entities;

/**
 * @author fuhi
 */
public class HoaDon {

    private String dot;
    private String danhBo;
    private String tenKhachHang;
    private String soNha;
    private String duong;
    private String giaBieu;
    private String dinhMuc;
    private String ky;
    private String nam;
    private String codeCu;
    private String chiSoCu;
    private String codeMoi;
    private String chiSoMoi;
    private String tieuThuMoi;
    private String maLoTrinh;
    private String sdt;
    private String ghiChu;
    private String image;
    private Code_CSC_SanLuong code_CSC_SanLuong;
    private int flag;

    public HoaDon() {
        super();

    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getSdt() {
        return sdt;
    }

    public String getCodeMoi() {
        return codeMoi;
    }

    public void setCodeMoi(String codeMoi) {
        this.codeMoi = codeMoi;
    }

    public String getChiSoMoi() {
        return chiSoMoi;
    }

    public void setChiSoMoi(String chiSoMoi) {
        this.chiSoMoi = chiSoMoi;
    }

    public String getTieuThuMoi() {
        return tieuThuMoi;
    }

    public void setTieuThuMoi(String tieuThuMoi) {
        this.tieuThuMoi = tieuThuMoi;
    }

    public HoaDon(String dot, String danhBo, String tenKhachHang, String soNha, String duong, String giaBieu, String dinhMuc, String ky, String chiSoCu, String maLoTrinh, String sdt, int flag) {
        this.dot = dot;
        this.danhBo = danhBo;
        this.tenKhachHang = tenKhachHang;
        this.soNha = soNha;
        this.duong = duong;
        this.giaBieu = giaBieu;
        this.dinhMuc = dinhMuc;
        this.ky = ky;
        this.chiSoCu = chiSoCu;
        this.maLoTrinh = maLoTrinh;
        this.sdt = sdt;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Code_CSC_SanLuong getCode_CSC_SanLuong() {
        return code_CSC_SanLuong;
    }

    public void setCode_CSC_SanLuong(Code_CSC_SanLuong code_CSC_SanLuong) {
        this.code_CSC_SanLuong = code_CSC_SanLuong;
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

    public void setDanhBo(String danhBo) {
        this.danhBo = danhBo;
    }


    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    public String getDuong() {
        return duong;
    }

    public void setDuong(String duong) {
        this.duong = duong;
    }

    public String getGiaBieu() {
        return giaBieu;
    }

    public void setGiaBieu(String giaBieu) {
        this.giaBieu = giaBieu;
    }

    public String getDinhMuc() {
        return dinhMuc;
    }

    public void setDinhMuc(String dinhMuc) {
        this.dinhMuc = dinhMuc;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getCodeCu() {
        return codeCu;
    }

    public void setCodeCu(String codeCu) {
        this.codeCu = codeCu;
    }


    public String getChiSoCu() {
        return chiSoCu;
    }

    public void setChiSoCu(String chiSoCu) {
        this.chiSoCu = chiSoCu;
    }


    public String getMaLoTrinh() {
        return maLoTrinh;
    }

    public void setMaLoTrinh(String maLoTrinh) {
        this.maLoTrinh = maLoTrinh;
    }

    public String getDiaChi() {
        return getSoNha() + " " + getDuong();
    }


}
