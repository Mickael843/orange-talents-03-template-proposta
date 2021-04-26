package com.mikkaeru.request.card.model;

public enum WalletType {

    PAYPAL("PayPal");

    String name;

    WalletType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
