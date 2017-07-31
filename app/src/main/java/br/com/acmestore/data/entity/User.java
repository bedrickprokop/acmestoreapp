package br.com.acmestore.data.entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private Long id;
    private String email;

    //COYOTE or ROADRUNNER
    private String type;
    private Double money;
    private List<Product> productList;

    public User() {
    }

    public User(String email, String type, Double money, List<Product> productList) {
        this(null, email, type, money, productList);
    }

    public User(Long id, String email, String type, Double money, List<Product> productList) {
        this.id = id;
        this.email = email;
        this.type = type;
        this.money = money;
        this.productList = productList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
