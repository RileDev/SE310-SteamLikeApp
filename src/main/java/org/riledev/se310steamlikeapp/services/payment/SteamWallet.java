package org.riledev.se310steamlikeapp.services.payment;

/**
 * Konkretna strategija placanja putem Steam Wallet-a (digitalni novcanik).
 * Implementira {@link Payment} interfejs u okviru Strategy obrasca.
 *
 * @see Payment
 */
public class SteamWallet implements Payment {

    /**
     * Izvrsava placanje putem Steam Wallet-a.
     *
     * @return "steam_wallet" kao identifikator metode placanja
     */
    @Override
    public String makePayment() {
        return "steam_wallet";
    }
}
