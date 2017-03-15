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
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
import java.util.concurrent.ExecutionException;

import mehdi.sakout.fancybuttons.FancyButton;

public class ShowLastSubmitActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listview;
    View head;
    ListViewAdapter_last adapter;
    FancyButton btnNow, btnLast;
    DoSort ds;
    JSONObject jo;
    JSONArray ja;
    String[][] tableKeyword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_last_submit);
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


        btnNow = (FancyButton) findViewById(R.id.btn_now);
        btnLast = (FancyButton) findViewById(R.id.btn_past);
        ds = new DoSort();

        //new ReceivePost().execute();
        try {
            new ReceivePost().execute().get();

            btnNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.clearItem();
                    ds.printLast();
                    adapter.notifyDataSetChanged();
                }
            });

            btnLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.clearItem();
                    ds.printNow();
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
            Log.d("xxx", "ShowLastSubmit_result =" + result);
            try {
//                 불편상황
                jo = new JSONObject(result);
                ja = jo.getJSONArray("output");
                adapter = new ListViewAdapter_last();
                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.listview1);
                head = getLayoutInflater().inflate(R.layout.list_view_head_last, null, false);
                listview.addHeaderView(head);
                listview.setHeaderDividersEnabled(true);
                listview.setAdapter(adapter);
                tableKeyword = new String[ja.length()][4];
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject c = ja.getJSONObject(i);
                    String created = c.getString("created");
                    String created_date = c.getString("created_date");
                    String flag_ok = c.getString("flag_ok");
                    String contextKey = c.getString("context_key");
                    tableKeyword[i][0] = created_date;
                    tableKeyword[i][1] = created;
                    tableKeyword[i][2] = contextKey;
                    tableKeyword[i][3] = flag_ok;

                    Log.d("xxx", "last_tableKeyword[i][0]" + tableKeyword[i][0]);
                    Log.d("xxx", "last_tableKeyword[i][1]" + tableKeyword[i][1]);
                    Log.d("xxx", "last_tableKeyword[i][2]" + tableKeyword[i][2]);
                    Log.d("xxx", "last_tableKeyword[i][3]" + tableKeyword[i][3]);

                    adapter.addItem(tableKeyword[i][0], tableKeyword[i][1], tableKeyword[i][2], tableKeyword[i][3]);

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
                HttpClient client = StartActivity.client;
                ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();

                // 객체 연결 설정 부분, 연결 최대시간 등등
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000);
                HttpConnectionParams.setSoTimeout(params, 5000);
                // Post객체 생성

                HttpPost httpPost = new HttpPost("http://52.78.45.238/showLastSubmit.php");

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

    public class DoSort {
        public DoSort() {
        }

        public void printLast() {
            Arrays.sort(tableKeyword, new Comparator<String[]>() {// 최신날짜
                @Override
                public int compare(String[] entry1, String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2) * -1;
                }
            });
            for (int i = 0; i < ja.length(); i++) {
                adapter.addItem(tableKeyword[i][0], tableKeyword[i][1], tableKeyword[i][2], tableKeyword[i][3]);
            }
        }

        public void printNow() {
            Arrays.sort(tableKeyword, new Comparator<String[]>() {// 최신날짜
                @Override
                public int compare(String[] entry1, String[] entry2) {
                    final String time1 = entry1[0];
                    final String time2 = entry2[0];
                    return time1.compareTo(time2);
                }
            });
            for (int i = 0; i < ja.length(); i++) {
                adapter.addItem(tableKeyword[i][0], tableKeyword[i][1], tableKeyword[i][2], tableKeyword[i][3]);
            }

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
            Intent intent = new Intent(ShowLastSubmitActivity.this, ShowRoadSubmitActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_lastSubmit) {
            //null
        } else if (id == R.id.nav_logout) { // 로그아웃
            new LogoutPost().execute();
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowLastSubmitActivity.this);
            builder.setTitle("로그아웃 완료");
            builder.setMessage("로그아웃이 완료 되었습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ShowLastSubmitActivity.this, StartActivity.class);
                            startActivity(intent);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_rank) {
            Intent intent = new Intent(ShowLastSubmitActivity.this, ShowRankActivity.class);
            startActivity(intent);
            finish();
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
