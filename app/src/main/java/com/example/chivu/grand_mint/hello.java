package com.example.chivu.grand_mint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.chivu.grand_mint.register.mRoofRef;

public class hello extends AppCompatActivity {

    TextView text,signout;
    String userid;
    FirebaseAuth auth;
    Firebase mref;
    DatabaseReference database;
    ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        text = findViewById(R.id.text);
        Firebase.setAndroidContext(this);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //Authstate Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(hello.this, MainActivity.class));
                    finish();
                }
            }
        };

        signout = findViewById(R.id.signout);
        //Progress Bar
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        userid = auth.getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance().getReference();
        //to retrieve the saved data
        mref = new Firebase("https://rajat-b182e.firebaseio.com/").child("User_details");
        mref.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(userid).child("Name").getValue(String.class);
                progressBar.setVisibility(View.GONE);
                text.setText("Hello, "+name+"!");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Sign out
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });


    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

}
