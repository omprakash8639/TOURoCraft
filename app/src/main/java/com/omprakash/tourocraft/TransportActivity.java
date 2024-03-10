package com.omprakash.tourocraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class TransportActivity extends AppCompatActivity {
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        result=findViewById(R.id.result);
        String response=getIntent().getStringExtra(optionsActivity.EXTRA1);
        result.setText(response.toString().trim());
    }
}