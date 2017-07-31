package br.com.acmestore.product.products;

import java.util.List;

import br.com.acmestore.data.entity.Product;

public interface ProductsContract {

    interface View {
        void setProgressIndicator(boolean isActive);

        void setListProgressIndicator(boolean isActive);

        void showProductList(List<Product> productList, boolean doRefresh);

        void showProductDetailUi(Long productId);

        void changeActionBarWhenProductSelected();

        void changeActionBarWhenProductUnselected(int productPosition);

        void removeProduct(int position);

        void showMessage(String message);
    }

    interface UserActionListener {

        void loadProductList(Long ownerId, String status, int page, boolean doRefresh);

        void selectProduct(int selectedProductPosition, Product selectedProduct);

        void unselectProduct(int selectedProductPosition);

        void deleteSelectedProduct();

        void openProductDetail(Product product);
    }
}
