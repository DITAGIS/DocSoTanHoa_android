package com.ditagis.hcm.docsotanhoa.utities;

/**
 * Created by ThanLe on 11/22/2017.
 */

public class Calculate_TienNuoc {
    private String mGB;
    private int mDM;
    private int mTieuThu, mTieuThuSH, mTieuThuSX, mTieuThuDV, mTieuThuHC;
    private double mTienNuoc;
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

    public double getmTienNuoc() {
        return mTienNuoc;
    }


    private void reset() {
        SHTM = SHVM1 = SHVM2 = mTieuThuSH = mTieuThuSX = mTieuThuDV = mTieuThuHC = 0;
    }

    private int getSHTM() {
        SHTM = mTieuThuSH - mDM > 0 ? mDM : mTieuThuSH;
        return SHTM;
    }

    private int getSHVM() {
        return mTieuThuSH - SHTM;
    }

    private int getSHVM1() {
        SHVM1 = mTieuThuSH - 1.5 * mDM > 0 ? mDM / 2 : mTieuThuSH - SHTM;
        return SHVM1;
    }

    private int getSHVM2() {
        SHVM2 = mTieuThuSH - SHTM - SHVM1;
        return SHVM2;
    }

    private boolean hasRatio() {
        return mSH > 0 || mSX > 0 || mDV > 0 || mHC > 0;
    }

    private boolean hasSHVM() {
        return SHTM >= mDM;
    }

    private boolean hasSHVM2() {
        return SHVM1 >=(double) mDM/2;
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
        return mTieuThuSH * 11400;
    }

    private int calculateSX() {
        return mTieuThuSX * 9600;
    }

    private int calculateDV() {
        return mTieuThuDV * 16900;
    }

    private int calculateHC() {
        return mTieuThuHC * 10300;
    }

    public void calculate() {

        switch (mGB) {
            case "11":
                reset();
                if (mDM == 0)
                    mTienNuoc = mTieuThu * 11400;
                else {
                    mTieuThuSH = mTieuThu;
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2();
                }
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

                    mTieuThuSH = (int) ((double) mSH / 100 * mTieuThu);
                    mTieuThuSX = (int) ((double) mSX / 100 * mTieuThu);
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateSX();
                } else {
                    mTieuThuSH = mTieuThu;
                    calculateSHTM_VM();
                    mTienNuoc = SHTM * 5300 + SHVM1 * 9600;
                }
                break;
            case "15":
            case "25":
                reset();
                if (hasRatio()) {

                    mTieuThuSH = (int) ((double) mSH / 100 * mTieuThu);
                    mTieuThuDV = (int) ((double) mDV / 100 * mTieuThu);
                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateDV();
                } else {
                    mTieuThuSH = mTieuThu;
                    calculateSHTM_VM();
                    mTienNuoc = SHTM * 5300 + SHVM1 * 16900;
                }
                break;
            case "16":
            case "26":
                reset();
                if (mSH > 0) {

                    mTieuThuSH = (int) ((double) mSH / 100 * mTieuThu);
                    mTieuThuDV = (int) ((double) mDV / 100 * mTieuThu);
                    mTieuThuSX = (int) ((double) mSX / 100 * mTieuThu);

                    calculateSHTM_VM1_VM2();
                    mTienNuoc = calculateTienNuoc_TM_VM1_VM2() + calculateSX() + calculateDV();
                } else {
                    if (mTieuThu >= mDM) {
                        mTieuThuSH = mDM;
                        mTieuThuSX = (int) ((double) mSX / 100 * (mTieuThu - mDM));
                        mTieuThuDV = (int) ((double) mDV / 100 * (mTieuThu - mDM));
                    } else {
                        mTieuThuSH = mTieuThu;
                    }
                    mTienNuoc = calculateSH() + calculateSX() + calculateDV();
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
                    mTieuThuSH = (int) ((double) mSH / 100 * mTieuThu);
                    mTieuThuHC = (int) ((double) mHC / 100 * mTieuThu);
                    calculateTienNuoc_TM_VM1_VM2();
                    mTienNuoc = calculateSH() + calculateHC();
                } else {
                    calculateSHTM_VM();
                    mTienNuoc = SHTM * 5300 + SHVM1 * 10300;
                }
                break;
            case "19":
            case "29":
            case "39":
                reset();
                mTieuThuSH = (int) ((double) mSH / 100 * mTieuThu);
                mTieuThuSX = (int) ((double) mSX / 100 * mTieuThu);
                mTieuThuDV = (int) ((double) mDV / 100 * mTieuThu);
                mTieuThuHC = (int) ((double) mHC / 100 * mTieuThu);
                calculateSHTM_VM1_VM2();
                mTienNuoc = calculateSH() + calculateSX() + calculateDV() + calculateHC();
                break;
            case "21":
                reset();
                mTieuThuSH = mTieuThu;
                calculateSHTM_VM1_VM2();
                mTienNuoc = calculateTienNuoc_TM_VM1_VM2();
                break;
            case "31":
                mTienNuoc = mTieuThu * 10300;
                break;
            case "34":
                reset();
                mTieuThuSX = (int) ((double) mSX / 100 * mTieuThu);
                mTieuThuHC = (int) ((double) mHC / 100 * mTieuThu);
                mTienNuoc = calculateSX() + calculateHC();
                break;
            case "35":
                reset();
                mTieuThuDV = (int) ((double) mDV / 100 * mTieuThu);
                mTieuThuHC = (int) ((double) mHC / 100 * mTieuThu);
                mTienNuoc = calculateDV() + calculateHC();
                break;
            case "36":
                reset();
                mTieuThuSX = (int) ((double) mSX / 100 * mTieuThu);
                mTieuThuDV = (int) ((double) mDV / 100 * mTieuThu);
                mTieuThuHC = (int) ((double) mHC / 100 * mTieuThu);
                mTienNuoc = calculateSX() + calculateDV() + calculateHC();
                break;

            case "51":
                reset();
                mTieuThuSH = mTieuThu;
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

            default:
                mTienNuoc = 0;
                break;

        }

    }

}
