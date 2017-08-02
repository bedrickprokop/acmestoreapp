package br.com.acmestore.data.entity;

import java.io.Serializable;

public class MessageQueue implements Serializable {

    private Long productId;
    private Boolean fromNotification;
    private String fromView;
    private User user;
    private String message;

    public MessageQueue() {
    }

    public MessageQueue(Long productId, Boolean fromNotification, String fromView, User user, String message) {
        this.productId = productId;
        this.fromNotification = fromNotification;
        this.fromView = fromView;
        this.user = user;
        this.message = message;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getFromNotification() {
        return fromNotification;
    }

    public void setFromNotification(Boolean fromNotification) {
        this.fromNotification = fromNotification;
    }

    public String getFromView() {
        return fromView;
    }

    public void setFromView(String fromView) {
        this.fromView = fromView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
