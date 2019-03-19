package com.stubbornjava.webapp.integrations;

import java.io.IOException;

import org.jooq.lambda.Unchecked;

import com.stubbornjava.common.Configs;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.webapp.StubbornJavaBootstrap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Slack {
    private static final String legacyToken = Configs.properties().getString("slack.legacyToken");
    private static final OkHttpClient client = HttpClient.globalClient();

    public static boolean invite(String email) {
        HttpUrl url = HttpUrl.parse("https://slack.com/api/users.admin.invite");
        FormBody body = new FormBody.Builder()
           .add("token", legacyToken)
           .add("email", email)
           .add("resend", "true")
           .build();
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException ex) {
            Unchecked.throwChecked(ex);
            return false;
        }
    }

    public static void main(String[] args) {
        StubbornJavaBootstrap.run(() -> {
            System.out.println(legacyToken);
        });

    }
}
