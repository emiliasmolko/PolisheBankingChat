package com.munatayev.timur.ibm.ebankingdemov3.ViewPack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

public class SurfaceBackgroundView  extends ViewGroup{

    Paint whiteColorLogo = new Paint();
    Paint whiteThinColor = new Paint();
    Paint whiteCircleRecognizer = new Paint();
    private int ScreenWidth = 1080;
    private int ScreenHeight = 1920;
    private String color = "#ffffff";
    boolean trick = false;


    public SurfaceBackgroundView(Context context) {
        super(context);
        init(context);
    }

    public SurfaceBackgroundView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    public SurfaceBackgroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void init(Context context){

        //calling display metrics to calculate width and height of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        //getting true width and heigh of the screen
        ScreenHeight = metrics.heightPixels;
        ScreenWidth = metrics.widthPixels;

        whiteColorLogo.setColor(Color.parseColor(color));
        whiteColorLogo.setAntiAlias(true);
        whiteColorLogo.setStyle(Paint.Style.STROKE);
        whiteColorLogo.setStrokeWidth(8);

        whiteCircleRecognizer.setColor(Color.parseColor(color));
        whiteCircleRecognizer .setAntiAlias(true);
        whiteCircleRecognizer.setStyle(Paint.Style.STROKE);
        whiteCircleRecognizer.setStrokeWidth(8);

        whiteThinColor.setAntiAlias(true);
        whiteThinColor.setColor(Color.parseColor(color));
        whiteThinColor.setStyle(Paint.Style.STROKE);

        //setting black background
        this.setBackgroundColor(Color.BLACK);
    }

    public void changeOpacity(boolean active){
        if(active) {
            color = "#2AFFFFFF";
        }else{
            color = "#ffffff";
        }
        whiteColorLogo.setColor(Color.parseColor(color));
        whiteCircleRecognizer.setColor(Color.parseColor(color));
        whiteThinColor.setColor(Color.parseColor(color));
        this.invalidate();
    }

    public void setCircleColor(String hex){
        whiteCircleRecognizer.setColor(Color.parseColor(hex));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw Watson eBank logo
        canvas.drawCircle(ScreenWidth / 2, ScreenHeight / 2 - ScreenHeight / 20 * 2, ScreenWidth / 2 - ScreenWidth / 10, whiteCircleRecognizer);
        canvas.drawLine(ScreenWidth/2+2, ScreenHeight/2 - ScreenHeight/20*4, ScreenWidth/2 - ScreenWidth/10 *2, ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2-2, ScreenHeight/2 - ScreenHeight/20*4, ScreenWidth/2 + ScreenWidth/10*2, ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2 - ScreenWidth/10 *2-2, ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40,ScreenWidth/2 + ScreenWidth/10*2+2, ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2-ScreenWidth/10 -ScreenWidth/20, ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40,ScreenWidth/2-ScreenWidth/10 -ScreenWidth/20, ScreenHeight/2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2 -ScreenWidth/20,ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40,ScreenWidth/2 -ScreenWidth/20,ScreenHeight/2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2 +ScreenWidth/20,ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40,ScreenWidth/2 +ScreenWidth/20,ScreenHeight/2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2+ScreenWidth/10 +ScreenWidth/20, ScreenHeight/2 - ScreenHeight/20*2 - ScreenHeight/40,ScreenWidth/2+ScreenWidth/10 +ScreenWidth/20, ScreenHeight/2 - ScreenHeight/40, whiteColorLogo);
        canvas.drawLine(ScreenWidth/2 - ScreenWidth/10*2, ScreenHeight/2 - ScreenHeight/40, ScreenWidth/2 + ScreenWidth/10*2, ScreenHeight/2 - ScreenHeight/40, whiteColorLogo);

        canvas.drawCircle(ScreenWidth/2+ScreenWidth/20, ScreenHeight/2 - ScreenHeight/18*2, ScreenWidth / 3.2f, whiteThinColor);
        canvas.drawCircle(ScreenWidth/2-ScreenWidth/20, ScreenHeight/2 - ScreenHeight/22*2, ScreenWidth / 3.2f, whiteThinColor);
        canvas.drawCircle(ScreenWidth/2+ScreenWidth/20, ScreenHeight/2 - ScreenHeight/22*2, ScreenWidth / 3.2f, whiteThinColor);
        canvas.drawCircle(ScreenWidth/2-ScreenWidth/20, ScreenHeight/2 - ScreenHeight/18*2, ScreenWidth / 3.2f, whiteThinColor);
        canvas.drawCircle(ScreenWidth/2, ScreenHeight/2 - ScreenHeight/20*2, ScreenWidth / 3.2f, whiteThinColor);

    }
}
