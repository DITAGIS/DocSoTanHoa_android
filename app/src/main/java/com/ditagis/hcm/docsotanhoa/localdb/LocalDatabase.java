package com.ditagis.hcm.docsotanhoa.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ditagis.hcm.docsotanhoa.entities.Code_CSC_SanLuong;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.utities.Flag;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;


public class LocalDatabase extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 2;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "HoaDon_Manager";


    // Tên bảng: HoaDon.
    private static final String TABLE_HOADON = "HoaDon";
    private static final String COLUMN_HOADON_DOT = "HoaDon_Dot";
    private static final String COLUMN_HOADON_DANHBO = "HoaDon_DanhBo";
    private static final String COLUMN_HOADON_KHACHHANG = "HoaDon_TenKhachHang";
    private static final String COLUMN_HOADON_SONHA = "HoaDon_SoNha";
    private static final String COLUMN_HOADON_DUONG = "HoaDon_Duong";
    private static final String COLUMN_HOADON_GIABIEU = "HoaDon_GiaBieu";
    private static final String COLUMN_HOADON_DINHMUC = "HoaDon_DinhMuc";
    private static final String COLUMN_HOADON_KY = "HoaDon_Ky";
    private static final String COLUMN_HOADON_CHISOCU = "HoaDon_ChiSoCu";
    private static final String COLUMN_HOADON_MALOTRINH = "HoaDon_MaLoTrinh";
    private static final String COLUMN_HOADON_SDT = "HoaDon_Sdt";
    private static final String COLUMN_HOADON_CODE1 = "HoaDon_Code1";
    private static final String COLUMN_HOADON_CODE2 = "HoaDon_Code2";
    private static final String COLUMN_HOADON_CODE3 = "HoaDon_Code3";
    private static final String COLUMN_HOADON_CSC1 = "HoaDon_CSC1";
    private static final String COLUMN_HOADON_CSC2 = "HoaDon_CSC2";
    private static final String COLUMN_HOADON_CSC3 = "HoaDon_CSC3";
    private static final String COLUMN_HOADON_SANLUONG1 = "HoaDon_SanLuong1";
    private static final String COLUMN_HOADON_SANLUONG2 = "HoaDon_SanLuong2";
    private static final String COLUMN_HOADON_SANLUONG3 = "HoaDon_SanLuong3";
    private static final String COLUMN_HOADON_FLAG = "HoaDon_Flag";
    private static final String COLUMN_HOADON_CODE_MOI = "HoaDon_CodeMoi";
    private static final String COLUMN_HOADON_CSM = "HoaDon_CSM";
    private static final String COLUMN_HOADON_TTMOI = "HoaDon_TTMoi";
    private static final String COLUMN_HOADON_GHI_CHU = "HoaDon_GhiChu";
    private static final String COLUMN_HOADON_HINH = "HoaDon_Hinh";
    private static final String COLUMN_HOADON_SO_THAN = "HoaDon_SoThan";
    private static final String COLUMN_HOADON_HIEU = "HoaDon_Hieu";
    private static final String COLUMN_HOADON_CO = "HoaDon_Co";
    private static final String COLUMN_HOADON_VI_TRI = "HoaDon_ViTri";


    private static final String TABLE_LUUDANHBO = "LuuDanhBo";

    private static final String COLUMN_LUUDANHBO_DANHBO = "LuuDanhBo_DanhBo";
    private static final String COLUMN_LUUDANHBO_MALOTRINH = "LuuDanhBo_MaLoTrinh";
    private static final String COLUMN_LUUDANHBO_DOT = "LuuDanhBo_Dot";
    private static final String COLUMN_LUUDANHBO_TEN_KH = "LuuDanhBo_TenKH";
    private static final String COLUMN_LUUDANHBO_DIA_CHI = "LuuDanhBo_DiaChi";
    private static final String COLUMN_LUUDANHBO_SDT = "LuuDanhBo_SDT";
    private static final String COLUMN_LUUDANHBO_GIA_BIEU = "LuuDanhBo_GiaBieu";
    private static final String COLUMN_LUUDANHBO_CODE = "LuuDanhBo_Code";
    private static final String COLUMN_LUUDANHBO_CSC = "LuuDanhBo_ChiSoCu";
    private static final String COLUMN_LUUDANHBO_CSM = "LuuDanhBo_ChiSoMoi";
    private static final String COLUMN_LUUDANHBO_TIEU_THU = "LuuDanhBo_TieuThu";
    private static final String COLUMN_LUUDANHBO_GHI_CHU = "LuuDanhBo_GhiChu";
    private static final String COLUMN_LUUDANHBO_HINHANH = "LuuDanhBo_HinhAnh";
    private static final String COLUMN_LUUDANHBO_LUU = "LuuDanhBo_Luu"; // lwu khi có hinh ảnh


    private static final String TABLE_LOGGEDIN = "Login";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";

    private static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context) {
        if (instance == null)
            instance = new LocalDatabase(context.getApplicationContext());
        return instance;
    }

    private LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALOTRINH);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);
        Log.i(TAG, "LocalDatabase.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_HOADON + "("
                + COLUMN_HOADON_DOT + " TEXT,"
                + COLUMN_HOADON_DANHBO + " TEXT PRIMARY KEY,"
                + COLUMN_HOADON_KHACHHANG + " TEXT,"
                + COLUMN_HOADON_SONHA + " TEXT,"
                + COLUMN_HOADON_DUONG + " TEXT,"
                + COLUMN_HOADON_GIABIEU + " TEXT,"
                + COLUMN_HOADON_DINHMUC + " TEXT,"
                + COLUMN_HOADON_KY + " TEXT,"
                + COLUMN_HOADON_CHISOCU + " TEXT,"
                + COLUMN_HOADON_MALOTRINH + " TEXT,"
                + COLUMN_HOADON_SDT + " TEXT,"
                + COLUMN_HOADON_CODE1 + " TEXT,"
                + COLUMN_HOADON_CODE2 + " TEXT,"
                + COLUMN_HOADON_CODE3 + " TEXT,"
                + COLUMN_HOADON_CSC1 + " TEXT,"
                + COLUMN_HOADON_CSC2 + " TEXT,"
                + COLUMN_HOADON_CSC3 + " TEXT,"
                + COLUMN_HOADON_SANLUONG1 + " TEXT,"
                + COLUMN_HOADON_SANLUONG2 + " TEXT,"
                + COLUMN_HOADON_SANLUONG3 + " TEXT,"
                + COLUMN_HOADON_FLAG + " TEXT,"
                + COLUMN_HOADON_CODE_MOI + " TEXT,"
                + COLUMN_HOADON_CSM + " TEXT,"
                + COLUMN_HOADON_TTMOI + " TEXT,"
                + COLUMN_HOADON_GHI_CHU + " TEXT,"
                + COLUMN_HOADON_HINH + " TEXT,"
                + COLUMN_HOADON_SO_THAN + " TEXT,"
                + COLUMN_HOADON_HIEU + " TEXT,"
                + COLUMN_HOADON_CO + " TEXT,"
                + COLUMN_HOADON_VI_TRI + " TEXT" + ")";


        String script2 = "CREATE TABLE " + TABLE_LUUDANHBO + "("
                + COLUMN_LUUDANHBO_DANHBO + " TEXT PRIMARY KEY,"
                + COLUMN_LUUDANHBO_MALOTRINH + " TEXT,"
                + COLUMN_LUUDANHBO_DOT + " TEXT,"
                + COLUMN_LUUDANHBO_TEN_KH + " TEXT,"
                + COLUMN_LUUDANHBO_DIA_CHI + " TEXT,"
                + COLUMN_LUUDANHBO_SDT + " TEXT,"
                + COLUMN_LUUDANHBO_GIA_BIEU + " TEXT,"
                + COLUMN_LUUDANHBO_CODE + " TEXT,"
                + COLUMN_LUUDANHBO_CSC + " TEXT,"
                + COLUMN_LUUDANHBO_CSM + " TEXT,"
                + COLUMN_LUUDANHBO_TIEU_THU + " TEXT,"
                + COLUMN_LUUDANHBO_GHI_CHU + " TEXT,"
                + COLUMN_LUUDANHBO_HINHANH + " TEXT,"
                + COLUMN_LUUDANHBO_LUU + " TEXT )";

        // Chạy lệnh tạo bảng.
        db.execSQL(script);

        db.execSQL(script2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "LocalDatabase.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);
        // Và tạo lại.
        onCreate(db);
    }

    public void create() {
        SQLiteDatabase db = this.getWritableDatabase();

        onCreate(db);
    }

    public void Upgrade() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);

        onCreate(db);
    }

    public boolean addHoaDon(HoaDon hoaDon) {
        //check exist
        if (getHoaDon_UnRead(hoaDon.getDanhBo()) != null)
            return false;
        if (getHoaDon_Read(hoaDon.getDanhBo()) != null)
            return false;
        if (getHoaDon_Synchronized(hoaDon.getDanhBo()) != null)
            return false;
        Log.i(TAG, "LocalDatabase.addHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO " + TABLE_HOADON + " ("
                + COLUMN_HOADON_DOT + ", "
                + COLUMN_HOADON_DANHBO + ", "
                + COLUMN_HOADON_KHACHHANG + ", "
                + COLUMN_HOADON_SONHA + ", "
                + COLUMN_HOADON_DUONG + ", "
                + COLUMN_HOADON_GIABIEU + ", "
                + COLUMN_HOADON_DINHMUC + ", "
                + COLUMN_HOADON_KY + ", "
                + COLUMN_HOADON_CHISOCU + ", "
                + COLUMN_HOADON_MALOTRINH + ", "
                + COLUMN_HOADON_SDT + ", "
                + COLUMN_HOADON_CODE1 + ", "
                + COLUMN_HOADON_CODE2 + ", "
                + COLUMN_HOADON_CODE3 + ", "
                + COLUMN_HOADON_CSC1 + ", "
                + COLUMN_HOADON_CSC2 + ", "
                + COLUMN_HOADON_CSC3 + ", "
                + COLUMN_HOADON_SANLUONG1 + ", "
                + COLUMN_HOADON_SANLUONG2 + ", "
                + COLUMN_HOADON_SANLUONG3 + ", "
                + COLUMN_HOADON_FLAG + ", "
                + COLUMN_HOADON_CODE_MOI + ", "
                + COLUMN_HOADON_CSM + ", "
                + COLUMN_HOADON_TTMOI + ", "
                + COLUMN_HOADON_GHI_CHU + ", "
                + COLUMN_HOADON_HINH + ", "
                + COLUMN_HOADON_SO_THAN + ", "
                + COLUMN_HOADON_HIEU + ", "
                + COLUMN_HOADON_CO + ", "
                + COLUMN_HOADON_VI_TRI
                + ") Values ('" + hoaDon.getDot() + "', '"
                + hoaDon.getDanhBo() + "', '" +
                hoaDon.getTenKhachHang() + "', '" +
                hoaDon.getSoNha() + "', '" +
                hoaDon.getDuong() + "','" +
                hoaDon.getGiaBieu() + "','" +
                hoaDon.getDinhMuc() + "','" +
                hoaDon.getKy() + "','" +
                hoaDon.getChiSoCu() + "','" +
                hoaDon.getMaLoTrinh() + "','" +
                hoaDon.getSdt() + "','" +
                hoaDon.getCode_CSC_SanLuong().getCode1() + "','" +
                hoaDon.getCode_CSC_SanLuong().getCode2() + "','" +
                hoaDon.getCode_CSC_SanLuong().getCode3() + "','" +
                hoaDon.getCode_CSC_SanLuong().getCSC1() + "','" +
                hoaDon.getCode_CSC_SanLuong().getCSC2() + "','" +
                hoaDon.getCode_CSC_SanLuong().getCSC3() + "','" +
                hoaDon.getCode_CSC_SanLuong().getSanLuong1() + "','" +
                hoaDon.getCode_CSC_SanLuong().getSanLuong2() + "','" +
                hoaDon.getCode_CSC_SanLuong().getSanLuong3() + "'," +
                hoaDon.getFlag() + ",'" +
                hoaDon.getCodeMoi() + "','" +
                hoaDon.getChiSoMoi() + "','" +
                hoaDon.getTieuThuMoi() + "','" +
                hoaDon.getGhiChu() + "','" +
                hoaDon.getImage() + "','" +
                hoaDon.getSoThan() + "','" +
                hoaDon.getHieu() + "','" +
                hoaDon.getCo() + "','" +
                hoaDon.getViTri() + "')";
        db.execSQL(sql);

        // Đóng kết nối database.
        db.close();
        return true;
    }

    private HoaDon getHoaDon(String danhBo, int flag) {
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        HoaDon hoaDon = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_DANHBO + " = '" + danhBo + "' and " + COLUMN_HOADON_FLAG + " = " + flag, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            hoaDon = new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(20));
            Code_CSC_SanLuong code_csc_sanLuong = new Code_CSC_SanLuong(cursor.getString(11), cursor.getString(12), cursor.getString(13),
                    cursor.getString(14), cursor.getString(15), cursor.getString(16),
                    cursor.getString(17), cursor.getString(18), cursor.getString(19));
            hoaDon.setCode_CSC_SanLuong(code_csc_sanLuong);
            hoaDon.setCodeMoi(cursor.getString(21));
            hoaDon.setChiSoMoi(cursor.getString(22));
            hoaDon.setTieuThuMoi(cursor.getString(23));
            hoaDon.setGhiChu(cursor.getString(24));
            hoaDon.setImage(cursor.getString(25));
            hoaDon.setSoThan(cursor.getString(26));
            hoaDon.setHieu(cursor.getString(27));
            hoaDon.setCo(cursor.getString(28));
            hoaDon.setViTri(cursor.getString(29));
        }
        return hoaDon;
    }

    public HoaDon getHoaDon_UnRead(String danhBo) {
        return getHoaDon(danhBo, Flag.UNREAD);
    }

    public HoaDon getHoaDon_Read(String danhBo) {
        return getHoaDon(danhBo, Flag.READ);
    }

    public HoaDon getHoaDon_Synchronized(String danhBo) {
        return getHoaDon(danhBo, Flag.SYNCHRONIZED);
    }

    private List<HoaDon> getAllHoaDon(int flag) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_FLAG + " = " + flag, null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(20));
                hoaDon.setMaLoTrinh(cursor.getString(9));
                Code_CSC_SanLuong code_csc_sanLuong = new Code_CSC_SanLuong(cursor.getString(11), cursor.getString(12), cursor.getString(13),
                        cursor.getString(14), cursor.getString(15), cursor.getString(16),
                        cursor.getString(17), cursor.getString(18), cursor.getString(19));
                hoaDon.setCode_CSC_SanLuong(code_csc_sanLuong);
                hoaDon.setCodeMoi(cursor.getString(21));
                hoaDon.setChiSoMoi(cursor.getString(22));
                hoaDon.setTieuThuMoi(cursor.getString(23));
                hoaDon.setGhiChu(cursor.getString(24));
                hoaDon.setImage(cursor.getString(25));
                hoaDon.setSoThan(cursor.getString(26));
                hoaDon.setHieu(cursor.getString(27));
                hoaDon.setCo(cursor.getString(28));
                hoaDon.setViTri(cursor.getString(29));
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        return hoaDons;
    }

    public List<HoaDon> getAllHoaDon_Read(String like) {
        return getAllHoaDon(like, Flag.READ);
    }

    public List<HoaDon> getAllHoaDon_UnRead(String like) {
        return getAllHoaDon(like, Flag.UNREAD);
    }

    public List<HoaDon> getAllHoaDon_Synchronized(String like) {
        return getAllHoaDon(like, Flag.SYNCHRONIZED);
    }

    public List<HoaDon> getAllHoaDon(String like, int flag) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + like + "' and " + COLUMN_HOADON_FLAG + " = " + flag, null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(20));
                hoaDon.setMaLoTrinh(cursor.getString(9));
                Code_CSC_SanLuong code_csc_sanLuong = new Code_CSC_SanLuong(cursor.getString(11), cursor.getString(12), cursor.getString(13),
                        cursor.getString(14), cursor.getString(15), cursor.getString(16),
                        cursor.getString(17), cursor.getString(18), cursor.getString(19));
                hoaDon.setCode_CSC_SanLuong(code_csc_sanLuong);
                hoaDon.setCodeMoi(cursor.getString(21));
                hoaDon.setChiSoMoi(cursor.getString(22));
                hoaDon.setTieuThuMoi(cursor.getString(23));
                hoaDon.setGhiChu(cursor.getString(24));
                hoaDon.setImage(cursor.getString(25));
                hoaDon.setSoThan(cursor.getString(26));
                hoaDon.setHieu(cursor.getString(27));
                hoaDon.setCo(cursor.getString(28));
                hoaDon.setViTri(cursor.getString(29));
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        return hoaDons;
    }

//    public List<HoaDon> getAllHoaDonByMaLoTrinh(String mlt) {
//        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
//        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_HOADON, new String[]{
//                        COLUMN_HOADON_DOT,
//                        COLUMN_HOADON_DANHBO,
//                        COLUMN_HOADON_KHACHHANG,
//                        COLUMN_HOADON_KY,
//                        COLUMN_HOADON_CHISOCU,
//                        COLUMN_HOADON_MALOTRINH,
//                        COLUMN_HOADON_SONHA,
//                        COLUMN_HOADON_DUONG,
//                        COLUMN_HOADON_GIABIEU,
//                        COLUMN_HOADON_DINHMUC,
//                        COLUMN_HOADON_SDT,
//                        COLUMN_HOADON_CODE1,
//                        COLUMN_HOADON_CODE2,
//                        COLUMN_HOADON_CODE3,
//                        COLUMN_HOADON_CSC1,
//                        COLUMN_HOADON_CSC2,
//                        COLUMN_HOADON_CSC3,
//                        COLUMN_HOADON_SANLUONG1,
//                        COLUMN_HOADON_SANLUONG2,
//                        COLUMN_HOADON_SANLUONG3
//                }, COLUMN_HOADON_MALOTRINH + "=?",
//                new String[]{String.valueOf(mlt)}, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                HoaDon hoaDon = new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
//                        cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(20));
//                Code_CSC_SanLuong code_csc_sanLuong = new Code_CSC_SanLuong(cursor.getString(11), cursor.getString(12), cursor.getString(13),
//                        cursor.getString(14), cursor.getString(15), cursor.getString(16),
//                        cursor.getString(17), cursor.getString(18), cursor.getString(19));
//                hoaDon.setCode_CSC_SanLuong(code_csc_sanLuong);
//                hoaDons.add(hoaDon);
//            } while (cursor.moveToNext());
//        }
//        return hoaDons;
//    }

    public boolean updateHoaDonUnRead(HoaDon hoadon) {
        return updateHoaDon(hoadon, Flag.READ, Flag.UNREAD);
    }

    public boolean updateHoaDonRead(HoaDon hoadon) {
        return updateHoaDon(hoadon, Flag.READ, Flag.READ);
    }

    public boolean updateHoaDonSynchronized(HoaDon hoaDon) {
        return updateHoaDon(hoaDon, Flag.SYNCHRONIZED, Flag.READ);
    }

    private boolean updateHoaDon(HoaDon hoaDon, int flag, int flag_old) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_HOADON_DOT, hoaDon.getDot());
//        values.put(COLUMN_HOADON_DANHBO, hoaDon.getDanhBo());
//        values.put(COLUMN_HOADON_KHACHHANG, hoaDon.getTenKhachHang());
//        values.put(COLUMN_HOADON_SONHA, hoaDon.getSoNha());
//        values.put(COLUMN_HOADON_DUONG, hoaDon.getDuong());
//        values.put(COLUMN_HOADON_GIABIEU, hoaDon.getGiaBieu());
//        values.put(COLUMN_HOADON_DINHMUC, hoaDon.getCodeMoi());
//        values.put(COLUMN_HOADON_KY, hoaDon.getKy());
//        values.put(COLUMN_HOADON_CHISOCU, hoaDon.getChiSoCu());
//        values.put(COLUMN_HOADON_MALOTRINH, hoaDon.getMaLoTrinh());
//        values.put(COLUMN_HOADON_SDT, hoaDon.getSdt());
//        values.put(COLUMN_HOADON_CODE1, hoaDon.getCode_CSC_SanLuong().getCode1());
//        values.put(COLUMN_HOADON_CODE2, hoaDon.getCode_CSC_SanLuong().getCode2());
//        values.put(COLUMN_HOADON_CODE3, hoaDon.getCode_CSC_SanLuong().getCode3());
//        values.put(COLUMN_HOADON_CSC1,hoaDon.getCode_CSC_SanLuong().getCSC1());
//        values.put(COLUMN_HOADON_CSC2,hoaDon.getCode_CSC_SanLuong().getCSC2());
//        values.put(COLUMN_HOADON_CSC3,hoaDon.getCode_CSC_SanLuong().getCSC3());
//        values.put(COLUMN_HOADON_SANLUONG1,hoaDon.getCode_CSC_SanLuong().getSanLuong1());
//        values.put(COLUMN_HOADON_SANLUONG2,hoaDon.getCode_CSC_SanLuong().getSanLuong2());
//        values.put(COLUMN_HOADON_SANLUONG3,hoaDon.getCode_CSC_SanLuong().getSanLuong3());
//        values.put(COLUMN_HOADON_FLAG, flag);
//        values.put(COLUMN_HOADON_CODE_MOI, hoaDon.getCodeMoi());
//        values.put(COLUMN_HOADON_CSM, hoaDon.getChiSoMoi());
//        values.put(COLUMN_HOADON_TTMOI, hoaDon.getTieuThuMoi());
//        values.put(COLUMN_HOADON_GHI_CHU, hoaDon.getGhiChu());
//        values.put(COLUMN_HOADON_HINH, hoaDon.getImage());
//        // Trèn một dòng dữ liệu vào bảng.
//        long result = db.update(TABLE_HOADON, values, COLUMN_HOADON_DANHBO + " = ? and "
//                + COLUMN_HOADON_FLAG + " = ?" , new String[]{hoaDon.getDanhBo(), Flag.UNREAD + ""});

        String sql = "update " + TABLE_HOADON + " set " +
                COLUMN_HOADON_FLAG + " = " + flag + "," +
                COLUMN_HOADON_CODE_MOI + " = '" + hoaDon.getCodeMoi() + "'," +
                COLUMN_HOADON_CSM + " = '" + hoaDon.getChiSoMoi() + "'," +
                COLUMN_HOADON_TTMOI + " = '" + hoaDon.getTieuThuMoi() + "'," +
                COLUMN_HOADON_GHI_CHU + " = '" + hoaDon.getGhiChu() + "'," +
                COLUMN_HOADON_HINH + " = '" + hoaDon.getImage() + "'" +
                " where " +
                COLUMN_HOADON_DANHBO + " ='" + hoaDon.getDanhBo() + "' and " +
                COLUMN_HOADON_FLAG + " = " + flag_old;

        db.execSQL(sql);
        // Đóng kết nối database.
        db.close();
        return true;
    }

    public boolean updateHoaDon_without_csm(HoaDon hoaDon, int flag_old) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "update " + TABLE_HOADON + " set " +
                COLUMN_HOADON_CODE_MOI + " = '" + hoaDon.getCodeMoi() + "'," +
                COLUMN_HOADON_SDT + " = '" + hoaDon.getSdt() + "'," +
                COLUMN_HOADON_GHI_CHU + " = '" + hoaDon.getGhiChu() + "'," +
                COLUMN_HOADON_HINH + " = '" + hoaDon.getImage() + "'" +
                " where " +
                COLUMN_HOADON_DANHBO + " ='" + hoaDon.getDanhBo() + "' and " +
                COLUMN_HOADON_FLAG + " = " + flag_old;

        db.execSQL(sql);
        // Đóng kết nối database.
        db.close();
        return true;
    }

    public void deleteHoaDon(String danhBo) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + danhBo);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOADON, COLUMN_HOADON_DANHBO + " = ?",
                new String[]{danhBo});
        db.close();
    }

//    public void deleteDanhBo_CSM(DanhBo_ChiSoMoi danhBo_chiSoMoi) {
//        Log.i(TAG, "LocalDatabase.updateDanhBoChiSoMoi ... " + danhBo_chiSoMoi.getDanhBo());
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_LUUDANHBO, COLUMN_LUUDANHBO_DANHBO + " = ?",
//                new String[]{danhBo_chiSoMoi.getDanhBo()});
//        db.close();
//    }

//    public ArrayList<DanhBo_ChiSoMoi> getAllDanhBo_CSM() {
//        Log.i(TAG, "LocalDatabase.getAllDanhbo_ChiSoMoi ... ");
//
//        ArrayList<DanhBo_ChiSoMoi> danhBoChiSoMois = new ArrayList<DanhBo_ChiSoMoi>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_LUUDANHBO;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        // Duyệt trên con trỏ, và thêm vào danh sách.
//        if (cursor.moveToFirst()) {
//            do {
//                DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(cursor.getString(0),
//                        cursor.getString(1),
//                        cursor.getString(2),
//                        cursor.getString(3),
//                        cursor.getString(4),
//                        cursor.getString(5),
//                        cursor.getString(6),
//                        cursor.getString(7),
//                        cursor.getString(8),
//                        cursor.getString(9),
//                        cursor.getString(10),
//                        cursor.getString(11),
//                        cursor.getString(12),
//                        cursor.getInt(13));
//
//
//                // Thêm vào danh sách.
//                danhBoChiSoMois.add(danhBo_chiSoMoi);
//            } while (cursor.moveToNext());
//        }
//
//        // return hoaDon list
//        return danhBoChiSoMois;
//    }

//    public boolean updateDanhBo_CSM(DanhBo_ChiSoMoi danhBoChiSoMoi) {
////        Log.i(TAG, "LocalDatabase.addHoaDon ... " + danhBoChiSoMoi.getDanhBo());
////
////        SQLiteDatabase db = this.getWritableDatabase();
////
////        ContentValues values = new ContentValues();
////
////        values.put(COLUMN_LUUDANHBO_DANHBO, danhBoChiSoMoi.getDanhBo());
////        values.put(COLUMN_LUUDANHBO_MALOTRINH, danhBoChiSoMoi.getMaLoTrinh());
////        values.put(COLUMN_LUUDANHBO_DOT, danhBoChiSoMoi.getDot());
////        values.put(COLUMN_LUUDANHBO_TEN_KH, danhBoChiSoMoi.getTenKH());
////        values.put(COLUMN_LUUDANHBO_DIA_CHI, danhBoChiSoMoi.getDiaChi());
////        values.put(COLUMN_LUUDANHBO_SDT, danhBoChiSoMoi.getSdt());
////        values.put(COLUMN_LUUDANHBO_GIA_BIEU, danhBoChiSoMoi.getGiaBieu());
////        values.put(COLUMN_LUUDANHBO_CODE, danhBoChiSoMoi.getCode());
////        values.put(COLUMN_LUUDANHBO_CSC, danhBoChiSoMoi.getChiSoCu());
////        values.put(COLUMN_LUUDANHBO_CSM, danhBoChiSoMoi.getChiSoMoi());
////        values.put(COLUMN_LUUDANHBO_TIEU_THU, danhBoChiSoMoi.getTieuThu());
////        values.put(COLUMN_LUUDANHBO_GHI_CHU, danhBoChiSoMoi.getNote());
////        values.put(COLUMN_LUUDANHBO_HINHANH, danhBoChiSoMoi.getImage());
////        values.put(COLUMN_LUUDANHBO_LUU, danhBoChiSoMoi.getHasImage());
////        // Trèn một dòng dữ liệu vào bảng.
////        long result = db.update(TABLE_LUUDANHBO, values, COLUMN_LUUDANHBO_DANHBO + " = " + danhBoChiSoMoi.getDanhBo(), null);
////        // Đóng kết nối database.
////        db.close();
////        return result > 0;
////    }
//
////    public boolean saveDanhBo_CSM(DanhBo_ChiSoMoi danhBoChiSoMoi) {
////        Log.i(TAG, "LocalDatabase.addHoaDon ... " + danhBoChiSoMoi.getDanhBo());
////
////        SQLiteDatabase db = this.getWritableDatabase();
////
////        ContentValues values = new ContentValues();
////
////        values.put(COLUMN_LUUDANHBO_DANHBO, danhBoChiSoMoi.getDanhBo());
////        values.put(COLUMN_LUUDANHBO_MALOTRINH, danhBoChiSoMoi.getMaLoTrinh());
////        values.put(COLUMN_LUUDANHBO_DOT, danhBoChiSoMoi.getDot());
////        values.put(COLUMN_LUUDANHBO_TEN_KH, danhBoChiSoMoi.getTenKH());
////        values.put(COLUMN_LUUDANHBO_DIA_CHI, danhBoChiSoMoi.getDiaChi());
////        values.put(COLUMN_LUUDANHBO_SDT, danhBoChiSoMoi.getSdt());
////        values.put(COLUMN_LUUDANHBO_GIA_BIEU, danhBoChiSoMoi.getGiaBieu());
////        values.put(COLUMN_LUUDANHBO_CODE, danhBoChiSoMoi.getCode());
////        values.put(COLUMN_LUUDANHBO_CSC, danhBoChiSoMoi.getChiSoCu());
////        values.put(COLUMN_LUUDANHBO_CSM, danhBoChiSoMoi.getChiSoMoi());
////        values.put(COLUMN_LUUDANHBO_TIEU_THU, danhBoChiSoMoi.getTieuThu());
////        values.put(COLUMN_LUUDANHBO_GHI_CHU, danhBoChiSoMoi.getNote());
////        values.put(COLUMN_LUUDANHBO_HINHANH, danhBoChiSoMoi.getImage());
////        values.put(COLUMN_LUUDANHBO_LUU, danhBoChiSoMoi.getHasImage());
////        // Trèn một dòng dữ liệu vào bảng.
////        long result = db.insert(TABLE_LUUDANHBO, null, values);
////
////
////        // Đóng kết nối database.
////        db.close();
////        return result > 0;
////    }
//
////    public DanhBo_ChiSoMoi getDanhBo_CSM(String danhBo) {
////        ArrayList<HoaDon> hoaDonList = new ArrayList<HoaDon>();
////        // Select All Query
////        String selectQuery = "SELECT *" +
////                " FROM " + TABLE_LUUDANHBO +
////                " where " + this.COLUMN_LUUDANHBO_DANHBO + "='" + danhBo + "'";
////
////        SQLiteDatabase db = this.getWritableDatabase();
////        Cursor cursor = db.rawQuery(selectQuery, null);
////
////
////        // Duyệt trên con trỏ, và thêm vào danh sách.
////        if (cursor.moveToFirst()) {
////            do {
////                return new DanhBo_ChiSoMoi(cursor.getString(0),
////                        cursor.getString(1),
////                        cursor.getString(2),
////                        cursor.getString(3),
////                        cursor.getString(4),
////                        cursor.getString(5),
////                        cursor.getString(6),
////                        cursor.getString(7),
////                        cursor.getString(8),
////                        cursor.getString(9),
////                        cursor.getString(10),
////                        cursor.getString(11),
////                        cursor.getString(12),
////                        cursor.getInt(13)
////                );
////            } while (cursor.moveToNext());
////        }
////        return null;
////    }
//
////    public boolean getStateDanhBo_CSM(String danhBo) {
////        ArrayList<HoaDon> hoaDonList = new ArrayList<HoaDon>();
////        // Select All Query
////        String selectQuery = "SELECT " + this.COLUMN_LUUDANHBO_LUU +
////                " FROM " + TABLE_LUUDANHBO +
////                " where " + this.COLUMN_LUUDANHBO_DANHBO + "='" + danhBo + "'";
////
////        SQLiteDatabase db = this.getWritableDatabase();
////        Cursor cursor = db.rawQuery(selectQuery, null);
////
////
////        // Duyệt trên con trỏ, và thêm vào danh sách.
////        if (cursor.moveToFirst()) {
////            do {
////                return cursor.getInt(0) > 0;
////            } while (cursor.moveToNext());
////        }
////        return false;
////    }
}