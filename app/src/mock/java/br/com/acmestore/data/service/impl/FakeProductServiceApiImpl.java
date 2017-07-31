package br.com.acmestore.data.service.impl;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import br.com.acmestore.data.entity.Product;
import br.com.acmestore.data.entity.User;
import br.com.acmestore.data.service.ProductServiceApi;

public class FakeProductServiceApiImpl implements ProductServiceApi {

    public static List<Product> PRODUCT_LIST;
    public static User RANDOM_OWNER;

    public static List<Product> MY_PRODUCT_LIST_BOUGHT;
    public static List<Product> MY_PRODUCT_LIST_TOSELL;
    public static User ME;


    static {
        PRODUCT_LIST = new ArrayList<>();
        RANDOM_OWNER = new User(new Long(2), "kirb@gmail.com", "COYOTE", 500d, PRODUCT_LIST);

        for (int i = 0; i < 20; i++) {
            Product product = new Product(new Long(i + 1), "EarthQuake Pills " + (i + 1),
                    "ACME EarthQuake Pills - Why wait? Make your own earthquakes-loads of fun! " + (i + 1),
                    "http://static.tvtropes.org/pmwiki/pub/images/acme_products.jpg",
                    (10.5d + i), "TOSELL", RANDOM_OWNER);
            PRODUCT_LIST.add(product);
        }


        MY_PRODUCT_LIST_BOUGHT = new ArrayList<>();
        ME = new User(new Long(1), "greg@gmail.com", "COYOTE", 500d, MY_PRODUCT_LIST_BOUGHT);
        for (int i = 0; i < 20; i++) {
            Product product = new Product(new Long(i + 1), "AXLE GREASE " + (i + 1),
                    "ACME AXLE GREASE - Guaranteed Slippery! " + (i + 1),
                    "http://s-media-cache-ak0.pinimg.com/originals/da/f5/a5/daf5a51c5a6a4a8aed0c8d39faf779f7.jpg",
                    (10.5d + i), "TOSELL", ME);
            MY_PRODUCT_LIST_BOUGHT.add(product);
        }


        MY_PRODUCT_LIST_TOSELL = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Product product = new Product(new Long(i + 1), "Desintegrating Pistol " + (i + 1),
                    "ACME Desintegrating Pistol " + (i + 1),
                    "http://2.bp.blogspot.com/_QvSi3BrEMBA/SS-IybPfPpI/AAAAAAAAGI0/w0sI1AlE1G4/s400/acmedis.jpg",
                    (10.5d + i), "TOSELL", ME);
            MY_PRODUCT_LIST_TOSELL.add(product);
        }
    }


    //buscar todos os produtos que: - geral
    //status == 'TOSELL'
    //owner != me

    //buscar todos os produtos que comprei
    //status == 'BOUGHT'
    //owner == me

    //buscar todos os produtos que coloquei para vender
    //status == 'TOSELL'
    //owner == me
    @Override
    public void list(final Long ownerId, final String status, Integer page, final ProductCallback<List<Product>> callback) {
        page = page - 1;
        final int startIndex = page * 10;
        final int endIndex = startIndex + 10;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                List<Product> productList;
                try {
                    if (null != ownerId && ownerId.equals(ME.getId())) {
                        if (status.equals("BOUGHT")) {
                            productList = MY_PRODUCT_LIST_BOUGHT.subList(startIndex, endIndex);
                        } else {
                            productList = MY_PRODUCT_LIST_TOSELL.subList(startIndex, endIndex);
                        }
                    } else {
                        productList = PRODUCT_LIST.subList(startIndex, endIndex);
                    }
                } catch (IndexOutOfBoundsException e) {
                    productList = new ArrayList<>();
                }
                callback.onLoaded(productList);
            }
        }, 2000);
    }

    @Override
    public void findById(Long productId, ProductCallback<Product> callback) {
        Product finded = null;
        for (Product product : PRODUCT_LIST) {
            if (product.getId().equals(productId)) {
                finded = product;
                break;
            }
        }
        callback.onLoaded(finded);
    }

    @Override
    public void findByText(Long ownerId, String status, String text, Integer page, ProductCallback<List<Product>> callback) {

    }


    @Override
    public void create(Product product, ProductCallback<Product> callback) {
        Long lastInsertId = MY_PRODUCT_LIST_TOSELL.get(MY_PRODUCT_LIST_TOSELL.size() - 1).getId();
        product.setId(++lastInsertId);
        MY_PRODUCT_LIST_TOSELL.add(product);

        callback.onLoaded(product);
    }

    @Override
    public void update(Product product, ProductCallback<Product> callback) {

    }

    @Override
    public void delete(Product product, ProductCallback<Product> callback) {
        int indexToRemove = -1;
        for (int i = 0; i < MY_PRODUCT_LIST_TOSELL.size(); i++) {
            if (product.getId().equals(MY_PRODUCT_LIST_TOSELL.get(i).getId())) {
                indexToRemove = i;
                break;
            }
        }
        MY_PRODUCT_LIST_TOSELL.remove(indexToRemove);
        callback.onLoaded(product);
    }

    @Override
    public void buy(Long productId, ProductCallback<Boolean> callback) {
        int matchedIndex = -1;
        for (int i = 0; i < PRODUCT_LIST.size(); i++) {
            if (PRODUCT_LIST.get(i).getId().equals(productId)) {
                matchedIndex = i;
                break;
            }
        }
        Product product = PRODUCT_LIST.get(matchedIndex);
        PRODUCT_LIST.remove(matchedIndex);

        MY_PRODUCT_LIST_BOUGHT.add(product);

        callback.onLoaded(true);
    }

    @Override
    public void sell(Long productId, ProductCallback<Boolean> callback) {
        int matchedIndex = -1;
        for (int i = 0; i < MY_PRODUCT_LIST_BOUGHT.size(); i++) {
            if (MY_PRODUCT_LIST_BOUGHT.get(i).getId().equals(productId)) {
                matchedIndex = i;
                break;
            }
        }
        Product product = MY_PRODUCT_LIST_BOUGHT.get(matchedIndex);
        MY_PRODUCT_LIST_BOUGHT.remove(matchedIndex);

        MY_PRODUCT_LIST_TOSELL.add(product);

        callback.onLoaded(true);
    }
}
