package com.omprakash.tourocraft;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA="com.omprakash.tourocraft.name";
    private Button login;
    private EditText username;
    private EditText password,name;
    TextView newaccount;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private TextView skip;
    String pattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String SHARED_PREFS="sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.R_username);
        login=findViewById(R.id.register);
        name=findViewById(R.id.name);
        password=findViewById(R.id.R_password);
        skip=findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_user();
            }
        });
        newaccount=findViewById(R.id.newaccount);

        progressDialog=new ProgressDialog(this);     //progress dialog

        mAuth=FirebaseAuth.getInstance();          //FireBase login
        mUser=mAuth.getCurrentUser();

        checkbox();

        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,registerActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("1234") )
                {
                    Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();      //Developer login
                    openactivity(v);
                }else {
                    user_login();                                                                                      //user login
                }
            }
        });
    }
    public void openactivity(View v)
    {
        Intent intent=new Intent(this,MainActivity2.class);
        String user=name.getText().toString();
        if(user.isEmpty())
        {
            name.setError("Please enter your name");
        }else {
            intent.putExtra(EXTRA, user);
            startActivity(intent);
        }
    }
    private void checkbox()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check=sharedPreferences.getString("logged_in","");
        String user=sharedPreferences.getString("name","");

        if(check.equals("true"))
        {
            Intent intent=new Intent(MainActivity.this,MainActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //clear past screen so user don't go back
            intent.putExtra(EXTRA,user);
            startActivity(intent);
        }
    }
    public void temp_user()
    {
        Intent intent=new Intent(MainActivity.this,MainActivity2.class);
        String user="user";
        intent.putExtra(EXTRA,user);
        startActivity(intent);
    }
    public void user_login()
    {
        String email = username.getText().toString().trim();
        String pass = password.getText().toString();
        if(name.getText().toString().trim().isEmpty())
        {
            name.setError("Please enter your name");
        }
        else if(!email.matches(pattern))
        {
            username.setError("Enter email in correct format");
        } else if (pass.isEmpty() || pass.length()<6){
            password.setError("password must be greater than 6 digit");
        }else {
            progressDialog.setMessage("please wait...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();

                        SharedPreferences  sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("logged_in","true");
                        editor.putString("name",name.getText().toString().trim());
                        editor.apply();

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //clear past screen so user don't go back
                        String user=name.getText().toString();
                        intent.putExtra(EXTRA,user);
                        startActivity(intent);
                    }else
                    {
                        progressDialog.dismiss();                                                    //stop the progress dial
                        Toast.makeText(MainActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show(); //show the error ,if any
                    }
                }
            });
        }
    }
}