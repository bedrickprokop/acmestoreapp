package br.com.acmestore.product.products;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.acmestore.Constants;
import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.service.ProductServiceApi;
import br.com.acmestore.data.service.UserServiceApi;

public class ProductsPresenter implements ProductsContract.UserActionListener {

    private ProductServiceApi productServiceApi;
    private ProductsContract.View mView;

    private int selectedProductPosition;
    private Product selectedProduct;

    public ProductsPresenter(@NonNull ProductServiceApi productServiceApi,
                             @NonNull ProductsContract.View productsView) {
        this.productServiceApi = productServiceApi;
        this.mView = productsView;

        this.selectedProductPosition = -1;
        this.selectedProduct = null;
    }

    @Override
    public void loadProductList(Long ownerId, String status, int page, final boolean doRefresh) {

        if (doRefresh) {
            mView.setProgressIndicator(true);
        } else {
            mView.setListProgressIndicator(true);
        }

        productServiceApi.list(ownerId, status, page, new ProductServiceApi.ProductCallback<List<Product>>() {
            @Override
            public void onLoaded(List<Product> data) {


                if (doRefresh) {
                    mView.setProgressIndicator(false);
                } else {
                    mView.setListProgressIndicator(false);
                }

                mView.showProductList(data, doRefresh);
            }
        });
    }

    @Override
    public void selectProduct(int selectedProductPosition, Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        this.selectedProductPosition = selectedProductPosition;

        mView.changeActionBarWhenProductSelected();
    }

    @Override
    public void unselectProduct(int selectedProductPosition) {
        mView.changeActionBarWhenProductUnselected(this.selectedProductPosition);

        this.selectedProduct = null;
        this.selectedProductPosition = -1;
    }

    @Override
    public void deleteSelectedProduct() {

        productServiceApi.delete(selectedProduct, new ProductServiceApi.ProductCallback<Product>() {
            @Override
            public void onLoaded(Product data) {
                mView.changeActionBarWhenProductUnselected(ProductsPresenter.this.selectedProductPosition);
                mView.removeProduct(ProductsPresenter.this.selectedProductPosition);

                String productName = data.getName().split(Constants.BLANK_SPACE)[0];
                mView.showMessage(productName.concat(Constants.BLANK_SPACE).concat("was removed successfully"));

                ProductsPresenter.this.selectedProductPosition = -1;
                ProductsPresenter.this.selectedProduct = null;
            }
        });
    }

    @Override
    public void openProductDetail(Product product) {
        mView.showProductDetailUi(product.getId());
    }

}
