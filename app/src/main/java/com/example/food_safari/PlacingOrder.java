package com.example.food_safari;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PlacingOrder extends AppCompatActivity {

    EditText placeNameET;
    EditText placeAddress1ET;
    EditText placeAddress2ET;
    EditText placePhoneET;

    Button confirmBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placing_order);
        placeNameET = findViewById(R.id.placeNameET);
        placeAddress1ET = findViewById(R.id.placeAddress1ET);
        placeAddress2ET = findViewById(R.id.placeAddress2ET);
        placePhoneET = findViewById(R.id.placePhoneET);

        confirmBTN = findViewById(R.id.confirmBTN);
        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }

            private void check() {
            }
        });
    }
}
