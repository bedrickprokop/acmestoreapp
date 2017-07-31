package br.com.acmestore.product.productadd;

import br.com.acmestore.data.entity.Product;

public interface ProductAddContract {

    interface View {

        void showProductsUi();

        void showProductsUi(String message, int activityResult);
    }

    interface UserActionListener {

        void openProductList();

        void createNewProduct(Product product);
    }
}
