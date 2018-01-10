package com.ditagis.hcm.docsotanhoa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ditagis.hcm.docsotanhoa.R;
import com.ditagis.hcm.docsotanhoa.entities.Code_Describle;

/**
 * Created by ThanLe on 12/6/2017.
 */

public class CodeSpinnerAdapter extends ArrayAdapter<Code_Describle> {
    private Code_Describle[] codeDescribles;
    private int layoutResourceId;
    private Context context;

    public CodeSpinnerAdapter(Context context, int textViewResourceId,
                              Code_Describle[] codeDescribles) {
        super(context, textViewResourceId, codeDescribles);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.codeDescribles = codeDescribles;
    }

    public int getCount() {
        return codeDescribles.length;
    }

    public Code_Describle getItem(int position) {
        return codeDescribles[position];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spView = inflater.inflate(R.layout.spincode_dropdown, parent, false);

        TextView row_code = (TextView) spView.findViewById(R.id.row_code);
        row_code.setText(codeDescribles[position].getCode());

        TextView row_describle = (TextView) spView.findViewById(R.id.row_describle);
        row_describle.setText(codeDescribles[position].getDescribe());

        return spView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dropDownView = inflater.inflate(layoutResourceId, parent, false);

        TextView row_code = (TextView) dropDownView.findViewById(R.id.row_code);
        row_code.setText(codeDescribles[position].getCode());

        TextView row_describle = (TextView) dropDownView.findViewById(R.id.row_describle);
        row_describle.setText(codeDescribles[position].getDescribe());

        return dropDownView;

    }

}
