package com.example.food_safari.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.food_safari.R;
import com.example.food_safari.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    EditText nameET;
    EditText pwd;
    EditText phno;
    EditText address;
    EditText email;
    EditText confirmpasswd;
    DatabaseReference databaseReference;
    AwesomeValidation mAwesomeValidation;


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
        confirmpasswd=findViewById(R.id.confirmpasswordET);
        TextView loginBTN = findViewById(R.id.link_signin);



        mAwesomeValidation = new AwesomeValidation(BASIC);

        mAwesomeValidation.addValidation(this, R.id.NameET, "[a-zA-Z\\s]+", R.string.err_name);
        String telephone ="^\\(?([0-9]{3})\\)?[-.●]?([0-9]{3})[-.●]?([0-9]{4})$";

        mAwesomeValidation.addValidation(this, R.id.phoneET, telephone, R.string.err_tel);
        mAwesomeValidation.addValidation(this, R.id.EmailET, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);

        // to validate the confirmation of another field
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        mAwesomeValidation.addValidation(this, R.id.passwordET, regexPassword, R.string.err_password);
// to validate a confirmation field (don't validate any rule other than confirmation on confirmation field)
        mAwesomeValidation.addValidation(this, R.id.confirmpasswordET, R.id.passwordET, R.string.err_password_confirmation);



        phno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            int len;
            @Override
            public void afterTextChanged(Editable s) {
                if (len > phno.getText().length() ){
                    len--;
                    return;
                }
                len = phno.getText().length();

                if (len == 4 || len== 8) {
                    String number = phno.getText().toString();
                    String dash = number.charAt(number.length() - 1) == '-' ? "" : "-";
                    number = number.substring(0, (len - 1)) + dash + number.substring((len - 1), number.length());
                    phno.setText(number);
                    phno.setSelection(number.length());
                }
            }
        });



        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        Button RegisterBTN = findViewById(R.id.RegisterBTN);
        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                createUser();
                if (mAwesomeValidation.validate()) {

                    String emailid = email.getText().toString();
                    String passwd = pwd.getText().toString();
                    mFirebaseAuth.createUserWithEmailAndPassword(emailid, passwd)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser user = auth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    createUser();
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

    public void createUser() {
        final ProgressDialog csprogress = new ProgressDialog(SignupActivity.this);

        csprogress.setMessage("Loading...");
        csprogress.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                csprogress.dismiss();

            }
        }, 1000);
        Log.d("", "Email sent.");
        String name = nameET.getText().toString();
        String password = pwd.getText().toString();
        String emailid = email.getText().toString();
        String phone = phno.getText().toString();
        String addr = address.getText().toString();
        Log.d("users", "" + name + password + phone + addr);
        //getting the user-id which is same as current user
        String user_id = mFirebaseAuth.getCurrentUser().getUid();
        //connecting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userdata").child(user_id);
        UserData userData = new UserData(name, emailid, password, addr, phone);
        nameET.setText("");
        pwd.setText("");
        email.setText("");
        phno.setText("");
        address.setText("");
        databaseReference.setValue(userData);
        Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();

    }

}
