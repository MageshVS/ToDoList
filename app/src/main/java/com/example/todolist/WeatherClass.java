package com.example.todolist;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherClass extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {

        /*ProgressDialog p = new ProgressDialog(MainActivity.)*/
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... address) {
        try {
            URL url = new URL(address[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();
            String content = "";
            char ch;
            while (data != -1){
                ch = (char)data;
                content = content + ch;
                data = inputStreamReader.read();
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
