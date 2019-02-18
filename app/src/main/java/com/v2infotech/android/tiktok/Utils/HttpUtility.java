package com.v2infotech.android.tiktok.Utils;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtility {
    public static final int CONNECTION_TIME_OUT = 500000;
    public static final int SOCKET_TIME_OUT = 800000;
    private static HttpURLConnection httpConn;

    public static HttpURLConnection sendGetRequest(String requestURL) throws IOException {
        httpConn = (HttpURLConnection) new URL(requestURL).openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true);
        httpConn.setDoOutput(false);
        return httpConn;
    }

    public static HttpURLConnection sendPostRequest(String requestURL, Map<String, String> params, Activity context) throws IOException {
        httpConn = (HttpURLConnection) new URL(requestURL).openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(10000);
//        System.out.println("token issss>>>>" + CommonMethod.getSessionId(context));
//        httpConn.setRequestProperty("Cookie", "frontend=" + CommonMethod.getSessionId(context));
        StringBuffer requestParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            httpConn.setDoOutput(true);
            for (String key : params.keySet()) {
                String value = (String) params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
        }
        return httpConn;
    }

    public static HttpURLConnection sendPostRequestForLogin(String requestURL, Map<String, String> params) throws IOException {
        httpConn = (HttpURLConnection) new URL(requestURL).openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(10000);
        StringBuffer requestParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            httpConn.setDoOutput(true);
            for (String key : params.keySet()) {
                String value = (String) params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
        }
        return httpConn;
    }

    public static String readSingleLineRespone() throws IOException {
        if (httpConn != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String response = reader.readLine();
            reader.close();
            return response;
        }
        throw new IOException("Connection is not established.");
    }

    public static String[] readMultipleLinesRespone() throws IOException {
        if (httpConn != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            List<String> response = new ArrayList();
            String str = "";
            while (true) {
                str = reader.readLine();
                if (str != null) {
                    response.add(str);
                } else {
                    reader.close();
                    return (String[]) response.toArray(new String[0]);
                }
            }
        }
        throw new IOException("Connection is not established.");
    }

    public static void disconnect() {
        if (httpConn != null) {
            httpConn.disconnect();
        }
    }
}
