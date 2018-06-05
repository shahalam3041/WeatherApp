package com.example.shahalam.json;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView, cityName, temperature1, countryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        cityName = findViewById(R.id.cityID);
        temperature1 = findViewById(R.id.temperatureID);
        countryName = findViewById(R.id.countryID);

        JSONTask jsonTask = new JSONTask();
        jsonTask.execute();
    }

    public class JSONTask extends AsyncTask<String,String,String> {


        String cityIDofAPI = "1203891"; /// for dinajpur city
        String appIDofAPI = "992979a2c20cfaae30adbdff69a1f724"; /// api key, it is fixed
        String link = "http://api.openweathermap.org/data/2.5/weather?id="+cityIDofAPI+"&appid="+appIDofAPI;
        CustomData customData = new CustomData();
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String name, description, temperature, country;
        String file;
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(link);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";

                while ((line=bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                file = stringBuffer.toString();
                JSONObject mainObject = new JSONObject(file);
                // extract city name
                name = mainObject.getString("name");

                // extract temperature
                String temp = mainObject.getString("main");
                JSONObject innerMain = new JSONObject(temp);
                temperature = innerMain.getString("temp");

                /// extract country name
                String counName = mainObject.getString("sys");
                JSONObject countrName = new JSONObject(counName);
                country = countrName.getString("country");


                // put them to custom data
                customData.addCityName(name);
                customData.addCountry(country);
                customData.addTemperature(temperature);



                return country;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

           String shohor =  customData.getCityName().toString();
           String tap = customData.getTemperature().toString();
           String desh = customData.getCountry().toString();
           cityName.setText(shohor);
           countryName.setText(desh);
           temperature1.setText(tap);

        }
    }
}
