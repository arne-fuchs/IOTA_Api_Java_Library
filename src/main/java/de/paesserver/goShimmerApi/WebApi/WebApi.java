package de.paesserver.goShimmerApi.WebApi;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class WebApi {

    private final String protocol;
    private final String baseURL;

    public WebApi(String protocol, String host, int port) {
        this.protocol = protocol;
        baseURL = protocol + "://" + host + ":" + port + "/";
    }

    String doGetRequest(URLConnection urlConnection) throws IOException {
        if (protocol.equals("http") || protocol.equals("https")) {
            HttpURLConnection httpURLConnection;
            if (urlConnection instanceof HttpsURLConnection)
                httpURLConnection = (HttpsURLConnection) urlConnection;
            else
                httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");

            return readResponse(httpURLConnection);
        } else {
            throw new IllegalArgumentException("Invalid protocol: " + protocol);
        }
    }

    String doPostRequest(String data, URLConnection urlConnection) throws IOException {
        HttpURLConnection httpURLConnection;
        if (urlConnection instanceof HttpsURLConnection)
            httpURLConnection = (HttpsURLConnection) urlConnection;
        else
            httpURLConnection = (HttpURLConnection) urlConnection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Length", "" + data.getBytes(StandardCharsets.UTF_8).length);
        httpURLConnection.setUseCaches(false);


        //Write to stream
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        //Read stream
        return readResponse(httpURLConnection);
    }

    private String readResponse(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader in;
        switch (httpURLConnection.getResponseCode()) {
            case 500, 404, 400 -> in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            default -> in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        }
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        return content.toString();
    }

    public String getBaseURL(){
        return baseURL;
    }

    public String getProtocol(){
        return protocol;
    }

}
