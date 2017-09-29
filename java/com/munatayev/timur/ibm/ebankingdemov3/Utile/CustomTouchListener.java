package com.munatayev.timur.ibm.ebankingdemov3.Utile;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.IVariantController;

public class CustomTouchListener implements View.OnTouchListener {
    IVariantController variantController;
    LinearLayout linearLayout;

    public CustomTouchListener(IVariantController variantController, LinearLayout linearLayout){
        this.variantController = variantController;
        this.linearLayout = linearLayout;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                variantController.alloted(((TextView) view).getText().toString());
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    FrameLayout fl = (FrameLayout)linearLayout.getChildAt(i);
                    TextView tv = (TextView)fl.getChildAt(0);
                    tv.setTextColor(Color.WHITE);
                }
                ((TextView) view).setTextColor(Color.GREEN);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}