package com.ditagis.hcm.docsotanhoa.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ThanLe on 11/10/2017.
 */

public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;
    private String danhbo;

    public PhotoHandler(Context context, String danhBo) {
        this.context = context;
        this.danhbo = danhBo;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        File pictureFileDir = getDir();

//        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
//
//            Toast.makeText(context, "Có lỗi xảy ra khi lưu ảnh",
//                    Toast.LENGTH_LONG).show();
//            return;
//
//        }

        String photoFile = "Doc so Tan Hoa/" + File.separator + danhbo + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(context, "Đã lưu:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Toast.makeText(context, "Có lỗi xảy ra khi lưu ảnh",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, File.separator + "Doc so Tan Hoa" + File.separator + danhbo + ".jpg");
    }
}

