package com.ditagis.hcm.docsotanhoa.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ditagis.hcm.docsotanhoa.adapter.GridViewSelectFolderAdapter;
import com.ditagis.hcm.docsotanhoa.entities.Code_CSC_SanLuong;
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.entities.Location;
import com.ditagis.hcm.docsotanhoa.entities.TTDHN;
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
    private static final String COLUMN_HOADON_ID = "HoaDonID";
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
    private static final String COLUMN_HOADON_HINH_BYTE_ARRAY = "HoaDon_Hinh_Byte_Array";
    private static final String COLUMN_HOADON_THOI_GIAN = "HoaDon_ThoiGian";
    private static final String COLUMN_HOADON_SO_THAN = "HoaDon_SoThan";
    private static final String COLUMN_HOADON_HIEU = "HoaDon_Hieu";
    private static final String COLUMN_HOADON_CO = "HoaDon_Co";
    private static final String COLUMN_HOADON_VI_TRI = "HoaDon_ViTri";
    private static final String COLUMN_HOADON_SH = "HoaDon_SH";
    private static final String COLUMN_HOADON_SX = "HoaDon_SX";
    private static final String COLUMN_HOADON_DV = "HoaDon_DV";
    private static final String COLUMN_HOADON_HC = "HoaDon_HC";
    private static final String COLUMN_HOADON_TU_NGAY = "HoaDon_tuNgay";
    private static final String COLUMN_HOADON_DEN_NGAY = "HoaDon_denNgay";
    private static final String COLUMN_HOADON_CSGO = "HoaDon_CSGO";
    private static final String COLUMN_HOADON_CSGANMOI = "HoaDon_CSGANMOI";


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


    private static final String TABLE_LOCATION = "VITRI";
    private static final String COLUMN_LOCATION_LONG = "LOCATION_LONG";
    private static final String COLUMN_LOCATION_LAT = "LOCATION_LAT";
    private static final String COLUMN_LOCATION_ID = "LOCATION_ID";

    private static final String TABLE_TTDHN = "TTDHN";
    private static final String COLUMN_TTDHN_TTDHN = "TTDHN_TTDHN";
    private static final String COLUMN_TTDHN_CODE = "TTDHN_CODE";

    private static LocalDatabase instance;

    private LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static LocalDatabase getInstance(Context context) {
        if (instance == null)
            instance = new LocalDatabase(context.getApplicationContext());
        return instance;
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
                + COLUMN_HOADON_DANHBO + " TEXT ,"
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
                + COLUMN_HOADON_HINH_BYTE_ARRAY + " blob,"
                + COLUMN_HOADON_THOI_GIAN + " TEXT,"
                + COLUMN_HOADON_SO_THAN + " TEXT,"
                + COLUMN_HOADON_HIEU + " TEXT,"
                + COLUMN_HOADON_CO + " TEXT,"
                + COLUMN_HOADON_VI_TRI + " TEXT,"
                + COLUMN_HOADON_SH + " TEXT,"
                + COLUMN_HOADON_SX + " TEXT,"
                + COLUMN_HOADON_DV + " TEXT,"
                + COLUMN_HOADON_HC + " TEXT,"
                + COLUMN_HOADON_TU_NGAY + " TEXT,"
                + COLUMN_HOADON_DEN_NGAY + " TEXT,"
                + COLUMN_HOADON_CSGO + " TEXT,"
                + COLUMN_HOADON_CSGANMOI + " TEXT,"
                + COLUMN_HOADON_ID + " TEXT" + ")";


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

        String script3 = "CREATE TABLE " + TABLE_TTDHN + "("
                + COLUMN_TTDHN_CODE + " TEXT ,"
                + COLUMN_TTDHN_TTDHN + " TEXT )";

        String script4 = "CREATE TABLE " + TABLE_LOCATION + "("
                + COLUMN_LOCATION_ID + " TEXT ,"
                + COLUMN_LOCATION_LONG + " TEXT ,"
                + COLUMN_LOCATION_LAT + " TEXT )";

        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        db.execSQL(script2);
        db.execSQL(script3);
        db.execSQL(script4);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "LocalDatabase.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUUDANHBO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TTDHN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TTDHN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        onCreate(db);
    }

    public boolean addHoaDon(HoaDon hoaDon, boolean getImage) {

        //check exist
//        if (getHoaDon_UnRead(hoaDon.getDanhBo()) != null)
//            return false;
        if (getHoaDon_Read(hoaDon.getDanhBo(), getImage) != null)
            return false;
//        if (getHoaDon_Synchronized(hoaDon.getDanhBo()) != null)
//            return false;

        Log.i(TAG, "LocalDatabase.addHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_HOADON_DOT, hoaDon.getDot());
            values.put(COLUMN_HOADON_DANHBO, hoaDon.getDanhBo());
            values.put(COLUMN_HOADON_KHACHHANG, hoaDon.getTenKhachHang());
            values.put(COLUMN_HOADON_SONHA, hoaDon.getSoNha());
            values.put(COLUMN_HOADON_DUONG, hoaDon.getDuong());
            values.put(COLUMN_HOADON_GIABIEU, hoaDon.getGiaBieu());
            values.put(COLUMN_HOADON_DINHMUC, hoaDon.getDinhMuc());
            values.put(COLUMN_HOADON_KY, hoaDon.getKy());
            values.put(COLUMN_HOADON_CHISOCU, hoaDon.getChiSoCu());
            values.put(COLUMN_HOADON_MALOTRINH, hoaDon.getMaLoTrinh());
            values.put(COLUMN_HOADON_SDT, hoaDon.getSdt());
            values.put(COLUMN_HOADON_CODE1, hoaDon.getCode_CSC_SanLuong().getCode1());
            values.put(COLUMN_HOADON_CODE2, hoaDon.getCode_CSC_SanLuong().getCode2());
            values.put(COLUMN_HOADON_CODE3, hoaDon.getCode_CSC_SanLuong().getCode3());
            values.put(COLUMN_HOADON_CSC1, hoaDon.getCode_CSC_SanLuong().getCSC1());
            values.put(COLUMN_HOADON_CSC2, hoaDon.getCode_CSC_SanLuong().getCSC2());
            values.put(COLUMN_HOADON_CSC3, hoaDon.getCode_CSC_SanLuong().getCSC3());
            values.put(COLUMN_HOADON_SANLUONG1, hoaDon.getCode_CSC_SanLuong().getSanLuong1());
            values.put(COLUMN_HOADON_SANLUONG2, hoaDon.getCode_CSC_SanLuong().getSanLuong2());
            values.put(COLUMN_HOADON_SANLUONG3, hoaDon.getCode_CSC_SanLuong().getSanLuong3());
            values.put(COLUMN_HOADON_FLAG, hoaDon.getFlag());
            values.put(COLUMN_HOADON_CODE_MOI, hoaDon.getCodeMoi());
            values.put(COLUMN_HOADON_CSM, hoaDon.getChiSoMoi());
            values.put(COLUMN_HOADON_TTMOI, hoaDon.getTieuThuMoi());
            values.put(COLUMN_HOADON_GHI_CHU, hoaDon.getGhiChu());
            values.put(COLUMN_HOADON_HINH, hoaDon.getImage());
            values.put(COLUMN_HOADON_HINH_BYTE_ARRAY, hoaDon.getImage_byteArray());
            values.put(COLUMN_HOADON_THOI_GIAN, hoaDon.getThoiGian());
            values.put(COLUMN_HOADON_SO_THAN, hoaDon.getSoThan());
            values.put(COLUMN_HOADON_HIEU, hoaDon.getHieu());
            values.put(COLUMN_HOADON_CO, hoaDon.getCo());
            values.put(COLUMN_HOADON_VI_TRI, hoaDon.getViTri());
            values.put(COLUMN_HOADON_SH, hoaDon.getSh());
            values.put(COLUMN_HOADON_SX, hoaDon.getSx());
            values.put(COLUMN_HOADON_DV, hoaDon.getDv());
            values.put(COLUMN_HOADON_HC, hoaDon.getHc());
            values.put(COLUMN_HOADON_TU_NGAY, hoaDon.getTuNgay());
            values.put(COLUMN_HOADON_DEN_NGAY, hoaDon.getDenNgay());
            values.put(COLUMN_HOADON_CSGO, hoaDon.getCsgo());
            values.put(COLUMN_HOADON_CSGANMOI, hoaDon.getCsganmoi());
            values.put(COLUMN_HOADON_ID, hoaDon.getId());

            long value = db.insert(TABLE_HOADON, null, values);

            // Đóng kết nối database.
            db.close();
            return true;
        } catch (Exception e) {
            Log.d("ERROR", e.toString());
        }

        return false;
    }

    public boolean addTTDHN(List<TTDHN> ttdhnList) {


        Log.i(TAG, "LocalDatabase.addTTDHn ... " + ttdhnList.size());

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "Delete from " + TABLE_TTDHN;
        db.execSQL(sql);
        try {
            for (TTDHN ttdhn : ttdhnList) {
                sql = "INSERT INTO " + TABLE_TTDHN + " ("
                        + COLUMN_TTDHN_CODE + ", "
                        + COLUMN_TTDHN_TTDHN

                        + ") Values ('" + ttdhn.getCode() + "', '"
                        + ttdhn.getTTDHN() +
                        "')";
                db.execSQL(sql);
            }
            // Đóng kết nối database.
            db.close();

//            return true;
        } catch (Exception e) {
            Log.i(TAG, "LocalDatabase.addTTDHn ... " + e.toString());
        }
        return false;
    }

    public boolean addLocation(Location location) {
        if (updateLocation(location))
            return false;

        Log.i(TAG, "LocalDatabase.addTTDHn ... " + location.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        String sql;
        try {
            sql = "INSERT INTO " + TABLE_LOCATION + " ("
                    + COLUMN_LOCATION_ID + ", "
                    + COLUMN_LOCATION_LONG + ", "
                    + COLUMN_LOCATION_LAT

                    + ") Values ('" + location.getId() + "', "
                    + location.getLongtitue() + ", "
                    + location.getLatitude() + ")";

            db.execSQL(sql);

            // Đóng kết nối database.
            db.close();
            return true;
//            return true;
        } catch (Exception e) {
            Log.i(TAG, "LocalDatabase.addTTDHn ... " + e.toString());
        }
        return false;
    }

    public boolean update(Location location) {
        Log.i(TAG, "LocalDatabase.getTTDHN ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_LOCATION + " where " + COLUMN_LOCATION_ID + " = '" + id + "'", null);
        boolean flag = false;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            flag = updateLocation(location);

        }
        cursor.close();
        db.close();
        return flag;
    }

    private boolean updateLocation(Location location) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + location.getId());

        SQLiteDatabase db = this.getWritableDatabase();
//
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION_LONG, location.getLongtitue());
        values.put(COLUMN_LOCATION_LAT, location.getLatitude());

        int result = db.update(TABLE_LOCATION, values, COLUMN_LOCATION_ID + " = ?", new String[]{location.getId()});
        db.close();
        return result > 0;
    }

    public Location getLocation(String id) {
        Location location = new Location();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            Log.i(TAG, "LocalDatabase.getTTDHN ... " + id);

            cursor = db.rawQuery("select * from " + TABLE_LOCATION + " where " + COLUMN_LOCATION_ID + " = '" + id + "'", null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                location.setId(cursor.getString(0));
                location.setLongtitue(cursor.getDouble(1));
                location.setLatitude(cursor.getDouble(2));
            }
        } catch (Exception e) {
            Log.d("dsf", e.toString());
        } finally {
            cursor.close();
            db.close();
        }

        return location;
    }

    public String getTTDHN(String code) {
        Log.i(TAG, "LocalDatabase.getTTDHN ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_TTDHN + " where " + COLUMN_TTDHN_CODE + " = '" + code + "'", null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(1);
        }
        cursor.close();
        db.close();
        return "";
    }

    private HoaDon getHoaDon(String danhBo, int flag, boolean getImage) {
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        HoaDon hoaDon = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_DANHBO + " = '" + danhBo + "' and " + COLUMN_HOADON_FLAG + " in( " + flag + "," + Flag.CODE_F + "," + Flag.CODE_F_SYNCHRONIZED + ")", null);

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
            hoaDon.setThoiGian(cursor.getString(27));
            hoaDon.setSoThan(cursor.getString(28));
            hoaDon.setHieu(cursor.getString(29));
            hoaDon.setCo(cursor.getString(30));
            hoaDon.setViTri(cursor.getString(31));
            hoaDon.setSh(cursor.getInt(32));
            hoaDon.setSx(cursor.getInt(33));
            hoaDon.setDv(cursor.getInt(34));
            hoaDon.setHc(cursor.getInt(35));
            hoaDon.setTuNgay(cursor.getString(36));
            hoaDon.setDenNgay(cursor.getString(37));

            hoaDon.setCsgo(cursor.getInt(38));
            hoaDon.setCsgan(cursor.getInt(39));
            hoaDon.setId(cursor.getString(40));
            try {
                if (getImage)
                    hoaDon.setImage_byteArray(cursor.getBlob(26));
            } catch (Exception e) {

            }
        }
        cursor.close();
        db.close();
        return hoaDon;
    }

    public HoaDon getHoaDon_UnRead(String danhBo, boolean getImage) {
        return getHoaDon(danhBo, Flag.UNREAD, getImage);
    }

    public HoaDon getHoaDon_Read(String danhBo, boolean getImage) {
        return getHoaDon(danhBo, Flag.READ, getImage);
    }

    public HoaDon getHoaDon_Synchronized(String danhBo, boolean getImage) {
        return getHoaDon(danhBo, Flag.SYNCHRONIZED, getImage);
    }


//    public List<HoaDon> getAllHoaDon_Read(String ky) {
//        return getAllHoaDon(like, Flag.READ);
//    }

    public List<HoaDon> getAllHoaDon_UnRead(int ky, boolean getImage) {
        return getAllHoaDon(ky, Flag.UNREAD, getImage);
    }

    //    public List<HoaDon> getAllHoaDon_UnRead(int nam) {
//        return getAllHoaDon(nam, Flag.UNREAD);
//    }
    public List<HoaDon> getAllHoaDon_UnRead(String like, int ky, boolean getImage) {
        return getAllHoaDon(like, ky, Flag.UNREAD, getImage);
    }

    public List<HoaDon> getAllHoaDon_Read(String like, int ky, boolean getImage) {
        return getAllHoaDon(like, ky, Flag.READ, getImage);
    }

    //    public List<HoaDon> getAllHoaDon_Synchronized(String like) {
//        return getAllHoaDon(like, Flag.SYNCHRONIZED);
//    }
    public List<HoaDon> getAllHoaDon_Synchronized(String like, int ky, boolean getImage) {
        return getAllHoaDon(like, ky, Flag.SYNCHRONIZED, getImage);
    }

    public int getAllHoaDonSizeDocSo(String like, int ky, boolean getImage) {
        return getAllHoaDonSize(like, ky, Flag.READ, getImage) + getAllHoaDonSize(like, ky, Flag.UNREAD, getImage) + getAllHoaDonSize(like, ky, Flag.CODE_F, getImage) + getAllHoaDonSize(like, ky, Flag.CODE_F_SYNCHRONIZED, getImage);

    }

    public int getAllHoaDonSize(String like, int ky, int flag, boolean getImage) {
        int count = 0;
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " +
                COLUMN_HOADON_DANHBO + "" +
                " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + like + "%' and " + COLUMN_HOADON_KY
                + "=" + ky + " and " + COLUMN_HOADON_FLAG + " = " + flag, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<HoaDon> getAllHoaDon(String like, int ky, int flag, boolean getImage) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String query = "";
//        if (flag == Flag.UNREAD) {
        query = "select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + like +
                "' and " + COLUMN_HOADON_KY + " = " + ky +
                " and " + COLUMN_HOADON_FLAG + " in ( " + flag + "," + Flag.CODE_F + "," + Flag.CODE_F_SYNCHRONIZED + ")";
        cursor = db.rawQuery(query, null);
//        }else
//            cursor = db.rawQuery("select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + like +
//                    "' and " + COLUMN_HOADON_KY + " = " + ky +
//                    " and " + COLUMN_HOADON_FLAG + " = " + flag, null);
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
                hoaDon.setThoiGian(cursor.getString(27));
                hoaDon.setSoThan(cursor.getString(28));
                hoaDon.setHieu(cursor.getString(29));
                hoaDon.setCo(cursor.getString(30));
                hoaDon.setViTri(cursor.getString(31));
                hoaDon.setSh(cursor.getInt(32));
                hoaDon.setSx(cursor.getInt(33));
                hoaDon.setDv(cursor.getInt(34));
                hoaDon.setHc(cursor.getInt(35));
                hoaDon.setTuNgay(cursor.getString(36));
                hoaDon.setDenNgay(cursor.getString(37));
                hoaDon.setCsgo(cursor.getInt(38));
                hoaDon.setCsgan(cursor.getInt(39));
                hoaDon.setId(cursor.getString(40));
                try {
                    if (getImage)
                        hoaDon.setImage_byteArray(cursor.getBlob(26));
                    else if (hoaDons.size() > 0)
                        return hoaDons;
                } catch (Exception e) {

                }
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }

    public List<HoaDon> getAllHoaDon(int ky, int flag, boolean getImage) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOADON + " where " + COLUMN_HOADON_KY + " = " + ky + " and " + COLUMN_HOADON_FLAG + " = " + flag, null);
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
                hoaDon.setThoiGian(cursor.getString(27));
                hoaDon.setSoThan(cursor.getString(28));
                hoaDon.setHieu(cursor.getString(29));
                hoaDon.setCo(cursor.getString(30));
                hoaDon.setViTri(cursor.getString(31));
                hoaDon.setSh(cursor.getInt(32));
                hoaDon.setSx(cursor.getInt(33));
                hoaDon.setDv(cursor.getInt(34));
                hoaDon.setHc(cursor.getInt(35));
                hoaDon.setTuNgay(cursor.getString(36));
                hoaDon.setDenNgay(cursor.getString(37));
                hoaDon.setCsgo(cursor.getInt(38));
                hoaDon.setCsgan(cursor.getInt(39));
                hoaDon.setId(cursor.getString(40));
                try {
                    if (getImage)
                        hoaDon.setImage_byteArray(cursor.getBlob(26));
                    else if (hoaDons.size() > 0)
                        return hoaDons;
                } catch (Exception e) {

                }
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }


    public int getAllHoaDonSize(String may, String dot, String ky, boolean getImage) {
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(" + COLUMN_HOADON_DANHBO + ") from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + dot + may + "%' and " + COLUMN_HOADON_KY
                + "='" + ky + "'", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return 0;
    }


    public List<HoaDon> getAllHoaDon(boolean getImage) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOADON, null);
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

                hoaDon.setThoiGian(cursor.getString(27));
                hoaDon.setSoThan(cursor.getString(28));
                hoaDon.setHieu(cursor.getString(29));
                hoaDon.setCo(cursor.getString(30));
                hoaDon.setViTri(cursor.getString(31));
                hoaDon.setSh(cursor.getInt(32));
                hoaDon.setSx(cursor.getInt(33));
                hoaDon.setDv(cursor.getInt(34));
                hoaDon.setHc(cursor.getInt(35));
                hoaDon.setTuNgay(cursor.getString(36));
                hoaDon.setDenNgay(cursor.getString(37));
                hoaDon.setCsgo(cursor.getInt(38));
                hoaDon.setCsgan(cursor.getInt(39));
                hoaDon.setId(cursor.getString(40));
                try {
                    if (getImage)
                        hoaDon.setImage_byteArray(cursor.getBlob(26));
                    else if (hoaDons.size() > 0)
                        return hoaDons;
                } catch (Exception e) {

                }
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }

    public boolean updateHoaDonUnRead(HoaDon hoadon, boolean isCodeF) {
        return updateHoaDon(hoadon, Flag.READ, Flag.UNREAD, isCodeF);
    }

    public boolean updateHoaDonRead(HoaDon hoadon) {
        return updateHoaDon(hoadon, Flag.READ, Flag.READ, false);
    }

    public boolean updateHoaDonSynchronized(HoaDon hoaDon) {
        return updateHoaDon(hoaDon, Flag.SYNCHRONIZED, Flag.READ, false);
    }

    private boolean updateHoaDon(HoaDon hoaDon, int flag, int flag_old, boolean isCodeF) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
//
        ContentValues values = new ContentValues();
        if (isCodeF)
            values.put(COLUMN_HOADON_FLAG, Flag.CODE_F);
        else if (flag == Flag.SYNCHRONIZED) {
            if (hoaDon.getFlag() == Flag.CODE_F)
                values.put(COLUMN_HOADON_FLAG, Flag.CODE_F_SYNCHRONIZED);
            else
                values.put(COLUMN_HOADON_FLAG, flag);
        } else
            values.put(COLUMN_HOADON_FLAG, flag);
        values.put(COLUMN_HOADON_CODE_MOI, hoaDon.getCodeMoi());
        values.put(COLUMN_HOADON_CSM, hoaDon.getChiSoMoi());
        values.put(COLUMN_HOADON_TTMOI, hoaDon.getTieuThuMoi());
        values.put(COLUMN_HOADON_SONHA, hoaDon.getSoNha());
        values.put(COLUMN_HOADON_DUONG, hoaDon.getDuong());
        values.put(COLUMN_HOADON_GHI_CHU, hoaDon.getGhiChu());
        values.put(COLUMN_HOADON_SDT, hoaDon.getSdt());
        values.put(COLUMN_HOADON_HINH, hoaDon.getImage());
        values.put(COLUMN_HOADON_HINH_BYTE_ARRAY, hoaDon.getImage_byteArray());
        values.put(COLUMN_HOADON_VI_TRI, hoaDon.getViTri());
        values.put(COLUMN_HOADON_THOI_GIAN, hoaDon.getThoiGian());

        db.update(TABLE_HOADON, values, COLUMN_HOADON_DANHBO + " = ? and "
                + COLUMN_HOADON_FLAG + " in( ?,?,?)", new String[]{hoaDon.getDanhBo(), flag_old + "", Flag.CODE_F + "", Flag.CODE_F_SYNCHRONIZED + ""});
        db.close();
        return true;
    }

    public boolean updateHoaDonFlag(HoaDon hoaDon, int flag) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
//
        ContentValues values = new ContentValues();

        values.put(COLUMN_HOADON_FLAG, flag);


        db.update(TABLE_HOADON, values, COLUMN_HOADON_DANHBO + " = ? "
                , new String[]{hoaDon.getDanhBo()});
        db.close();
        return true;
    }

    public boolean updateHoaDon_Image(HoaDon hoaDon, int flag_old) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + hoaDon.getDanhBo());

        SQLiteDatabase db = this.getWritableDatabase();
//
        ContentValues values = new ContentValues();

        values.put(COLUMN_HOADON_HINH_BYTE_ARRAY, hoaDon.getImage_byteArray());

//        // Trèn một dòng dữ liệu vào bảng.
        long result = db.update(TABLE_HOADON, values, COLUMN_HOADON_DANHBO + " = ? and "
                + COLUMN_HOADON_FLAG + " = ?", new String[]{hoaDon.getDanhBo(), flag_old + ""});


        db.close();
        return true;
    }

    public boolean updateHoaDon_Address(String danhBo, String soNha, String duong, int flag_old) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + danhBo);

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "update " + TABLE_HOADON + " set " +
                COLUMN_HOADON_SONHA + " = '" + soNha + "'," +
                COLUMN_HOADON_DUONG + " = '" + duong + "'" +


                " where " +
                COLUMN_HOADON_DANHBO + " ='" + danhBo + "' and " +
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
                COLUMN_HOADON_SONHA + " = '" + hoaDon.getSoNha() + "'," +
                COLUMN_HOADON_DUONG + " = '" + hoaDon.getDuong() + "'," +
                COLUMN_HOADON_GHI_CHU + " = '" + hoaDon.getGhiChu() + "'," +
                COLUMN_HOADON_VI_TRI + " = '" + hoaDon.getViTri() + "'," +
                COLUMN_HOADON_HINH + " = '" + hoaDon.getImage() + "'" +


                " where " +
                COLUMN_HOADON_DANHBO + " ='" + hoaDon.getDanhBo() + "' and " +
                COLUMN_HOADON_FLAG + " = " + flag_old;

        db.execSQL(sql);
        // Đóng kết nối database.
        db.close();
        return true;
    }

    public void deleteHoaDon(String danhBo, int flag) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + danhBo);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOADON, COLUMN_HOADON_DANHBO + " = ? ",
                new String[]{danhBo});
        db.close();
    }

    public void deleteHoaDon(GridViewSelectFolderAdapter adapter) {

    }

    public void deleteHoaDon(String ky, String dot) {
        Log.i(TAG, "LocalDatabase.updateHoaDon ... " + ky + dot);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOADON, COLUMN_HOADON_KY + " = ? and " + COLUMN_HOADON_DOT + " = ?",
                new String[]{Integer.parseInt(ky) + "", dot});
        db.close();
    }

    public boolean getExistHoaDon(String may, String dot, String ky) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + COLUMN_HOADON_DANHBO + " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + dot + may + "%' and " + COLUMN_HOADON_KY
                + "=" + ky, null);
        if (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public List<HoaDon> getAllHoaDonQLDS(String mLike, int ky) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " +

                COLUMN_HOADON_DANHBO + "," +
                COLUMN_HOADON_CHISOCU + "," +
                COLUMN_HOADON_CSM + "," +
                COLUMN_HOADON_CODE_MOI + "," +
                COLUMN_HOADON_SONHA + "," +
                COLUMN_HOADON_DUONG + "," +

                COLUMN_HOADON_THOI_GIAN + "," +
                COLUMN_HOADON_TTMOI + "," +
                COLUMN_HOADON_FLAG + "," +
                COLUMN_HOADON_MALOTRINH + "," +
                COLUMN_HOADON_KHACHHANG + "" +
                " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + mLike + "%' and " + COLUMN_HOADON_KY
                + "=" + ky + " and " + COLUMN_HOADON_FLAG + " in (" + Flag.SYNCHRONIZED +
                " , " + Flag.READ + "," + Flag.CODE_F + "," + Flag.CODE_F_SYNCHRONIZED + ")", null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon();

                hoaDon.setDanhBo(cursor.getString(0));
                hoaDon.setChiSoCu(cursor.getString(1));
                hoaDon.setChiSoMoi(cursor.getString(2));
                hoaDon.setCodeMoi(cursor.getString(3));
                hoaDon.setSoNha(cursor.getString(4));
                hoaDon.setDuong(cursor.getString(5));
                hoaDon.setThoiGian(cursor.getString(6));
                hoaDon.setTieuThuMoi(cursor.getString(7));
                hoaDon.setFlag(cursor.getInt(8));
                hoaDon.setMaLoTrinh(cursor.getString(9));
                hoaDon.setTenKhachHang(cursor.getString(10));
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }
    public List<HoaDon> getAllHoaDon(String mLike, int ky) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " +

                COLUMN_HOADON_DANHBO + "," +
                COLUMN_HOADON_CODE_MOI + "," +


                COLUMN_HOADON_FLAG +
                " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + mLike + "%' and " + COLUMN_HOADON_KY
                + "=" + ky , null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon();

                hoaDon.setDanhBo(cursor.getString(0));
                hoaDon.setCodeMoi(cursor.getString(1));
                hoaDon.setFlag(cursor.getInt(2));
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }

    public List<HoaDon> getAllHoaDonLayLoTrinh(String mLike, int ky) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " +
                COLUMN_HOADON_DANHBO + "" +
                " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + mLike + "' and " + COLUMN_HOADON_KY
                + "=" + ky;
//                + " and( " + COLUMN_HOADON_FLAG +
//                " <> " + Flag.SYNCHRONIZED +
//                " in (" + Flag.UNREAD +
//                " ," + Flag.READ +
//                " ," + Flag.CODE_F +
//                " ," + Flag.CODE_F_SYNCHRONIZED +
//                " or (" + COLUMN_HOADON_FLAG + " = " + Flag.SYNCHRONIZED + " and " +
//                COLUMN_HOADON_CODE_MOI + " like 'F%'" + ")" +
//                ")";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setDanhBo(cursor.getString(0));

                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }

    public List<HoaDon> getallhoadonDS(String mLike, int ky) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " +
                COLUMN_HOADON_DANHBO + "," +
                COLUMN_HOADON_MALOTRINH + "," +
                COLUMN_HOADON_KHACHHANG + "," +
                COLUMN_HOADON_SONHA + "," +
                COLUMN_HOADON_DUONG + "" +
                " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + mLike + "%' and " + COLUMN_HOADON_KY
                + "=" + ky + " and " + COLUMN_HOADON_FLAG + " in (" + Flag.UNREAD + "," + Flag.CODE_F + "," + Flag.CODE_F_SYNCHRONIZED + ")", null);
        if (cursor.moveToFirst()) {
            do {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setDanhBo(cursor.getString(0));
                hoaDon.setMaLoTrinh(cursor.getString(1));
                hoaDon.setTenKhachHang(cursor.getString(2));
                hoaDon.setSoNha(cursor.getString(3));
                hoaDon.setDuong(cursor.getString(4));
                hoaDons.add(hoaDon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoaDons;
    }

    public int getAllHoaDonSumSize(String mLike, int ky) {
        List<HoaDon> hoaDons = new ArrayList<HoaDon>();
        Log.i(TAG, "LocalDatabase.getHoaDon_UnRead ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        int size = 0;
        Cursor cursor = db.rawQuery("select " +
                COLUMN_HOADON_DANHBO + "" +
                " from " + TABLE_HOADON + " where " + COLUMN_HOADON_MALOTRINH + " like '" + mLike + "%' and " + COLUMN_HOADON_KY
                + "=" + ky, null);
        if (cursor.moveToFirst()) {
            do {
                size++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return size;
    }

    class KyDotAdapter {
        private String ky;
        private String dot;

        public KyDotAdapter(String ky, String dot) {
            this.ky = ky;
            this.dot = dot;
        }

        public String getKy() {
            return ky;
        }

        public void setKy(String ky) {
            this.ky = ky;
        }

        public String getDot() {
            return dot;
        }

        public void setDot(String dot) {
            this.dot = dot;
        }
    }
}