package com.example.myshoppinglist.myshoppinglist.others;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by ameliebarre1 on 07/12/16.
 */
public class Utility {

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length() > 0 ? true: false;
    }
}
