package idv.david.gcmandroidex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private final static String PREF_NAME = "gcm_pref";
    private final static String KEY_NAME_REG_ID = "regId";
    private final static String KEY_NAME_APP_VERSION = "appVersion";
    // 模擬器使用此URL
    private final static String URL = "http://172.16.0.189:8080/GCMServerEx/RegisterServlet";
    //Google Play Services專用的請求代碼
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //從Google API Console上取得的專案編號即為SenderID
    private final static String SENDER_ID = "361595324240";
    private GoogleCloudMessaging gcm;
    private TextView tvRegId;
    private ProgressDialog progressDialog;
    private String regId;

    private InstanceID instanceID;
    private Context context;
    private String scope = "idv.david.gcmandroidex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        tvRegId = (TextView) findViewById(R.id.tvRegId);
        if (checkPlayServices()) {
            instanceID = InstanceID.getInstance(context);
//            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegId();
            if (regId.isEmpty())
                new RegisterTask(1).execute();
        }
    }


    public void onDeleteTokenClick(View v) {
        new RegisterTask(0).execute();
    }

    // 由手動方式重新向Server註冊
    public void onSendToServerClick(View view) {
        new RegisterTask(1).execute();
    }

    /**
     * 原官方GCM文件使用的 GooglePlayServicesUtil多數方法已經deprecated
     * 改使用建議的GoogleApiAvailability類別與其方法
     * <p/>
     * 檢查該裝置是否有Google Play Services APK
     * 若是沒有就跳出提示訊息要求使用者安裝
     * Dialog已由Google實作完畢，我們只需呼叫方法即可
     *
     * @return checked result
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                //當使用者點擊對話視窗的確認，系統即自動轉到Google Play Store
                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                String errMsg = "該設備不支持 Google Play Services.";
                Log.e(TAG, errMsg);
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * 從偏好設定檔裡找到已儲存的Registration ID
     *
     * @return Registration ID
     */
    private String getRegId() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String regId = preferences.getString(KEY_NAME_REG_ID, "");
        if (regId == null || regId.isEmpty()) {
            Log.i(TAG, "Registration id not found.");
            return "";
        }

        int appVersionInPrefs = preferences.getInt(KEY_NAME_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (appVersionInPrefs != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return regId;
    }

    /**
     * 將得到的Registration ID與當前的app版本編號存入偏好設定檔裡
     *
     * @param regId Registration ID
     */
    private void storeRegId(String regId) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int appVersion = getAppVersion();
        Log.i(TAG, "Saving regId on app version " + appVersion);
        Log.i(TAG, "Saving regId " + regId);
        preferences.edit()
                .putString(KEY_NAME_REG_ID, regId)
                .putInt(KEY_NAME_APP_VERSION, appVersion)
                .apply();
    }

    /**
     * 取得當前app的版本號碼
     *
     * @return Current app version code
     */

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException nfe) {
            throw new RuntimeException("Could not get package name: " + nfe);
        }
    }

    /**
     * 1. 送出SENDER_ID給Server
     * 2. 取得由Server回傳的Registration ID
     */
//    public static final MediaType JSON
//            = MediaType.parse("application/json; charset=utf-8");

    private class RegisterTask extends AsyncTask<String, Integer, String> {
        private int type;

        RegisterTask(int type) {
            this.type = type;
        }
//        OkHttpClient client = new OkHttpClient();
//
//        String post(String url, String json) throws IOException {
//            RequestBody body = RequestBody.create(JSON, json);
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("GCM Token 處理中 \n Processing...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String msg;

            try {

                if (instanceID == null)
                    instanceID = InstanceID.getInstance(context);

                if (type == 1) {
                    regId = instanceID.getToken(SENDER_ID, scope);
                } else {
                    instanceID.deleteToken(SENDER_ID, scope);
                    regId = null;
                    sendRegIdToServer();
                }
                Log.i(TAG, " instanceID regId " + regId);
                msg = "Device registered, registration ID = " + regId;
                Log.d(TAG, msg);
                storeRegId(regId);
            } catch (IOException e) {
                msg = "Error :" + e.getMessage();
            }

            return msg;
        }

        // 取 GCM Token 過程中
        @Override
        protected void onPostExecute(String msg) {

            tvRegId.setText(msg + "\n");
            progressDialog.cancel();
        }

        private void sendRegIdToServer() {
            DataOutputStream dos = null;
            HttpURLConnection con = null;
            try {
                URL url = new URL(URL);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setUseCaches(false);
                con.connect();

                dos = new DataOutputStream(con.getOutputStream());
                String content = "regId=" + URLEncoder.encode(regId, "UTF-8");
                Log.i(TAG, "sendRegIdToServer () URL" + content);
                dos.writeBytes(content);
                dos.flush();

                int statusCode = con.getResponseCode();
                if (statusCode == 200) {
                    Log.e(TAG, "send regId to server");
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }

                if (con != null) {
                    con.disconnect();
                }
            }

//            // oKHttp
//            try {
//                String content = "regId=" + URLEncoder.encode(regId, "UTF-8");
//                post(URL,content);
//            }catch (Exception e) {
//
//            }


        }


    }

}
