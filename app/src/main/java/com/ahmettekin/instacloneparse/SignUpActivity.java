package com.ahmettekin.instacloneparse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.LogInCallback;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText passwordText, nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordText = findViewById(R.id.passwordText);
        nameText = findViewById(R.id.nameText);

        if(ParseUser.getCurrentUser()!= null){

            Intent intent = new Intent(getApplicationContext(),FeedActivity.class);

            startActivity(intent);


        }



    }
    public  void SignIn (View view){
        ParseUser.logInInBackground(nameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(e!= null){
                    e.printStackTrace();
                }
                else{
                    Toast.makeText(getApplicationContext(),"welcome "+user.getUsername(),Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignUpActivity.this,FeedActivity.class);


                    startActivity(intent);


                }
            }
        });


    }

    public void SignUp (View view){

        ParseUser user = new ParseUser();

        user.setUsername(nameText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "user signed up", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignUpActivity.this, FeedActivity.class);
                    startActivity(intent);

                }
            }
        });


    }
}