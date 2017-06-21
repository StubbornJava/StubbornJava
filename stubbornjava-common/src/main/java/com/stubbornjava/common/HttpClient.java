package com.stubbornjava.common;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

public class HttpClient {
    private HttpClient() {
    };
    // {{start:logging}}
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private static final HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor((msg) -> {
            log.debug(msg);
        });
    static {
        loggingInterceptor.setLevel(Level.BODY);
    }

    public static HttpLoggingInterceptor getLoggingInterceptor() {
        return loggingInterceptor;
    }
    // {{end:logging}}

    // {{start:client}}
    private static final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addNetworkInterceptor(loggingInterceptor)
        .build()
    ;

    /*
     * Global client that can be shared for common HTTP tasks.
     */
    public static OkHttpClient globalClient() {
        return client;
    }
    // {{end:client}}

    // {{start:cookieJar}}
    /*
     * Creates a new client from the global client with
     * a stateful cookie jar. This is useful when you need
     * to access password protected sites.
     */
    public static OkHttpClient newClientWithCookieJar() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        return client.newBuilder().cookieJar(cookieJar).build();
    }
    // {{end:cookieJar}}

    public static RuntimeException unknownException(Response response) throws IOException {
        return new RuntimeException(String.format("code: %s, body: %s", response.code(), response.body().string()));
    }

    /*
     * This is very bad practice and should NOT be used in production.
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return new java.security.cert.X509Certificate[]{};
            }
        }
    };
    private static final SSLContext trustAllSslContext;
    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

    public static OkHttpClient trustAllSslClient(OkHttpClient client) {
        log.warn("Using the trustAllSslClient is highly discouraged and should not be used in Production!");
        Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
          @Override
          public boolean verify(String hostname, SSLSession session) {
            return true;
          }
        });
        return builder.build();
    }
}
