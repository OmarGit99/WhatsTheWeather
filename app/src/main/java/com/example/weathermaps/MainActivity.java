package com.example.weathermaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button;
    String main;
    String description;
    String message;
    JsonDownloader jsonDownloader;
    String check;
    String resultcode;
    InputMethodManager manager;


    public void clickFunction(View view) {

        String cityName = editText.getText().toString();
        jsonDownloader = new JsonDownloader();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }
        String encodedcityName = URLEncoder.encode(cityName);
        Log.i("yes", "http://api.openweathermap.org/data/2.5/weather?q=" + encodedcityName + "&appid=b018226a3764c5bdc8a7e00dd23c219b");
        if(encodedcityName != "") {
            try {
                check = jsonDownloader.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedcityName + "&appid=b018226a3764c5bdc8a7e00dd23c219b").get();
                Log.i("yes", check);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "No text entered!", Toast.LENGTH_LONG).show();
        }

    }

    public class JsonDownloader extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                resultcode = "";
                int data = inputStreamReader.read();
                while (data != -1){
                    char resy = (char) data;
                    resultcode +=resy;
                    data = inputStreamReader.read();
                }
                return resultcode;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String resultcode) {
            super.onPostExecute(resultcode);
            try {
                JSONObject jsonObject = new JSONObject(resultcode);
                String getWeather = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(getWeather);
                for(int i =0; i<=(jsonArray.length()-1); i++){
                    JSONObject jsonpart = jsonArray.getJSONObject(i);
                    main = jsonpart.getString("main");
                    description = jsonpart.getString("description");
                }
                message = main +": "+ description;
                textView.setText(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        jsonDownloader = new JsonDownloader();


    }
}
