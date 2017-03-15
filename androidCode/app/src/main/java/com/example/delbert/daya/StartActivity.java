package com.example.delbert.daya;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class StartActivity extends FragmentActivity {
    //    로그인 유지
    String getDeviceID;
    ProgressDialog dialog = null;
    EditText etId;
    EditText etPw;
    String loginID, loginPW;
    CheckBox autologin;
    Boolean loginChecked;
    List<NameValuePair> params;
    public SharedPreferences settings;
    static HttpClient client;

    FancyButton uBtn;
    FancyButton noBtn;
    private Context context;
    Context mContext;

    static boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d("xxx", "onCreate StartActivity");
        isLogin = false;
        if(NetworkConnection() == false){
            NotConnected_showAlert();
        }
        etId = (EditText) findViewById(R.id.id);
        etPw = (EditText) findViewById(R.id.pw);
        autologin = (CheckBox) findViewById(R.id.rb);
//      로그인 정보 가져오기
        settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        Log.d("xxx",settings.getString("loginID",""));
        loginChecked = settings.getBoolean("LoginChecked", false);
        if(loginChecked){
            Log.d("xxx","loginChecked");

            etId.setText(settings.getString("loginID", ""));
            etPw.setText(settings.getString("loginPW",""));
            autologin.setChecked(true);
        }
        if(!settings.getString("loginID","").equals(""))
            etPw.requestFocus();

        FancyButton submit = (FancyButton) findViewById(R.id.loginBtn);
        submit.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(StartActivity.this,"","확인중...",true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
        String[] per = {Manifest.permission.CALL_PHONE,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//      //  권한 검사
        PermissionUtil.checkAndRequestPermission(StartActivity.this, 100, per);

        mContext = this;
        uBtn = (FancyButton) findViewById(R.id.userBtn);
        noBtn = (FancyButton) findViewById(R.id.nonUserBtn);

        uBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toJoin = new Intent(StartActivity.this, JoinActivity.class);
                startActivity(toJoin);
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMain = new Intent(StartActivity.this, NavigationActivity.class);

                startActivity(toMain);
            }
        });


    }
    @Override
    protected void onPause() {
        Log.d("xxx", "onPause StartActivity");
        super.onPause();
    }
    @Override
    protected void onStart() {
        Log.d("xxx", "onStart StartActivity");
        super.onStart();
    }
    @Override
    protected void onStop() {
        Log.d("xxx", "onStop StartActivity");
        super.onStop();
        if(autologin.isChecked()){
            settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("loginID",loginID);
            editor.putString("loginPW",loginPW);
            editor.putBoolean("LoginChecked",true);
            editor.commit();
        }else{
            settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
        }
    }
    @Override
    protected void onResume() {
        Log.d("xxx", "onResume StartActivity");

        super.onResume();
    }
    @Override
    protected void onDestroy() {
        Log.d("xxx", "onDestroy StartActivity");
        super.onDestroy();
    }



    //   로그인 유지 function
    private boolean NetworkConnection(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
            return true;
        }
        else{
            return false;
        }
    }
    private void NotConnected_showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("네트워크 연결 오류");
        builder.setMessage("사용 가능한 네트워크가 없습니다.")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    void login(){
        try{
            loginID = etId.getText().toString().trim();
            loginPW = etPw.getText().toString().trim();

//           단말기의 정보 가져오기
            TelephonyManager mTelephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            if(mTelephony.getDeviceId()!= null){
                getDeviceID = mTelephony.getDeviceId();
            } else{
                getDeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            String postURL = "http://52.78.45.238/loginCheck.php";
            HttpPost httppost = new HttpPost(postURL);

            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("loginID",loginID));
            params.add(new BasicNameValuePair("loginPW",loginPW));
            params.add(new BasicNameValuePair("deviceID",getDeviceID));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httppost.setEntity(ent);

            client = new DefaultHttpClient();
//            CookieStore cookieStore = new BasicCookieStore();

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String responsePost = client.execute(httppost,responseHandler);

            List<Cookie> cookies = ((DefaultHttpClient)client).getCookieStore().getCookies();

            if(!cookies.isEmpty()) {
                CookieManager cookieManager = CookieManager.getInstance();
                CookieSyncManager.createInstance(getApplicationContext());
                cookieManager.setAcceptCookie(true);

                for(int i=0; i < cookies.size(); i++) {
                String cookiesString = cookies.get(i).getName() + "," + cookies.get(i).getValue()
                        + "," + cookies.get(i).getDomain() + "," + cookies.get(i).getPath();
//                    cookieManager.setCookie("http://52.78.45.238/loginCheck.php",cookiesString);
                Log.d("xxx_cookie:",cookiesString);
            }

        }
            Log.d("xxx","DeviceID:" + getDeviceID);
            Log.d("xxx","Response:" + responsePost);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
            if(responsePost.equalsIgnoreCase("success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StartActivity.isLogin = true;
                        TastyToast.makeText(getApplicationContext(), "로그인 되었습니다", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }
                });
                if(loginID.equals("root@")){
                    startActivity(new Intent(StartActivity.this, WebViewActivity.class));
                }
                else {
                    startActivity(new Intent(StartActivity.this, NavigationActivity.class));
                    finish();
                }
            } else{
                Log.i("xxx",responsePost);
                showAlert();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }
    public void showAlert(){
        StartActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("로그인 에러");
                builder.setMessage("로그인 정보가 일치하지 않습니다.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("xxx", "onRequestPermissionResult StartActivity , requestCode : " + requestCode);
        PermissionDialog pd = null;
        switch (requestCode) {
            case 100:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    // 권한을 얻었다. Do something with permission
                } else {
                    // 권한을 얻지 못했다. Show Rational Dialog
                    pd = com.example.delbert.daya.PermissionDialog.newInstance("권한 오류");
                    pd.show(getFragmentManager(), "tag");
                }
                break;
        }
    }
}


