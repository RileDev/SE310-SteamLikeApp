package org.riledev.se310steamlikeapp.services.payment;

/**
 * Konkretna strategija placanja putem MasterCard kartice.
 * Implementira {@link Payment} interfejs u okviru Strategy obrasca.
 *
 * @see Payment
 */
public class MasterCard implements Payment {

    /**
     * Izvrsava placanje putem MasterCard kartice.
     *
     * @return "mastercard" kao identifikator metode placanja
     */
    @Override
    public String makePayment() {
        return "mastercard";
    }
}
