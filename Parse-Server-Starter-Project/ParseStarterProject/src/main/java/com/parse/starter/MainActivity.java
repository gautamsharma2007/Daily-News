/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {


  EditText username;
  EditText password;
  Button mainbutton;
  Button changebutton;

  public void showuserlist(){
    Intent intent=new Intent(getApplicationContext(),MainActivity2.class);
    startActivity(intent);
  }

  public void changethething(View view){
    if(mainbutton.getText().toString().equals("Sign Up")){
      mainbutton.setText("Log in");
      changebutton.setText("or Sign Up");
    }else if(mainbutton.getText().toString().equals("Log in")){
      mainbutton.setText("Sign Up");
      changebutton.setText("or Log in");
    }
  }

  public void dothething(View view){
    if(username.getText()!=null && password.getText()!=null){
      if(mainbutton.getText().toString().equals("Sign Up")){
        ParseUser user=new ParseUser();
        user.setPassword(password.getText().toString());
        user.setUsername(username.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if(e==null){
              showuserlist();
            }else if(e!=null){
              Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }else if (mainbutton.getText().toString().equals("Log in")){
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user!=null && e==null){
              showuserlist();
            }else {
              Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }else {
        Toast.makeText(this, "Please Enter username or password", Toast.LENGTH_SHORT).show();
      }
    }else {
      Toast.makeText(this, "Please Enter username or password", Toast.LENGTH_SHORT).show();
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    username=(EditText) findViewById(R.id.usernametext);
    password=(EditText) findViewById(R.id.passwordext);
    mainbutton=(Button) findViewById(R.id.mainbutton);
    mainbutton=(Button) findViewById(R.id.changebutton);
    if(ParseUser.getCurrentUser()!=null){
      showuserlist();
    }
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}