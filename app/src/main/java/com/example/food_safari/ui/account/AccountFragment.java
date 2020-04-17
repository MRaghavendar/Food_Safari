package com.example.food_safari.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.food_safari.NavigationHome;
import com.example.food_safari.R;
import com.example.food_safari.account.ForgotPassword;
import com.example.food_safari.account.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class AccountFragment extends Fragment {

    EditText nameEt;
    TextView pwdTV, saveChangebtn, close_settings_btn;
    EditText emailet;
    EditText phoneET;
    EditText addresset;
    Button Chgpasswordbtn, deleteBTN;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    AwesomeValidation         mAwesomeValidation = new AwesomeValidation(BASIC);



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.activity_account, container, false);

        //initializing the firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //getting the reference to account layout
        nameEt = root.findViewById(R.id.accNameET);
        pwdTV = root.findViewById(R.id.accPasswordET);
        emailet = root.findViewById(R.id.accEmailET);
        phoneET = root.findViewById(R.id.accPhoneET);
        addresset = root.findViewById(R.id.accAddressTV);
        deleteBTN = root.findViewById(R.id.deleteAccount);
        String user_id = firebaseAuth.getCurrentUser().getUid();
        mAwesomeValidation.addValidation(nameEt, getString(R.string.validStr), getString(R.string.err_name));
        String telephone ="^\\(?([0-9]{3})\\)?[-.●]?([0-9]{3})[-.●]?([0-9]{4})$";

        mAwesomeValidation.addValidation(phoneET, telephone, getString(R.string.err_tel));
        mAwesomeValidation.addValidation( emailet, android.util.Patterns.EMAIL_ADDRESS, getString(R.string.err_email));

        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            int len;
            @Override
            public void afterTextChanged(Editable s) {
                if (len > phoneET.getText().length() ){
                    len--;
                    return;
                }
                len = phoneET.getText().length();

                if (len == 4 || len== 8) {
                    String number = phoneET.getText().toString();
                    String dash = number.charAt(number.length() - 1) == '-' ? "" : "-";
                    number = number.substring(0, (len - 1)) + dash + number.substring((len - 1), number.length());
                    phoneET.setText(number);
                    phoneET.setSelection(number.length());
                }
            }
        });


        Chgpasswordbtn = root.findViewById(R.id.ChangePwdBTN);
        Chgpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent b1 = new Intent(getActivity().getApplication(), ForgotPassword.class);
                startActivity(b1);
            }
        });
        close_settings_btn = root.findViewById(R.id.close_settings_btn);
        close_settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(getActivity().getApplication(), NavigationHome.class));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("Deleting this account will completely removes your account and you won't be able to access this app");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity().getApplication(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                Log.d("deleted", "User account deleted.");
                                                Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getActivity().getApplication(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userdata").child(user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                nameEt.setText(name);
                pwdTV.setText(password);
                emailet.setText(email);
                phoneET.setText(phonenumber);
                addresset.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        saveChangebtn = root.findViewById(R.id.saveChangeBTN);
        saveChangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAwesomeValidation.validate()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    System.out.println("entered");
                    nameEt = root.findViewById(R.id.accNameET);
                    pwdTV = root.findViewById(R.id.accPasswordET);
                    emailet = root.findViewById(R.id.accEmailET);
                    phoneET = root.findViewById(R.id.accPhoneET);
                    addresset = root.findViewById(R.id.accAddressTV);
                    final String user_id = firebaseAuth.getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("userdata").child(user_id);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = nameEt.getText().toString();
//                        String password = pwdTV.getText().toString();
                            String email = emailet.getText().toString();
                            String phonenumber = phoneET.getText().toString();
                            String address = addresset.getText().toString();
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                            String path = "/" + dataSnapshot.getKey() + "/" + user_id;
                            HashMap<String, Object> result = new HashMap<>();
                            databaseReference.child("address").setValue(address);
                            databaseReference.child("email").setValue(email);
                            databaseReference.child("name").setValue(name);
                            databaseReference.child("phonenumber").setValue(phonenumber);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            user.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("", "User email address updated.");
                                            }
                                        }
                                    });
                            Toast.makeText(getActivity().getApplication(), "Updated succesfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            } });


        return root;
    }


}
