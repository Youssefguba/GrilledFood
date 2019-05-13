package com.example.grilledfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignIn extends AppCompatActivity {
    EditText editPhone;
    EditText editPassword;
    Button signInButton;
    CheckBox rememberCheckBox;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    ProgressDialog mProgressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arvo-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_sign_in);

        //EditText Declaration
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);

        //Button Declaration
        signInButton = findViewById(R.id.btnSignIn);
        rememberCheckBox =(CheckBox) findViewById(R.id.checkBox_RememberMe);

        //Init Paper
        Paper.init(this);


        //Firebase init
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("User");

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Common.isConnectionToInternet(getBaseContext())) {

                    //Save User & Password
                    if(rememberCheckBox.isChecked()){
                        Paper.book().write(Common.USER_KEY, editPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, editPassword.getText().toString());

                    }


                    mProgressDialog = new ProgressDialog(SignIn.this);
                    mProgressDialog.setMessage("Please Waiting...");
                    mProgressDialog.show();

                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Check if the User exist in databases or not!
                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                //Get User Information
                                User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                                user.setPhone(editPhone.getText().toString()); // Set Phone


                                if (user.getPassword().equals(editPassword.getText().toString())) {
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.mCurrentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(SignIn.this, "User not exist!", Toast.LENGTH_SHORT).show();

                            }
                            mProgressDialog.dismiss();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignIn.this, "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
