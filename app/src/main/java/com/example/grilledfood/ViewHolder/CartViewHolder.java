package com.example.grilledfood.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Interface.ItemClickListener;
import com.example.grilledfood.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        , View.OnCreateContextMenuListener {

    public TextView textCartName, textCartPrice, riceText, riceQuantity, halfText, quarterText, halfQuantity, quarterQuantity;
    public ElegantNumberButton quantityButton;
    public ImageView cartImage;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    public LinearLayout halfLayout, quarterLayout, extraRiceLayout;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        textCartName = itemView.findViewById(R.id.cart_item_name);
        textCartPrice = itemView.findViewById(R.id.cart_item_price);
        quantityButton = itemView.findViewById(R.id.quantity_cart_button);
        cartImage = itemView.findViewById(R.id.cart_image);
        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);
        riceText = itemView.findViewById(R.id.nameOfExtra);
        riceQuantity = itemView.findViewById(R.id.quantityOfExtra);
        halfText = itemView.findViewById(R.id.halfOrder);
        halfQuantity = itemView.findViewById(R.id.halfOrderQuantity);
        quarterText = itemView.findViewById(R.id.quarterOrder);
        quarterQuantity = itemView.findViewById(R.id.quarterOrderQuantity);

        halfLayout = itemView.findViewById(R.id.halfLayout);
        quarterLayout = itemView.findViewById(R.id.quarterLayout);
        extraRiceLayout = itemView.findViewById(R.id.riceExtraLayout);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void setTextCartName(TextView textCartName) {
        this.textCartName = textCartName;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0, 0, getAdapterPosition(), Common.DELETE);
    }
}
