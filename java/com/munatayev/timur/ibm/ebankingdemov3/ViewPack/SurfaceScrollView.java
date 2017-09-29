package com.munatayev.timur.ibm.ebankingdemov3.ViewPack;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.munatayev.timur.ibm.ebankingdemov3.MainActivity;
import com.munatayev.timur.ibm.ebankingdemov3.R;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.CustomTouchListener;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.IVariantController;

public class SurfaceScrollView extends ScrollView{

    private int ScreenWidth = 1080;
    private int ScreenHeight = 1920;
    private LinearLayout layout;
    private boolean expand = false;

    public SurfaceScrollView(Context context) {
        super(context);
        init(context);
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public SurfaceScrollView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    public SurfaceScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        init(context);
    }

    public void init(Context context){

        //calling display metrics to calculate width and height of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        //getting true width and heigh of the screen
        ScreenHeight = metrics.heightPixels;
        ScreenWidth = metrics.widthPixels;

        this.setPadding(10, ScreenHeight / 20 * 3, 0, 0);
        this.setBackgroundColor(getResources().getColor(R.color.transparent));

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        this.addView(layout);
    }

    public void addText(Context context, String text, IVariantController variantController){
            TextView tv = new TextView(context);
            tv.setText(text);
            tv.setTextSize(35);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setBackgroundResource(R.drawable.backtext);
            tv.setOnTouchListener(new CustomTouchListener(variantController, layout));
            FrameLayout fl = new FrameLayout(context);
            fl.setPadding(30,10,10,30);
            fl.addView(tv);
            layout.addView(fl);
    }

    public void exapnd(boolean expand){
        this.expand = expand;
        this.invalidate();
    }
}
