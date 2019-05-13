package com.example.grilledfood.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grilledfood.Interface.ItemClickListener;
import com.example.grilledfood.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textMenuView;
    public ImageView imageMenuView;
    public ImageView favoriteIcon;
    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        textMenuView = (TextView) itemView.findViewById(R.id.menu_name);
        imageMenuView = itemView.findViewById(R.id.menu_image);
//        favoriteIcon = itemView.findViewById(R.id.fav_icon);
        itemView.setOnClickListener(this);

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
