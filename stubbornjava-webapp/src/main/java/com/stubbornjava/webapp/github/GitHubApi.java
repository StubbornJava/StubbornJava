package com.stubbornjava.webapp.github;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.Json;
import com.stubbornjava.common.Retry;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class GitHubApi {
    private static final Logger logger = LoggerFactory.getLogger(GitHubApi.class);
    private static final String HOST = "https://api.github.com";
    private static final String VERSION_HEADER = "application/vnd.github.v3+json";

    private final OkHttpClient client;

    // TODO: make this configurable
    private final LoadingCache<FileReference, FileContent> CACHE = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build(new CacheLoader<FileReference, FileContent>() {
                @Override
                public FileContent load(FileReference key) {
                    return getFileNoCache(key);
                  }
                }
            );

    GitHubApi(OkHttpClient client) {
        super();
        this.client = client;
    }

    public FileContent getFile(FileReference fileRef) {
        return CACHE.getUnchecked(fileRef);
    }

    private FileContent getFileNoCache(FileReference fileRef) {
        HttpUrl url = HttpUrl.parse(HOST)
                             .newBuilder()
                             .addPathSegment("repos")
                             .addPathSegment(fileRef.getOwner())
                             .addPathSegment(fileRef.getRepo())
                             .addPathSegment("contents")
                             .addPathSegments(fileRef.getPath())
                             .build();
        Request request = new Request.Builder()
                   .url(url)
                   .get()
                   .build();
        String json = Retry.retryUntilSuccessfulWithBackoff(() -> client.newCall(request).execute());
        JsonNode node = Json.serializer().nodeFromJson(json);

        String name = node.path("name").asText();
        String ghUrl = node.path("html_url").asText();
        String base64 = node.path("content").asText().replaceAll("\n", "");
        String content = new String(Base64.getDecoder().decode(base64), Charset.forName("UTF-8"));
        FileContent fileContent = new FileContent(fileRef.getName(), name, ghUrl, content);
        return fileContent;
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String ref = "master";

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder ref(String ref) {
            this.ref = ref;
            return this;
        }

        public GitHubApi build() {
            OkHttpClient client = HttpClient.globalClient()
                .newBuilder()
                .addInterceptor(HttpClient.getHeaderInterceptor("Accept", VERSION_HEADER))
                .addInterceptor(HttpClient.basicAuth(clientId, clientSecret))
                .addNetworkInterceptor(HttpClient.getLoggingInterceptor())
                .build();
            return new GitHubApi(client);
        }
    }
}
