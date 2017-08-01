package br.com.acmestore.data.service;

import br.com.acmestore.data.entity.User;

public interface UserServiceApi {

    interface UserCallback<T> {
        void onLoaded(T data);
    }

    void create(User user, UserCallback<User> callback);

    void delete(User user, UserCallback<User> callback);

    void findById(Long userId, UserCallback<User> callback);
}
