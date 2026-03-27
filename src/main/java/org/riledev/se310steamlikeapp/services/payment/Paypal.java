package org.riledev.se310steamlikeapp.services.payment;

/**
 * Konkretna strategija placanja putem PayPal servisa.
 * Implementira {@link Payment} interfejs u okviru Strategy obrasca.
 *
 * @see Payment
 */
public class Paypal implements Payment {

    /**
     * Izvrsava placanje putem PayPal-a.
     *
     * @return "paypal" kao identifikator metode placanja
     */
    @Override
    public String makePayment() {
        return "paypal";
    }
}
