package org.riledev.se310steamlikeapp.util;

public class InputValidator {

    public static boolean isPasswordConfirmed(String password, String confirmedPassword){
        return password.trim().equals(confirmedPassword.trim());
    }

    public static boolean isInputEmpty(String value){
        return value.isEmpty();
    }

    public static boolean isAuthFieldEmpty(String username, String password, String confirmPassword){
        if(confirmPassword == null)
            return isInputEmpty(username) || isInputEmpty(password);

        return isInputEmpty(username) || isInputEmpty(password) || isInputEmpty(confirmPassword);
    }

}
