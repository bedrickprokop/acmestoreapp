package br.com.acmestore.data.entity;

import java.io.Serializable;

public class Product implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String pictureUrl;
    private Double unitPrice;

    //TOSELL or BOUGHT
    private String status;
    private User owner;

    public Product() {
    }

    public Product(String name, String description, String pictureUrl, Double unitPrice,
                   String status, User owner) {
        this(null, name, description, pictureUrl, unitPrice, status, owner);
    }

    public Product(Long id, String name, String description, String pictureUrl, Double unitPrice,
                   String status, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.unitPrice = unitPrice;
        this.status = status;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
