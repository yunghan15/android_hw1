package com.example.bmi_bmr_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class edit extends AppCompatActivity {
    private TextView inputName;
    private TextView inputHeight;
    private TextView inputWeight;
    private TextView inputAge;
    private RadioGroup inputGender;
    private Button btnSubmit;
    private Button btnClear;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //get id
        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");

        //button Submit
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(bntSubmitOnClick);

        //Button Clear
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(bntClearOnClick);

        //get other id
        inputName = findViewById(R.id.inputName);
        inputHeight = findViewById(R.id.inputHeight);
        inputWeight = findViewById(R.id.inputWeight);
        inputAge = findViewById(R.id.inputAge);
        inputGender = findViewById(R.id.radioGender);
    }
    Button.OnClickListener bntSubmitOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            String name = inputName.getText().toString();
            double height = Double.parseDouble(inputHeight.getText().toString());
            double weight = Double.parseDouble(inputWeight.getText().toString());
            int age = Integer.parseInt(inputAge.getText().toString());
            int gender = inputGender.getCheckedRadioButtonId();
            double bmi, bmr = 0.0;

            //calculate BMI
            bmi = weight / Math.pow((height / 100), 2);

            //Calculate BMR
            switch (gender) {
                //Male
                case R.id.radioMale:
                    bmr = 66 + 13.7 * weight + 5.0 * height - 6.8 * (double) age;
                    break;
                //Female
                case R.id.radioFemale:
                    bmr = 655 + 9.6 * weight + 1.8 * height - 4.7 * (double) age;
                    break;
            }

            final String params = "id=" + id
                    + "&name=" + name
                    + "&weight=" + weight
                    + "&height=" + height
                    + "&age=" + age
                    + "&gender=" + ((gender == R.id.radioMale) ? "Male" : "Female")
                    + "&bmi=" + bmi
                    + "&bmr=" + bmr;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        URL url = new URL("http://10.0.2.2/androidhomework/edit.php");
                        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();//對資料庫打開連結
                        urlConnection.setRequestMethod("POST");
                        urlConnection.connect();//connect database

                        OutputStream os = urlConnection.getOutputStream();
                        os.write(params.getBytes());
                        os.flush();
                        os.close();
                        InputStream is = urlConnection.getInputStream();//從database 開啟 stream

                        Intent intent = new Intent();
                        intent.setClass(edit.this, result.class);
                        startActivity(intent);
                    } catch(IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
            }).start();
        }
    };

    Button.OnClickListener bntClearOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            inputName.setText("");
            inputHeight.setText("");
            inputWeight.setText("");
            inputAge.setText("");
        }
    };
}
