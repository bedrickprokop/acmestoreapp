package br.com.acmestore.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.acmestore.Constants;
import br.com.acmestore.data.HttpEndpointGenerator;
import br.com.acmestore.data.endpoint.ProductEndpoint;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.service.ProductServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductServiceApiImpl implements ProductServiceApi {

    @Override
    public void list(Long ownerId, String status, final Integer page,
                     final ProductCallback<List<Product>> callback) {

        Map<String, String> queryString = new HashMap<>();
        queryString.put("ownerId", (null == ownerId) ? Constants.EMPTY_SPACE : ownerId.toString());
        queryString.put("status", status);
        queryString.put("page", page.toString());

        Call<List<Product>> call = new HttpEndpointGenerator<ProductEndpoint>()
                .gen(ProductEndpoint.class).list(queryString);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> body = response.body();
                if (null == body) {
                    body = new ArrayList<>();
                    callback.onLoaded(body);
                }

                int p = page - 1;
                int startIndex = p * 10;
                int endIndex = startIndex + 10;

                List<Product> productList = body;
                if (startIndex > body.size()) {
                    productList = new ArrayList<>();
                } else if (endIndex > body.size()) {
                    endIndex = body.size();
                    productList = body.subList(startIndex, endIndex);
                } else {
                    productList = body.subList(startIndex, endIndex);
                }
                callback.onLoaded(productList);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });

    }

    @Override
    public void findById(Long productId, final ProductCallback<Product> callback) {
        Call<Product> call = new HttpEndpointGenerator<ProductEndpoint>()
                .gen(ProductEndpoint.class).findById(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product body = response.body();
                callback.onLoaded(body);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }

    @Override
    public void findByText(Long ownerId, String status, String text, Integer page,
                           ProductCallback<List<Product>> callback) {
        //TODO not using
    }

    @Override
    public void create(final Product product, final ProductCallback<Product> callback) {
        Call<Product> call = new HttpEndpointGenerator<ProductEndpoint>()
                .gen(ProductEndpoint.class).create(product);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product body = response.body();
                product.setId(body.getId());
                callback.onLoaded(product);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }

    @Override
    public void update(Product product, ProductCallback<Product> callback) {
        //TODO not using
    }

    @Override
    public void delete(final Product product, final ProductCallback<Product> callback) {
        Call<Product> call = new HttpEndpointGenerator<ProductEndpoint>()
                .gen(ProductEndpoint.class).delete(product.getId());

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    callback.onLoaded(product);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }

    @Override
    public void buy(Long buyerId, Long productId, final ProductCallback<Boolean> callback) {
        Call<Boolean> call = new HttpEndpointGenerator<ProductEndpoint>()
                .gen(ProductEndpoint.class).buy(buyerId, productId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    callback.onLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    @Override
    public void sell(Long productId, final ProductCallback<Boolean> callback) {
        Call<Boolean> call = new HttpEndpointGenerator<ProductEndpoint>()
                .gen(ProductEndpoint.class).sell(productId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    callback.onLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
}
