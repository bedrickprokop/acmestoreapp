package br.com.acmestore.data.service;

import java.util.List;

import br.com.acmestore.data.entity.Product;

public interface ProductServiceApi {

    interface ProductCallback<T> {
        void onLoaded(T data);
    }

    void list(Long ownerId, String status, Integer page, ProductCallback<List<Product>> callback);

    void findById(Long productId, ProductCallback<Product> callback);

    void findByText(Long ownerId, String status, String text, Integer page, ProductCallback<List<Product>> callback);

    void create(Product product, ProductCallback<Product> callback);

    void update(Product product, ProductCallback<Product> callback);

    void delete(Product product, ProductCallback<Product> callback);

    void buy(Long buyerId, Long productId, ProductCallback<Boolean> callback);

    void sell(Long productId, ProductCallback<Boolean> callback);
}
