
package com.example.delbert.daya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ShowRankActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listview;
    View head, myRankView;
    ListViewAdapter_rank adapter;
    JSONObject jo;
    JSONArray ja;
    String[][] tableKeyword = null;
    String myId = "";
    static int idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new ReceivePost().execute();
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
            Log.d("xxx", "ShowRank_result =" + result);
            try {
//                 불편상황
                jo = new JSONObject(result);
                ja = jo.getJSONArray("output");
                adapter = new ListViewAdapter_rank();
                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.listview3);
                head = getLayoutInflater().inflate(R.layout.list_view_head_rank, null, false);
                myRankView = getLayoutInflater().inflate(R.layout.list_view_rank_my_rank, null, false);
                listview.addHeaderView(head);
                listview.setHeaderDividersEnabled(true);
                listview.setAdapter(adapter);
                tableKeyword = new String[ja.length()][4];
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject c = ja.getJSONObject(i);
                    String id = c.getString("user_email");
                    String total = c.getString("count");
                    String ok = c.getString("score");
                    String point = calcRank(total, ok);
                    tableKeyword[i][0] = point;
                    tableKeyword[i][1] = id;
                    tableKeyword[i][2] = total;
                    tableKeyword[i][3] = ok;

                    Log.d("xxx", "rank_tableKeyword[i][0]" + tableKeyword[i][0]);
                    Log.d("xxx", "rank_tableKeyword[i][1]" + tableKeyword[i][1]);
                    Log.d("xxx", "rank_tableKeyword[i][2]" + tableKeyword[i][2]);
                    Log.d("xxx", "rank_tableKeyword[i][3]" + tableKeyword[i][3]);

                }
                Arrays.sort(tableKeyword, new Comparator<String[]>() {// 랭킹 sort
                    @Override
                    public int compare(String[] entry1, String[] entry2) {
                        final int time1 = Integer.parseInt(entry1[0]);
                        final int time2 = Integer.parseInt(entry2[0]);
                        return time2 - time1 ;
                    }
                });

                int rootcheck=0;

                for (int i = 0; i < tableKeyword.length; i++) {
                    if (myId.equals(tableKeyword[i][1])) {
                        idx = i;
                    }
                    if(tableKeyword[i][1].equals("root@")) {
                        rootcheck = -1;
                    }
                    adapter.addItem(i + 1 + rootcheck +"", tableKeyword[i][0], tableKeyword[i][1], tableKeyword[i][2], tableKeyword[i][3]);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // 실제 전송하는 부분
        public String executeClient() {
            Log.d("xxx", "executeClient function");
            try {
                // 연결 HttpClient 객체 생성
                HttpClient client = StartActivity.client;
                ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();

                List<Cookie> cookies = ((DefaultHttpClient) client).getCookieStore().getCookies();
                CookieManager cookieManager = CookieManager.getInstance();
                CookieSyncManager.createInstance(getApplicationContext());
                cookieManager.setAcceptCookie(true);

                myId = cookies.get(1).getValue();
                myId = myId.replace("%40", "@");

                // 객체 연결 설정 부분, 연결 최대시간 등등
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000);
                HttpConnectionParams.setSoTimeout(params, 5000);
                // Post객체 생성

                HttpPost httpPost = new HttpPost("http://52.78.45.238/showRank.php");

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

        public String calcRank(String a, String b) {
            return ((Integer.parseInt(b) * 100) +
                    (Integer.parseInt(a) - Integer.parseInt(b)) * 10) + "";
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
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("xxx", "Nav onNavigationItemSelected()");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_roadSubmit) {
            Intent intent = new Intent(ShowRankActivity.this, ShowRoadSubmitActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_lastSubmit) {
            Intent intent = new Intent(ShowRankActivity.this, ShowLastSubmitActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logout) { // 로그아웃
            new LogoutPost().execute();
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowRankActivity.this);
            builder.setTitle("로그아웃 완료");
            builder.setMessage("로그아웃이 완료 되었습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ShowRankActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_rank) {
            //null
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        Log.d("xxx", "onResume SLSActivity_flag");
        super.onResume();

    }
}

