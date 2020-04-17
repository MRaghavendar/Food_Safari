package com.example.food_safari.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.food_safari.R;

import io.smooch.ui.ConversationActivity;

public class ChatFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button mHelpBtn = rootView.findViewById(R.id.button_help);
        mHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Showing Smooch on the button click
                ConversationActivity.show(getActivity(), Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
        return rootView;
    }


}