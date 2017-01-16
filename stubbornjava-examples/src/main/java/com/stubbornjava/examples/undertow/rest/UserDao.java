package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.stubbornjava.common.exceptions.Exceptions;

// {{start:dao}}
/*
 * In memory Dao. Less than ideal but just for an example.
 */
public class UserDao {
    private final ConcurrentMap<String, User> userMap;

    public UserDao() {
        this.userMap = new ConcurrentHashMap<>();
    }

    public User create(String email, Set<User.Role> roles) {
        User user = new User(email, roles, LocalDate.now());

        // If we get a non null value that means the user already exists in the Map.
        if (null != userMap.putIfAbsent(user.getEmail(), user)) {
            return null;
        }
        return user;
    }

    public User get(String email) {
        return userMap.get(email);
    }

    // Alternate implementation to throw exceptions instead of return nulls for not found.
    public User getThrowNotFound(String email) {
        User user = userMap.get(email);
        if (null == user) {
            throw Exceptions.notFound(String.format("User %s not found", email));
        }
        return user;
    }

    public User update(User user) {
        // This means no user existed so update failed. return null
        if (null == userMap.replace(user.getEmail(), user)) {
            return null;
        }
        // Update succeeded return the user
        return user;
    }

    public boolean delete(String email) {
        return null != userMap.remove(email);
    }

    public List<User> listUsers() {
        return userMap.values()
                      .stream()
                      .sorted(Comparator.comparing((User u) -> u.getEmail()))
                      .collect(Collectors.toList());
    }
}
// {{end:dao}}
