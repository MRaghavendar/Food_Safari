package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SimpleDisplayActivity extends AppCompatActivity {

    TextView textView_id, textView_name, textView_age;
    String tempUserName, tempUserId, tempUserAge;
    Button pd_add_to_cart_button;
    DatabaseReference databaseReference;
    FirebaseAuth mFirebaseAuth;
    private ElegantNumberButton numberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_display);

        Intent intent = getIntent();
        tempUserId = intent.getStringExtra("itemname");
        tempUserName = intent.getStringExtra("description");
        tempUserAge = intent.getStringExtra("price");

        mFirebaseAuth = FirebaseAuth.getInstance();

        databaseReference =  FirebaseDatabase.getInstance().getReference();
        CharSequence mTitle = getTitle();
        mTitle = "Edit cart";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //view init
        textView_id = findViewById(R.id.main_text_id);
        textView_name = findViewById(R.id.main_text_name);
        textView_age = findViewById(R.id.main_text_age);
        pd_add_to_cart_button = findViewById(R.id.pd_add_to_cart_button);
        numberButton = findViewById(R.id.number_btn);

        pd_add_to_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();

            }
        });

        if (!tempUserAge.isEmpty() && !tempUserId.isEmpty() && !tempUserName.isEmpty()) {
            textView_id.setText(tempUserId);
            textView_name.setText(tempUserName);
            textView_age.setText(tempUserAge);
        }

    }

    private void addingToCartList() {
//        String saveCurrentTime, saveCurrentDate;
//
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS a");
//        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<String, Object>();
        cartMap.put("itemname", tempUserId);
        cartMap.put("description", tempUserName);
        cartMap.put("price", tempUserAge);
//        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());

        String user_id = mFirebaseAuth.getCurrentUser().getUid();
        //connecting the database reference
      databaseReference.child("cartlist").child(user_id).child("orders").child(tempUserId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(new Intent(SimpleDisplayActivity.this, NavigationHome.class));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }
                });


    }
}