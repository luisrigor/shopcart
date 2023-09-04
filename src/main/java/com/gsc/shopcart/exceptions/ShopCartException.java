package com.gsc.shopcart.exceptions;

public class ShopCartException extends RuntimeException {

    public ShopCartException(String s) {
        super(s);
    }

    public ShopCartException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
