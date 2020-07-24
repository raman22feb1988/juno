package com.example.juno;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONhelper {
    HttpURLConnection connection;
    String data;

    public String postdatatourl(String requestJSON, String url){
        String responseJSON = new String();
        try{
            URL url1 = new URL(url);

            connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");

            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (requestJSON != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(requestJSON);
                writer.flush();
            }

            connection.connect();
            InputStream inputStream = connection.getInputStream();

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                inputStream = new BufferedInputStream(connection.getInputStream());
            }
            else {
                inputStream = new BufferedInputStream(connection.getErrorStream());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer buffer = new StringBuffer();

            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            responseJSON = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }

    public String getdatafromurl(String url){
        String result = new String();
        try{
            URL url1 = new URL(url);

            connection = (HttpURLConnection) url1.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer buffer = new StringBuffer();

            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            result = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}