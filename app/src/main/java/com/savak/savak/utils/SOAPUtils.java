package com.savak.savak.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aditya Kulkarni
 */

public class SOAPUtils {

    public static byte[] getData(String ACTION, HashMap<String, String> map) {
        StringBuilder builder = new StringBuilder();
        builder.append(getRequestMap(ACTION, map));
        return builder.toString().trim().getBytes();
    }

    public static byte[] getIntData(String ACTION, HashMap<String, Integer> map) {
        StringBuilder builder = new StringBuilder();
        builder.append(getIntRequestMap(ACTION, map));
        return builder.toString().trim().getBytes();
    }

    public static String getRequestMap(String ACTION,
                                 HashMap<String, String> map) {
        String SOAPRequestXML = null;
        String split[] = ACTION.split("/");
        if (split.length > 0) {
            String METHOD = split[split.length - 1].trim();
            SOAPRequestXML = "<?xml version='1.0' encoding='utf-8'?>"
                    + "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema'  xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>"
                    + "<soap:Body>" + "<" + METHOD

                    + " xmlns='http://tantraved.in/'>" + createRequest(map)
                    + "</" + METHOD + ">" + "</soap:Body>"
                    + "</soap:Envelope>";
        }
        Log.e("SOAP XML", SOAPRequestXML);
        return SOAPRequestXML;
    }

    public static String getIntRequestMap(String ACTION,
                                       HashMap<String, Integer> map) {
        String SOAPRequestXML = null;
        String split[] = ACTION.split("/");
        if (split.length > 0) {
            String METHOD = split[split.length - 1].trim();
            SOAPRequestXML = "<?xml version='1.0' encoding='utf-8'?>"
                    + "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema'  xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>"
                    + "<soap:Body>" + "<" + METHOD

                    + " xmlns='http://tantraved.in/'>" + createIntRequest(map)
                    + "</" + METHOD + ">" + "</soap:Body>"
                    + "</soap:Envelope>";
        }
        Log.e("SOAP XML", SOAPRequestXML);
        return SOAPRequestXML;
    }

    private static String createRequest(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("<");
            sb.append(entry.getKey());
            sb.append(">");
            sb.append(entry.getValue());
            sb.append("</");
            sb.append(entry.getKey());
            sb.append(">");
        }
        return sb.toString();
    }

    private static String createIntRequest(HashMap<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append("<");
            sb.append(entry.getKey());
            sb.append(">");
            sb.append(entry.getValue());
            sb.append("</");
            sb.append(entry.getKey());
            sb.append(">");
        }
        return sb.toString();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
