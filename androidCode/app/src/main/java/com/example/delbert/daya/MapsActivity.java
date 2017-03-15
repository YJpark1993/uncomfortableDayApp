package com.example.delbert.daya;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    double latitude, longitude;
    JSONObject jo,joRoad;
    JSONArray ja,jaRoad;
    double[][] table, tableRoad;
    String[] tableKeyword, tableKeywordRoad, tableTimeRoad;
    LatLng myPosition;
    LocationManager locManager; // 위치 정보 프로바이더
    LocationListener locationListener; // 위치 정보가 업데이트시 동작
    Location location;
    double newCoordinate[][];
    String newKeyWord[];
    int newCount[];
    ImageButton ib;
    boolean flag = false;
    GoogleMap ReturnMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.d("xxx", "onCreate MapsActivity");
        new ReceivePost().execute();
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
        if (location == null) {
            latitude = 35.2321;
            longitude = 129.085;
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 0, locationListener);
        onStop();
    }

    @Override
    protected void onResume() {
        Log.d("xxx", "onResume MapsActivity");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("xxx", "onPause MapsActivity");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locManager.removeUpdates(locationListener);
        Log.d("xxx", "onDestroy MapsActivity");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d("xxx", "onStart MapsActivity");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("xxx", "onStop MapsActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("xxx", "onRestart MapsActivity");
        super.onRestart();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapsActivity.this);

//      불편상황 초기화
        CheckScopeClass creater = new CheckScopeClass();
        final SelectIcon selector = new SelectIcon();
        creater.CheckScopeFunction(tableKeyword, table);
        newCoordinate = creater.ReturnCoordinate();
        newKeyWord = creater.ReturnKeyWord();
        newCount = creater.ReturnCount();
//      도로상황 초기화
        final SelectIconRoad selectorRoad = new SelectIconRoad();



        ib = (ImageButton) findViewById(R.id.mapChange);
        if (table.length != 0) {
            ReturnMap = selector.ConnectIcon(map, newCoordinate, newKeyWord, newCount);
            myPosition = new LatLng(latitude, longitude);

            ReturnMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 16));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "GPS를 켜 주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            ReturnMap.setMyLocationEnabled(true);

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == false) {
                        flag = true;
                        TastyToast.makeText(getApplicationContext(), "도로상황지도", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        ReturnMap.clear();
                        ReturnMap = selectorRoad.ConnectRoadIcon(map, tableRoad, tableKeywordRoad,tableTimeRoad);
                        myPosition = new LatLng(latitude, longitude);
                        ReturnMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 16));


                    } else {
                        flag = false;
                        TastyToast.makeText(getApplicationContext(), "불편상황지도", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        ReturnMap.clear();
                        if (table.length != 0) {
                            ReturnMap = selector.ConnectIcon(map, newCoordinate, newKeyWord, newCount);
                            myPosition = new LatLng(latitude, longitude);
                            ReturnMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 16));
                        }

                    }
                }
            });
        }
    }


    private class ReceivePost extends AsyncTask<Void, Void, String> {
            protected String doInBackground(Void... unused) {
                String content = executeClient();
                Log.d("xxx", "doInBackground function");
                return content;
            }
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
            protected void onPostExecute(String result) {
                Log.d("xxx", "onPostExecute function");
                Log.d("xxx", "result =" + result);
                try {
//                 불편상황
                    jo = new JSONObject(result);
                    ja = jo.getJSONArray("output");
                    table = new double[ja.length()][2];
                    tableKeyword = new String[ja.length()];
                    for(int i =0; i<ja.length();i++){
                        JSONObject c = ja.getJSONObject(i);
                        String latitude2 = c.getString("latitude");
                        String longitude2 = c.getString("longitude");
                        String contextKey = c.getString("context_key");
                        double lati = Double.parseDouble(latitude2);
                        double longi = Double.parseDouble(longitude2);
                        table[i][0] = lati;
                        table[i][1] = longi;
                        tableKeyword[i] = contextKey;
                    }
//                  도로상황
                    joRoad = new JSONObject(result);
                    jaRoad = joRoad.getJSONArray("outputRoad");
                    tableRoad = new double[jaRoad.length()][2];
                    tableKeywordRoad = new String[jaRoad.length()];
                    tableTimeRoad = new String[jaRoad.length()];
                    for(int i =0; i<jaRoad.length();i++){
                        JSONObject cRoad = jaRoad.getJSONObject(i);
                        String latitudeRoad = cRoad.getString("latitude");
                        String longitudeRoad = cRoad.getString("longitude");
                        String contextKeyRoad = cRoad.getString("context_key_road");
                        String curTimeRoad = cRoad.getString("curtime");
                        double latiRoad = Double.parseDouble(latitudeRoad);
                        double longiRoad = Double.parseDouble(longitudeRoad);
                        tableRoad[i][0] = latiRoad;
                        tableRoad[i][1] = longiRoad;
                        tableKeywordRoad[i] = contextKeyRoad;
                        tableTimeRoad[i] = curTimeRoad;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onRestart();
            }
            // 실제 전송하는 부분
            public String executeClient() {
                Log.d("xxx", "executeClient function");
                try {
                    // 연결 HttpClient 객체 생성
                    HttpClient client = new DefaultHttpClient();
                    ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();

                    // 객체 연결 설정 부분, 연결 최대시간 등등
                    HttpParams params = client.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    // Post객체 생성

                    HttpPost httpPost = new HttpPost("http://52.78.45.238/getCoordinate.php");

                    UrlEncodedFormEntity entity = null;

                    entity = new UrlEncodedFormEntity(post, "UTF-8");

                    httpPost.setEntity(entity);
                    client.execute(httpPost);

                    HttpResponse res = null;
                    res = client.execute(httpPost);

                    HttpEntity entityResponse = res.getEntity();
                    // 값 읽어 오기
                    String tmp = null;

                    tmp = EntityUtils.toString(entityResponse);
                    return tmp;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
    }
}

