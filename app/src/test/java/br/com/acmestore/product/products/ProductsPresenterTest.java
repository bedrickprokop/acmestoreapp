package br.com.acmestore.product.products;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.ProductServiceApi;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class ProductsPresenterTest {

    public static List<Product> PRODUCT_LIST;
    public static User OWNER;

    static {
        PRODUCT_LIST = new ArrayList<>();
        OWNER = new User(new Long(1), "Gregs", "COYOTE", 500d, new ArrayList<Product>());

        for (int i = 0; i < 20; i++) {
            Product product = new Product(new Long(i + 1), "AcmeProduct " + (i + 1),
                    "AcmeProduct description " + (i + 1), "", (10.99d + i), "TOSELL", OWNER);
            PRODUCT_LIST.add(product);
        }

        OWNER.getProductList().addAll(PRODUCT_LIST);
    }

    //Quem implementa esta interface é o presenter
    private ProductsContract.UserActionListener productsPresenter;

    @Mock
    private ProductServiceApi productServiceApi;
    @Captor
    ArgumentCaptor<ProductServiceApi.ProductCallback> productServiceApiCallbackArgumentCaptor;

    //Quem implementa esta interface ou é a activity ou o fragment
    @Mock
    private ProductsContract.View productsView;

    private int page;
    private boolean doRefresh;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        productsPresenter = new ProductsPresenter(productServiceApi, productsView);

        page = 1;
        doRefresh = true;
    }

    @Test
    public void loadProductsFromApi_PutIntoView() {
        //productsPresenter.loadProductList(page, doRefresh);

        verify(productsView).setProgressIndicator(true);
        verify(productServiceApi).list(null, eq("TOSELL"), eq(page), productServiceApiCallbackArgumentCaptor.capture());
        productServiceApiCallbackArgumentCaptor.getValue().onLoaded(PRODUCT_LIST);

        verify(productsView).setProgressIndicator(false);
        verify(productsView).showProductList(PRODUCT_LIST, doRefresh);
    }

    @Test
    public void loadProductsFromApiWhenScroll_PutIntoview() {
        //productsPresenter.loadProductList(++page, doRefresh = false);

        verify(productsView).setListProgressIndicator(true);
        verify(productServiceApi).list(null, eq("TOSELL"), eq(page), productServiceApiCallbackArgumentCaptor.capture());
        productServiceApiCallbackArgumentCaptor.getValue().onLoaded(PRODUCT_LIST);

        verify(productsView).setListProgressIndicator(false);
        verify(productsView).showProductList(PRODUCT_LIST, doRefresh);
    }

    @Test
    public void clickOnProduct_ShowProductDetailUi() {
        productsPresenter.openProductDetail(PRODUCT_LIST.get(0));
        verify(productsView).showProductDetailUi(eq(PRODUCT_LIST.get(0).getId()));
    }
}
