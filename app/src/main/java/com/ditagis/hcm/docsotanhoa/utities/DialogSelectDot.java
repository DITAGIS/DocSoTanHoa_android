package com.ditagis.hcm.docsotanhoa.utities;

import android.content.Context;
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
import com.ditagis.hcm.docsotanhoa.entities.HoaDon;
import com.ditagis.hcm.docsotanhoa.localdb.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanLe on 1/8/2018.
 */

public class DialogSelectDot {
    public static int show(Context context, int mDot, int mKy, int mNam, String mUsername, final DocSo docSo) {
        final GridViewSelectFolderAdapter selectFolderAdapter = new GridViewSelectFolderAdapter(context, new ArrayList<GridViewSelectFolderAdapter.Item>());

        int count = 0;
        for (int i = 20; i >= 1; i--) {
            if (count == 3)
                break;
            String dotString = i + "";
            if (i < 10)
                dotString = "0" + i;
            List<HoaDon> hoaDons = LocalDatabase.getInstance(context).getAllHoaDon(mUsername, dotString, mKy + "");
            if (hoaDons.size() > 0)
                selectFolderAdapter.add(new GridViewSelectFolderAdapter.Item(mKy + "", i + "", mNam + "",
                        hoaDons.size() + "", mUsername, hoaDons.get(0).getFlag()));
            if (selectFolderAdapter.getCount() > 0)
                count++;

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Chọn quyển đọc số");
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
                docSo.selectDotFromDialog(selectFolderAdapter.getItem(position).getDot());
                dialog.dismiss();
            }
        });
        return 0;
    }

    public static int show(Context context, int mDot, int mKy, int mNam, String mUsername, final QuanLyDocSo qlds) {
        final GridViewSelectFolderAdapter selectFolderAdapter = new GridViewSelectFolderAdapter(context, new ArrayList<GridViewSelectFolderAdapter.Item>());

        int count = 0;
        for (int i = 20; i >= 1; i--) {
            if (count == 3)
                break;
            String dotString = i + "";
            if (i < 10)
                dotString = "0" + i;
            List<HoaDon> hoaDons = LocalDatabase.getInstance(context).getAllHoaDonForSelectFolderQLDS(dotString + mUsername + "%", Flag.UNREAD);
            if (hoaDons.size() > 0)
                selectFolderAdapter.add(new GridViewSelectFolderAdapter.Item(mKy + "", i + "", mNam + "",
                        hoaDons.size() + "", mUsername, hoaDons.get(0).getFlag()));
            if (selectFolderAdapter.getCount() > 0)
                count++;

        }
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
                qlds.selectDotFromDialog(selectFolderAdapter.getItem(position).getDot());
                dialog.dismiss();
            }
        });
        return 0;
    }
}
