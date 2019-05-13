package com.example.grilledfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUp extends AppCompatActivity {

    MaterialEditText editName,editPhone,editPassword;
    Button signUpButton;

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

        setContentView(R.layout.activity_sign_up);

        //UI Init
        editName = findViewById(R.id.editName);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        signUpButton = findViewById(R.id.btnSignUp);

        //Firebase init
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("User");


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectionToInternet(getBaseContext())) {
                    mProgressDialog = new ProgressDialog(SignUp.this);
                    mProgressDialog.setMessage("Please Waiting...");
                    mProgressDialog.show();

                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Check if the user is exist or not?
                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                mProgressDialog.dismiss();

                                Toast.makeText(SignUp.this, "Phone Number  is already exist!", Toast.LENGTH_SHORT).show();

                            } else {
                                mProgressDialog.dismiss();

                                User user = new User(editName.getText().toString(), editPassword.getText().toString());
                                mDatabaseReference.child(editPhone.getText().toString()).setValue(user);

                                Toast.makeText(SignUp.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }  else {
                    Toast.makeText(SignUp.this, "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
