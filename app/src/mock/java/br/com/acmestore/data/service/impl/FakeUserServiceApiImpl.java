package br.com.acmestore.data.service.impl;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.UserServiceApi;

public class FakeUserServiceApiImpl implements UserServiceApi {

    private static Map<String, User> USERS = new HashMap<>();

    @Override
    public void create(final User user, final UserCallback<User> callback) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (USERS.containsKey(user.getEmail())) {
                    callback.onLoad(null);
                } else {
                    user.setId(new Long(1));
                    USERS.put(user.getEmail(), user);
                    callback.onLoad(user);
                }
            }
        }, 2000);
    }

    @Override
    public void delete(final User user, final UserCallback<User> callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User remove = USERS.remove(user.getEmail());
                callback.onLoad(remove);
            }
        }, 2000);
    }

    @Override
    public void findById(Long userId, UserCallback<User> callback) {
    }
}
