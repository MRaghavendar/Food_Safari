package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.example.food_safari.model.OrderOne;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    CardForm cardForm;
    Button btn;
    String totalPrice;
    String time;
    String address;
    String typeOfDelivery;
    String orderNumber;
    String user_id;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cardForm = findViewById(R.id.card);
        btn = findViewById(R.id.btnBuy);


        mFirebaseAuth = FirebaseAuth.getInstance();
        user_id = mFirebaseAuth.getCurrentUser().getUid();


        CharSequence mTitle = getTitle();
        mTitle = "Payment";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(PaymentActivity.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardForm.isValid();

                if (!(cardForm.getCardNumber().length() == 0
                        && cardForm.getCardholderName().length() == 0
                        && cardForm.getPostalCode().length() == 0
                        && cardForm.getCvv().length() == 0
                        && cardForm.getPostalCode().length() == 0)) {

                    Toast.makeText(PaymentActivity.this, "Order placed", Toast.LENGTH_SHORT).show();

                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    time = currentDate.format(calendar.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    String saveCurrentTime = currentTime.format(calendar.getTime());

                    Random rand=new Random();
                    long l=Math.abs(rand.nextLong()+1000000);
                    orderNumber = ""+l;

                    Intent intent = getIntent();
                    totalPrice = intent.getStringExtra("price");
                    address = intent.getStringExtra("addr");
                    typeOfDelivery = intent.getStringExtra("dlvy");

                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("cartlist").child(user_id).child("orders").setValue(null);


                    final HashMap<String, Object> cartMap = new HashMap<String, Object>();
                    cartMap.put("orderNumber", orderNumber);
                    cartMap.put("totalPrice", totalPrice);
                    cartMap.put("typeOfDelivery", typeOfDelivery);
                    cartMap.put("time", time);
                    cartMap.put("address", address);
                    databaseReference.child("orderlist").child(user_id).child("orders").child(orderNumber)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Intent intent1 = new Intent(PaymentActivity.this, NavigationHome.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            }
                        }
                    });
                } else {
                    Toast.makeText(PaymentActivity.this, "Enter the details to continue", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
