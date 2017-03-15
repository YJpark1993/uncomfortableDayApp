package com.example.delbert.daya;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;

import java.util.ArrayList;

/**
 * Created by Delbert on 2016-08-02.
 */
public class sttBuilder extends Activity {
    String ApiKey = "";
    String identifier = "";
    Intent from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sttbuilder);
        Log.d("xxx", "onCreate sttBuilder");

        from = getIntent();
        ApiKey = from.getStringExtra("ApiKey");
        identifier = from.getStringExtra("identifier");


        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder()
                .setApiKey(ApiKey)
                .setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION)
                .setGlobalTimeOut(50)
                .setUserDictionary("방지턱\n" +
                        "교차로\n" +
                        "간판\n" +
                        "가로등\n" +
                        "펜스\n" +
                        "자전거도로\n" +
                        "도로\n" +
                        "낙석\n" +
                        "신호등\n" +
                        "경찰\n" +
                        "사람\n" +
                        "복잡\n" +
                        "배치\n" +
                        "너무\n" +
                        "보수\n" +
                        "해주세요");

        SpeechRecognizerClient client = builder.build();
        client.setSpeechRecognizeListener(new SpeechRecognizeListener() {
            @Override
            public void onReady() {
                Log.d("yyy", "onReady sttBuilder function");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("yyy", "onBeginningOfSpeech sttBuilder function");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("yyy", "onEndOfSpeech sttBuilder function");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("yyy", "onError sttBuilder function" + s);
                setResult(5);
                finish();
            }

            @Override
            public void onPartialResult(String s) {
                Log.d("yyy", "onPartialResult sttBuilder function" + s);
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> texts = bundle.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
                ArrayList<Integer> confs = bundle.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);
            Log.d("xxx", "onResults sttBuilder function" + texts);
            Log.d("xxx", "onResults sttBuilder function" + confs);

            String[] result = new String[texts.size()];
            String out = "";
            texts.toArray(result);
            out = out + result[0];
            Log.d("yyy", out);
            TastyToast.makeText(getApplicationContext(), "불편사항: " + out, TastyToast.LENGTH_SHORT, TastyToast.INFO);

            from.putExtra("context", out);
            setResult(3, from);
            finish();
        }

            @Override
            public void onAudioLevel(float v) {
            }

            @Override
            public void onFinished() {
                Log.d("xxx", "onFinished sttBuilder function");
                finish();
            }
        });

        client.startRecording(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xxx", "onStart sttBuilder");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xxx", "onStop sttBuilder");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xxx", "onPause sttBuilder");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xxx", "onResume sttBuilder");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("xxx", "onDestroy sttBuilder");
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }
}
