package com.example.chivu.grand_mint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView register;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Progress bar
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        //register button
        register= findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,register.class);
                startActivity(i);
            }
        });


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login  = findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String emailid = email.getText().toString();
                final String pwd = password.getText().toString();

                //Check whether Email and passwords are correct or not
                if (TextUtils.isEmpty(emailid)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //to login
                auth.signInWithEmailAndPassword(emailid,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Authentication Failed, Check your email id and password.",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            //Check that Email is verified or not
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(MainActivity.this,hello.class));


                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"Login Failed!",Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this,"Please verify the email first!",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });



            }
        });



    }
}
