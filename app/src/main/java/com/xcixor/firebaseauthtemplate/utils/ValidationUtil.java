package com.xcixor.firebaseauthtemplate.utils;

public class ValidationUtil {
//    private static ValidationUtil instance;

    private ValidationUtil(){}

//    public static ValidationUtil getInstance(){
//        if(instance == null){
//            instance = new ValidationUtil();
//        }
//        return instance;
//    }

    /**
     * Return true if emails match
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Return true if password is greater than six characters
     * @param value
     * @return
     */
    public static boolean isGreaterThanSixCharacters(String value){
        if (value.length() >= 6){
            return true;
        }
        return false;
    }

    /**
     * Return true if values match
     * @param valueOne
     * @param valueTwo
     * @return
     */
    public static boolean textValuesMatch(String valueOne, String valueTwo){
        if (valueOne.equals(valueTwo)){
            return true;
        }else {
            return false;
        }
    }
}
