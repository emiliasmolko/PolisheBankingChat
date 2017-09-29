package com.munatayev.timur.ibm.ebankingdemov3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.munatayev.timur.ibm.ebankingdemov3.Cloud.SpeachService;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.SpeechToTextConvertor;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.IVariantController;
import com.munatayev.timur.ibm.ebankingdemov3.ViewPack.SurfaceBackgroundView;
import com.munatayev.timur.ibm.ebankingdemov3.ViewPack.SurfaceListenIndicatorView;
import com.munatayev.timur.ibm.ebankingdemov3.ViewPack.SurfaceScrollView;

public class MainActivity extends AppCompatActivity implements IVariantController{

    private static final int MY_PERMISSIONS_REQUESTS = 11;
    private static final String TAG = "MainActivity";
    private Button cancel;
    private boolean btn_state = false;
    private String choosePerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate1");

        cancel = (Button)findViewById(R.id.btnCancel);
        cancel.setText("ANULUJ");
        cancel.setTypeface(Typeface.createFromAsset(getAssets(), "font.ttf"));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_state){
                    String toSend = choosePerson+" ";
                    Intent i = new Intent("test");
                    i.putExtra("accept", toSend);
                    sendBroadcast(i);
                } else {
                    onDestroy();
                    System.exit(0);
                }
            }
        });

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUESTS);
        }
        hideNavBar();
        registerReceiver(onBroadcast, new IntentFilter("test"));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSaveInstanceState(Bundle savedInstanceState) {
    }

    private BroadcastReceiver onBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            String[] messages = intent.getStringArrayExtra("toScreen");
            String color = intent.getStringExtra("circleState");

            SurfaceScrollView surfaceScrollView = (SurfaceScrollView) findViewById(R.id.surface_scroll_view);
            SurfaceBackgroundView surfaceBackgroundView = (SurfaceBackgroundView) findViewById(R.id.background);
            SurfaceListenIndicatorView surfaceListenIndicatorView = (SurfaceListenIndicatorView)findViewById(R.id.surfaceListenIndicatorView);

            if (messages != null) {
                    surfaceScrollView.getLayout().removeAllViews();
                    surfaceBackgroundView.changeOpacity(true);
                    surfaceScrollView.exapnd(true);
                    surfaceListenIndicatorView.alloted(true);
                    btn_state = true;
                    cancel.setText("KONTYNUJ");

                    for (String message : messages) {
                        if(message.equals("complete transaction")){
                            choosePerson = "acceptance";
                        }else {
                            if(!message.equals("choose person")) {
                                surfaceScrollView.addText(ctxt, message, MainActivity.this);
                                surfaceScrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        }
                        if (message.equals("close")) {
                            onDestroy();
                            System.exit(0);
                        }
                    }
            } else {
                SpeechToTextConvertor.changeState(true);
                surfaceScrollView.getLayout().removeAllViews();
                surfaceBackgroundView.changeOpacity(false);
                surfaceScrollView.exapnd(false);
                surfaceListenIndicatorView.alloted(false);
                btn_state = false;
                cancel.setText("ANULUJ");
            }

            if(color != null && messages == null){
                surfaceBackgroundView.setCircleColor(color);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Main Finished");
        Intent ser = new Intent(MainActivity.this, SpeachService.class);
        stopService(ser);
        unregisterReceiver(onBroadcast);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUESTS: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void hideNavBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void alloted(String text) {
        choosePerson = text;
    }
}
