package com.omprakash.tourocraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText c_password;
    Button register;
    TextView old_account;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String pattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username=findViewById(R.id.R_username);
        password=findViewById(R.id.R_password);
        c_password=findViewById(R.id.con_password);
        old_account=findViewById(R.id.old_account);
        register=findViewById(R.id.register);
        progressDialog=new ProgressDialog(this);     //progress dialog

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();


        old_account.setOnClickListener(new View.OnClickListener() {     //realized that old user
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerActivity.this,MainActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {        //calling new registration function
            @Override
            public void onClick(View v) {
                perform_auth();
            }
        });
    }
    private void perform_auth()                                 //for new registration
    {
        String email = username.getText().toString().trim();
        String pass = password.getText().toString();
        String c_pass = c_password.getText().toString();
        if(!email.matches(pattern))
        {
            username.setError("Enter email in correct format");
        } else if (pass.isEmpty() || pass.length()<6){
            password.setError("password must be greater than 6 digit");
        } else if (!pass.equals(c_pass)){
            c_password.setError("password does not match");
        }else
        {
            progressDialog.setMessage("please wait...");
            progressDialog.setTitle("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(registerActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(registerActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //clear past screen so user don't go back
                        startActivity(intent);
                    }else
                    {
                        progressDialog.dismiss();                                                    //stop the progress dial
                        Toast.makeText(registerActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show(); //show the error ,if any
                    }
                }
            });
        }
    }
}