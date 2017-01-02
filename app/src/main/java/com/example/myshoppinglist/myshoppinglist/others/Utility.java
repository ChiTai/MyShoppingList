package com.example.myshoppinglist.myshoppinglist.others;

import android.widget.EditText;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by ameliebarre1 on 07/12/16.
 */
public class Utility {

    public static boolean checkEmail(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length() > 0 ? true: false;
    }
}
