package com.example.food_safari;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//21v home activity
public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // productId=getIntent().getStringExtra(pid);

        addToCartBtn=(Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton=(ElegantNumberButton) findViewById(R.id.number_btn);
        productImage=(ImageView) findViewById(R.id.product_image_details);
        productName=(TextView) findViewById(R.id.product_name_details);
        productDescription=(TextView) findViewById(R.id.product_description_details);
        productPrice=(TextView) findViewById(R.id.product_price_details);

        //21v 22v
    }
}

