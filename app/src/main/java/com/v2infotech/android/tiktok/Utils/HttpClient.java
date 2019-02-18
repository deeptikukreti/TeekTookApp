package com.v2infotech.android.tiktok.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private String boundary = ("SwA" + Long.toString(System.currentTimeMillis()) + "SwA");
    private HttpURLConnection con;
    private String delimiter = "--";
    private OutputStream os;
    private String url;

    public HttpClient(String url) {
        this.url = url;
    }

    public byte[] downloadImage(String imgName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.out.println("URL [" + this.url + "] - Name [" + imgName + "]");
            HttpURLConnection con = (HttpURLConnection) new URL(this.url).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write(("name=" + imgName).getBytes());
            InputStream is = con.getInputStream();
            byte[] b = new byte[1024];
            while (is.read(b) != -1) {
                baos.write(b);
            }
            con.disconnect();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return baos.toByteArray();
    }

    public void connectForMultipart() throws Exception {
        this.con = (HttpURLConnection) new URL(this.url).openConnection();
        this.con.setRequestMethod("POST");
        this.con.setDoInput(true);
        this.con.setDoOutput(true);
        this.con.setRequestProperty("Connection", "Keep-Alive");
        this.con.setRequestProperty("Accept-Charset", "UTF-8");
        this.con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        this.con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
        this.con.connect();
        this.os = this.con.getOutputStream();
    }

    public void addFormPart(String paramName, String value) throws Exception {
        writeParamData(paramName, value);
    }

    public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
        this.os.write((this.delimiter + this.boundary + "\r\n").getBytes());
        this.os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        this.os.write("Content-Type: application/octet-stream\r\n".getBytes());
        this.os.write("Content-Transfer-Encoding: binary\r\n".getBytes());
        this.os.write("\r\n".getBytes());
        this.os.write(data);
        this.os.write("\r\n".getBytes());
    }

    public void finishMultipart() throws Exception {
        this.os.write((this.delimiter + this.boundary + this.delimiter + "\r\n").getBytes());
    }

    public String getResponse() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.con.getInputStream(), "UTF-8"));
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                stringBuilder.append(line);
            } else {
                String responseData = stringBuilder.toString();
                this.con.disconnect();
                return responseData;
            }
        }
    }

    private void writeParamData(String paramName, String value) throws Exception {
        this.os.write((this.delimiter + this.boundary + "\r\n").getBytes());
        this.os.write("Content-Type: text/plain\r\n".getBytes());
        this.os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());
        this.os.write(("\r\n" + value + "\r\n").getBytes());
    }
}
