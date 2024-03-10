package com.omprakash.tourocraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Accomodation_Activity extends AppCompatActivity {
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation);

        result=findViewById(R.id.result);
        String response=getIntent().getStringExtra(optionsActivity.EXTRA1);
        result.setText(response);
    }
}