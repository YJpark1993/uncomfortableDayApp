package com.example.delbert.daya;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public class SendToServerUserInfoActivity extends AppCompatActivity {

    Intent from;
    boolean isSuccess = false;
    String email, pwd, name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        from = getIntent();
        email = from.getStringExtra("email");
        pwd = from.getStringExtra("pwd");
        name = from.getStringExtra("name");
        phone = from.getStringExtra("phone");
        new SendPost().execute();
    }

    private class SendPost extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            Log.d("xxx", "doInBackground upload function");
            return content;
        }

        protected void onPostExecute(String result) {
            Log.d("xxx", "onPostExecute upload function");
            Intent toMain = getIntent();
            if (isSuccess) {
                setResult(2, toMain);
            } else {
                setResult(102, toMain);
            }
            finish();
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxx", "executeClient upload function");
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            Log.d("xxx", "email:" + email + " pwd:" + pwd+ " name:" + name+ " phone:" + phone);
            post.add(new BasicNameValuePair("email", email));
            post.add(new BasicNameValuePair("pwd", pwd));
            post.add(new BasicNameValuePair("name", name));
            post.add(new BasicNameValuePair("phone", phone));


            // 연결 HttpClient 객체 생성
            HttpClient client = new DefaultHttpClient();

            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            // Post객체 생성
            HttpPost httpPost = new HttpPost("http://52.78.45.238/login.php");

            try {
                isSuccess = true;
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                client.execute(httpPost);
                return EntityUtils.getContentCharSet(entity);
            } catch (IOException e) {
                isSuccess = false;
                e.printStackTrace();
            }
            return null;
        }
    }
}
