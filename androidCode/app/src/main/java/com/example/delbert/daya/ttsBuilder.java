package com.example.delbert.daya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import net.daum.mf.speech.api.TextToSpeechClient;
import net.daum.mf.speech.api.TextToSpeechListener;
import net.daum.mf.speech.api.TextToSpeechManager;

/**
 * Created by Delbert on 2016-08-01.
 */
public class ttsBuilder extends Activity implements TextToSpeechListener {
    TextToSpeechClient ttsClient;

    String ApiKey = "";
    String identifier,key;

    String keyword = "키워드를 말씀해주세요";
    String inconvenience = "불편사항을 말씀해주세요";

    Intent from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ttsbuilder);
        Log.d("xxx", "onCreate ttsBuilder");

        from = getIntent();
        ApiKey = from.getStringExtra("ApiKey");
        identifier = from.getStringExtra("identifier");
        if(from.getStringExtra("key") != null)
            key = from.getStringExtra("key");

        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());
        ttsClient = new TextToSpeechClient.Builder()
                .setApiKey(ApiKey)
                .setSpeechMode(TextToSpeechClient.NEWTONE_TALK_2)
                .setSpeechSpeed(1.0)
                .setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT)
                .setListener(this)
                .build();

        if (identifier.equalsIgnoreCase("keyword")) {
            ttsClient.setSpeechText(keyword);
            ttsClient.play();
        } else if (identifier.equalsIgnoreCase("inconvenience")) {
            ttsClient.setSpeechText(inconvenience);
            ttsClient.play();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xxx", "onStart ttsBuilder");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xxx", "onStop ttsBuilder");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xxx", "onPause ttsBuilder");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xxx", "onResume ttsBuilder");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //라이브러리 해제
        TextToSpeechManager.getInstance().finalizeLibrary();
        Log.d("xxx", "onDestroy ttsBuilder");
    }


    //  ........TTS function keyword
    @Override
    public void onFinished() {
        Log.d("xxx", "onFinished ttsBuilder function");
//        int intSentSize = ttsClient.getSentDataSize(); // 전송한 데이터 사이즈
//        int intRecvSize = ttsClient.getReceivedDataSize(); // 전송받은 데이터 사이즈
//        final String strInacctiveText = "handleFinished() SentSize: " + intSentSize + "RecvSize: " + intRecvSize;

        from.putExtra("ApiKey", ApiKey);
        from.putExtra("identifier", identifier);
        setResult(1, from);
        finish();
    }

    @Override
    public void onError(int i, String s) {
        Log.d("xxx",i+"");
        Log.d("xxx",s);
        Log.d("xxx", "onError ttsBuilder function");
    }

//  ........TTS function end


}
