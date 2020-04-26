package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
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
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shipment_mode, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        deliveryType = spinner.getSelectedItem().toString();
        address = nameEd.getText().toString() + "\n" +
                addressEd.getText().toString() + "\nCity " +
                cityEd.getText().toString() + "\nPhone number " + phoneEd.getText().toString();
        confirmOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentActivity.class);
                intent.putExtra("addr", address);
                intent.putExtra("price", totalAmount);
                intent.putExtra("dlvy", deliveryType);
                startActivity(intent);
            }
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

