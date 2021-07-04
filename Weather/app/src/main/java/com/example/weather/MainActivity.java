package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void  gettheweather(View view){


        try {
            jsoon task=new jsoon();
            String encodedtext= URLEncoder.encode(editText.getText().toString(),"UTF-8");
            String jsooon=task.execute("https://api.openweathermap.org/data/2.5/weather?q="+encodedtext+"&appid=4887ad2c54bb692c6cb069e2768daea7").get();
            InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_SHORT).show();
        }
    }

    public  class jsoon extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url= null;
            String result="";
            try {
                url = new URL(urls[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in= connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    char letter=(char)data;
                    result+=letter;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();


                return  null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String finalmessage="";
                String weather=jsonObject.getString("weather");
                JSONArray array=new JSONArray(weather);
                for (int i=0;i<array.length();i++) {
                    JSONObject object = array.getJSONObject(i);
                    String main =object.getString("main");
                    String description =object.getString("description");
                    if (!main.equals("")&&!description.equals("")){
                        finalmessage=main+" : "+description;
                    }
                }
                if(!finalmessage.equals("")){
                    textView.setText(finalmessage);
                }else {
                    Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.edittext2);
        textView=(TextView) findViewById(R.id.textView2);

    }


}