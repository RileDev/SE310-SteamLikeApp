package org.riledev.se310steamlikeapp.services.payment;

public class MasterCard implements Payment{
    @Override
    public String makePayment() {
        return "mastercard";
    }
}
