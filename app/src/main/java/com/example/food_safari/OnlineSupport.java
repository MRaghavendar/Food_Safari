package com.example.food_safari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.smooch.ui.ConversationActivity;

public class OnlineSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_support);
        ConversationActivity.show(this);

    }
}
