package com.ditagis.hcm.docsotanhoa.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ditagis.hcm.docsotanhoa.entities.DanhBo_ChiSoMoi;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.LoTrinh;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String COLUMN_HOADON_CULY = "HoaDon_CuLy";
    private static final String COLUMN_HOADON_KHACHHANG = "HoaDon_TenKhachHang";
    private static final String COLUMN_HOADON_KY = "HoaDon_Ky";
    private static final String COLUMN_HOADON_CODE = "HoaDon_Code";
    private static final String COLUMN_HOADON_CHISOCU = "HoaDon_ChiSoCu";
    private static final String COLUMN_HOADON_CHISOMOI = "HoaDon_ChiSoMoi";
    private static final String COLUMN_HOADON_MALOTRINH = "HoaDon_MaLoTrinh";
    private static final String COLUMN_HOADON_SONHA = "HoaDon_SoNha";
    private static final String COLUMN_HOADON_DUONG = "HoaDon_Duong";
    private static final String COLUMN_HOADON_GIABIEU = "HoaDon_GiaBieu";
    private static final String COLUMN_HOADON_DINHMUC = "HoaDon_DinhMuc";

    private static final String TABLE_MALOTRINH = "LoTrinh";
    private static final String COLUMN_MALOTRINH_ID = "MaLoTrinh_ID";
    private static final String COLUMN_MALOTRINH_SOLUONG = "MaLoTrinh_SoLuong";
//    private static final String COLUMN_MALOTRINH_SONHA = "MaLoTrinh_SoNha";
//    private static final String COLUMN_MALOTRINH_DUONG = "MaLoTrinh_Duong";
//    private static final String COLUMN_MALOTRINH_GIABIEU = "MaLoTrinh_GiaBieu";
//    private static final String COLUMN_MALOTRINH_DINHMUC = "MaloTrinh_DinhMuc";

    private static final String TABLE_LUUDANHBO = "LuuDanhBo";

    private static final String COLUMN_LUUDANHBO_DANHBO = "LuuDanhBo_DanhBo";
    private static final String COLUMN_LUUDANHBO_MALOTRINH = "LuuDanhBo_MaLoTrinh";
    private static final String COLUMN_LUUDANHBO_TEN_KH = "LuuDanhBo_TenKH";
    private static final String COLUMN_LUUDANHBO_DIA_CHI = "LuuDanhBo_DiaChi";
    private static final String COLUMN_LUUDANHBO_SDT = "LuuDanhBo_SDT";
    private static final String COLUMN_LUUDANHBO_CODE = "LuuDanhBo_Code";
    private static final String COLUMN_LUUDANHBO_CSC = "LuuDanhBo_ChiSoCu";
    private static final String COLUMN_LUUDANHBO_CSM = "LuuDanhBo_ChiSoMoi";
    private static final String COLUMN_LUUDANHBO_GHI_CHU = "LuuDanhBo_GhiChu";
    private static final String COLUMN_LUUDANHBO_HINHANH = "LuuDanhBo_HinhAnh";
    private static final String COLUMN_LUUDANHBO_LUU = "LuuDanhBo_Luu"; // lwu khi có hinh ảnh


    private static final String TABLE_LOGGEDIN = "Login";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";

    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALOTRINH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);
        Log.i(TAG, "LocalDatabase.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_HOADON + "("
                + COLUMN_HOADON_DOT + " TEXT,"
                + COLUMN_HOADON_DANHBO + " TEXT PRIMARY KEY,"
                + COLUMN_HOADON_KHACHHANG + " TEXT,"
                + COLUMN_HOADON_KY + " TEXT,"
                + COLUMN_HOADON_CODE + " TEXT,"
                + COLUMN_HOADON_CHISOCU + " TEXT,"
                + COLUMN_HOADON_CHISOMOI + " TEXT,"
                + COLUMN_HOADON_MALOTRINH + " TEXT,"
                + COLUMN_HOADON_SONHA + " TEXT,"
                + COLUMN_HOADON_DUONG + " TEXT,"
                + COLUMN_HOADON_GIABIEU + " TEXT,"
                + COLUMN_HOADON_DINHMUC + " TEXT" + ")";
        // Chạy lệnh tạo bảng.
        String script1 = "CREATE TABLE " + TABLE_MALOTRINH + "("
                + COLUMN_MALOTRINH_ID + " TEXT PRIMARY KEY,"
                + COLUMN_MALOTRINH_SOLUONG + " INTEGER )";

        String script2 = "CREATE TABLE " + TABLE_LUUDANHBO + "("
                + COLUMN_LUUDANHBO_DANHBO + " TEXT PRIMARY KEY,"
                + COLUMN_LUUDANHBO_MALOTRINH + " TEXT,"
                + COLUMN_LUUDANHBO_TEN_KH + " TEXT,"
                + COLUMN_LUUDANHBO_DIA_CHI + " TEXT,"
                + COLUMN_LUUDANHBO_SDT + " TEXT,"
                + COLUMN_LUUDANHBO_CODE + " TEXT,"
                + COLUMN_LUUDANHBO_CSC + " TEXT,"
                + COLUMN_LUUDANHBO_CSM + " TEXT,"
                + COLUMN_LUUDANHBO_GHI_CHU + " TEXT,"
                + COLUMN_LUUDANHBO_HINHANH + " TEXT,"
                + COLUMN_LUUDANHBO_LUU + " TEXT )";

        // Chạy lệnh tạo bảng.
        String script3 = "CREATE TABLE " + TABLE_LOGGEDIN + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " INTEGER )";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        db.execSQL(script1);
        db.execSQL(script2);
        db.execSQL(script3);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "LocalDatabase.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALOTRINH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);
        // Và tạo lại.
        onCreate(db);
    }

    public void Upgrade() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALOTRINH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);

        onCreate(db);
    }


    public void addLoTrinh(LoTrinh loTrinh) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MALOTRINH_ID, loTrinh.getMaLoTrinh());
        values.put(COLUMN_MALOTRINH_SOLUONG, loTrinh.getSoLuong());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_MALOTRINH, null, values);


        // Đóng kết nối database.
        db.close();
    }

    public boolean addHoaDon(HoaDon hoaDon) {
        Log.i(TAG, "LocalDatabase.addHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_HOADON_CHISOCU, hoaDon.getChiSoCu());
        values.put(COLUMN_HOADON_CHISOMOI, "");
        values.put(COLUMN_HOADON_CODE, "");
        values.put(COLUMN_HOADON_DANHBO, hoaDon.getDanhBo());
        values.put(COLUMN_HOADON_DOT, hoaDon.getDot());
        values.put(COLUMN_HOADON_KHACHHANG, hoaDon.getTenKhachHang());
        values.put(COLUMN_HOADON_KY, hoaDon.getKy());
        values.put(COLUMN_HOADON_MALOTRINH, hoaDon.getMaLoTrinh());
        values.put(COLUMN_HOADON_SONHA, hoaDon.getSoNha());
        values.put(COLUMN_HOADON_DUONG, hoaDon.getDuong());
        values.put(COLUMN_HOADON_GIABIEU, hoaDon.getGiaBieu());
        values.put(COLUMN_HOADON_DINHMUC, hoaDon.getDinhMuc());

        // Trèn một dòng dữ liệu vào bảng.
        long result = db.insert(TABLE_HOADON, null, values);


        // Đóng kết nối database.
        db.close();
        return result > 0;
    }

    public HoaDon getHoaDon(String danhbo) {
        Log.i(TAG, "LocalDatabase.getHoaDon ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HOADON, new String[]{

                        COLUMN_HOADON_DOT,
                        COLUMN_HOADON_DANHBO,
                        COLUMN_HOADON_KHACHHANG,
                        COLUMN_HOADON_KY,
                        COLUMN_HOADON_CODE,
                        COLUMN_HOADON_CHISOCU,
                        COLUMN_HOADON_CHISOMOI,
                        COLUMN_HOADON_MALOTRINH,
                        COLUMN_HOADON_SONHA,
                        COLUMN_HOADON_DUONG,
                        COLUMN_HOADON_GIABIEU,
                        COLUMN_HOADON_DINHMUC
                }, COLUMN_HOADON_DANHBO + "=?",
                new String[]{String.valueOf(danhbo)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HoaDon hoaDon = new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));
        return hoaDon;
    }

    public HashMap<String, Integer> getAllMLT() {
        Log.i(TAG, "LocalDatabase.getAllHoaDons ... ");
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        List<LoTrinh> loTrinhs = getAllMaLoTrinh();
        String mlt;
        for (LoTrinh loTrinh : loTrinhs) {
            result.put(loTrinh.getMaLoTrinh(), loTrinh.getSoLuong());
        }

        // return hoaDon list
        return result;
    }

    public List<LoTrinh> getAllMaLoTrinh() {
        Log.i(TAG, "LocalDatabase.getAllHoaDons ... ");

        List<LoTrinh> loTrinhs = new ArrayList<LoTrinh>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MALOTRINH;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                LoTrinh mlt = new LoTrinh();
                mlt.setMaLoTrinh(cursor.getString(0));
                mlt.setSoLuong(Integer.parseInt(cursor.getString(1)));

                // Thêm vào danh sách.
                loTrinhs.add(mlt);
            } while (cursor.moveToNext());
        }

        // return hoaDon list
        return loTrinhs;
    }


    public List<HoaDon> getAllHoaDonByMaLoTrinh(String mlt) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HOADON, new String[]{
                        COLUMN_HOADON_DOT,
                        COLUMN_HOADON_DANHBO,
                        COLUMN_HOADON_KHACHHANG,
                        COLUMN_HOADON_KY,
                        COLUMN_HOADON_CODE,
                        COLUMN_HOADON_CHISOCU,
                        COLUMN_HOADON_CHISOMOI,
                        COLUMN_HOADON_MALOTRINH,
                        COLUMN_HOADON_SONHA,
                        COLUMN_HOADON_DUONG,
                        COLUMN_HOADON_GIABIEU,
                        COLUMN_HOADON_DINHMUC
                }, COLUMN_HOADON_MALOTRINH + "=?",
                new String[]{String.valueOf(mlt)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        return hoaDons;
    }

    public void deleteHoaDon(HoaDon hoaDon) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOADON, COLUMN_HOADON_DANHBO + " = ?",
                new String[]{String.valueOf(hoaDon.getDanhBo())});
        db.close();
    }

    public void deleteDanhBo_CSM(DanhBo_ChiSoMoi danhBo_chiSoMoi) {
        Log.i(TAG, "LocalDatabase.updateDanhBoChiSoMoi ... " + danhBo_chiSoMoi.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LUUDANHBO, COLUMN_LUUDANHBO_DANHBO + " = ?",
                new String[]{danhBo_chiSoMoi.getDanhBo()});
        db.close();
    }

    public void deleteMLT(String mlt) {
        Log.i(TAG, "LocalDatabase.updateMaLoTrinh ... " + mlt);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MALOTRINH, COLUMN_MALOTRINH_ID + " = ?",
                new String[]{mlt});
        db.close();
    }

    public void addAllHoaDon(List<HoaDon> hoaDons) {
        for (HoaDon hd : hoaDons
                ) {
            this.addHoaDon(hd);
        }
    }

    public ArrayList<DanhBo_ChiSoMoi> getAllDanhBo_CSM() {
        Log.i(TAG, "LocalDatabase.getAllDanhbo_ChiSoMoi ... ");

        ArrayList<DanhBo_ChiSoMoi> danhBoChiSoMois = new ArrayList<DanhBo_ChiSoMoi>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LUUDANHBO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                DanhBo_ChiSoMoi danhBo_chiSoMoi = new DanhBo_ChiSoMoi(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getInt(10));


                // Thêm vào danh sách.
                danhBoChiSoMois.add(danhBo_chiSoMoi);
            } while (cursor.moveToNext());
        }

        // return hoaDon list
        return danhBoChiSoMois;
    }

    public boolean saveDanhBo_CSM(DanhBo_ChiSoMoi danhBoChiSoMoi) {
        Log.i(TAG, "LocalDatabase.addHoaDon ... " + danhBoChiSoMoi.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_LUUDANHBO_DANHBO, danhBoChiSoMoi.getDanhBo());
        values.put(COLUMN_LUUDANHBO_MALOTRINH, danhBoChiSoMoi.getMaLoTrinh());
        values.put(COLUMN_LUUDANHBO_TEN_KH, danhBoChiSoMoi.getTenKH());
        values.put(COLUMN_LUUDANHBO_DIA_CHI, danhBoChiSoMoi.getDiaChi());
        values.put(COLUMN_LUUDANHBO_SDT, danhBoChiSoMoi.getSdt());
        values.put(COLUMN_LUUDANHBO_CODE, danhBoChiSoMoi.getCode());
        values.put(COLUMN_LUUDANHBO_CSC, danhBoChiSoMoi.getChiSoCu());
        values.put(COLUMN_LUUDANHBO_CSM, danhBoChiSoMoi.getChiSoMoi());
        values.put(COLUMN_LUUDANHBO_GHI_CHU, danhBoChiSoMoi.getNote());
        values.put(COLUMN_LUUDANHBO_HINHANH, danhBoChiSoMoi.getImage());
        values.put(COLUMN_LUUDANHBO_LUU, danhBoChiSoMoi.getHasImage());
        // Trèn một dòng dữ liệu vào bảng.
        long result = db.insert(TABLE_LUUDANHBO, null, values);


        // Đóng kết nối database.
        db.close();
        return result > 0;
    }

    public DanhBo_ChiSoMoi getDanhBo_CSM(String danhBo) {
        ArrayList<HoaDon> hoaDonList = new ArrayList<HoaDon>();
        // Select All Query
        String selectQuery = "SELECT *" +
                " FROM " + TABLE_LUUDANHBO +
                " where " + this.COLUMN_LUUDANHBO_DANHBO + "='" + danhBo + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                return new DanhBo_ChiSoMoi(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getInt(10)
                );
            } while (cursor.moveToNext());
        }
        return null;
    }

    public boolean getStateDanhBo_CSM(String danhBo) {
        ArrayList<HoaDon> hoaDonList = new ArrayList<HoaDon>();
        // Select All Query
        String selectQuery = "SELECT " + this.COLUMN_LUUDANHBO_LUU +
                " FROM " + TABLE_LUUDANHBO +
                " where " + this.COLUMN_LUUDANHBO_DANHBO + "='" + danhBo + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                return cursor.getInt(0) > 0;
            } while (cursor.moveToNext());
        }
        return false;
    }
}