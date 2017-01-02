package com.example.myshoppinglist.myshoppinglist.others;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ameliebarre1 on 27/12/2016.
 */

public class HttpUtility {

    private static HttpURLConnection httpConn;

    public static HttpURLConnection sendGetRequest(String requestURL) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);

        httpConn.setReadTimeout(15000);
        httpConn.setConnectTimeout(15000);
        httpConn.setRequestMethod("GET");
        httpConn.setDoInput(true);
        httpConn.setDoOutput(true);


        httpConn.connect();

        return httpConn;
    }

    public static String readMultipleLines() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        StringBuffer sb = new StringBuffer("");
        String line = "";

        while((line = in.readLine()) != null) {
            sb.append(line);
            break;
        }

        in.close();
        return sb.toString();

    }

    public static int getResponseCode() throws IOException {
        int responseCode = httpConn.getResponseCode();
        return responseCode;
    }

}
