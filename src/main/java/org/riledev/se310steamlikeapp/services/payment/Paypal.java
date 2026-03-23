package org.riledev.se310steamlikeapp.services.payment;

public class Paypal implements Payment{

    @Override
    public String makePayment() {
        return "paypal";
    }
}
