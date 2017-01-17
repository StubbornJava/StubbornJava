package com.stubbornjava.examples.undertow.rest;

import java.util.List;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.handlers.ApiHandlers;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
// {{start:routes}}
public class UserRoutes {
    private static final UserRequests userRequests = new UserRequests();
    private static final UserDao userDao = new UserDao();

    public static void createUser(HttpServerExchange exchange) {
        User userInput = userRequests.user(exchange);
        User user = userDao.create(userInput.getEmail(), userInput.getRoles());
        if (null == user) {
            ApiHandlers.badRequest(exchange, String.format("User %s already exists.", userInput.getEmail()));
            return;
        }
        exchange.setStatusCode(StatusCodes.CREATED);
        Exchange.body().sendJson(exchange, user);
    }

    public static void getUser(HttpServerExchange exchange) {
        String email = userRequests.email(exchange);
        User user = userDao.get(email);
        if (null == user) {
            ApiHandlers.notFound(exchange, String.format("User %s not found.", email));
            return;
        }
        Exchange.body().sendJson(exchange, user);
    }

    // Alternative Not Found by throwing / handling Exceptions.
    public static void getUserThrowNotFound(HttpServerExchange exchange) {
        String email = userRequests.email(exchange);
        User user = userDao.getThrowNotFound(email);
        Exchange.body().sendJson(exchange, user);
    }

    public static void updateUser(HttpServerExchange exchange) {
        User userInput = userRequests.user(exchange);
        User user = userDao.update(userInput);
        if (null == user) {
            ApiHandlers.notFound(exchange, String.format("User {} not found.", userInput.getEmail()));
            return;
        }
        Exchange.body().sendJson(exchange, user);
    }

    public static void deleteUser(HttpServerExchange exchange) {
        String email = userRequests.email(exchange);

        // If you care about it you can handle it.
        if (false == userDao.delete(email)) {
            ApiHandlers.notFound(exchange, String.format("User {} not found.", email));
            return;
        }
        exchange.setStatusCode(StatusCodes.NO_CONTENT);
        exchange.endExchange();
    }

    public static void listUsers(HttpServerExchange exchange) {
        List<User> users = userDao.listUsers();
        Exchange.body().sendJson(exchange, users);
    }
}
//{{end:routes}}

