package com.example.delbert.daya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class upload extends AppCompatActivity {
    Intent from;
    boolean isSuccess = false;
    String key, context;
    double latitude, longitude;
    Location location;
    LocationManager locManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        from = getIntent();

        key = from.getStringExtra("key");
        context = from.getStringExtra("context");
        Log.d("xxx", "OnCreate upload function");

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "네트워크 설정을 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        location = locManager.getLastKnownLocation(locManager.NETWORK_PROVIDER);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if(location==null){
            latitude = 35.2321;
            longitude = 129.085;
        }else{
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("xxx",latitude+"");
            Log.d("xxx",longitude+"");
        }

        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 0, locationListener);

        new SendPost().execute();

    }

    private class SendPost extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            Log.d("xxx", "doInBackground upload function");
            return content;
        }

        protected void onPostExecute(String result) {
            Intent toMain = getIntent();
            if (isSuccess) {
                setResult(4, toMain);
            } else {
                setResult(5, toMain);
            }
            finish();
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxx", "executeClient upload function");
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();

            post.add(new BasicNameValuePair("key",  key + ""));
            post.add(new BasicNameValuePair("context", context + ""));

            post.add(new BasicNameValuePair("latitude", latitude + ""));
            post.add(new BasicNameValuePair("longitude", longitude + ""));

            // Post객체 생성
            HttpPost httpPost;
            if (StartActivity.isLogin) {
                // 연결 HttpClient 객체 생성
                HttpClient client = StartActivity.client;
                List<Cookie> cookies = ((DefaultHttpClient)client).getCookieStore().getCookies();
                if(!cookies.isEmpty()){
                    CookieManager cookieManager = CookieManager.getInstance();
                    CookieSyncManager.createInstance(getApplicationContext());
                    cookieManager.setAcceptCookie(true);
                    for (int i = 0; i < cookies.size(); i++) {
                        String cookiesString = cookies.get(i).getName() + "," + cookies.get(i).getValue()
                                + "," + cookies.get(i).getDomain() + "," + cookies.get(i).getPath();
                        Log.d("xxxupload", cookiesString);
                    }
                }

                // 객체 연결 설정 부분, 연결 최대시간 등등
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params,5000);
                HttpConnectionParams.setSoTimeout(params,5000);
                // Post객체 생성
                Log.i("xxx","isLogin");
                httpPost = new HttpPost("http://52.78.45.238/insert_Login.php");
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
            } else {
                // 연결 HttpClient 객체 생성
                HttpClient client = new DefaultHttpClient();
                // 객체 연결 설정 부분, 연결 최대시간 등등
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params,5000);
                HttpConnectionParams.setSoTimeout(params,5000);
                // Post객체 생성
                Log.i("xxx","NoLogin");
                httpPost = new HttpPost("http://52.78.45.238/insert_noLogin.php");
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
            }
            return null;
        }
    }

}