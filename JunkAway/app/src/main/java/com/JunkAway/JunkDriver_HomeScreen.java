//written by Collin Duddy
//tested by Collin Duddy
//Debugged by all members


package com.JunkAway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class JunkDriver_HomeScreen extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junk_driver__home_screen);
        getSupportActionBar().hide();
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");
        TextView welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + user.get_fname() + "!\n Driver Mode");
        Button mOpenJobs = (Button) findViewById(R.id.buttonOpenJobs);
        mOpenJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JunkDriver_HomeScreen.this,Open_Jobs.class);
                intent.putExtra("User",user);
                startActivity(intent);
            }
        });
        Button mAcceptedJobs = (Button) findViewById(R.id.buttonAcceptedJobs);
        mAcceptedJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JunkDriver_HomeScreen.this,Current_Jobs.class);
                intent.putExtra("User",user);
                startActivity(intent);
            }
        });
        getSupportActionBar().hide();
        Button mCompletedJobs = (Button) findViewById(R.id.buttonCompletedJobs);
        mCompletedJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JunkDriver_HomeScreen.this,Completed_Jobs.class);
                intent.putExtra("User",user);
                startActivity(intent);
            }
        });
        Switch mDriverModeToggle = (Switch) findViewById(R.id.switchToggleHomeScreen);
        mDriverModeToggle.setVisibility(Switch.VISIBLE);
        mDriverModeToggle.setChecked(true);
        mDriverModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JunkDriver_HomeScreen.this,NormalUser_HomeScreen.class);
                intent.putExtra("User",user);
                startActivity(intent);
                JunkDriver_HomeScreen.this.finish();
            }
        });

    }
}
