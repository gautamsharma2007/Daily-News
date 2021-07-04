package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebimages = new ArrayList<String>();
    ArrayList<String> celebnames = new ArrayList<String>();
    int selectedceleb=0;
    String[] answers=new String[4];
    int correctpos=0;
    ImageView imageView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public  void  clicked(View view){
        if(view.getTag().toString().equals(Integer.toString(correctpos))){
            Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"Wrong! it was "+ celebnames.get(selectedceleb),Toast.LENGTH_LONG).show();
        }
        newques();
    }

    public class imagedownload extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public void newques() {
        try {
            Random random = new Random();
            selectedceleb = random.nextInt(celebimages.size());

            imagedownload imagedownload = new imagedownload();
            Bitmap celebpic = imagedownload.execute(celebimages.get(selectedceleb)).get();
            imageView.setImageBitmap(celebpic);

            correctpos = random.nextInt(4);
            int incorrectpos;

            for (int i = 0; i < 4; i++) {
                if (i == correctpos) {
                    answers[i] = celebnames.get(selectedceleb);
                } else {
                    incorrectpos = random.nextInt(celebimages.size());
                    while (incorrectpos == correctpos) {
                        incorrectpos = random.nextInt(celebimages.size());
                    }
                    answers[i] = celebnames.get(incorrectpos);
                }
            }
            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public class downloader extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result=" ";
            URL url;
            HttpURLConnection connection=null;

            try {
                url=new URL(urls[0]);
                connection=(HttpURLConnection)url.openConnection();
                InputStream in =connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    char letter=(char) data;
                    result+=letter;
                    data=reader.read();
                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloader task =new downloader();
        String html=null;
        imageView=findViewById(R.id.imageView);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);

        try {
            html=task.execute("https://web.archive.org/web/20190119082828/www.posh24.se/kandisar").get();
            String[] splitted = html.split("sidebarContainer");
            Pattern p=Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitted[0]);

            while(m.find()){
                celebimages.add(m.group(1));
            }
             p=Pattern.compile("alt=\"(.*?)\"");
             m = p.matcher(splitted[0]);

            while(m.find()){
                celebnames.add(m.group(1));
            }
            newques();

        } catch (Exception e) {

            Log.i("error","soory");
        }

    }
}