package br.com.acmestore.product.productadd;

import android.app.Activity;
import android.support.annotation.NonNull;

import br.com.acmestore.Constants;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.service.ProductServiceApi;

public class ProductAddPresenter implements ProductAddContract.UserActionListener {

    private ProductServiceApi mServiceApi;
    private ProductAddContract.View mView;

    public ProductAddPresenter(@NonNull ProductServiceApi productServiceApi, ProductAddContract.View productAddView) {
        this.mServiceApi = productServiceApi;
        this.mView = productAddView;
    }

    @Override
    public void openProductList() {
        mView.showProductsUi();
    }

    @Override
    public void createNewProduct(Product product) {
        mServiceApi.create(product, new ProductServiceApi.ProductCallback<Product>() {
            @Override
            public void onLoaded(Product data) {
                String name = data.getName().split(Constants.BLANK_SPACE)[0];
                mView.showProductsUi("Product " + name + " created successfully", Activity.RESULT_OK);
            }
        });
    }
}
