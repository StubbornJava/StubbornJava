package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.List;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.Json;
import com.stubbornjava.common.RequestBodies;
import com.stubbornjava.examples.undertow.rest.User.Role;

import io.undertow.util.StatusCodes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestClient {
    private static final Logger log = LoggerFactory.getLogger(RestClient.class);

    // {{start:constructor}}
    private final String host;
    private final OkHttpClient client;
    public RestClient(String host, OkHttpClient client) {
        super();
        this.host = host;
        this.client = client;
    }
    // {{end:constructor}}

    // {{start:listUsers}}
    public List<User> listUsers() {
        HttpUrl route = HttpUrl.parse(host + "/users");
        Request request = new Request.Builder().url(route).get().build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    List<User> users = Json.serializer().fromInputStream(response.body().byteStream(), User.listTypeRef());
                    return users;
                }
                throw HttpClient.unknownException(response);
            }
        }).get();
    }
    // {{end:listUsers}}

    // {{start:getUserByEmail}}
    public User getUserByEmail(String email) {
        HttpUrl route = HttpUrl.parse(host + "/users")
                               .newBuilder()
                               .addPathSegment(email)
                               .build();
        Request request = new Request.Builder().url(route).get().build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                // The user exists
                if (response.isSuccessful()) {
                    User user = Json.serializer().fromInputStream(response.body().byteStream(), User.typeRef());
                    return user;
                }

                /*
                 *  404 Not Found - Either return null or throw your own exception.
                 *  We prefer nulls.
                 */
                if (response.code() == StatusCodes.NOT_FOUND) {
                    return null;
                }
                throw HttpClient.unknownException(response);
            }
        }).get();
    }
    // {{end:getUserByEmail}}

    // {{start:deleteUserByEmail}}
    public boolean deleteUserByEmail(String email) {
        HttpUrl route = HttpUrl.parse(host + "/users")
                               .newBuilder()
                               .addPathSegment(email)
                               .build();
        Request request = new Request.Builder().url(route).delete().build();
        return Unchecked.booleanSupplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == StatusCodes.NO_CONTENT) {
                    return true;
                }

                // Maybe you would throw an exception here? We don't feel the need to.
                if (response.code() == StatusCodes.NOT_FOUND) {
                    return false;
                }
                throw HttpClient.unknownException(response);
            }
        }).getAsBoolean();
    }
    // {{end:deleteUserByEmail}}

    // {{start:createUser}}
    public User createUser(User inputUser) {
        HttpUrl route = HttpUrl.parse(host + "/users");
        Request request = new Request.Builder()
            .url(route)
            .post(RequestBodies.jsonObj(inputUser))
            .build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == StatusCodes.CREATED) {
                    User user = Json.serializer().fromInputStream(response.body().byteStream(), User.typeRef());
                    return user;
                }

                if (response.code() == StatusCodes.BAD_REQUEST) {
                    return null;
                }
                throw HttpClient.unknownException(response);
            }
        }).get();
    }
    // {{end:createUser}}

    // {{start:updateUser}}
    public User updateUser(User inputUser) {
        HttpUrl route = HttpUrl.parse(host + "/users");
        Request request = new Request.Builder()
                .url(route)
                .put(RequestBodies.jsonObj(inputUser))
                .build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    User user = Json.serializer().fromInputStream(response.body().byteStream(), User.typeRef());
                    return user;
                }

                if (response.code() == StatusCodes.NOT_FOUND) {
                    return null;
                }
                throw HttpClient.unknownException(response);
            }
        }).get();
    }
    // {{end:updateUser}}

    // {{start:main}}
    public static void main(String[] args) {
        RestClient client = new RestClient("http://localhost:8080", HttpClient.globalClient());

        log.debug("**** Creating Users ****");
        User user1 = new User("user1@test.com", Sets.newHashSet(Role.USER), LocalDate.now());
        log.debug(Json.serializer().toString(client.createUser(user1)));
        User user2 = new User("user2@test.com", Sets.newHashSet(Role.ADMIN), LocalDate.now());
        log.debug(Json.serializer().toString(client.createUser(user2)));

        log.debug("\n\n");
        log.debug("**** Updating User ****");
        User user2Updated = new User("user2@test.com", Sets.newHashSet(Role.ADMIN, Role.USER), LocalDate.now());
        log.debug(Json.serializer().toString(client.updateUser(user2Updated)));

        log.debug("\n\n");
        log.debug("**** Listing Users ****");
        List<User> users = client.listUsers();
        log.debug(Json.serializer().toString(users));

        log.debug("\n\n");
        log.debug("**** Get Users ****");
        log.debug(Json.serializer().toString(client.getUserByEmail("user1@test.com")));
        log.debug(Json.serializer().toString(client.getUserByEmail("user2@test.com")));

        log.debug("\n\n");
        log.debug("**** Delete Users ****");
        client.deleteUserByEmail("user1@test.com");

        log.debug("\n\n");
        log.debug("**** Get Missing User ****");
        log.debug(Json.serializer().toString(client.getUserByEmail("user1@test.com")));

        log.debug("\n\n");
        log.debug("**** Exception ****");
        log.debug(Json.serializer().toString(client.createUser(null)));
    }
    // {{end:main}}
}
