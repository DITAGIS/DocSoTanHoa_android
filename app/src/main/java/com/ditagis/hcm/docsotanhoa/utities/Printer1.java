package com.ditagis.hcm.docsotanhoa.utities;

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

    private Printer1() {
    }

    public static Printer1 getInstance() {
        if (instance == null)
            instance = new Printer1();
        return instance;
    }

    public void initialize(OutputStream mmOutputStream) {

        this.mmOutputStream = mmOutputStream;
    }

    public boolean print() {
        try {
            this.sendData();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException, ParseException {
        cal.setTime(formatter_old.parse(mHoaDon.getThoiGian()));
        int[] dates = getDates(cal);

        mmOutputStream.write((mmChuoi));
        int y = 5;
        mmBuilder = new StringBuilder();
        mmBuilder.append("EZ\n");
        mmBuilder.append("{PRINT:\n");

        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|  CTY CP CẤP NƯỚC TÂN HÒA|\n", y));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|   95 PHẠM HỮU CHÍ, P12, Q5|\n", y));
        y = handlingYMoreThan450(y, 30);
        mmBuilder.append(String.format("@%d,25:TIMNR,HMULT2,VMULT1|BIÊN NHẬN THU TIỀN NƯỚC|\n", y));
        y = handlingYMoreThan450(y, 75);
        mmBuilder.append(String.format("@%d,0:PD417,YDIM 6,XDIM 2,COLUMNS 2,SECURITY 3|%s|\n", y, mHoaDon.getDanhBo()));
        y = handlingYMoreThan450(y, 75);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Kỳ:|\n", y));

        mmBuilder.append(String.format("@%d,300:TIMNR,HMULT1,VMULT1|%s/%s|\n", y, mHoaDon.getKy(), mNam));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Từ ngày:|\n", y));
        mmBuilder.append(String.format("@%d,140:TIMNR,HMULT1,VMULT1|06/06/2018-06/07/2018|\n", y));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|DB:|\n", y));

        mmBuilder.append(String.format("@%d,218:TIMNR,HMULT1,VMULT1|%s|\n", y, spaceDB(mHoaDon.getDanhBo())));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|KH:|\n", y));
        mmBuilder.append(String.format("@%d,43:TIMNR,HMULT1,VMULT1|%s|\n", y, mHoaDon.getTenKhachHang()));
        y = handlingYMoreThan450(y, 50);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Đ/Chỉ:|\n", y));
        mmBuilder.append(String.format("@%d,76:TIMNR,HMULT1,VMULT1|%s|\n", y, mHoaDon.getDiaChi()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|GB:|\n", y));
        mmBuilder.append(String.format("@%d,44:TIMNR,HMULT1,VMULT1|%s|\n", y, mHoaDon.getGiaBieu()));
        mmBuilder.append(String.format("@%d,110:TIMNR,HMULT1,VMULT1|DM:|\n", y));
        mmBuilder.append(String.format("@%d,154:TIMNR,HMULT1,VMULT1|%s|\n", y, mHoaDon.getDinhMuc()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Mlt:|\n", y));
        mmBuilder.append(String.format("@%d,275:TIMNR,HMULT1,VMULT1|%s|\n", y, spaceMLT(mHoaDon.getMaLoTrinh())));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Cs Cũ:|\n", y));
        mmBuilder.append(String.format("@%d,320:TIMNR,HMULT1,VMULT1|%s|\n", y, mHoaDon.getChiSoCu()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Cs mới:|\n", y));
        mmBuilder.append(String.format("@%d,320:TIMNR,HMULT1,VMULT1|%s|\n", y, mHoaDon.getChiSoMoi()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Tiêu thụ:|\n", y));
        mmBuilder.append(String.format("@%d,309:TIMNR,HMULT1,VMULT1|%sm3|\n", y, mHoaDon.getTieuThuMoi()));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Tiền nước:|\n", y));
        mmBuilder.append(String.format("@%d,200:TIMNR,HMULT1,VMULT1|%20sđ|\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc)));
        y = handlingYMoreThan450(y, 25);

        double BVMT = 0, VAT = mTienNuoc / 20;
        if (!mHoaDon.getGiaBieu().equals("52"))
            BVMT = mTienNuoc / 10;

        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Thuế VAT:|\n", y));
        mmBuilder.append(String.format("@%d,200:TIMNR,HMULT1,VMULT1|%20sđ|\n", y, NumberFormat.getNumberInstance(Locale.US).format(VAT)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Phí BVMT:|\n", y));
        mmBuilder.append(String.format("@%d,200:TIMNR,HMULT1,VMULT1|%20sđ|\n", y, NumberFormat.getNumberInstance(Locale.US).format(BVMT)));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Tổng cộng:|\n", y));
        mmBuilder.append(String.format("@%d,200:TIMNR,HMULT1,VMULT1|%20sđ|\n", y, NumberFormat.getNumberInstance(Locale.US).format(mTienNuoc + BVMT + VAT)));
        y = handlingYMoreThan450(y, 50);
        mmBuilder.append(String.format("@%d,100:HLINE,Length200,Thick2|\n", y));
        y = handlingYMoreThan450(y, 5);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Ngày thu tiền dự kiến %s -> %s|\n", y, String.format("%02d", dates[0]), String.format("%02d", dates[1])));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Điện thoại: 39 557 795 để được hướng|\n", y));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|dẫn|\n", y));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,0:TIMNR,HMULT1,VMULT1|Phiếu này không có giá trị thanh toán|\n", y));
        y = handlingYMoreThan450(y, 25);
        mmBuilder.append(String.format("@%d,60:TIMNR,HMULT1,VMULT1|Được in vào: %s|\n", y, formatter.format(Calendar.getInstance().getTime())));
        y = handlingYMoreThan450(y, 50);
        mmBuilder.append(String.format("@%d,60:TIMNR,HMULT1,VMULT1| |\n", y));
        mmBuilder.append("}\n");
        mmBuilder.append("{LP}");
        mmOutputStream.write(mmBuilder.toString().getBytes());
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
