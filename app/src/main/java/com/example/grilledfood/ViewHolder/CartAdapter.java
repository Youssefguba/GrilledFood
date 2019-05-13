package com.example.grilledfood.ViewHolder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.grilledfood.Cart;
import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Database.Database;
import com.example.grilledfood.Model.Food;
import com.example.grilledfood.Model.Order;
import com.example.grilledfood.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private static String TAG = "CartAdapter";
    private List<Order> orderList = new ArrayList<>();
    private Cart cart;
    private Food currentFood = new Food();


    public CartAdapter(List<Order> orderList, Cart cart) {
        this.orderList = orderList;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(cart);
        View itemView = layoutInflater.inflate(R.layout.cart_layout,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int position) {

        Picasso.with(cart.getBaseContext()).load(orderList.get(position).getImage())
                .resize(70, 70)
                .centerCrop()
                .into(cartViewHolder.cartImage);

        cartViewHolder.quantityButton.setNumber(orderList.get(position).getQuantity());
        cartViewHolder.riceQuantity.setText(orderList.get(position).getExtraQuantity());
        cartViewHolder.halfText.setText(orderList.get(position).getHalfOrderName());
        cartViewHolder.halfQuantity.setText(orderList.get(position).getHalfOrderQuantity());
        cartViewHolder.quarterText.setText(orderList.get(position).getQuarterOrderName());
        cartViewHolder.quarterQuantity.setText(orderList.get(position).getQuarterOrderQuantity());

        if (orderList.get(position).getQuarterOrderQuantity().equals("null") && orderList.get(position).getQuarterOrderName().equals("null") &&
                orderList.get(position).getHalfOrderQuantity().equals("null") && orderList.get(position).getHalfOrderName().equals("null")) {
            cartViewHolder.halfLayout.setVisibility(View.GONE);
            cartViewHolder.quarterLayout.setVisibility(View.GONE);

        } else if (orderList.get(position).getQuarterOrderQuantity().equals("0") && orderList.get(position).getHalfOrderQuantity().equals("0")) {
            cartViewHolder.quarterLayout.setVisibility(View.GONE);
            cartViewHolder.halfLayout.setVisibility(View.GONE);

        } else if (!orderList.get(position).getHalfOrderQuantity().equals("0") && orderList.get(position).getQuarterOrderQuantity().equals("0")) {
            cartViewHolder.halfLayout.setVisibility(View.VISIBLE);
            cartViewHolder.quarterLayout.setVisibility(View.GONE);

        } else if (orderList.get(position).getHalfOrderQuantity().equals("0") && !orderList.get(position).getQuarterOrderQuantity().equals("0")) {
            cartViewHolder.halfLayout.setVisibility(View.GONE);
            cartViewHolder.quarterLayout.setVisibility(View.VISIBLE);

        } else {
            cartViewHolder.halfLayout.setVisibility(View.VISIBLE);
            cartViewHolder.quarterLayout.setVisibility(View.VISIBLE);
        }

        if (orderList.get(position).getExtraQuantity().equals("0")) {
            cartViewHolder.extraRiceLayout.setVisibility(View.GONE);

        } else {
            cartViewHolder.extraRiceLayout.setVisibility(View.VISIBLE);
        }

        cartViewHolder.quantityButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = orderList.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //Update total text
                //Calculate total price
                List<Order> orders = new Database(cart).getCarts(Common.mCurrentUser.getPhone());
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

                    cart.textTotalPrice.setText(numberFormat.format(totalPrice));
                }

                Locale locale = new Locale("en", "US");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                int price = (Integer.parseInt(orderList.get(position).getPrice())) * (Integer.parseInt(orderList.get(position).getQuantity()));

                cartViewHolder.textCartPrice.setText(numberFormat.format(price));
                cartViewHolder.textCartName.setText(orderList.get(position).getProductName());
            }
        });


        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        int price = (Integer.parseInt(orderList.get(position).getPrice())) * (Integer.parseInt(orderList.get(position).getQuantity()));
        cartViewHolder.textCartPrice.setText(numberFormat.format(price));
        cartViewHolder.textCartName.setText(orderList.get(position).getProductName());

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public Order getItem(int position) {
        return orderList.get(position);
    }

    public void removeItem(int position) {
        orderList.remove(position);
        notifyItemRemoved(position);

    }

    public void restoreItem(Order item, int position) {
        orderList.add(position, item);
        notifyItemInserted(position);

    }
}

