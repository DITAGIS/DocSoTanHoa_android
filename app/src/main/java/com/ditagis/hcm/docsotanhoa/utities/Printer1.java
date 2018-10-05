package com.ditagis.hcm.docsotanhoa.utities;

import android.util.Log;

import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.utities.printUtities.PrinterCommands;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * Created by ThanLe on 1/3/2018.
 */

public class Printer1 {
    // android built in classes for bluetooth operations
    // needed for communication to bluetooth device / network
    static final int LEFT_ALIGN = 0;
    private static final int CENTER_ALIGN = 1;
    private static final int RIGHT_ALIGN = 2;
    OutputStream mmOutputStream;
    DateFormat formatter_old = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
    DateFormat formatter_day_of_week = new SimpleDateFormat("E");
    Calendar cal = Calendar.getInstance();
    private static Printer1 instance = null;
    private String address;
    private String mTuNgay, mDenNgay, mStaffName, mStaffPhone;
    private HoaDon mHoaDon;
    private double mTienNuoc;
    private int mNam;
    StringBuilder mmBuilder;
    private final byte[] mmChuoi = {0x1b};

    public boolean isInitialized() {
        return isInitialized;
    }

    public boolean isInitialized = false;

    private Printer1() {
    }

    public static Printer1 getInstance() {
        if (instance == null)
            instance = new Printer1();
        return instance;
    }

    public void initialize(OutputStream mmOutputStream) {

        this.mmOutputStream = mmOutputStream;
        if (mmOutputStream != null)
            this.isInitialized = true;
    }

    public boolean print() {
        try {
            this.printNew();
//            this.printOld();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean printOld() {
//        Thread t = new Thread() {
//            public void run() {
        try {
//            mBluetoothSocket = mBluetoothDevice
//                    .createRfcommSocketToServiceRecord(applicationUUID);
//            mBluetoothAdapter.cancelDiscovery();
//            mBluetoothSocket.connect();
//            mHandler.sendEmptyMessage(0);
            NumberFormat.getNumberInstance(Locale.CANADA).format(35634646);
            cal.setTime(formatter_old.parse(mHoaDon.getThoiGian()));
            int[] dates = getDates(cal);

            int y = 100;
            StringBuilder builder = new StringBuilder();
            builder.append("! 0 200 200 1000 1\n" +

                    "CENTER\n" +

                    "TEXT 0 1 0 0 CTY CP CAP NUOC TAN HOA\n" + //font 0,4,7
                    "TEXT 0 1 0 30 95 PHAM HUU CHI, P12, Q5\n" +
                    "TEXT 7 1 0 50 PHIEU BAO C.SO & TIEN NUOC DU KIEN\n");
            builder.append(String.format("TEXT 0 1 0 %d KY %s/%s\n", y, mHoaDon.getKy(), mNam + ""));
            y += 20;
            builder.append(String.format("TEXT 0 1 0 %d (%s * %s)\n", y, mHoaDon.getTuNgay(), mHoaDon.getDenNgay()));

            builder.append("TEXT 7 0 0 135 -------------------------------\n");
            y += 40;
            builder.append(String.format("BARCODE 128 1 1 50 0 %d %s\n", y, mHoaDon.getDanhBo()) +
                    "LEFT\n");
            y += 60;
            builder.append(String.format("TEXT 7 0 20 %d NHAN VIEN: %s\n", y, removeAccent(mStaffName)));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d So dien thoai: %s\n", y, mStaffPhone));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d KHACH HANG: %s\n", y, removeAccent(mHoaDon.getTenKhachHang())));
            y += 40;
            builder.append(String.format("TEXT 7 0 20 %d DIA CHI: %s\n", y, removeAccent(mHoaDon.getDiaChi())));
            y += 45;
            builder.append(String.format("TEXT 7 0 20 %d DANH BA: %s%11s%s\n", y, "             ", "MLT: ", ""));
            y -= 20;
            builder.append(String.format("TEXT 7 1 20 %d          %s%11s%s\n", y, spaceDB(mHoaDon.getDanhBo()), " ", spaceMLT(mHoaDon.getMaLoTrinh())));
            y += 50;
            builder.append(String.format("TEXT 7 0 20 %d GIA BIEU: %s - DINH MUC: %s m3\n", y, mHoaDon.getGiaBieu(), mHoaDon.getDinhMuc()));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO MOI%23s\n", y, mHoaDon.getChiSoMoi()));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d CHI SO CU%24s\n", y, mHoaDon.getChiSoCu()));
            y += 40;
            builder.append(String.format("TEXT 7 0 70 %d TIEU THU\n", y, mHoaDon.getTieuThuMoi()));
            y -= 10;
            builder.append(String.format("TEXT 7 1 70 %d         %25s m3\n", y, mHoaDon.getTieuThuMoi()));
            y += 55;
            builder.append(String.format("TEXT 7 0 70 %d TIEN NUOC%24s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc))); //.0f
            y += 35;

            double BVMT = 0, VAT = mTienNuoc / 20;
            if (!mHoaDon.getGiaBieu().equals("52"))
                BVMT = mTienNuoc / 10;
            builder.append(String.format("TEXT 7 0 70 %d PHI BVMT%25s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(BVMT)));
            y += 35;
            builder.append(String.format("TEXT 7 0 70 %d THUE VAT%25s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(VAT)));
            y += 35;
            builder.append(String.format("TEXT 7 0 230 %d %25s\n", y, "-----------"));
            y += 10;
            builder.append(String.format("TEXT 7 1 40 %d TONG CONG%27s VND\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc + BVMT + VAT)) +
                    "CENTER\n");
            y += 50;
            builder.append(String.format("TEXT 7 0 0 %d -------------------------------\n", y));
            y += 15;
            builder.append(String.format("TEXT 7 1 0 %d NGAY THU TIEN DU KIEN %s -> %s\n", y, String.format("%02d", dates[0]), String.format("%02d", dates[1])));
            y += 60;
            builder.append(String.format("TEXT 7 0 0 %d DIEN THOAI: 39 557 795 DE DUOC HUONG DAN\n", y));
            y += 40;
            builder.append(String.format("TEXT 7 1 0 %d PHIEU NAY KHONG CO GIA TRI THANH TOAN\n", y));
            y += 50;
            builder.append(String.format("TEXT 7 0 0 %d -------------------------------\n", y));
            y += 20;
            builder.append(String.format("TEXT 7 0 80 %d Printed on: %s\n", y, formatter.format(formatter_old.parse(mHoaDon.getThoiGian()))));
            y += 30;
            builder.append(String.format("TEXT 7 0 0 %d -------------------------------\n", y) +
//                                    "FORM\n" +
                    "PRINT\n");

            mmOutputStream.write(builder.toString().getBytes());
            return true;
////
        } catch (Exception e) {
            Log.e("MainActivity", "Exe ", e);
        }
        return false;
    }


    // this will send text data to be printed by the bluetooth printer
    void printNew() throws IOException, ParseException {
        cal.setTime(formatter_old.parse(mHoaDon.getThoiGian()));
        int[] dates = getDates(cal);

        mmOutputStream.write((mmChuoi));
        int y = 5;
        mmBuilder = new StringBuilder();
        mmBuilder.append("EZ\n");
        mmBuilder.append("{PRINT:\n");

        mmBuilder.append(printLine("CTY CP CẤP NƯỚC TÂN HÒA", 3, y, 25, 1, 1));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("95 PHẠM HỮU CHÍ, P12, Q5", 1, y, 40, 1, 1));
        y = handlingYMoreThan450(y, 30);
        mmBuilder.append(printLine("PHIẾU BÁO CHỈ SỐ", 4, y, 80, 2, 1));
        y = handlingYMoreThan450(y, 50);
        mmBuilder.append(printLine("VÀ TIỀN NƯỚC DỰ KIẾN", 4, y, 40, 2, 1));
        y = handlingYMoreThan450(y, 75);
        mmBuilder.append(String.format("@%d,0:PD417,YDIM 6,XDIM 2,COLUMNS 2,SECURITY 3|%s|\n", y, mHoaDon.getDanhBo()));
        y = handlingYMoreThan450(y, 75);
        mmBuilder.append(printLine("Kỳ:", 1, y, 0, 1, 1));

        mmBuilder.append(printLine("%s/%s", 3, y, 305, 1, 1, mHoaDon.getKy(), mNam));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("DB:", 1, y, 0, 1, 1));

        mmBuilder.append(printLine("%s", 4, y, 240, 1, 1, mHoaDon.getDanhBo()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("KH:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 43, 1, 1, mHoaDon.getTenKhachHang()));
        y = handlingYMoreThan450(y, 50);
        mmBuilder.append(printLine("Đ/Chỉ:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 76, 1, 1, mHoaDon.getDiaChi()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("GB:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 44, 1, 1, mHoaDon.getGiaBieu()));
        mmBuilder.append(printLine("ĐM:", 1, y, 100, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 160, 1, 1, mHoaDon.getDinhMuc()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("MLT:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 255, 1, 1, spaceMLT(mHoaDon.getMaLoTrinh())));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Cs cũ:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 0, 1, 1,
                padLeft(mHoaDon.getChiSoCu(), 31)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Cs mới:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%s", 3, y, 0, 1, 1,
                padLeft(mHoaDon.getChiSoMoi(), 31)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Tiêu thụ:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%sm3", 3, y, 0, 1, 1,
                padLeft(mHoaDon.getTieuThuMoi(), 28)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Tiền nước:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%sđ", 3, y, 0, 1, 1,
                padLeft(NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc)
                        .replace(",", " "), 30)));
        y = handlingYMoreThan450(y, 25);

        double BVMT = 0, VAT = mTienNuoc / 20;
        if (!mHoaDon.getGiaBieu().equals("52"))
            BVMT = mTienNuoc / 10;

        mmBuilder.append(printLine("Thuế VAT:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%sđ", 3, y, 0, 1, 1,
                padLeft(NumberFormat.getNumberInstance(Locale.US).format(VAT)
                        .replace(",", " "), 30)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Phí BVMT:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%sđ", 3, y, 0, 1, 1,
                padLeft(NumberFormat.getNumberInstance(Locale.US).format(BVMT)
                        .replace(",", " "), 30)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Tổng cộng:", 1, y, 0, 1, 1));
        mmBuilder.append(printLine("%sđ", 3, y, 0, 1, 1,
                padLeft(NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc + BVMT + VAT)
                        .replace(",", " "), 30)));
        y = handlingYMoreThan450(y, 50);
        mmBuilder.append(String.format("@%d,80:HLINE,Length200,Thick3|\n", y));
        y = handlingYMoreThan450(y, 5);
        mmBuilder.append(printLine("Ngày thu tiền dự kiến từ %s đến %s", 1, y, 0, 1, 1, String.format("%02d", dates[0]), String.format("%02d", dates[1])));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Điện thoại: 39 557 795 để được hướng", 1, y, 0, 1, 1));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("dẫn", 1, y, 0, 1, 1));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Phiếu này không có giá trị thanh toán", 1, y, 0, 1, 1));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(printLine("Được in vào: %s", 3, y, 60, 1, 1, formatter.format(Calendar.getInstance().getTime())));
        y = handlingYMoreThan450(y, 100);
        mmBuilder.append(printLine(".", 1, y, 0, 1, 1));
        mmBuilder.append("}\n");
        mmOutputStream.write(mmBuilder.toString().getBytes());
    }

    private String printLine(String data, int boldNumber, java.lang.Object... args) {
        String base = "@%d,%d:TIMNR,HMULT%d,VMULT%d|" + data + "|\n";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < boldNumber; i++) {
            builder.append(String.format(base, args));
            args[1] = (int) args[1] + 1;
        }
        return builder.toString();
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(" ", "  ");
    }

    private int handlingYMoreThan450(int y, int delta) {
        if (y + delta > 450) {
            mmBuilder.append("}\n");
            try {
                mmOutputStream.write(mmBuilder.toString().getBytes());
                mmOutputStream.write(mmChuoi);

                mmBuilder = new StringBuilder();
                mmBuilder.append("EZ\n");
                mmBuilder.append("{PRINT:\n");


            } catch (IOException e) {

            } finally {
                return 0;
            }


        } else
            return y + delta;
    }

    public void setValue(int nam, String staffName, String staffPhone, HoaDon hoaDon, double tienNuoc) {
        mNam = nam;
        mStaffName = staffName;
        mStaffPhone = staffPhone;
        mHoaDon = hoaDon;
        mTienNuoc = tienNuoc;
    }

    private int[] getDates(Calendar cal) {
        int[] dates = new int[2];
        cal.add(Calendar.DATE, 4);
        while (cal.get(Calendar.DAY_OF_WEEK) > 6 || cal.get(Calendar.DAY_OF_WEEK) < 2)
            cal.add(Calendar.DATE, 1);
        dates[0] = cal.get(Calendar.DATE);


        cal.add(Calendar.DATE, 1);
        while (cal.get(Calendar.DAY_OF_WEEK) > 6 || cal.get(Calendar.DAY_OF_WEEK) < 2)
            cal.add(Calendar.DATE, 1);
        dates[1] = cal.get(Calendar.DATE);
        return dates;
    }

    private String spaceMLT(String mlt) {
        String output = "";
        output = (mlt.substring(0, 2)).concat(" ").concat(mlt.substring(2, 4)).concat(" ").concat(mlt.substring(4));
        return output;
    }

    private String spaceDB(String danhBo) {
        String output = "";
        output = (danhBo.substring(0, 4)).concat(" ").concat(danhBo.substring(4, 7)).concat(" ").concat(danhBo.substring(7));
        return output;
    }

    private String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
