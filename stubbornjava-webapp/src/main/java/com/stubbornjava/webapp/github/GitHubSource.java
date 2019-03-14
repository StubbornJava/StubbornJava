package com.stubbornjava.webapp.github;

import com.stubbornjava.common.Configs;
import com.stubbornjava.webapp.StubbornJavaBootstrap;

public class GitHubSource {
    private static final String clientId = Configs.properties().getString("github.clientId");
    private static final String clientSecret = Configs.properties().getString("github.clientSecret");
    private static final String ref = Configs.properties().getString("github.ref");

    private static final GitHubApi githubClient = new GitHubApi.Builder()
                                                      .clientId(clientId)
                                                      .clientSecret(clientSecret)
                                                      .ref(ref)
                                                      .build();

    public static GitHubApi githubClient() {
        return githubClient;
    }

    public static void main(String[] args) {
        StubbornJavaBootstrap.run(() -> {
            FileContent result = githubClient().getFile(
                    FileReference.stubbornJava(
                                    "test",
                                    "src/main/java/com/stubbornjava/examples/utils/JsonUtil.java")
                                 );
               System.out.println();
        });
    }
}
