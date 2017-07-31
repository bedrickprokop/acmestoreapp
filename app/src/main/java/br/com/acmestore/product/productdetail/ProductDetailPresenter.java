package br.com.acmestore.product.productdetail;

import android.support.annotation.NonNull;

import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.service.ProductServiceApi;

public class ProductDetailPresenter implements ProductDetailContract.UserActionListener {

    private ProductServiceApi mServiceApi;
    private ProductDetailContract.View mView;

    public ProductDetailPresenter(
            @NonNull ProductServiceApi productServiceApi,
            @NonNull ProductDetailContract.View productDetailView) {
        this.mServiceApi = productServiceApi;
        this.mView = productDetailView;
    }

    @Override
    public void loadProduct(Long productId) {

        mView.setProgressIndicator(true);

        mServiceApi.findById(productId, new ProductServiceApi.ProductCallback<Product>() {

            @Override
            public void onLoaded(Product data) {

                mView.setProgressIndicator(false);
                mView.showProduct(data);
            }
        });
    }

    @Override
    public void buyProduct(Long productId) {
        mServiceApi.buy(productId, new ProductServiceApi.ProductCallback<Boolean>() {
            @Override
            public void onLoaded(Boolean data) {
                if (data) {
                    mView.showProductListUi("Product purchased successfully");
                }
            }
        });

    }

    @Override
    public void sellProduct(Long productId) {
        mServiceApi.sell(productId, new ProductServiceApi.ProductCallback<Boolean>() {
            @Override
            public void onLoaded(Boolean data) {
                if (data) {
                    mView.showProductListUi("Product offered for sale successfully");
                }
            }
        });
    }

    @Override
    public void editProduct(Long productId) {
        //TODO
    }

    @Override
    public void openProductList() {
        mView.showProductListUi();
    }
}
