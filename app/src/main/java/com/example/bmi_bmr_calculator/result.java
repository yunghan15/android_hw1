package com.example.bmi_bmr_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class result extends AppCompatActivity {

    private TextView outputBMI;
    private TextView outputBMR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        outputBMI = findViewById(R.id.outputBMI);
        outputBMR = findViewById(R.id.outputBMR);

        Bundle bundle = getIntent().getExtras();

        double bmi = bundle.getDouble("bmi");
        double bmr = bundle.getDouble("bmr");

        outputBMI.setText(Double.toString(bmi));
        outputBMR.setText(Double.toString(bmr));
    }
}
