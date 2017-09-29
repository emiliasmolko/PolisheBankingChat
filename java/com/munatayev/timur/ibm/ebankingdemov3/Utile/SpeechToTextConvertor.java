package com.munatayev.timur.ibm.ebankingdemov3.Utile;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.munatayev.timur.ibm.ebankingdemov3.Cloud.Profanity;
import com.munatayev.timur.ibm.ebankingdemov3.Cloud.Translator;
import com.munatayev.timur.ibm.ebankingdemov3.R;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.ConversionCallaback;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.IConvertor;
import com.munatayev.timur.ibm.ebankingdemov3.ViewPack.SurfaceBackgroundView;

import java.util.ArrayList;

public class SpeechToTextConvertor implements IConvertor {

    private ConversionCallaback conversionCallaback;
    private Context mContext;
    private static boolean stopBoolean = true;

    public SpeechToTextConvertor(ConversionCallaback conversioCallaBack, Context mContext) {
        this.conversionCallaback = conversioCallaBack;
        this.mContext = mContext;
    }

    @Override
    public SpeechToTextConvertor initialize() {

        muteAudio();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "pl");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                mContext.getPackageName());

        CustomRecognitionListener listener = new CustomRecognitionListener();
        SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(mContext);
        sr.setRecognitionListener(listener);
        sr.startListening(intent);

        return this;
    }

    public void muteAudio() {
        AudioManager mAlramMAnager = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void unMuteAudio() {
        AudioManager mAlramMAnager = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }


    class CustomRecognitionListener implements RecognitionListener {
        private static final String TAG = "RecognitionListener";

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            if(stopBoolean != false) {
                sendBrodcastToScreen("#FF65B3F9");
            }
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {}

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            if(stopBoolean != false) {
                sendBrodcastToScreen("#ffffff");
            }
            Log.d(TAG, "onEndofSpeech");
        }

        public void onError(int error) {
           // Log.d("ERROR",error+" happened");
            initialize();
        }

        public void onResults(Bundle results) {
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String toService;
            String[] arrayToMain = new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                arrayToMain[i] = data.get(i).toString();
            }
            toService = data.get(0) + "";

            if(Profanity.filterText(new Translator().translateMessage(toService))){
                conversionCallaback.onSuccess(null);
            }else {
                conversionCallaback.toScreen(arrayToMain);
                conversionCallaback.onSuccess(toService);
            }
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
    public void sendBrodcastToScreen(String color){
        Intent i = new Intent("test");
        i.putExtra("circleState", color);
        mContext.sendBroadcast(i);
    }

    public static void changeState(boolean b){
        stopBoolean = b;
    }
} 