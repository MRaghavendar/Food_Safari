package com.example.food_safari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    Button accountBTN;
    Button restaurantsBTN;
    Button cartBTN;
    Button ordersBTN, aboutBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        accountBTN = findViewById(R.id.accountBTN);
        restaurantsBTN = findViewById(R.id.restaurantsBTN);
        cartBTN = findViewById(R.id.cartBTN);
        ordersBTN = findViewById(R.id.ordersBTN);
        aboutBTN=findViewById(R.id.aboutBTN);


        accountBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this,Account.class));
            }
        });

        aboutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this,AboutUs.class));
            }
        });


        cartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this,Cart.class));
            }
        });

        restaurantsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this,Home.class));
            }
        });

    }
}
