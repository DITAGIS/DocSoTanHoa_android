package com.ditagis.hcm.docsotanhoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class GridViewLayLoTrinhAdapter extends BaseAdapter {
private Context context;
    private String[] _maLoTrinh;
    private int[] _tongDanhBo;

    public GridViewLayLoTrinhAdapter(Context context, String[] _maLoTrinh, int[] _tongDanhBo) {
        this.context = context;
        this._maLoTrinh = _maLoTrinh;
        this._tongDanhBo = _tongDanhBo;
    }

    public GridViewLayLoTrinhAdapter(Context context, String[] _maLoTrinh) {
        this.context = context;
        this._maLoTrinh = _maLoTrinh;
    }

    @Override
    public int getCount() {
        return _maLoTrinh.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_lay_lo_trinh, null);

        TextView txtMaLoTrinh = (TextView)convertView.findViewById(R.id.llt_txt_malotrinh);
        txtMaLoTrinh.setText(_maLoTrinh[position]);
//
//        TextView txtTongDanhBo = (TextView)convertView.findViewById(R.id.llt_txt_tongDanhBo);
//        txtTongDanhBo.setText(_tongDanhBo[position]);
        return convertView;
    }
}
