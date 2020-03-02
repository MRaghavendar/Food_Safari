package com.example.food_safari;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEd,phoneEd,addressEd,cityEd;
    private Button confirmOrderbtn;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(ConfirmFinalOrderActivity.this,"Total Price= "+totalAmount,Toast.LENGTH_SHORT).show();

        confirmOrderbtn=(Button) findViewById(R.id.confirm_order_btn);
        nameEd=(EditText) findViewById(R.id.ship_name);
        phoneEd=(EditText) findViewById(R.id.ship_phonenum);
        addressEd=(EditText) findViewById(R.id.ship_address);
        cityEd=(EditText) findViewById(R.id.ship_city);

    }
}
