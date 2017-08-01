package br.com.acmestore.data.endpoint;

import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserEndpoint {

    @GET("user/{userId}")
    Call<User> findById(@Path("userId") Long userId);

    @POST("user")
    Call<User> create(@Body User user);

    @DELETE("user/{userId}")
    Call<User> delete(@Path("userId") Long userId);
}
