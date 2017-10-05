package com.ditagis.hcm.docsotanhoa;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class DocSoChupAnhActivity extends AppCompatActivity {
    private ImageButton buttonImage;
    private ImageView imageView;

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_so_chup_anh);

        this.buttonImage = (ImageButton) this.findViewById(R.id.imgBtn_dsca_ChupAnh);
        this.imageView = (ImageView) this.findViewById(R.id.img_dsca_view);

        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

    }

    private void captureImage() {
        // Tạo một Intent không tường minh,
        // để yêu cầu hệ thống mở Camera chuẩn bị chụp hình.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start Activity chụp hình, và chờ đợi kết quả trả về.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }




    // Khi yêu cầu hỏi người dùng được trả về (Chấp nhận hoặc không chấp nhận).
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case REQUEST_ID_READ_WRITE_PERMISSION: {

                // Chú ý: Nếu yêu cầu bị hủy, mảng kết quả trả về là rỗng.
                // Người dùng đã cấp quyền (đọc/ghi).
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.captureImage();

                }
                // Hủy bỏ hoặc bị từ chối.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    // Khi activy chụp hình (Hoặc quay video) hoàn thành, phương thức này sẽ được gọi.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageView.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

}
