package com.savak.savak.utils;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Aditya Kulkarni
 */

public class URLConnectionUtil {

    public static HttpURLConnection config(String action, HashMap<String, Object> map){
        try {
            URL url = new URL(URLConstants.BASE_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            urlConnection.setRequestProperty("SOAPAction", action);
            Log.e("Length", String.valueOf(SOAPUtils.getData(action, map).length));
            urlConnection.setRequestProperty("Content-length", SOAPUtils.getData(action, map).length + "");
            HttpURLConnection.setFollowRedirects(false);
            urlConnection.setUseCaches(true);
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            return urlConnection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
