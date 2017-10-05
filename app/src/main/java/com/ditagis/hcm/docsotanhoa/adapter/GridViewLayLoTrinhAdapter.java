package com.ditagis.hcm.docsotanhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.R;

/**
 * Created by ThanLe on 04/10/2017.
 */

public class GridViewLayLoTrinhAdapter extends BaseAdapter {
    private Context context;
    private String[] _maLoTrinh;
    private int[] _tongDanhBo;
    private int[] _checked_position;

    public GridViewLayLoTrinhAdapter(Context context, String[] _maLoTrinh, int[] _tongDanhBo, int[] _checked_position) {
        this.context = context;
        this._maLoTrinh = _maLoTrinh;
        this._tongDanhBo = _tongDanhBo;
        this._checked_position = _checked_position;
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_lay_lo_trinh, null);
        }
        TextView txtMaLoTrinh = (TextView) convertView.findViewById(R.id.row_llt_txt_malotrinh);
        txtMaLoTrinh.setText(_maLoTrinh[position]);

        TextView txtTongDanhBo = (TextView) convertView.findViewById(R.id.row_llt_txt_tongDanhBo);
        txtTongDanhBo.setText(_tongDanhBo[position]==0?"Chưa xác định" : _tongDanhBo[position] + "");

        ImageView imgCheck = (ImageView) convertView.findViewById(R.id.row_llt_img_Check);
        if(_checked_position[position] == 1)
            imgCheck.setImageResource(R.drawable.checked);
        else
            imgCheck.setImageResource(0);
        return convertView;
    }
}
