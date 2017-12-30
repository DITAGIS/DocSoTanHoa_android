package com.ditagis.hcm.docsotanhoa.utities;

import android.content.Context;
import android.os.Environment;
import android.view.View;

import com.ditagis.hcm.docsotanhoa.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ThanLe on 12/8/2017.
 */

public class ImageFile {
    public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static File getFile(Date currentTime, View rootView, String danhBo){

        if (currentTime == null)
            return null;
        String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
        File outFile = new File(path, rootView.getContext().getString(R.string.path_saveImage));

        if (!outFile.exists())
            outFile.mkdir();
        String datetime = formatter.format(currentTime);
        File f = new File(outFile, datetime + "_" + danhBo + ".jpeg");
        return f;
    }
    public static File getFile(String dateTime, Context context, String danhBo){

        String path = Environment.getExternalStorageDirectory().getPath();
//                path = path.substring(0, path.length() - 1).concat("1");
        File outFile = new File(path,context.getString(R.string.path_saveImage));

        if (!outFile.exists())
            outFile.mkdir();

        File f = new File(outFile, dateTime + "_" + danhBo + ".jpeg");
        return f;
    }
}
