package com.ditagis.hcm.docsotanhoa.entities;

/**
 * @author fuhi
 *
 */
public class HoaDon {
	private int id;
	private String khu;
	private String dot;
	private String danhBo;
	private String cuLy;
	private String hopDong;
	private String tenKhachHang;
	private String soNha;
	private String duong;
	private String giaBieu;
	private String dinhMuc;
	private String ky;
	private String nam;
	private String code;
	private String codeFU;
	private String chiSoCu;
	private String chiSoMoi;
	private String quan;
	private String phuong;
	private String maLoTrinh;
	public HoaDon() {
		super();

	}

	public HoaDon(int id, String dot, String danhBo, String tenKhachHang, String ky, String code, String chiSoCu, String chiSoMoi, String maLoTrinh,String soNha,String duong,String giaBieu,String dinhMuc) {
		this.id = id;
		this.dot = dot;
		this.danhBo = danhBo;
		this.tenKhachHang = tenKhachHang;
		this.ky = ky;
		this.code = code;
		this.chiSoCu = chiSoCu;
		this.chiSoMoi = chiSoMoi;
		this.maLoTrinh = maLoTrinh;
		this.soNha = soNha;this.duong = duong;this.giaBieu =  giaBieu;this.dinhMuc=dinhMuc;
	}

	public HoaDon(String danhBo, String tenKhachHang, String soNha, String duong, String ky, String nam, String code,
				  String chiSoCu, String chiSoMoi, String maLoTrinh) {
		super();
		this.danhBo = danhBo;
		this.tenKhachHang = tenKhachHang;
		this.soNha = soNha;
		this.duong = duong;
		this.ky = ky;
		this.nam = nam;
		this.code = code;
		this.chiSoCu = chiSoCu;
		this.chiSoMoi = chiSoMoi;
		this.maLoTrinh = maLoTrinh;
	}

	public HoaDon(int id, String khu, String dot, String danhBo, String cuLy, String hopDong, String tenKhachHang,
			String soNha, String duong, String giaBieu, String dinhMuc, String ky, String nam, String code,
			String codeFU, String chiSoCu, String chiSoMoi, String quan, String phuong, String maLoTrinh) {
		super();
		this.id = id;
		this.khu = khu;
		this.dot = dot;
		this.danhBo = danhBo;
		this.cuLy = cuLy;
		this.hopDong = hopDong;
		this.tenKhachHang = tenKhachHang;
		this.soNha = soNha;
		this.duong = duong;
		this.giaBieu = giaBieu;
		this.dinhMuc = dinhMuc;
		this.ky = ky;
		this.nam = nam;
		this.code = code;
		this.codeFU = codeFU;
		this.chiSoCu = chiSoCu;
		this.chiSoMoi = chiSoMoi;
		this.quan = quan;
		this.phuong = phuong;
		this.maLoTrinh = maLoTrinh;
	}
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKhu() {
		return khu;
	}
	public void setKhu(String khu) {
		this.khu = khu;
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
	public String getCuLy() {
		return cuLy;
	}
	public void setCuLy(String cuLy) {
		this.cuLy = cuLy;
	}
	public String getHopDong() {
		return hopDong;
	}
	public void setHopDong(String hopDong) {
		this.hopDong = hopDong;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeFU() {
		return codeFU;
	}
	public void setCodeFU(String codeFU) {
		this.codeFU = codeFU;
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
	public String getQuan() {
		return quan;
	}
	public void setQuan(String quan) {
		this.quan = quan;
	}
	public String getPhuong() {
		return phuong;
	}
	public void setPhuong(String phuong) {
		this.phuong = phuong;
	}
	public String getMaLoTrinh() {
		return maLoTrinh;
	}
	public void setMaLoTrinh(String maLoTrinh) {
		this.maLoTrinh = maLoTrinh;
	}

	public String getDiaChi() {
			return getSoNha() + " " + getDuong() ;
	}

	@Override
	public String toString() {
		return "HoaDon [id=" + id + ", khu=" + khu + ", dot=" + dot + ", danhBo=" + danhBo + ", cuLy=" + cuLy
				+ ", hopDong=" + hopDong + ", tenKhachHang=" + tenKhachHang + ", soNha=" + soNha + ", duong=" + duong
				+ ", giaBieu=" + giaBieu + ", dinhMuc=" + dinhMuc + ", ky=" + ky + ", nam=" + nam + ", code=" + code
				+ ", codeFU=" + codeFU + ", chiSoCu=" + chiSoCu + ", chiSoMoi=" + chiSoMoi + ", quan=" + quan
				+ ", phuong=" + phuong + ", maLoTrinh=" + maLoTrinh + "]";
	}
	
	
}
