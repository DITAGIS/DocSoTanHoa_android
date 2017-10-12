package com.ditagis.hcm.docsotanhoa.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String COLUMN_HOADON_ID = "HoaDon_Id";
    private static final String COLUMN_HOADON_DOT = "HoaDon_Dot";
    private static final String COLUMN_HOADON_DANHBO = "HoaDon_DanhBo";
    private static final String COLUMN_HOADON_CULY = "HoaDon_CuLy";
    private static final String COLUMN_HOADON_KHACHHANG = "HoaDon_TenKhachHang";
    private static final String COLUMN_HOADON_KY = "HoaDon_Ky";
    private static final String COLUMN_HOADON_CODE = "HoaDon_Code";
    private static final String COLUMN_HOADON_CHISOCU = "HoaDon_ChiSoCu";
    private static final String COLUMN_HOADON_CHISOMOI = "HoaDon_ChiSoMoi";
    private static final String COLUMN_HOADON_MALOTRINH = "HoaDon_MaLoTrinh";

    private static final String TABLE_MALOTRINH = "LoTrinh";
    private static final String COLUMN_MALOTRINH_ID = "MaLoTrinh_ID";
    private static final String COLUMN_MALOTRINH_SOLUONG = "MaLoTrinh_SoLuong";
    private static final String COLUMN_HOADON_SONHA = "MaLoTrinh_SoNha";
    private static final String COLUMN_HOADON_DUONG = "MaLoTrinh_Duong";
    private static final String COLUMN_HOADON_GIABIEU = "MaLoTrinh_GiaBieu";
    private static final String COLUMN_HOADON_DINHMUC = "MaloTrinh_DinhMuc";

    private static final String TABLE_LUUDANHBO = "LuuDanhBo";
    private static final String COLUMN_LUUDANHBO_DANHBO = "LuuDanhBo_DanhBo";
    private static final String COLUMN_LUUDANHBO_LUU = "LuuDanhBo_Luu";

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
                + COLUMN_HOADON_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_HOADON_DOT + " TEXT,"
                + COLUMN_HOADON_DANHBO + " TEXT,"
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
                + COLUMN_LUUDANHBO_LUU + " BIT )";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        db.execSQL(script1);
        db.execSQL(script2);
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

    public void Create() {
        SQLiteDatabase db = this.getWritableDatabase();
        String script = "CREATE TABLE " + TABLE_HOADON + "("
                + COLUMN_HOADON_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_HOADON_DOT + " TEXT,"
                + COLUMN_HOADON_DANHBO + " TEXT,"
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
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        db.execSQL(script1);
    }

    // Nếu trong bảng HoaDon chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultHoaDonsIfNeed() {
//        int count = this.getHoaDonsCount();
//        if(count ==0 ) {
//            HoaDon hoaDon1 = new HoaDon("Firstly see Android ListView",
//                    "See Android ListView Example in o7planning.org");
//            HoaDon hoaDon2 = new HoaDon("Learning Android SQLite",
//                    "See Android SQLite Example in o7planning.org");
//            this.addHoaDon(hoaDon1);
//            this.addHoaDon(hoaDon2);
//        }
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
        Log.i(TAG, "LocalDatabase.addHoaDon ... " + hoaDon.getId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOADON_ID, hoaDon.getId());
        values.put(COLUMN_HOADON_CHISOCU, hoaDon.getChiSoCu());
        values.put(COLUMN_HOADON_CHISOMOI, hoaDon.getChiSoMoi());
        values.put(COLUMN_HOADON_CODE, hoaDon.getCode());
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
                        COLUMN_HOADON_ID,
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

        HoaDon hoaDon = new HoaDon(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12));
        // return hoaDon
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

    public ArrayList<HoaDon> getAllHoaDons() {
        Log.i(TAG, "LocalDatabase.getAllHoaDons ... ");

        ArrayList<HoaDon> hoaDonList = new ArrayList<HoaDon>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HOADON;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setId(Integer.parseInt(cursor.getString(0)));
                hoaDon.setChiSoCu(cursor.getString(1));
                hoaDon.setChiSoMoi(cursor.getString(2));
                hoaDon.setCode(cursor.getString(3));
                hoaDon.setDanhBo(cursor.getString(4));
                hoaDon.setDot(cursor.getString(5));
                hoaDon.setTenKhachHang(cursor.getString(6));
                hoaDon.setKy(cursor.getString(7));
                hoaDon.setMaLoTrinh(cursor.getString(8));


                // Thêm vào danh sách.
                hoaDonList.add(hoaDon);
            } while (cursor.moveToNext());
        }

        // return hoaDon list
        return hoaDonList;
    }

    public List<HoaDon> getAllHoaDonByMaLoTrinh(String mlt) {
        Log.i(TAG, "LocalDatabase.getAllHoaDons ... ");

        List<HoaDon> hoaDonList = new ArrayList<HoaDon>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HOADON + " WHERE " + COLUMN_HOADON_MALOTRINH + " = '" + mlt + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setId(Integer.parseInt(cursor.getString(0)));
                hoaDon.setDanhBo(cursor.getString(2));
                hoaDon.setTenKhachHang(cursor.getString(3));

                hoaDon.setCode(cursor.getString(5));
                hoaDon.setChiSoCu(cursor.getString(6));

                hoaDon.setMaLoTrinh(cursor.getString(8));

                // Thêm vào danh sách.
                hoaDonList.add(hoaDon);
            } while (cursor.moveToNext());
        }

        // return hoaDon list
        return hoaDonList;
    }

    public int getHoaDonsCount() {
        Log.i(TAG, "LocalDatabase.getHoaDonsCount ... ");

        String countQuery = "SELECT  * FROM " + TABLE_HOADON;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public int updateHoaDon(HoaDon hoaDon) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOADON_ID, hoaDon.getId());
        values.put(COLUMN_HOADON_CHISOCU, hoaDon.getChiSoCu());
        values.put(COLUMN_HOADON_CHISOMOI, hoaDon.getChiSoMoi());
        values.put(COLUMN_HOADON_CODE, hoaDon.getCode());
        values.put(COLUMN_HOADON_DANHBO, hoaDon.getDanhBo());
        values.put(COLUMN_HOADON_DOT, hoaDon.getDot());
        values.put(COLUMN_HOADON_KHACHHANG, hoaDon.getTenKhachHang());
        values.put(COLUMN_HOADON_KY, hoaDon.getKy());
        values.put(COLUMN_HOADON_MALOTRINH, hoaDon.getMaLoTrinh());

        // updating row
        return db.update(TABLE_HOADON, values, COLUMN_HOADON_ID + " = ?",
                new String[]{String.valueOf(hoaDon.getId())});
    }

    public void deleteHoaDon(HoaDon hoaDon) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOADON, COLUMN_HOADON_ID + " = ?",
                new String[]{String.valueOf(hoaDon.getId())});
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
}