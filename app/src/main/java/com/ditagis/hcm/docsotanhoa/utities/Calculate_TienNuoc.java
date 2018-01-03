package com.ditagis.hcm.docsotanhoa.utities;

import com.ditagis.hcm.docsotanhoa.entities.Code_CSC_SanLuong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class Calculate_TienNuoc {
    private String mGB;
    private Code_CSC_SanLuong mCodeCSCSanLuong;
    private int mCSC, mCSM, mTieuThu, mTienNuoc, mBVMT, mVAT;
    private String mCSMString;

    public Calculate_TienNuoc(String code, Code_CSC_SanLuong mCodeCSCSanLuong, int csc, String csm) {
        this.mGB = code;
        this.mCodeCSCSanLuong = mCodeCSCSanLuong;
        this.mCSMString = csm;
        this.mCSC = csc;
        this.mCSM = -1;
        this.mTieuThu = -1;
        calculate();
    }

    public String getmGB() {
        return mGB;
    }

    public void setmGB(String mGB) {
        this.mGB = mGB;
    }

    public Code_CSC_SanLuong getmCodeCSCSanLuong() {
        return mCodeCSCSanLuong;
    }

    public void setmCodeCSCSanLuong(Code_CSC_SanLuong mCodeCSCSanLuong) {
        this.mCodeCSCSanLuong = mCodeCSCSanLuong;
    }

    public int getmTienNuoc() {
        return mTienNuoc;
    }

    public int getmBVMT() {
        return mBVMT;
    }

    public int getmVAT() {
        return mVAT;
    }

    public String getCSM() {

        return mCSM == -1 ? "" : mCSM + "";
    }

    public String getTieuThu() {
        return mTieuThu == -1 ? "" : mTieuThu + "";
    }

    public void calculate() {

        switch (mGB) {
            case "11":
            case "12":
            case "13":
                if (!mCSMString.equals("null") && mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - mCSC;
                }

                break;
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
            case "19":
            case "21":
//                if (!mCSMString.equals("null") && mCSMString.length() > 0)
//                    mCSM = Integer.parseInt(mCSMString);
//                else
                mCSM = 0;
                mTieuThu = 0;
                break;
            case "22":
                if (!mCSMString.equals("null") && mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - Integer.parseInt(mCodeCSCSanLuong.getSanLuong1()) - Integer.parseInt(mCodeCSCSanLuong.getCSC1());
                }
                //todo code 5F
                break;
            case "23":
                if (!mCSMString.equals("null") && mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - mCSC;
                }
                //todo code 5K
                break;
            case "24":
                //todo ghi chỉ số ngưng

                mTieuThu = calTieuThuTB();
                mCSM = -1;
                break;
            case "25":
            case "26":
            case "27":
                //todo không ghi chỉ số
                mTieuThu = calTieuThuTB();
                mCSM = mTieuThu + mCSC;
                break;
            case "28":
                //todo để trống
                mTieuThu = calTieuThuTB();
                mCSM = mTieuThu + mCSC;
                break;
            case "29":
                mTieuThu = calTieuThuTB();
                mCSM = -1;
                break;
            case "31":
//                if (!mCSMString.equals("null") && mCSMString.length() > 0)
//                    mCSM = Integer.parseInt(mCSMString);
                mTieuThu = calTieuThuTB();
                mCSM = mCSC + mTieuThu;
                break;
            case "32":
                //todo để trống
                if (!mCSMString.equals("null") && mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - mCSC;
                }
//                mTieuThu = calTieuThuTB();
//                mCSM = mTieuThu + mCSC;
//                mTieuThu = calTieuThuTB();
//                mCSM = mCSC + mTieuThu;
                break;
            case "33":
            case "34":
                //todo để trống
                if (!mCSMString.equals("null") && mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - mCSC;
                }
//                mTieuThu = calTieuThuTB();
//                mCSM = mCSC + mTieuThu;
                break;
            case "35":
                mCSM = mCSC;
                mTieuThu = calTieuThuTB();
                break;
            case "36":
            case "38":
            case "39":
                mTieuThu = calTieuThuTB();
                mCSM = mTieuThu + mCSC;
                break;
            case "41":
                //todo để trống
                mCSM = mCSC;
                mTieuThu = 0;
                break;
            case "42":
            case "43":
            case "44":
            case "45":
                if (!mCSMString.equals("null") && mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - mCSC;
                }
                //todo gắn mới
                break;
            case "46":
            case "51":
            case "52":
                if (!mCSMString.equals("null") && mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                else
                    mCSM = 0;
                mTieuThu = 0;
                break;
            case "53":
                if (!mCSMString.equals("null") && mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                mTieuThu = retour();
                //todo retour
                break;
            case "54":
                //todo
                if (!mCSMString.equals("null") && mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                else
                    mCSM = 0;
                mTieuThu = 0;
                break;
            case "59":
                if (!mCSMString.equals("null") && mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                else
                    mCSM = 0;
                mTieuThu = 0;
                break;
            case "68":
                break;
            default:
                break;

        }

    }

    private int retour() {
        int tieuThu;
        String csmString = "1";

        int lenghtCSC = (mCSC + "").length();
        int lenghtCSM = (mCSM + "").length();
        int needAdd = lenghtCSC - lenghtCSM;
        for (int i = 0; i < needAdd; i++) {
            csmString += "0";
        }
        csmString += (mCSM + "");
        tieuThu = Integer.parseInt(csmString) - mCSC;
        return tieuThu;
    }

    private int calTieuThuTB() {
        int csc = Integer.parseInt(mCodeCSCSanLuong.getCSC1());
        String tt1 = mCodeCSCSanLuong.getSanLuong1();
        String tt2 = mCodeCSCSanLuong.getSanLuong2();
        String tt3 = mCodeCSCSanLuong.getSanLuong3();
        int tieuThu1 = 0, tieuThu2 = 0, tieuThu3 = 0;
        if (tt1.length() > 0)
            tieuThu1 = Integer.parseInt(tt1);
        if (tt2.length() > 0)
            tieuThu2 = Integer.parseInt(tt2);
        if (tt3.length() > 0)
            tieuThu3 = Integer.parseInt(tt3);
        double tempDouble = ((double) (tieuThu1 + tieuThu2 + tieuThu3)) / 3;
        //lấy 1 chữ số sau dấu thập phân
        int tempInt = (int) (tempDouble * 10);
        String s = tempInt + "";
        int lastNumber = Integer.parseInt(s.substring(s.length() - 1));
        if (lastNumber <= 5)
            return tempInt / 10;
        else return tempInt / 10 + 1;
    }

    public static boolean checkCSMFluctuation(String tt, String tt1, String tt2, String tt3) {
        int tieuThu = 0, sum = 0, avergare = 0, min = 0, max = 0;
        List<Integer> tieuThuList = new ArrayList<>();
        if (tt.length() > 0) {
            tieuThu = Integer.parseInt(tt);
            if (tt1.length() > 0)
                tieuThuList.add(Integer.parseInt(tt1));
            if (tt2.length() > 0)
                tieuThuList.add(Integer.parseInt(tt2));
            if (tt3.length() > 0)
                tieuThuList.add(Integer.parseInt(tt3));
            for (Integer item : tieuThuList)
                sum += item;
            avergare = sum / tieuThuList.size();
            min = avergare / 2;
            max = 3 * avergare / 2;
            for (Integer item : tieuThuList)
                if (max < item)
                    max = item;
            if (min <= tieuThu && tieuThu <= max)
                return false;
            else
                return true;

        } else
            return false;
    }


}
