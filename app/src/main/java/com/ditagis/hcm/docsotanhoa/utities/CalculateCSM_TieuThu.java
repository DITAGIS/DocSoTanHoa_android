package com.ditagis.hcm.docsotanhoa.utities;

import com.ditagis.hcm.docsotanhoa.entities.Code_CSC_SanLuong;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class CalculateCSM_TieuThu {
    private String mCode;
    private Code_CSC_SanLuong mCodeCSCSanLuong;
    private int mCSC, mCSM, mTieuThu;
    private String mCSMString;

    public CalculateCSM_TieuThu(String code, Code_CSC_SanLuong mCodeCSCSanLuong,int csc, String csm) {
        this.mCode = code;
        this.mCodeCSCSanLuong = mCodeCSCSanLuong;
        this.mCSMString = csm;
        this.mCSC = csc;
        this.mCSM = -1;
        this.mTieuThu = -1;
        calculate();
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public Code_CSC_SanLuong getmCodeCSCSanLuong() {
        return mCodeCSCSanLuong;
    }

    public void setmCodeCSCSanLuong(Code_CSC_SanLuong mCodeCSCSanLuong) {
        this.mCodeCSCSanLuong = mCodeCSCSanLuong;
    }

    public String getCSM() {

        return mCSM == -1 ? "" : mCSM + "";
    }

    public String getTieuThu() {
        return mTieuThu == -1 ? "" : mTieuThu + "";
    }

    public void calculate() {
        switch (mCode) {
            case "40":
            case "41":
            case "42":
                if (mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - mCSC;
                }

                break;
            case "54":
            case "55":
            case "56":
            case "58":
            case "5M":
            case "5Q":
            case "5N":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                mTieuThu = 0;
                break;
            case "5F":
                if (mCSMString.length() > 0) {
                    mCSM = Integer.parseInt(mCSMString);
                    mTieuThu = mCSM - Integer.parseInt(mCodeCSCSanLuong.getSanLuong1()) - Integer.parseInt(mCodeCSCSanLuong.getCSC1());
                }
                //todo code 5F
                break;
            case "5K":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                //todo code 5K
                break;
            case "60":
                //todo ghi chỉ số ngưng
                mCSM = -1;
                mTieuThu = calTieuThuTB();
                break;
            case "61":
            case "63":
            case "66":
                //todo không ghi chỉ số
                mTieuThu = calTieuThuTB();
                break;
            case "62":
            case "80":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                mTieuThu = calTieuThuTB();
                break;
            case "81":
                //todo để trống
                break;
            case "82":
            case "83":
                //todo để trống
                break;
            case "F1":
                //todo không ghi chỉ số
                mTieuThu = calTieuThuTB();
                break;
            case "F2":
            case "F3":
            case "F4":
                //todo để trống
                mTieuThu = calTieuThuTB();
                break;
            case "K":
                //todo để trống
                mTieuThu = 0;
                break;
            case "M0":
            case "M1":
            case "M2":
            case "M3":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                //todo gắn mới
                break;
            case "N1":
            case "N2":
            case "N3":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                mTieuThu = 0;
                break;
            case "X":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                //todo retour
                break;
            case "68":
                //todo
                mTieuThu = 0;
                break;
            case "Q":
                if (mCSMString.length() > 0)
                    mCSM = Integer.parseInt(mCSMString);
                mTieuThu = 0;
                break;
            default:
                break;

        }

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
        int tieuThu = (tieuThu1 + tieuThu2 + tieuThu3) / 3;


        return tieuThu;
    }
}
