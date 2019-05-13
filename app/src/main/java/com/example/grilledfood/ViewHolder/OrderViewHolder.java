package com.example.grilledfood.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.grilledfood.Interface.ItemClickListener;
import com.example.grilledfood.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textOrderId, textOrderPhone, textOrderStatus, textOrderAddress;
    private ItemClickListener itemClickListener;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        textOrderId = itemView.findViewById(R.id.orderId);
        textOrderAddress = itemView.findViewById(R.id.orderAddress);
        textOrderPhone = itemView.findViewById(R.id.orderPhone);
        textOrderStatus = itemView.findViewById(R.id.orderStatus);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
