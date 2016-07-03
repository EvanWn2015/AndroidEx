package app.inspection.com.fileupex;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by EVAN_ on 2016/5/16.
 */
public class OkHttpGet {
    private final static String TAG = "OkHttpGet";
    private String header_value = "";

    private OkHttpClient client;


    public OkHttpGet() {

    }

    public OkHttpGet(String header_value) {
        this.header_value = header_value;

    }

    public OkHttpClient getClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;

                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public String get(String url) throws IOException {

        Log.i(TAG, getClient() + "");
        client = getClient();
        Request request = new Request.Builder()
                .header("Authorization", header_value)
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
