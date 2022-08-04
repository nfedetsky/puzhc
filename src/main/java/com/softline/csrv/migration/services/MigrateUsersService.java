package com.softline.csrv.migration.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.softline.csrv.entity.User;
import com.softline.csrv.migration.Util;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MigrateUsersService {
    public static final String KEY_FIELD = "key";
    public static final String EMAIL_FIELD = "emailAddress";

    @Autowired
    private DataManager dataManager;
    @Autowired
    private Util util;

    public User getUserByKey(JsonNode objFields) {
        JsonNode userKey = objFields.get(KEY_FIELD);
        if (userKey == null) return null;

        User user = dataManager.load(User.class)
                .query("select o from User o where o.username = :username")
                .parameter("username", userKey.asText())
                .optional().orElse(null);

        JsonNode emailAddress = objFields.get(EMAIL_FIELD);
        if (user == null) {
            user = dataManager.create(User.class);
            user.setUsername(util.substring(userKey.asText(), 250));
            if (emailAddress != null) {
                user.setEmail(emailAddress.asText());
            }
            dataManager.save(user);
        } else if (user.getEmail() == null && emailAddress != null) {
            user.setEmail(util.substring(emailAddress.asText(), 250));
            dataManager.save(user);
        }
        return user;
    }

    public User getUserByKey(String userKey) {
        if (userKey == null) return null;
        User user = dataManager.load(User.class)
                .query("select o from User o where o.username = :username")
                .parameter("username", userKey)
                .optional().orElse(null);
        if (user == null) {
            user = dataManager.create(User.class);
            user.setUsername(util.substring(userKey, 250));
            dataManager.save(user);
        }
        return user;
    }
}