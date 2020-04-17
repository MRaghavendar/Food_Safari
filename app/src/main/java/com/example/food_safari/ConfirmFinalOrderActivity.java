package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEd,phoneEd,addressEd,cityEd;
    private Button confirmOrderbtn;
    private String totalAmount="";
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        CharSequence mTitle = getTitle();
        mTitle = "Order";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(ConfirmFinalOrderActivity.this,"Total Price= "+totalAmount,Toast.LENGTH_SHORT).show();

        confirmOrderbtn= findViewById(R.id.confirm_order_btn);
        nameEd= findViewById(R.id.order_nameET);
        phoneEd= findViewById(R.id.order_PhoneET);
        addressEd= findViewById(R.id.order_AddressET);
        cityEd= findViewById(R.id.order_CityET);
        spinner =findViewById(R.id.spinner);
        confirmOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmFinalOrderActivity.this,PaymentActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

