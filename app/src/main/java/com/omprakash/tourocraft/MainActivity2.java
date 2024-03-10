package com.omprakash.tourocraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    TextView name;
    TextView greeting;
    AutoCompleteTextView from_id;
    AutoCompleteTextView to_id;
    CalendarView calendarView;
    Spinner preferenceid;
    EditText budget_id;
    Button submit;
    String date;
    public static final String SHARED_PREFS="sharedPrefs";
    final String[] trans_mode = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=getIntent();
        String n = intent.getStringExtra(MainActivity.EXTRA);  //for name

        Resources res = getResources();
        String[] dest = res.getStringArray(R.array.destination_array);// auto reccom string-array
        String[] dropdown=res.getStringArray(R.array.preffered_transport);

        name=findViewById(R.id.name);
        to_id=findViewById(R.id.to_id);
        from_id=findViewById(R.id.from_id);
        budget_id=findViewById(R.id.budget_id);
        submit=findViewById(R.id.submit);
        greeting=findViewById(R.id.greeting);
        calendarView=findViewById(R.id.calendarView);
        preferenceid=findViewById(R.id.transport_pref);

        ArrayAdapter<String> preference_trans=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dropdown);
        preferenceid.setAdapter(preference_trans);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,dest);
        to_id.setAdapter(arrayAdapter);
        from_id.setAdapter(arrayAdapter);

        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String from_ =sharedPreferences.getString("from_","");
        String to_ =sharedPreferences.getString("to_","");
        String budget_ =sharedPreferences.getString("budget_","");
        if(!from_.isEmpty())
        {
            from_id.setText(from_);
        }
        if(!to_.isEmpty())
        {
            to_id.setText(to_);
        }
        if(!budget_.isEmpty())
        {
            budget_id.setText(budget_);
        }

        //set min date
        Calendar calendar = Calendar.getInstance();
        long currentDateMillis = calendar.getTimeInMillis();
        calendarView.setMinDate(currentDateMillis);

        //set max date
        calendar.add(Calendar.MONTH, 6);
        long maxDateMillis = calendar.getTimeInMillis();
        calendarView.setMaxDate(maxDateMillis);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date=dayOfMonth+"/"+(month+1)+"/"+year;
            }
        });

        greeting.setText("Hello!");
        name.setText(n);

        preferenceid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedText = parentView.getItemAtPosition(position).toString();
                trans_mode[0] =selectedText;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if you don't need to handle nothing selected case
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                String from=(from_id.getText().toString().trim());
                String destination=(to_id.getText().toString().trim());
                String budget=(budget_id.getText().toString().trim());
                    int ch = Integer.valueOf(budget);
                    if (ch < 500) {
                        budget_id.setError("Amount cannot be less than 500!");
                    } else {
                        Intent intent1=new Intent(MainActivity2.this,optionsActivity.class);
                        intent1.putExtra("from", from);
                        intent1.putExtra("destination", destination);
                        intent1.putExtra("date", date);
                        intent1.putExtra("budget", budget);
                        intent1.putExtra("pref", trans_mode[0]);
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("from_", from);
                        editor.putString("to_", destination);
                        editor.putString("budget_", budget);
                        editor.apply();
                        startActivity(intent1);
                    }

            }
        });

    }
}