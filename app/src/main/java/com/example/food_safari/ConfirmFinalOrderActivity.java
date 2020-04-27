package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText nameEd, phoneEd, addressEd, cityEd;
    private Button confirmOrderbtn;
    private String totalAmount = "", address;
    private Spinner spinner;
    private String deliveryType;

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


        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(ConfirmFinalOrderActivity.this, "Total Price= " + totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrderbtn = findViewById(R.id.confirm_order_btn);
        nameEd = findViewById(R.id.order_nameET);
        phoneEd = findViewById(R.id.order_PhoneET);
        addressEd = findViewById(R.id.order_AddressET);
        cityEd = findViewById(R.id.order_CityET);
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shipment_mode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

            deliveryType = spinner.getSelectedItem().toString();
            confirmOrderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nameEd.getText().toString();
                    String addrs = addressEd.getText().toString();
                    String City = cityEd.getText().toString();
                    String phn = phoneEd.getText().toString();
                    address = name + "\n" + addrs + "\nCity " + City + "\nPhone number " + phn;
                    System.out.println("address"+address);
                    Log.d("addresspradeep",address);
                    if (name.length() == 0 && addrs.length() == 0 && City.length() == 0 && phn.length() == 0) {
                        Toast.makeText(ConfirmFinalOrderActivity.this, "Enter All Details to continue", Toast.LENGTH_SHORT).show();
                    } else {
                    Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentActivity.class);
                    intent.putExtra("addr", address);
                    intent.putExtra("price", totalAmount);
                    intent.putExtra("dlvy", deliveryType);
                    startActivity(intent);
                }}
            });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        deliveryType = spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // Toast.makeText(ConfirmFinalOrderActivity.this,"Please select Item",Toast.LENGTH_LONG).show();

    }
}

