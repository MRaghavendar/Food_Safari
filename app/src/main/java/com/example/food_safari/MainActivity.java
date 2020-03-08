package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_safari.account.LoginActivity;
import com.example.food_safari.account.SignupActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, signBTN;
    Intent b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = findViewById(R.id.homeLoginBTN);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(b1);
            }
        });


        signBTN = findViewById(R.id.SignUpBTN);

        signBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b2 = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(b2);

            }
        });


    }



}
