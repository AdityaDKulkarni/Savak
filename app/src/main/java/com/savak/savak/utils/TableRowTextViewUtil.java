package com.savak.savak.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.savak.savak.R;

/**
 * @author Aditya Kulkarni
 */

public class TableRowTextViewUtil {

    public TextView config(Context context, int color, String text){
        TextView textView = new TextView(context);
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            Log.e("Reso", "Large");
            textView.setTextSize(context.getResources().getDimension(R.dimen.s14sp));
        }else{
            Log.e("Reso", "normal");
            textView.setTextSize(context.getResources().getDimension(R.dimen.s6sp));
        }
        textView.setTextColor(context.getResources().getColor(color));
        textView.setText(text);

        return textView;
    }
}
