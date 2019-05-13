package com.example.grilledfood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Model.User;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 7171;
    Button continueButton;
    TextView sloganText;
    ProgressDialog mProgressDialog;

    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountKit.initialize(this);
        setContentView(R.layout.activity_main);
        printKeyHash();
        //Button declaration
        continueButton = findViewById(R.id.btnContinue);


        //Text declaration
        //Paper Init
        Paper.init(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");


        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginSystem();
            }
        });

        if (Common.isConnectionToInternet(this)) {
            final AlertDialog facebookDialog = new SpotsDialog(this);

            //Check session Facebook account kit
            if (AccountKit.getCurrentAccessToken() != null) {
                // Create Dialog
                facebookDialog.show();
                facebookDialog.setMessage("Please Wait...");
                facebookDialog.setCancelable(false);

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        //Login
                        users.child(account.getPhoneNumber().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User localUser = dataSnapshot.getValue(User.class);
                                        Intent homeIntent = new Intent(MainActivity.this, FirstPage.class);
                                        Common.mCurrentUser = localUser;
                                        startActivity(homeIntent);
                                        facebookDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {

                    }
                });
            }
        } else {
            Toast.makeText(this, "Check Your Internet connection!", Toast.LENGTH_SHORT).show();
            final AlertDialog facebookDialog = new SpotsDialog(this);
            facebookDialog.dismiss();
        }

    }

    private void startLoginSystem() {
        Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder kitConfigurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, kitConfigurationBuilder.build());

        startActivityForResult(intent, REQUEST_CODE);
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.orderfood", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

//    private void login(String phone, String password) {
//        //Firebase init
//       final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//       final DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference("User");
//
//        if (Common.isConnectionToInternet(getBaseContext())) {
//
//            mProgressDialog = new ProgressDialog(MainActivity.this);
//            mProgressDialog.setMessage("Please Waiting...");
//            mProgressDialog.show();
//
//            mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    //Check if the User exist in databases or not!
//                    if (dataSnapshot.child(phone).exists()) {
//                        //Get User Information
//                        User user = dataSnapshot.child(phone).getValue(User.class);
//                        user.setPhone(phone); // Set Phone
//
//
//                        if (user.getPassword().equals(password)) {
//                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
//                            Common.mCurrentUser = user;
//                            startActivity(homeIntent);
//                            finish();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        Toast.makeText(MainActivity.this, "User not exist!", Toast.LENGTH_SHORT).show();
//
//                    }
//                    mProgressDialog.dismiss();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            Toast.makeText(MainActivity.this, "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                Toast.makeText(this, "" + loginResult.getError(), Toast.LENGTH_SHORT).show();
            } else if (loginResult.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            } else {
                if (loginResult.getAccessToken() != null) {
                    //Show Dialog
                    final AlertDialog alertDialog = new SpotsDialog(this);
                    alertDialog.show();
                    alertDialog.setMessage("Please Wait...");
                    alertDialog.setCancelable(false);

                    //Get Current Phone
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            String userPhone = account.getPhoneNumber().toString();

                            //Check if the user is in Firebase database or not ..
                            users.orderByKey().equalTo(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.child(userPhone).exists()) {

                                        User newUser = new User();     // If  User not to exist
                                        newUser.setPhone(userPhone);
                                        newUser.setName("");

                                        //Add to firebase
                                        users.child(userPhone)
                                                .setValue(newUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MainActivity.this, "User Register Successfully!", Toast.LENGTH_SHORT).show();

                                                            //Login
                                                            users.child(userPhone)
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            User localUser = dataSnapshot.getValue(User.class);
                                                                            Intent homeIntent = new Intent(MainActivity.this, FirstPage.class);
                                                                            Common.mCurrentUser = localUser;
                                                                            startActivity(homeIntent);
                                                                            alertDialog.dismiss();
                                                                            finish();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    } else { // If Exists
                                        //Login
                                        users.child(userPhone)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        User localUser = dataSnapshot.getValue(User.class);
                                                        Intent homeIntent = new Intent(MainActivity.this, FirstPage.class);
                                                        Common.mCurrentUser = localUser;
                                                        startActivity(homeIntent);
                                                        alertDialog.dismiss();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Toast.makeText(MainActivity.this, "" + accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }
}
