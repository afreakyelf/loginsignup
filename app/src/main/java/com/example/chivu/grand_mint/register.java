package com.example.chivu.grand_mint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class register extends AppCompatActivity {

    EditText name,email,password;
    Button register;
    FirebaseAuth auth;
    LinearLayout l1;
    TextView suc;
    static String emailid,nam;
    String userid;
    ProgressBar progressBar;

    static Firebase mRoofRef;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);


        //Progress Bar
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        l1= findViewById(R.id.l1);
        l1.setVisibility(View.VISIBLE);        //Activity Layout Handling

        auth = FirebaseAuth.getInstance();

        suc = findViewById(R.id.suc);
        suc.setVisibility(View.GONE);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        //To register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                emailid = email.getText().toString().trim();
                final String pwd = password.getText().toString().trim();
                nam= name.getText().toString().trim();

                //to check all the input fields
                if(TextUtils.isEmpty(nam)){
                    Toast.makeText(getApplicationContext(),"Please Enter name!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(emailid)){
                    Toast.makeText(getApplicationContext(),"Please Enter Email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(getApplicationContext(),"Please Enter password!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (pwd.length()<6){
                    Toast.makeText(getApplicationContext(),"Password too short, enter minimum 6 characters!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }



                //to register user
                auth.createUserWithEmailAndPassword(emailid,pwd).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(register.this,"Authentication Failed."+task.getException(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(register.this,"Registered",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                            //Store the details
                            database = FirebaseDatabase.getInstance().getReference();
                            userid = auth.getCurrentUser().getUid();
                            mRoofRef = new Firebase("https://rajat-b182e.firebaseio.com/").child("User_details").child(userid);   //Firebase desination

                            Firebase name = mRoofRef.child("Name");
                            name.setValue(nam);                                        //to store name

                            Firebase email = mRoofRef.child("Email");
                            email.setValue(emailid);                                   //to store name

                            sendemailverification();                                   //to send Email verification
                           }
                    }
                });
            }
        });
    }

    private void sendemailverification() {
        FirebaseAuth.getInstance().getCurrentUser().
                sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    l1.setVisibility(View.GONE);
                    suc.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Verification email sent to "+ emailid ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(register.this,"Email verification failed ! Please try again ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //back to login activity
    public void backtologin(View view){
        startActivity(new Intent(this,MainActivity.class));
    }

}
