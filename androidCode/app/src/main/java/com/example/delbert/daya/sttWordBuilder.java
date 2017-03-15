package com.example.delbert.daya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.sdsmdg.tastytoast.TastyToast;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;

import java.util.ArrayList;

/**
 * Created by Delbert on 2016-08-03.
 */
public class sttWordBuilder extends Activity {
    String ApiKey = "";
    String identifier = "";
    Intent from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sttbuilder);
        Log.d("xxx", "onCreate sttWordBuilder");

        from = getIntent();
        this.ApiKey = from.getStringExtra("ApiKey");
        identifier = from.getStringExtra("identifier");

        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder()
                .setApiKey(ApiKey)
                .setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD)
                .setGlobalTimeOut(20)
                .setUserDictionary("방지턱\n교차로\n간판\n표지판\n가로등\n펜스\n자전거도로\n도로\n낙석\n신호등");
        SpeechRecognizerClient client = builder.build();
        client.setSpeechRecognizeListener(new SpeechRecognizeListener() {

            @Override
            public void onReady() {
                Log.d("xxx", "onReady sttWordBuilder function");
            }
            @Override
            public void onBeginningOfSpeech() {
                Log.d("xxx", "onReady sttWordBuilder function");
            }
            @Override
            public void onEndOfSpeech() {
                Log.d("xxx", "onReady sttWordBuilder function");
            }
            @Override
            public void onError(int i, String s) {
                setResult(5);
                finish();
                Log.d("xxx", "onError sttWordBuilder function");
            }
            @Override
            public void onPartialResult(String s) {
                Log.d("xxx", "onPartialResult sttWordBuilder function");
            }
            @Override
            public void onResults(Bundle bundle) {
                Log.d("xxx", "onResults sttWordBuilder function");
                ArrayList<String> texts = bundle.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
                ArrayList<Integer> confs = bundle.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);
                Log.d("xxx", "onResults sttBuilder function" + texts);
                Log.d("xxx", "onResults sttBuilder function" + confs);

                String[] result = new String[texts.size()];
                String out = "";
                texts.toArray(result);
                out = out + result[0];

                TastyToast.makeText(getApplicationContext(), "키워드: " + out, TastyToast.LENGTH_LONG, TastyToast.INFO);

                from.putExtra("identifier","inconvenience");
                from.putExtra("key",out);
                setResult(2, from);
                finish();
            }

            @Override
            public void onAudioLevel(float v) {
            }
            @Override
            public void onFinished() {
                Log.d("xxx", "onFinished sttWordBuilder function");
            }
        });
        client.startRecording(false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xxx", "onStart sttWordBuilder");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xxx", "onResume sttWordBuilder");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xxx", "onPause sttWordBuilder");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xxx", "onStop sttWordBuilder");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("xxx", "onDestroy sttWordBuilder");
    }
}
