package com.munatayev.timur.ibm.ebankingdemov3.Cloud;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.munatayev.timur.ibm.ebankingdemov3.Utile.ConversationMessage;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.Filter;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.ConversionCallaback;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.IEndOfSpeach;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.SpeechToTextConvertor;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.TextToSpeach;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SpeachService extends Service implements ConversionCallaback, IEndOfSpeach {

    private final IBinder mBinder = new ServiceBinder();
    private static final String TAG = "SpeachService";
    private TextToSpeach textToSpeach;
    private SpeechToTextConvertor speechToTextConvertor;

    ConversationMessage message = null;
    Map<String, Object> history = new HashMap<>();
    String command = "no";

    @Override
    public void onSuccess(String result2) {
        //it is here
        if(result2.equals("podaj kwotę") || result2.equals("Dziękuję twoja transakcja zostanie zrealizowana w najbliższej przyszłości")) {
        }else{
            String result;
            if (command.equals("adding comments")) {
                result = result2;
            } else {
                result = new Translator().translateMessage(result2);
                String after_filter = Filter.groszFilter(result);
                result = after_filter;
            }
            if (result.equals("close ")) {
                onDestroy();
                System.exit(0);
            }
            Log.d("COME FROM STT", result);
            try {
                message = new PostTask().execute(new ConversationMessage(result, history, command)).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            speechToTextConvertor.unMuteAudio();

            if (message != null) {
                if (message.getCommand().equals("person not found")) {
                    Intent i = new Intent("test");
                    SpeechToTextConvertor.changeState(false);
                    String[] nv = {"choose person", "Jan Kowalski", "Timur Munatayev", "Emilia Sokolowska", "Black Jack"};
                    i.putExtra("toScreen", nv);
                    sendBroadcast(i);
                    speak(message.getMessage());
                } else if (message.getCommand().equals("complete transaction")) {
                    SpeechToTextConvertor.changeState(false);
                    Intent i = new Intent("test");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String[] toSend = {"complete transaction", dateFormat.format(new Date()), message.getMessage()};
                    i.putExtra("toScreen", toSend);
                    sendBroadcast(i);
                    String[] date = message.getMessage().split(" ");
                    try {
                        String first = "";
                        String second = "";
                        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date given_date = dateFormat2.parse(date[10]);
                        DateFormat dateFormat3 = new SimpleDateFormat("dd-MM-yyyy");
                        for (int ii = 0; ii < 10; ii++) first += date[ii] + " ";
                        for (int l = 12; l < date.length; l++) second += date[l] + " ";
                        speak("Proszę potwierdzić następującą transakcję. " + first + dateFormat3.format(given_date) + second);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (message.getCommand().equals("finish")) {
                    Intent i = new Intent("test");
                    String[] to_send = null;
                    i.putExtra("toScreen", to_send);
                    sendBroadcast(i);
                    speak(message.getMessage());
                } else if (message.getMessage().equals("Podaj kwotę.")) {
                    Intent i = new Intent("test");
                    String[] to_send = null;
                    i.putExtra("toScreen", to_send);
                    sendBroadcast(i);
                    speak(message.getMessage());
                    speechToTextConvertor.initialize();
                } else {
                    speak(message.getMessage());
                }
                Log.d(TAG, message.toString());
                history = message.getContext();
                command = message.getCommand();
            }
        }
    }

    @Override
    public void toScreen(String[] array) {
    }

    private BroadcastReceiver onBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            String accept = intent.getStringExtra("accept");
            if (accept != null) {
                    onSuccess(accept);
            }
        }
    };

    //end of text - to - speach
    @Override
    public void onEndOfSpeach() {
            speechToTextConvertor.initialize();
    }

    public class ServiceBinder extends Binder {
        public SpeachService getService() {
            return SpeachService.this;
        }
    }

    @Override
    public IBinder onBind(Intent context) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Profanity.loadConfigs();

        registerReceiver(onBroadcast, new IntentFilter("test"));

        speechToTextConvertor = new SpeechToTextConvertor(SpeachService.this, this.getApplicationContext());
        textToSpeach = new TextToSpeach(SpeachService.this, this.getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeach.onDestroy();
        unregisterReceiver(onBroadcast);
    }

    public void start() {
        speechToTextConvertor.initialize();
    }

    public void speak(String result) {
        Log.d(TAG, "Result : " + result);
        textToSpeach.speakOut(result);
    }
}