package com.munatayev.timur.ibm.ebankingdemov3.Utile;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.munatayev.timur.ibm.ebankingdemov3.Utile.interfaces.IEndOfSpeach;

import java.util.Locale;

public class TextToSpeach implements TextToSpeech.OnInitListener{
    private TextToSpeech tts;
    private IEndOfSpeach iEndOfSpeach;
    private Context mContext;

    public TextToSpeach(IEndOfSpeach iEndOfSpeach, Context context){
        this.iEndOfSpeach = iEndOfSpeach;
        this.mContext = context;
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("pl_PL"));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("TTS!!!!!!!!!!!!!","Language not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void speakOut(String text) {
        if(tts != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }catch(Exception e){
                Log.e("TTS",e.toString());
            }finally {
                boolean test = true;
                while(test) {
                    if(!tts.isSpeaking()) {
                        iEndOfSpeach.onEndOfSpeach();
                        test = false;
                    }
                }
            }
        } else {
            Log.e("TTS", "TextToSpeech Null");
        }
    }

    public void onDestroy(){
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
