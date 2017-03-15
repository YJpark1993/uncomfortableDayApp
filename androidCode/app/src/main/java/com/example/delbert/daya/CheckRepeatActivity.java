package com.example.delbert.daya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CheckRepeatActivity extends AppCompatActivity {

    Intent from;
    boolean isSuccess = false;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        from = getIntent();
        email = from.getStringExtra("email");
        new ReceivePost().execute();
    }

    private class ReceivePost extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            Log.d("xxx", "doInBackground upload function");
            return content;
        }

        protected void onPostExecute(String result) {
            Log.d("xxx", "onPostExecute upload function");
            Log.d("xxx", "result =" + result);

            if (isRepeat(result)) {
                Log.d("xxx", "return value:" + 1);
                setResult(1, from);
            } else {
                Log.d("xxx", "return value:" + 100);
                setResult(100, from);
            }

            finish();
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxx", "executeClient upload function");

            try {
                // 연결 HttpClient 객체 생성
                HttpClient client = new DefaultHttpClient();
                ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
                post.add(new BasicNameValuePair("email", email));

                // 객체 연결 설정 부분, 연결 최대시간 등등
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000);
                HttpConnectionParams.setSoTimeout(params, 5000);

                // Post객체 생성
                HttpPost httpPost = new HttpPost("http://52.78.45.238/login_checkRepeat.php");
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                client.execute(httpPost);
//              ---------------------------- 웹 upload //   아래 부터 웹 download-----------------

                HttpResponse res = client.execute(httpPost);
                HttpEntity entityResponse = res.getEntity();
                // 값 읽어 오기
                String tmp = EntityUtils.toString(entityResponse);

                return tmp;

            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }
    }
    private boolean isRepeat(String value) {
        return value.equals("repeat");
    }

    @Override
    protected void onDestroy() {
        Log.d("xxx", "onDestroy RSActivity");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d("xxx", "onStop RSActivity");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d("xxx", "onResume RSActivity");
        super.onResume();
    }

}
