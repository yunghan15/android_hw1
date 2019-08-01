package com.example.bmi_bmr_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;



public class result extends AppCompatActivity {

    private ListAdapter listAdapter;
    private ListView listView;
    private Handler handler;  //建立handler
    private TextView test;
    private static String inStr = "initial";
    Context txContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        test = findViewById(R.id.textView);

        txContext = this;
        String[] outputData = {""};
        //String[] outputData = {"1", "2", "3"};
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(txContext, android.R.layout.simple_list_item_1, outputData);
        listView.setAdapter(listAdapter);
        handler = new MyHandler();
        Query();
    }
    private void Query() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendEmptyMessage(0); //0: not finish yet
                try {
                    URL url = new URL("http://10.0.2.2/androidhomework/query.php");
                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();//對資料庫打開連結
                    urlConnection.setRequestMethod("POST");
                    urlConnection.connect();//connect database
                    OutputStream os = urlConnection.getOutputStream();
                    os.flush();
                    os.close();
                    InputStream is = urlConnection.getInputStream();//從database 開啟 stream

                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    if(br != null) {
                        inStr = br.readLine();
                        handler.sendEmptyMessage(1); //1: finished
                    }
                } catch(IOException e)
                {
                    System.out.println(e.getMessage());
                }

            }
        }).start();
    }

    class MyHandler extends Handler{
        ArrayList<String> arrayList = new ArrayList<>();
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1) {
                //Parse JsonArray and store in items List<Item>
                try {
                    JSONArray jsonArray = new JSONArray(inStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String tmp = "Name: " + obj.getString("name") +
                                "\nHeight:" + obj.getString("height") +
                                "\nWeight:" + obj.getString("weight") +
                                "\nAge:" + obj.getString("age") +
                                "\nGender:" + obj.getString("gender") +
                                "\nBMI:" + obj.getString("bmi") +
                                "\nBMR:" + obj.getString("bmr");
                        //String tmp = Integer.toString(i);
                        //test.setText(inStr);
                        arrayList.add(tmp);
                    }
                } catch(Exception e){
                    System.err.println("Error: " + e.getMessage());
                }

                //convert ListArray into StringArray
                Object list[] = arrayList.toArray();
                String[] outputData = Arrays.copyOf(list, list.length, String[].class);

                listAdapter = new ArrayAdapter<String>(txContext, android.R.layout.simple_list_item_1, outputData);
                listView.setAdapter(listAdapter);
            }
        }
    }
}















/*class Item implements java.io.Serializable {

    // name, height, weight, age, gender, bmi, bmr
    private String name;
    private String height;
    private String weight;
    private String age;
    private String gender;
    private String bmi;
    private String bmr;

    public Item(String name, String height, String weight, String age, String gender, String bmi, String bmr) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.bmi = bmi;
        this.bmr = bmr;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }
}*/
