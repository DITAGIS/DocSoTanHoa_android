package com.ditagis.hcm.docsotanhoa.utities;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class Calculate_TienNuoc {
    private String mGB;
    private int mDM;
    private int mTieuThu;
    private double mTienNuoc, mBVMT, mVAT;
    private int mSH, mSX, mDV, mHC;
    private int SHTM, SHVM1, SHVM2;

    public Calculate_TienNuoc(int tieuthu, String gb, String dm, int sh, int sx, int dv, int hc) {
        this.mGB = gb;
        mDM = Integer.parseInt(dm);
        mSH = sh;
        mSX = sx;
        mDV = dv;
        mHC = hc;
        this.mTieuThu = tieuthu;
        calculate();
    }

    public String getmGB() {
        return mGB;
    }

    public void setmGB(String mGB) {
        this.mGB = mGB;
    }


    public double getmTienNuoc() {
        return mTienNuoc;
    }

    public double getmBVMT() {
        return mBVMT;
    }

    public double getmVAT() {
        return mVAT;
    }


    public String getTieuThu() {
        return mTieuThu == -1 ? "" : mTieuThu + "";
    }

    private void reset() {
        SHTM = SHVM1 = SHVM2 = 0;
    }

    private int getSHTM() {
        return mTieuThu - mDM > 0 ? mDM : mTieuThu;
    }

    private int getSHVM() {
        return mTieuThu - SHTM;
    }

    private int getSHVM1() {
        return mTieuThu - 2 * mDM > 0 ? mDM : mTieuThu - SHTM;
    }

    private int getSHVM2() {
        return mTieuThu - SHTM - SHVM1;
    }

    private boolean hasRatio() {
        return mSH > 0 || mSX > 0 || mDV > 0 || mHC > 0;
    }

    private boolean hasSHVM() {
        return SHTM >= mDM;
    }

    private boolean hasSHVM2() {
        return SHVM1 >= mDM;
    }

    private void calculateSHTM_VM1_VM2() {
        SHTM = getSHTM();
        if (hasSHVM()) {
            SHVM1 = getSHVM1();
            if (hasSHVM2())
                SHVM2 = getSHVM2();
        }
    }

    private void calculateSHTM_VM() {
        SHTM = getSHTM();
        if (hasSHVM())
            SHVM1 = getSHVM();
    }

    private int calculateTienNuoc_TM_VM1_VM2() {
        return SHTM * 5300 + SHVM1 * 10200 + SHVM2 * 11400;
    }

    private int calculateSH() {
        return mTieuThu * 11400;
    }

    private int calculateSX() {
        return mTieuThu * 9600;
    }

    private int calculateDV() {
        return mTieuThu * 16900;
    }

    private int calculateHC() {
        return mTieuThu * 10300;
    }

    public void calculate() {

        switch (mGB) {
            case "11":
                reset();
                calculateSHTM_VM1_VM2();
                mTienNuoc = calculateTienNuoc_TM_VM1_VM2();
                break;
            case "12":
            case "22":
            case "32":
            case "42":
                mTienNuoc = mTieuThu * 9600;
                break;
            case "13":
            case "23":
            case "33":
            case "43":
                mTienNuoc = mTieuThu * 16900;
                break;
            case "14":
            case "24":
                reset();
                if (hasRatio()) {
                    calculateSHTM_VM1_VM2();
                    double ratioSH = (double)mSH / 100, ratioSX = (double)mSX / 100;
                    mTienNuoc = ratioSH * calculateTienNuoc_TM_VM1_VM2() + ratioSX * calculateSX();
                } else {
                    calculateSHTM_VM();
                    mTienNuoc = SHTM * 5300 + SHVM1 * 9600;
                }
                break;
            case "15":
            case "25":
                reset();
                if (hasRatio()) {
                    calculateSHTM_VM1_VM2();
                    double ratioSH =(double) mSH / 100, ratioDV = (double)mDV / 100;
                    mTienNuoc = ratioSH * calculateTienNuoc_TM_VM1_VM2() + ratioDV * calculateDV();
                } else {
                    calculateSHTM_VM();
                    mTienNuoc = SHTM * 5300 + SHVM1 * 16900;
                }
                break;
            case "16":
            case "26":
                reset();
                if (mSH > 0) {
                    calculateSHTM_VM1_VM2();
                    double ratioSH = (double)mSH / 100, ratioSX = (double)mSX / 100, ratioDV = (double)mDV / 100;
                    mTienNuoc = ratioSH * calculateTienNuoc_TM_VM1_VM2() + ratioSX * calculateSX() + ratioDV * calculateDV();
                } else {
                    calculateSHTM_VM();
                    double ratioSX = (double)mSX / 100, ratioDV = (double)mDV / 100;
                    mTienNuoc = ratioSX * calculateSX() + ratioDV * calculateDV();
                }
                break;
            case "17":
            case "27":
                mTienNuoc = mTieuThu * 5300;
                break;
            case "18":
            case "28":
            case "38":
                reset();
                if (hasRatio()) {
                    calculateSHTM_VM1_VM2();
                    double ratioSH = (double)mSH / 100, ratioHC = (double)mHC / 100;
                    mTienNuoc = ratioSH * calculateTienNuoc_TM_VM1_VM2() + ratioHC * calculateHC();
                } else {
                    calculateSHTM_VM();
                    mTienNuoc = SHTM * 5300 + SHVM1 * 10300;
                }
                break;
            case "19":
            case "29":
            case "39":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSH =(double) mSH / 100, ratioSX =(double) mSX / 100, ratioDV = (double)mDV / 100, ratioHC =(double) mHC / 100;
                mTienNuoc = ratioSH * calculateTienNuoc_TM_VM1_VM2() + ratioSX * calculateSX() + ratioDV * calculateDV() + ratioHC * calculateHC();
                break;
            case "21":
                reset();
                calculateSHTM_VM1_VM2();
                mTienNuoc = calculateTienNuoc_TM_VM1_VM2();
                break;
            case "31":
                mTienNuoc = mTieuThu * 10300;
                break;
            case "34":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSX34 = (double)mSX / 100, ratioHC34 =(double) mHC / 100;
                mTienNuoc = ratioSX34 * calculateSX() + ratioHC34 * calculateHC();
                break;
            case "35":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioDV35 = (double)mDV / 100, ratioHC35 = (double)mHC / 100;
                mTienNuoc = ratioDV35 * calculateDV() + ratioHC35 * calculateHC();
                break;
            case "36":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSX36 = (double)mSX / 100, ratioDV36 =(double) mDV / 100, ratioHC36 =(double) mHC / 100;
                mTienNuoc = ratioSX36 * calculateSX() + ratioDV36 * calculateDV() + ratioHC36 * calculateHC();
                break;
            case "41":
                mTienNuoc = mTieuThu * 11400;
                break;
            case "44":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSX44 = (double)mSX / 100, ratioSH44 =(double) mSH / 100;
                mTienNuoc = ratioSX44 * calculateSX() + ratioSH44 * calculateSH();
                break;
            case "45":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioDV45 = (double)mDV / 100, ratioSH45 =(double) mSH / 100;
                mTienNuoc = ratioDV45 * calculateDV() + ratioSH45 * calculateSH();
                break;
            case "46":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSX46 =(double) mSX / 100, ratioSH46 = (double)mSH / 100, ratioDV46 =(double) mDV / 100;
                mTienNuoc = ratioSX46 * calculateSX() + ratioSH46 * calculateSH() + ratioDV46 * calculateDV();
                break;
            case "51":
                reset();
                calculateSHTM_VM1_VM2();

                mTienNuoc = SHTM * 4770 + SHVM1 * 9180 + SHVM2 * 10260;
                break;
            case "52":
                mTienNuoc = mTieuThu * 8640;
                break;
            case "53":
                mTienNuoc = mTieuThu * 15210;
                break;
            case "54":
                mTienNuoc = mTieuThu * 9270;
                break;
            case "59":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSX59 = (double)mSX / 100, ratioSH59 =(double) mSH / 100, ratioDV59 =(double) mDV / 100, ratioHC59 = (double)mHC / 100;
                mTienNuoc = ratioSH59 * (SHTM * 4770 + SHVM1 * 9180 + SHVM2 * 10260) +
                        ratioSX59 * mTieuThu * 8640 + ratioDV59 * mTieuThu * 15210 + ratioHC59 * mTieuThu * 9270;
                break;
            case "68":
                reset();
                calculateSHTM_VM1_VM2();
                double ratioSH68 = (double)mSH / 100, ratioDV68 =(double) mDV / 100;
                mTienNuoc = ratioSH68 * (SHTM * 4770 + SHVM1 * 9180 + SHVM2 * 10260) +
                        +ratioDV68 * mTieuThu * 15210;
                break;
            default:
                mTienNuoc = 0;
                break;

        }

    }

}
