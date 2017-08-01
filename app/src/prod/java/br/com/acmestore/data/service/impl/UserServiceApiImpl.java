package br.com.acmestore.data.service.impl;

import br.com.acmestore.data.HttpEndpointGenerator;
import br.com.acmestore.data.endpoint.UserEndpoint;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.UserServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServiceApiImpl implements UserServiceApi {

    @Override
    public void create(final User user, final UserCallback<User> callback) {
        Call<User> call = new HttpEndpointGenerator<UserEndpoint>()
                .gen(UserEndpoint.class).create(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();
                user.setId(body.getId());
                callback.onLoad(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    @Override
    public void delete(final User user, final UserCallback<User> callback) {
        Call<User> call = new HttpEndpointGenerator<UserEndpoint>()
                .gen(UserEndpoint.class).delete(user.getId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onLoad(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    @Override
    public void findById(Long userId, UserCallback<User> callback) {
        //TODO not using
    }
}
