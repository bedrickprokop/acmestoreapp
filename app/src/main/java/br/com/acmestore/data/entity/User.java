package br.com.acmestore.data.entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private Long id;
    private String email;
    private Double money;
    private String token;
    private List<Product> productList;

    public User() {
    }

    public User(String email, Double money, String token, List<Product> productList) {
        this(null, email, money, token, productList);
    }

    public User(Long id, String email, Double money, String token, List<Product> productList) {
        this.id = id;
        this.email = email;
        this.money = money;
        this.token = token;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
