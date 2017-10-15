package com.ditagis.hcm.docsotanhoa.entities;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ThanLe on 15/10/2017.
 */

public class test {

    public static void main(String[] args) {
//        String path = Environment.getExternalStorageDirectory().getPath();
        String path = "a123";
//                path = path.substring(0, path.length() - 1).concat("1");
        File outFile = new File(path, "DocSoTanHoa");

        String s1 = outFile.getAbsolutePath();
        Date currentTime = Calendar.getInstance().getTime();
        File f = new File(outFile, currentTime.toString() + ".jpeg");
        System.out.println(f.getAbsolutePath());
        System.out.println(f.getAbsolutePath().substring(s1.length() + 1).split("\\.")[0]);
    }
}
