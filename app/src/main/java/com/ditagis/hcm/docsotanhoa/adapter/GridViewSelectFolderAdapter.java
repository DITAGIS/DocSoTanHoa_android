package com.ditagis.hcm.docsotanhoa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.R;

import java.util.List;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class GridViewSelectFolderAdapter extends ArrayAdapter<GridViewSelectFolderAdapter.Item> {
    private Context context;
    private List<Item> items;
    public GridViewSelectFolderAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


//    public Item getItem(String mlt) {
//        for (Item item : this.items)
//            if (item.getTieuThu().equals(mlt))
//                return item;
//        return null;
//    }
//
//    public boolean removeItem(String mlt) {
//        for (Item item : this.items)
//            if (item.getTieuThu().equals(mlt)) {
//                this.items.remove(item);
//                return true;
//            }
//        return false;
//    }

    @NonNull
    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.row_select_folder, null);
        }
        Item item = items.get(position);


        TextView txtDanhBo =  convertView.findViewById(R.id.row_select_folder_txt_quyen);
        //todo
        txtDanhBo.setText(item.getNam() + "_" + item.getKy() + "_" + item.getDot() + "_" + item.getMay());

        TextView txtCSC = convertView.findViewById(R.id.row_select_folder_txt_KH);
        txtCSC.setText(item.getSoLuong());

        TextView txtCSM =  convertView.findViewById(R.id.row_select_folder_txt_dot);
        txtCSM.setText(item.getDot());

        TextView txtSanLuong =  convertView.findViewById(R.id.row_select_folder_txt_ky);
        txtSanLuong.setText(item.getKy());
        TextView txtCode =  convertView.findViewById(R.id.row_select_folder_txt_nam);
        txtCode.setText(item.getNam());
        return convertView;
    }

    public static class Item {
        String ky;
        String dot;
        String nam;
        String soLuong;
        String may;
        int flag;

        public Item(String ky, String dot, String nam, String soLuong, String may, int flag) {
            this.ky = ky;
            this.dot = dot;
            this.nam = nam;
            this.soLuong = soLuong;
            this.may = may;
            this.flag = flag;
        }

        public String getMay() {
            return may;
        }

        public String getKy() {
            return ky;
        }

        public String getDot() {
            return dot;
        }

        public String getNam() {
            return nam;
        }

        String getSoLuong() {
            return soLuong;
        }

        public int getFlag() {
            return flag;
        }
    }
}
