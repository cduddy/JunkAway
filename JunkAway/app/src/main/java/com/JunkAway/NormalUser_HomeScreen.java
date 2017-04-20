package com.JunkAway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NormalUser_HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user__home_screen);
        Intent i = getIntent();
        User user = (User)i.getSerializableExtra("User");
        TextView welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + user.get_fname() + "!");
    }
}
