package com.JunkAway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JunkDriver_HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junk_driver__home_screen);
        getSupportActionBar().hide();
        Intent i = getIntent();
        User user = (User)i.getSerializableExtra("User");
        TextView welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + user.get_fname() + "! Thanks for being a Driver!");
    }
}
