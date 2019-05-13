package com.example.grilledfood;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Database.Database;
import com.example.grilledfood.Helper.LocaleHelper;
import com.example.grilledfood.Helper.RecyclerItemTouchHelper;
import com.example.grilledfood.Interface.RecyclerItemTouchHelperListener;
import com.example.grilledfood.Model.MyResponse;
import com.example.grilledfood.Model.Notification;
import com.example.grilledfood.Model.Order;
import com.example.grilledfood.Model.Request;
import com.example.grilledfood.Model.Sender;
import com.example.grilledfood.Model.Token;
import com.example.grilledfood.Remote.APIService;
import com.example.grilledfood.ViewHolder.CartAdapter;
import com.example.grilledfood.ViewHolder.CartViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.spark.submitbutton.SubmitButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static String TAG = "Cart";
    public TextView textTotalPrice;
    public TextView taxesNumber;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ActionBar actionBar;
    FirebaseDatabase database;
    DatabaseReference requestOrder;
    SubmitButton placeOrder;
    List<Order> cartList = new ArrayList<>();
    CartAdapter cartAdapter;

    String address, comment;

    LinearLayout rootLayout;

    APIService apiService;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arvo-Regular.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_cart);

        //Init Service
        apiService = Common.getFCMService();

        rootLayout = findViewById(R.id.root_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cart Items");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        database = FirebaseDatabase.getInstance();
        requestOrder = database.getReference("Requests");

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        textTotalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrderButton);
        taxesNumber = findViewById(R.id.tax_total);


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartList.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your Cart is Empty!!", Toast.LENGTH_SHORT).show();
            }
        });

        loadListOfFood();

        int deliveryFees = 9;
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));

        } else {
            taxesNumber.setText(numberFormat.format(deliveryFees));
        }
    }


    private void loadListOfFood() {
        cartList = new Database(this).getCarts(Common.mCurrentUser.getPhone());
        cartAdapter = new CartAdapter(cartList, this);
        recyclerView.setAdapter(cartAdapter);

        //Calculate total price
        int totalPrice;
        int foodPrice = 0;
        int extraPrice = 0;
        int halfPrice = 0;
        int quarterPrice = 0;

        for (Order item : cartList) {
            try {
                foodPrice += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                halfPrice += (Integer.parseInt(item.getHalfOrderPrice())) * (Integer.parseInt(item.getHalfOrderQuantity()));
                quarterPrice += (Integer.parseInt(item.getQuarterOrderPrice())) * (Integer.parseInt(item.getQuarterOrderQuantity()));
                extraPrice += (Integer.parseInt(item.getExtraPrice())) * (Integer.parseInt(item.getExtraQuantity()));


            } catch (NumberFormatException e) {
                Log.e(TAG, "There is an Error ");
            }

            totalPrice = foodPrice + extraPrice + halfPrice + quarterPrice;
            Locale locale = new Locale("en", "US");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            textTotalPrice.setText(numberFormat.format(totalPrice));
        }

        cartAdapter.notifyDataSetChanged();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setTitle("One More Step...");
        builder.setMessage("Enter Your Address");


        LayoutInflater inflater = this.getLayoutInflater();
        View orderAdressComment = inflater.inflate(R.layout.order_address_comment, null);

        final MaterialEditText editAddress = orderAdressComment.findViewById(R.id.adressEditText);
        final MaterialEditText editComment = orderAdressComment.findViewById(R.id.commentEditText);
        final RadioButton homeAddress = orderAdressComment.findViewById(R.id.radioBtnShipToHome);

        homeAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Common.mCurrentUser.getHomeAddress() != null || !TextUtils.isEmpty(Common.mCurrentUser.getHomeAddress())) {
                        address = Common.mCurrentUser.getHomeAddress();
                        editAddress.setText(address);

                    } else {
                        Toast.makeText(Cart.this, "Please Update your address", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        builder.setView(orderAdressComment);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.mCurrentUser.getName(),
                        Common.mCurrentUser.getPhone(),
                        textTotalPrice.getText().toString(),
                        editAddress.getText().toString(),
                        cartList,
                        "0",
                        editComment.getText().toString(),
                        Common.mCurrentUser.getGlobalAddress()
                );

                //Submit to Firebase
                String order_number = String.valueOf(System.currentTimeMillis());
                requestOrder.child(order_number).setValue(request);

                sendNotificationOrder(order_number);

                // Delete Cart
                new Database(getBaseContext()).cleanCart(Common.mCurrentUser.getPhone());

                Toast.makeText(Cart.this, "Your Order is placed, Thank you :)", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void sendNotificationOrder(String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("serverToken").equalTo(true); // Get all node with isServerToken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapShot.getValue(Token.class);

                    //Create raw payload to send
                    Notification notification = new Notification("Mohsen Restaurant", "You have new Order" + order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    apiService.sendNotification(content).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                    Toast.makeText(Cart.this, "Order was Updated", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(Cart.this, "Order was updated but failed to send notification!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());
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
    protected void onStart() {
        super.onStart();
        int deliveryFees = 9;
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));

        } else {
            taxesNumber.setText(numberFormat.format(deliveryFees));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        int deliveryFees = 9;
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));

        } else {
            taxesNumber.setText(numberFormat.format(deliveryFees));
        }
    }


    private void deleteCart(int position) {
        //We will remove item at List<Order> by position.
        cartList.remove(position);
        //After that we will delete all data from Database SQLite
        new Database(this).cleanCart(Common.mCurrentUser.getPhone());
        //And Final, we will update a new data from List<Order> to SQLite
        for (Order item : cartList) {
            new Database(this).addToCart(item);
            //Refresh
            loadListOfFood();
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            int deleteIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(), Common.mCurrentUser.getPhone());

            //Update total text
            //Calculate total price

            List<Order> orders = new Database(getBaseContext()).getCarts(Common.mCurrentUser.getPhone());
            int totalPrice;
            int foodPrice = 0;
            int extraPrice = 0;
            int halfPrice = 0;
            int quarterPrice = 0;

            for (Order item : orders)
                try {
                    foodPrice += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                    halfPrice += (Integer.parseInt(item.getHalfOrderPrice())) * (Integer.parseInt(item.getHalfOrderQuantity()));
                    quarterPrice += (Integer.parseInt(item.getQuarterOrderPrice())) * (Integer.parseInt(item.getQuarterOrderQuantity()));
                    extraPrice += (Integer.parseInt(item.getExtraPrice())) * (Integer.parseInt(item.getExtraQuantity()));


                } catch (NumberFormatException e) {
                    Log.e(TAG, "There is an Error ");
                }

            totalPrice = foodPrice + extraPrice + halfPrice + quarterPrice;
            Locale locale = new Locale("en", "US");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            textTotalPrice.setText(numberFormat.format(totalPrice));


            Snackbar snackbar = Snackbar.make(rootLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    //Update total text
                    //Calculate total price
                    List<Order> orders = new Database(getBaseContext()).getCarts(Common.mCurrentUser.getPhone());
                    int totalPrice;
                    int foodPrice = 0;
                    int extraPrice = 0;
                    int halfPrice = 0;
                    int quarterPrice = 0;
                    for (Order item : orders) {

                        try {
                            foodPrice += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                            halfPrice += (Integer.parseInt(item.getHalfOrderPrice())) * (Integer.parseInt(item.getHalfOrderQuantity()));
                            quarterPrice += (Integer.parseInt(item.getQuarterOrderPrice())) * (Integer.parseInt(item.getQuarterOrderQuantity()));
                            extraPrice += (Integer.parseInt(item.getExtraPrice())) * (Integer.parseInt(item.getExtraQuantity()));


                        } catch (NumberFormatException e) {
                            Log.e(TAG, "There is an Error ");
                        }

                        totalPrice = foodPrice + extraPrice + halfPrice + quarterPrice;
                        Locale locale = new Locale("en", "US");
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                        textTotalPrice.setText(numberFormat.format(totalPrice));

                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

}

