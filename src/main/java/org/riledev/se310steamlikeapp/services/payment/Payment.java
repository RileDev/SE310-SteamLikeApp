package org.riledev.se310steamlikeapp.services.payment;

/**
 * Interfejs za obradu placanja (Strategy obrazac dizajna).
 * Definise ugovor za razlicite metode placanja u sistemu.
 * Svaka konkretna strategija (Paypal, MasterCard, SteamWallet)
 * implementira ovaj interfejs i vraca naziv metode placanja.
 *
 * @see Paypal
 * @see MasterCard
 * @see SteamWallet
 */
public interface Payment {

    /**
     * Izvrsava placanje i vraca naziv koriscene metode.
     *
     * @return string identifikator metode placanja (npr. "paypal", "mastercard")
     */
    String makePayment();
}
