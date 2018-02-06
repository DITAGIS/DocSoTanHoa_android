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
import com.ditagis.hcm.docsotanhoa.utities.Flag;

import java.util.List;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class GridViewQuanLyDocSoAdapter extends ArrayAdapter<GridViewQuanLyDocSoAdapter.Item> {
    private Context context;
    private List<Item> items;
    public GridViewQuanLyDocSoAdapter(Context context, List<Item> items) {
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

    public Item getItem(String mlt) {
        for (Item item : this.items)
            if (item.getTieuThu().equals(mlt))
                return item;
        return null;
    }

    public boolean removeItem(String mlt) {
        for (Item item : this.items)
            if (item.getTieuThu().equals(mlt)) {
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

        TextView txtMLT = (TextView) convertView.findViewById(R.id.row_qlds_txt_mlt);
        txtMLT.setText(item.getMlt());

        TextView txtDanhBo = (TextView) convertView.findViewById(R.id.row_qlds_txt_danhBo);
        txtDanhBo.setText(item.getDanhbo());

        TextView txtCSC = (TextView) convertView.findViewById(R.id.row_qlds_txt_csc);
        txtCSC.setText(item.getCsc());

        TextView txtCSM = (TextView) convertView.findViewById(R.id.row_qlds_txt_csm);
        txtCSM.setText(item.getCsm());

        TextView txtSanLuong = (TextView) convertView.findViewById(R.id.row_qlds_txt_sanLuong);
        txtSanLuong.setText(item.getTieuThu());
        TextView txtCode = (TextView) convertView.findViewById(R.id.row_qlds_txt_code);
        txtCode.setText(item.getCode());

        TextView txtDiaChi = (TextView) convertView.findViewById(R.id.row_qlds_txt_diaChi);
        txtDiaChi.setText(item.getDiaChi());

        TextView txtThoiGian = (TextView) convertView.findViewById(R.id.row_qlds_txt_thoiGian);
        txtThoiGian.setText(item.getThoiGian());

        TextView txtTrangThai = (TextView) convertView.findViewById(R.id.row_qlds_txt_trangThai);

        LinearLayout row_layout = (LinearLayout) convertView.findViewById(R.id.row_qlds_layout);

        switch (item.getFlag()) {
            case Flag.UNREAD:
                txtTrangThai.setText(parent.getContext().getApplicationContext().getString(R.string.flag_unread));
                break;
            case Flag.READ:
                txtTrangThai.setText(parent.getContext().getApplicationContext().getString(R.string.flag_read));
                row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorBackground_1));
                break;
            case Flag.SYNCHRONIZED:
                txtTrangThai.setText(parent.getContext().getApplicationContext().getString(R.string.flag_sych));
                row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.color_row_check_1));
                break;
            case Flag.CODE_F:
                txtTrangThai.setText(parent.getContext().getApplicationContext().getString(R.string.flag_code_f));
                row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorAlertWrongPassword_2));
                break;
        }


//        row_layout.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.color_row_check_1));

        return convertView;
    }

    public static class Item {
        String mlt;
        String tieuThu;
        String danhbo;
        String csc, csm;
        String code;
        String diaChi;
        String thoiGian;
        int flag;

        public Item(String tieuThu,String mlt, String danhbo, String csc, String csm, String code, String diaChi, String thoiGian, int flag) {
            this.tieuThu = tieuThu;
            this.mlt = mlt;
            this.danhbo = danhbo;
            this.csc = csc;
            this.csm = csm;
            this.code = code;
            this.diaChi = diaChi;
            this.thoiGian = thoiGian;
            this.flag = flag;
        }

        public String getMlt() {
            return mlt;
        }

        public void setMlt(String mlt) {
            this.mlt = mlt;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getDiaChi() {
            return diaChi;
        }

        public void setDiaChi(String diaChi) {
            this.diaChi = diaChi;
        }

        public String getThoiGian() {
            return thoiGian;
        }

        public void setThoiGian(String thoiGian) {
            this.thoiGian = thoiGian;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTieuThu() {
            return tieuThu;
        }

        public void setTieuThu(String tieuThu) {
            this.tieuThu = tieuThu;
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
}
