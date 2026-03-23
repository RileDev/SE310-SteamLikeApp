package org.riledev.se310steamlikeapp.services.payment;

public class SteamWallet implements Payment{
    @Override
    public String makePayment() {
        return "steam_wallet";
    }
}
