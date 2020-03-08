package com.example.food_safari.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_safari.R;
import com.example.food_safari.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    EditText nameET;
    EditText pwd;
    EditText phno;
    EditText address;
    EditText email;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        mFirebaseAuth = FirebaseAuth.getInstance();
        nameET = findViewById(R.id.NameET);
        pwd = findViewById(R.id.passwordET);
        phno = findViewById(R.id.phoneET);
        address = findViewById(R.id.EditAddressTV);
        email = findViewById(R.id.EmailET);
        TextView loginBTN =findViewById(R.id.link_signin);
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });

        Button RegisterBTN = findViewById(R.id.RegisterBTN);
        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createUser();
                String emailid = email.getText().toString();
                String passwd = pwd.getText().toString();
                if (emailid.isEmpty()) {
                    email.setError("Enter the email");
                    email.requestFocus();
                } else if (passwd.isEmpty()) {
                    pwd.setError("Enter the password");
                    pwd.requestFocus();
                } else if (emailid.isEmpty() && passwd.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();

                } else if (!(emailid.isEmpty() && passwd.isEmpty())) {
                    Log.d("entered","");
                    mFirebaseAuth.createUserWithEmailAndPassword(emailid, passwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                //getting the values from the edittext to store the values
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    final ProgressDialog csprogress=new ProgressDialog(SignupActivity.this);

                                                    csprogress.setMessage("Loading...");
                                                    csprogress.show();
                                                    new Handler().postDelayed(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            csprogress.dismiss();
//whatever you want just you have to launch overhere.


                                                        }
                                                    }, 1000);
                                                    Log.d("", "Email sent.");
                                                    String name = nameET.getText().toString();
                                                    String password = pwd.getText().toString();
                                                    String emailid = email.getText().toString();
                                                    String phone = phno.getText().toString();
                                                    String addr = address.getText().toString();
                                                    Log.d("users",""+name+password+phone+addr);
                                                    //getting the user-id which is same as current user
                                                    String user_id= mFirebaseAuth.getCurrentUser().getUid();
                                                    //connecting the database reference
                                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("userdata").child(user_id);
                                                    UserData userData = new UserData( name, emailid, password, addr, phone);
                                                    nameET.setText("");
                                                    pwd.setText("");
                                                    email.setText("");
                                                    phno.setText("");
                                                    address.setText("");
                                                    databaseReference.setValue(userData);
                                                    Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                                    Intent b1 = new Intent(SignupActivity.this, LoginActivity.class);
                                                    startActivity(b1);
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }

            }
        });
    }
//
//    public void createUser() {
//        String name = nameET.getText().toString();
//        String password = pwd.getText().toString();
//        String emailid = email.getText().toString();
//        String phone = phno.getText().toString();
//        String addr = address.getText().toString();
//        if (!(emailid.isEmpty() && password.isEmpty())) {
//            String id = databaseReference.push().getKey();
//
//            UserData userData = new UserData(id, name, emailid, password, addr, phone);
//            databaseReference.child(id).setValue(userData);
//            nameET.setText("");
//            pwd.setText("");
//            email.setText("");
//            phno.setText("");
//            address.setText("");
//
//        }else{
//            Toast.makeText(this, "Please type the details", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }


//    public void create(View v){
//PBULIC VOID ADD
//
//        SharedPreferences myData = getSharedPreferences("save",Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed = myData.edit();
//
//        ed.putString("name", (nameET.getText().toString()));
//        ed.putString("phNo",phno.getText().toString());
//        ed.putString("address",address.getText().toString());
//        ed.putString("password",pwd.getText().toString());
//        ed.putString("email",email.getText().toString());
//        ed.commit();
//        Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();
//        Intent b1= new Intent(this,Login_Activity.class);
//        startActivity(b1);
//
//    }

}
