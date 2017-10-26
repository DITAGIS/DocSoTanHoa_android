package com.ditagis.hcm.docsotanhoa.utities;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ThanLe on 26/10/2017.
 */

public class SnackBar {
    public static void make(View view, String text, boolean isLong) {
        int time = isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT;
        Snackbar.make(view, text, time)
                .setAction("Action", null).show();
    }

}
