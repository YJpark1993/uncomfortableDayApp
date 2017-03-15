package com.example.delbert.daya;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String ApiKey = "81b6544b06ecdedc046113f362adc984";
    String key, context, keyRoad;
    LinearLayout btRecord, btEmg, btRoad, btMap;
    TextView totalS, admitS, point;
    Context mContext;
    Intent callIntent = null;
    Intent to = null;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            GPS_noticeDialog gd = GPS_noticeDialog.newInstance();
            gd.show(getFragmentManager(), "tag");
        } else {
            if (StartActivity.isLogin) {
                ///네비게이션 바 생성
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        try {
                            new ReceiveNavInfo().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        super.onDrawerOpened(drawerView);
                    }
                };
                drawer.setDrawerListener(toggle);

                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                totalS = (TextView) findViewById(R.id.totalSub);
                admitS = (TextView) findViewById(R.id.admitSub);
                point = (TextView) findViewById(R.id.point);

            }
        }
        //메소드


        mContext = this;
        btRecord = (LinearLayout) findViewById(R.id.btnRecord);
        btEmg = (LinearLayout) findViewById(R.id.btn119);
        btRoad = (LinearLayout) findViewById(R.id.btn112);
        btMap = (LinearLayout) findViewById(R.id.btnMap);
        String[] per = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.ACCESS_FINE_LOCATION};
        PermissionUtil.checkAndRequestPermission(NavigationActivity.this, 100, per);
        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTtsBuilder = new Intent(NavigationActivity.this, ttsBuilder.class);
                toTtsBuilder.putExtra("ApiKey", ApiKey);
                toTtsBuilder.putExtra("identifier", "keyword");
                startActivityForResult(toTtsBuilder, 1);
            }
        });

        btEmg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectOption();
            }
        });
        btRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectOptionRoad();
            }
        });
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMaps = new Intent(NavigationActivity.this, MapsActivity.class);
                startActivity(toMaps);
            }
        });
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (pressedTime == 0) {
                TastyToast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", TastyToast.LENGTH_LONG, TastyToast.INFO);
                pressedTime = System.currentTimeMillis();
            } else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);
                if (seconds > 2000) {
                    pressedTime = 0;
                } else {
                    finish();
                }
            }
//                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("xxx", "Nav onNavigationItemSelected()");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_roadSubmit) { //
            Intent intent = new Intent(NavigationActivity.this, ShowRoadSubmitActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lastSubmit) {
            Intent intent = new Intent(NavigationActivity.this, ShowLastSubmitActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) { // 로그아웃
            new LogoutPost().execute();
            AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
            builder.setTitle("로그아웃 완료");
            builder.setMessage("로그아웃이 완료 되었습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(NavigationActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_rank) {
            Intent intent = new Intent(NavigationActivity.this, ShowRankActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void DialogSelectOption() {
        final String items[] = {"인명피해 관련 상황 - 119", "한국도로교통공사-고속도로 무료 견인 서비스(1)", "한국도로교통공사-고속도로 무료 견인 서비스(2)"};
        AlertDialog.Builder ab = new AlertDialog.Builder(NavigationActivity.this);
        ab.setTitle("긴급 상황 입니까? 바로 연결해드리겠습니다");
        ab.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.d("xxx", "whichButton1 =" + whichButton);
                        // 각 리스트를 선택했을때
                        switch (whichButton) {
                            case 0:
                                // 119 버튼
                                callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:119"));
                                break;
                            case 1:
                                callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1588-2505"));
                                // "한국도로교통공사-고속도로 무료 견인 서비스(1)"
                                break;
                            case 2:
                                callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:080-701-0404"));
                                // "한국도로교통공사-고속도로 무료 견인 서비스(2)"
                                break;
                        }
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.

                        Log.d("xxx", "whichButton2 =" + whichButton);

                        if (callIntent != null) {
                            startActivity(callIntent);
                        }
                        callIntent = null;
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시

                    }
                });
        ab.show();
    }

    private void DialogSelectOptionRoad() {
        final String items[] = {"교통사고", "화재", "낙석", "단순 정체", "공사중"};
        AlertDialog.Builder ab = new AlertDialog.Builder(NavigationActivity.this);
        ab.setTitle("현재 위치에서의 도로 상황을 선택해주세요.");
        ab.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        // 각 리스트를 선택했을때
                        switch (whichButton) {
                            case 0:
                                to = new Intent(NavigationActivity.this, uploadRoad.class);
                                to.putExtra("keyRoad", "교통사고");
                                break;
                            case 1:
                                to = new Intent(NavigationActivity.this, uploadRoad.class);
                                to.putExtra("keyRoad", "화재");
                                break;
                            case 2:
                                to = new Intent(NavigationActivity.this, uploadRoad.class);
                                to.putExtra("keyRoad", "낙석");
                                break;
                            case 3:
                                to = new Intent(NavigationActivity.this, uploadRoad.class);
                                to.putExtra("keyRoad", "단순 정체");
                                break;
                            case 4:
                                to = new Intent(NavigationActivity.this, uploadRoad.class);
                                to.putExtra("keyRoad", "공사중");
                                break;
                        }
                    }
                }).setPositiveButton("신고하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (to != null) {
                            startActivityForResult(to, 1);
                        }
                        to = null;
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시

                    }
                });
        ab.show();
    }

    private class ReceiveNavInfo extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            Log.d("xxxNav", "doInBackground upload function");
            return content;
        }

        protected void onPostExecute(String result) {
            Log.d("xxxNav", "onPostExecute upload function");
            Log.d("xxxNav", "result =" + result);
            String[] data = result.trim().split(";");
            int count = Integer.parseInt(data[0]);
            int score = Integer.parseInt(data[1]);
            int _point = ((count - score) * 10) + (score * 100);
            Log.d("xxxNav", "count =" + count + " score =" + score);
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
            Log.d("xxxNav", String.valueOf(count) + String.valueOf(score) + String.valueOf(_point));
            totalS.setText(count + "");
            admitS.setText(score + "");
            point.setText(_point + "");
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxxNav", "executeClient upload function");

            try {
                // 연결 HttpClient 객체 생성
                HttpClient client = StartActivity.client;
                ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
                // post.add(new BasicNameValuePair("email", email));
                List<Cookie> cookies = ((DefaultHttpClient) client).getCookieStore().getCookies();
                if (!cookies.isEmpty()) {
                    CookieManager cookieManager = CookieManager.getInstance();
                    CookieSyncManager.createInstance(getApplicationContext());
                    cookieManager.setAcceptCookie(true);

                    for (int i = 0; i < cookies.size(); i++) {
                        String cookiesString = cookies.get(i).getName() + "," + cookies.get(i).getValue()
                                + "," + cookies.get(i).getDomain() + "," + cookies.get(i).getPath();
                        //cookieManager.setCookie("http://52.78.45.238/loginCheck.php",cookiesString);
                        Log.d("xxxpla", cookiesString);
                    }

                }

                // Post객체 생성
                HttpPost httpPost = new HttpPost("http://52.78.45.238/getNavInfo.php");
                //httpPost.addHeader("Cookie", strCookie[0]);
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

    private class LogoutPost extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String content = executeClient();
            Log.d("xxxNav", "doInBackground upload function");
            return content;
        }

        protected void onPostExecute(String result) {
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxxNav", "executeClient upload function");

            try {
                // 연결 HttpClient 객체 생성
                HttpClient client = StartActivity.client;
                ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
                HttpPost httpPost = new HttpPost("http://52.78.45.238/logout.php");
                //httpPost.addHeader("Cookie", strCookie[0]);
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                client.execute(httpPost);
                // Post객체 생성

                return "";

            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xxx", "onStart Nav");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xxx", "onStop Nav");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xxx", "onPause Nav");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xxx", "onResume Nav");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("xxx", "onDestroy Nav");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String identifier;
        switch (resultCode) {
            case 1:
                identifier = data.getStringExtra("identifier");
                // FROM tts after keyword, THEN GO TO sttWord
                if (identifier.equalsIgnoreCase("keyword")) {
                    Intent toSttWordBuilder = new Intent(NavigationActivity.this, sttWordBuilder.class);
                    toSttWordBuilder.putExtra("ApiKey", ApiKey);
                    toSttWordBuilder.putExtra("identifier", identifier);
                    startActivityForResult(toSttWordBuilder, 1);
                    break;
                    // FROM tts after inconvenience, THEN GO TO stt
                } else if (identifier.equalsIgnoreCase("inconvenience")) {
                    Intent toSttBuilder = new Intent(NavigationActivity.this, sttBuilder.class);
                    toSttBuilder.putExtra("ApiKey", ApiKey);
                    toSttBuilder.putExtra("identifier", identifier);
                    startActivityForResult(toSttBuilder, 1);
                    break;
                }
                break;
            case 2:
                // FROM sttWord after speeching keyword, THEN GO TO tts
                identifier = data.getStringExtra("identifier");
                key = data.getStringExtra("key");
                Intent toTtsBuilder = new Intent(NavigationActivity.this, ttsBuilder.class);
                toTtsBuilder.putExtra("ApiKey", ApiKey);
                toTtsBuilder.putExtra("identifier", identifier);
                startActivityForResult(toTtsBuilder, 1);
                break;
            case 3:
                // FROM stt after speeching inconvenience, THEN GO TO upload
                context = data.getStringExtra("context");
                Intent toUpload = new Intent(NavigationActivity.this, upload.class);
                toUpload.putExtra("key", key);
                toUpload.putExtra("context", context);
                startActivityForResult(toUpload, 1);
                break;
            case 4:
                TastyToast.makeText(getApplicationContext(), "접수가 되었습니다.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                setResult(1);
                break;
            case 5:
                TastyToast.makeText(getApplicationContext(), "접수 중 오류가 났습니다.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("xxx", "onRequestPermissionResult NavActivity , requestCode : " + requestCode);
        switch (requestCode) {
            case 100:
                if (PermissionUtil.verifyPermissions(grantResults)) {

                } else {
                    //   권한을 얻지 못했다. Show Rational Dialog
                    PermissionDialog pd = PermissionDialog.newInstance("권한 오류");
                    pd.show(getFragmentManager(), "tag");
                }
                break;

        }
    }
}