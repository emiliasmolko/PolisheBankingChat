package com.munatayev.timur.ibm.ebankingdemov3.ViewPack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class SurfaceTitleView extends View {

    Paint greenColor = new Paint();
    Paint blueColor = new Paint();
    Paint whiteColor = new Paint();
    private int ScreenWidth = 1080;
    private int ScreenHeight = 1920;


    public SurfaceTitleView(Context context) {
        super(context);
        init(context);
    }

    public SurfaceTitleView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    public SurfaceTitleView(Context context, AttributeSet attrs, int defStyle) {
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

        greenColor.setColor(Color.rgb(155, 210, 121));//green
        greenColor.setAntiAlias(true);
        blueColor.setColor(Color.rgb(101, 179, 249));//blue
        blueColor.setAntiAlias(true);

        //seting font to the paint to get clear text
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font.ttf");
        whiteColor.setTextAlign(Paint.Align.CENTER);
        whiteColor.setTypeface(typeface);
        whiteColor.setColor(Color.WHITE);
        whiteColor.setStrokeWidth(2);
        whiteColor.setTextSize(20 * getResources().getDisplayMetrics().density);
        whiteColor.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw top green line(startX,startY,endX,endY,color)
        canvas.drawLine(0, ScreenHeight / 20, ScreenWidth / 10 * 2, ScreenHeight / 20, greenColor);
        canvas.drawLine(ScreenWidth / 10 * 2, ScreenHeight / 20, ScreenWidth / 10 * 3,ScreenHeight / 40, greenColor);
        canvas.drawLine(ScreenWidth / 10 * 3, ScreenHeight / 40, ScreenWidth / 10 * 8, ScreenHeight / 40, greenColor);
        canvas.drawLine(ScreenWidth / 10 * 8, ScreenHeight / 40, ScreenWidth / 10 * 9, ScreenHeight / 20, greenColor);
        canvas.drawLine(ScreenWidth / 10 * 9, ScreenHeight / 20, ScreenWidth, ScreenHeight / 20, greenColor);

        //draw text on the top
        canvas.drawText("Twój Bank", ScreenWidth / 10 * 5, ScreenHeight / 20 * 1.5f, whiteColor);

        //draw top blue line
        canvas.drawLine(0, ScreenHeight / 20 * 3 - ScreenHeight / 40, ScreenWidth / 10, ScreenHeight / 20 * 3 - ScreenHeight / 40, blueColor);
        canvas.drawLine(ScreenWidth / 10, ScreenHeight / 20 * 3 - ScreenHeight / 40, ScreenWidth / 10 * 2, ScreenHeight / 20 * 2, blueColor);
        canvas.drawLine(ScreenWidth / 10 * 2, ScreenHeight / 20 * 2, ScreenWidth / 10 * 7, ScreenHeight / 20 * 2, blueColor);
        canvas.drawLine(ScreenWidth / 10 * 7, ScreenHeight / 20 * 2, ScreenWidth / 10 * 8, ScreenHeight / 20 * 3 - ScreenHeight / 40, blueColor);
        canvas.drawLine(ScreenWidth / 10 * 8, ScreenHeight / 20 * 3 - ScreenHeight / 40, ScreenWidth, ScreenHeight / 20 * 3 - ScreenHeight / 40, blueColor);

    }
}
