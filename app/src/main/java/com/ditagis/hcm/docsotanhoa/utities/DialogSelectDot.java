package com.ditagis.hcm.docsotanhoa.utities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ditagis.hcm.docsotanhoa.DocSo;
import com.ditagis.hcm.docsotanhoa.QuanLyDocSo;
import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.adapter.GridViewSelectFolderAdapter;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;

/**
 * Created by ThanLe on 1/8/2018.
 */

public class DialogSelectDot {
    public static int show(final Context context, int mDot, int mKy, int mNam, String mUsername, final DocSo docSo) {
        final GridViewSelectFolderAdapter selectFolderAdapter = new GridViewSelectFolderAdapter(context, new ArrayList<GridViewSelectFolderAdapter.Item>());

        int count = 0;
        for (int ky = 12; ky >= 1; ky--)
            for (int dot = 20; dot >= 1; dot--) {
//                if (count > CONSTANT.MAX_DOT)
//                    break;
                String dotString = dot + "";
                if (dot < 10)
                    dotString = "0" + dot;
                int size = LocalDatabase.getInstance(context).getAllHoaDonSize(mUsername, dotString, ky + "", false);
                if (size > 0)
                    selectFolderAdapter.add(new GridViewSelectFolderAdapter.Item(String.format("%02d", ky), String.format("%02d", dot), mNam + "",
                            size + "", mUsername, Flag.UNREAD));
                if (selectFolderAdapter.getCount() > 0)
                    count++;
                if (selectFolderAdapter.getCount() > CONSTANT.MAX_DOT)
                    break;

            }

        while (selectFolderAdapter.getCount() > CONSTANT.MAX_DOT) {
            GridViewSelectFolderAdapter.Item item = selectFolderAdapter.getItem(selectFolderAdapter.getCount() - 1);
            LocalDatabase.getInstance(context).deleteHoaDon(item.getKy(), item.getDot());
            selectFolderAdapter.remove(item);
        }
        if (selectFolderAdapter.getCount() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle("Chọn quyển đọc số");
            builder.setMessage("Nhấn và giữ quyển cần xóa trong 2 giây để xóa");
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_dot_folder, null);
            final GridView gridView = (GridView) dialogLayout.findViewById(R.id.grid_select_dot_folder);
            gridView.setAdapter(selectFolderAdapter);

            builder.setView(dialogLayout);
            final AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    docSo.selectDotFromDialog(selectFolderAdapter, selectFolderAdapter.getItem(position).getKy(), selectFolderAdapter.getItem(position).getDot());
                    dialog.dismiss();
                }
            });
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (selectFolderAdapter.getCount() <= 1) {
                        MySnackBar.make(gridView, "Không xóa được quyển do số quyển <= " + 1, true);
                        return true;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                    builder.setTitle("Xóa quyển đọc số");
                    final GridViewSelectFolderAdapter.Item item = selectFolderAdapter.getItem(position);
                    builder.setMessage("Bạn có muốn xóa quyển " + item.getNam() + "_" + item.getKy() + "_" + item.getDot() + "_" + item.getMay() + "?"
                            + "\nMọi dữ liệu chưa đọc, đã đọc và đã đồng bộ thuộc quyển này sẽ bị xóa!!!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LocalDatabase.getInstance(context).deleteHoaDon(item.getKy(), item.getDot());
                            selectFolderAdapter.remove(selectFolderAdapter.getItem(position));
                            selectFolderAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    return true;
                }
            });
        }
        return 0;
    }

    public static int show(Context context, int mDot, int mKy, int mNam, String mUsername, final QuanLyDocSo qlds) {
        final GridViewSelectFolderAdapter selectFolderAdapter = new GridViewSelectFolderAdapter(context, new ArrayList<GridViewSelectFolderAdapter.Item>());

        int count = 0;
        for (int ky = 12; ky >= 1; ky--)
            for (int dot = 20; dot >= 1; dot--) {
                if (count == CONSTANT.MAX_DOT)
                    break;
                String dotString = dot + "";
                if (dot < 10)
                    dotString = "0" + dot;
                int size = LocalDatabase.getInstance(context).getAllHoaDonSize(mUsername, dotString, ky + "", false);
                if (size > 0)
                    selectFolderAdapter.add(new GridViewSelectFolderAdapter.Item(ky + "", dot + "", mNam + "",
                            size + "", mUsername, Flag.UNREAD));
                if (selectFolderAdapter.getCount() > 0)
                    count++;
            }
        if (count > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle("Chọn quyển đã đọc");
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogLayout = inflater.inflate(R.layout.layout_dialog_select_dot_folder, null);
            GridView gridView = (GridView) dialogLayout.findViewById(R.id.grid_select_dot_folder);
            gridView.setAdapter(selectFolderAdapter);

            builder.setView(dialogLayout);
            final AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                docSo.selectDotFromDialog(selectFolderAdapter.getItem(position).getDot());
//                dialog.dismiss();
//                return false;
//            }
//        });
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    qlds.selectDotFromDialog(selectFolderAdapter, selectFolderAdapter.getItem(position).getKy(), selectFolderAdapter.getItem(position).getDot());
                    dialog.dismiss();
                }
            });
        }
        return 0;
    }
}
