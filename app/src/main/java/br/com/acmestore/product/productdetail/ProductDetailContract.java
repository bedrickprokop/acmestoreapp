package br.com.acmestore.product.productdetail;

import br.com.acmestore.data.entity.Product;

public interface ProductDetailContract {

    interface View {

        void setProgressIndicator(boolean isActive);

        void showProduct(Product student);

        void showProductListUi();

        void showProductListUi(String message);
    }

    interface UserActionListener {

        void loadProduct(Long productId);

        void buyProduct(Long buyerId, Long productId);

        void sellProduct(Long productId);

        void editProduct(Long productId);

        void openProductList();
    }
}
