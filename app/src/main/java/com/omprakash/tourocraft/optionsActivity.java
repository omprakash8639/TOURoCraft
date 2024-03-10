package com.omprakash.tourocraft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class optionsActivity extends AppCompatActivity {
    public static final String EXTRA1 = "com.omprakash.tourocraft.response";
    ImageView transport;
    ImageView accomodation;
    ImageView famousplaces;
    private String result;
    private String quest;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        progressDialog = new ProgressDialog(this);

        transport = findViewById(R.id.transport);
        accomodation = findViewById(R.id.accomodation);
        famousplaces = findViewById(R.id.famousplace);

        String from = getIntent().getStringExtra("from");
        String destination = getIntent().getStringExtra("destination");
        String date = getIntent().getStringExtra("date");
        String budget = getIntent().getStringExtra("budget");
        String pref=getIntent().getStringExtra("pref");

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject mymodule = py.getModule("script");
        PyObject value = mymodule.get("ai_model");

        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(optionsActivity.this, "Please wait for the response....", Toast.LENGTH_SHORT).show();
                quest ="Suggest best available "+pref+" via " + from + " to " + destination + " on " + date + " within the budget of " + budget + ". Provide detailed information for "+pref+" in a clear and presentable manner. Please use '\n' for new lines and incorporate emojis for both flights and trains.";
                new AIModelTask().execute(value, quest);
            }
        });
        accomodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert budget != null;
                int b=Integer.parseInt(budget);
                b=(40*b)/100;
                String bud=String.valueOf(b);

                Toast.makeText(optionsActivity.this, "Please wait for the response....", Toast.LENGTH_SHORT).show();
                quest ="Suggest me the best accommodation near the"+destination+" either walkable distance or at distance with minimal transportation charges under the "+bud+"(reduce transportation charge("+from+" to "+destination+")) on "+date+".Provide detailed information for accommodation in a clear and presentable manner. Please use '\n' for new lines and incorporate emojis for accommodation.";

                new AIModelTask1().execute(value, quest);
            }
        });
        famousplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(optionsActivity.this, "Please wait for the response....", Toast.LENGTH_SHORT).show();
                quest ="Suggest me the few famous or tourist or dams or reservoir places available near "+destination+" which are in the radius of 100km.Provide detailed information for each palaces in clear and presentable manner. Please use '\n' for new lines and incorporate emojis for famous places (if found no  Famous places around the destination then just return no places around your destination).";

                famous(quest);
            }
        });
    }

    private class AIModelTask extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Generating Information");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            PyObject value = (PyObject) params[0];
            String quest = (String) params[1];
            return value.call(quest).toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Intent intent4 = new Intent(optionsActivity.this, TransportActivity.class);
            intent4.putExtra(EXTRA1, result);
            startActivity(intent4);
        }

    }
    private class AIModelTask1 extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Generating Information");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            PyObject value = (PyObject) params[0];
            String quest = (String) params[1];
            return value.call(quest).toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Intent intent4 = new Intent(optionsActivity.this, Accomodation_Activity.class);
            intent4.putExtra(EXTRA1, result);
            startActivity(intent4);
        }

    }
    public void famous(String quest)
    {
        String api_key="AIzaSyDsXU_iHQ5CAkU3fWuxVuoi9KElpQOEXN8";

        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Generating Information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GenerativeModel gm = new GenerativeModel("gemini-pro",api_key);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder()
                .addText(quest)
                .build();
        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                Intent intent4 = new Intent(optionsActivity.this, famous_Activity.class);
                intent4.putExtra(EXTRA1, resultText);
                progressDialog.dismiss();
                startActivity(intent4);
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, executor);
    }
}