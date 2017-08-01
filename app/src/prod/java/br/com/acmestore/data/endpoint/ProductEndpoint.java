package br.com.acmestore.data.endpoint;

import java.util.List;
import java.util.Map;

import br.com.acmestore.data.entity.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ProductEndpoint {

    @GET("product")
    Call<List<Product>> list();

    @GET("product/query")
    Call<List<Product>> list(@QueryMap Map<String, String> queryString);

    @GET("product/{productId}")
    Call<Product> findById(@Path("productId") Long productId);

    @POST("product")
    Call<Product> create(@Body Product product);

    @DELETE("product/{productId}")
    Call<Product> delete(@Path("productId") Long productId);

    @POST("product/buy/{buyerId}/{productId}")
    Call<Boolean> buy(@Path("buyerId") Long buyerId, @Path("productId") Long productId);

    @POST("product/sell/{productId}")
    Call<Boolean> sell(@Path("productId") Long productId);

}
