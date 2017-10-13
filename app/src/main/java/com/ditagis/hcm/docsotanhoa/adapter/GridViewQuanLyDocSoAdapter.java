package com.ditagis.hcm.docsotanhoa.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.R;

import java.util.List;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class GridViewQuanLyDocSoAdapter extends ArrayAdapter<GridViewQuanLyDocSoAdapter.Item> {
    public static class Item {
        String mtl;
        String danhbo;
        String csc, csm;

        public Item(String mtl, String danhbo, String csc, String csm) {
            this.mtl = mtl;
            this.danhbo = danhbo;
            this.csc = csc;
            this.csm = csm;
        }

        public String getMtl() {
            return mtl;
        }

        public void setMtl(String mtl) {
            this.mtl = mtl;
        }

        public String getDanhbo() {
            return danhbo;
        }

        public void setDanhbo(String danhbo) {
            this.danhbo = danhbo;
        }

        public String getCsc() {
            return csc;
        }

        public void setCsc(String csc) {
            this.csc = csc;
        }

        public String getCsm() {
            return csm;
        }

        public void setCsm(String csm) {
            this.csm = csm;
        }
    }

    private Context context;
    private List<Item> items;

    public GridViewQuanLyDocSoAdapter(Context context, List<Item> items) {
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


    public Item getItem(String mlt) {
        for (Item item : this.items)
            if (item.getMtl().equals(mlt))
                return item;
        return null;
    }

    public boolean removeItem(String mlt) {
        for (Item item : this.items)
            if (item.getMtl().equals(mlt)) {
                this.items.remove(item);
                return true;
            }
        return false;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_quan_ly_doc_so, null);
        }
        Item item = items.get(position);
        TextView txtMaLoTrinh = (TextView) convertView.findViewById(R.id.row_qlds_txt_malotrinh);
        txtMaLoTrinh.setText(item.getMtl());

        TextView txtDanhBo = (TextView) convertView.findViewById(R.id.row_qlds_txt_danhBo);
        txtDanhBo.setText(item.getDanhbo());

        TextView txtCSC = (TextView) convertView.findViewById(R.id.row_qlds_txt_csc);
        txtCSC.setText(item.getCsc());

        TextView txtCSM = (TextView) convertView.findViewById(R.id.row_qlds_txt_csm);
        txtCSM.setText(item.getCsm());


        LinearLayout row_layout = (LinearLayout) convertView.findViewById(R.id.row_qlds_layout);


        row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.color_row_check));

        return convertView;
    }
}
