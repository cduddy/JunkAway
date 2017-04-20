package com.JunkAway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class NormalUser_HomeScreen extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user__home_screen);
        Intent i = getIntent();
        user = (User)i.getSerializableExtra("User");
        TextView welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + user.get_fname() + "!");
        Button mCreateRequest = (Button) findViewById(R.id.buttonCreateRequest);
        mCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
            }
        });
        Button mViewRequests = (Button) findViewById(R.id.buttonActiveRequests);
        mViewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //createUser();
            }
        });

        Button mCompletedRequests = (Button) findViewById(R.id.buttonCompleteRequests);
        mCompletedRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forgotPassword();
            }
        });
        Switch mDriverModeToggle = (Switch) findViewById(R.id.switchToggleHomeScreen);
        if(user.getJJ())
        {
            mDriverModeToggle.setVisibility(Switch.VISIBLE);
            mDriverModeToggle.setChecked(false);
        }
        mDriverModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalUser_HomeScreen.this,JunkDriver_HomeScreen.class);
                intent.putExtra("User",user);
                startActivity(intent);
                NormalUser_HomeScreen.this.finish();
            }
        });
    }
}
