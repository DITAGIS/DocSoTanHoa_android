package com.ditagis.hcm.docsotanhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.R;

import java.util.List;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class GridViewLayLoTrinhAdapter extends ArrayAdapter<GridViewLayLoTrinhAdapter.Item> {
    public static class Item {
        String mtl;
        int danhbo;
        int checkpos;

        public Item(String mtl, int danhbo, int checkpos) {
            this.mtl = mtl;
            this.danhbo = danhbo;
            this.checkpos = checkpos;
        }

        public String getMtl() {
            return mtl;
        }

        public void setMtl(String mtl) {
            this.mtl = mtl;
        }

        public int getDanhbo() {
            return danhbo;
        }

        public void setDanhbo(int danhbo) {
            this.danhbo = danhbo;
        }

        public int getCheckpos() {
            return checkpos;
        }

        public void setCheckpos(int checkpos) {
            this.checkpos = checkpos;
        }
    }

    private Context context;
    private List<Item> items;

    public GridViewLayLoTrinhAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_lay_lo_trinh, null);
        }
        Item item = items.get(position);
        TextView txtMaLoTrinh = (TextView) convertView.findViewById(R.id.row_llt_txt_malotrinh);
        txtMaLoTrinh.setText(item.getMtl());

        TextView txtTongDanhBo = (TextView) convertView.findViewById(R.id.row_llt_txt_tongDanhBo);
        txtTongDanhBo.setText(item.getDanhbo() == 0 ? "Chưa xác định" : item.getDanhbo() + "");

        ImageView imgCheck = (ImageView) convertView.findViewById(R.id.row_llt_img_Check);
        if (item.getCheckpos() == 1)
            imgCheck.setImageResource(R.drawable.checked);
        else
            imgCheck.setImageResource(0);
        return convertView;
    }
}
