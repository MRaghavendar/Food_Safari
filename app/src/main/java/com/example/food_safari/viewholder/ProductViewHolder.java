package com.example.food_safari.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public RecyclerView category_recyclerView;
    public RecyclerView.LayoutManager manager;


    public ProductViewHolder(View itemView) {
        super(itemView);

        manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        imageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.Restaurant_Name);
        txtProductDescription = itemView.findViewById(R.id.Restaurant_Timings);
        txtProductPrice = itemView.findViewById(R.id.Restaurant_desc);

        category_recyclerView = itemView.findViewById(R.id.menuRecycler);
        category_recyclerView.setLayoutManager(manager);

    }

//    public void setItemClickListner(ItemClickListner listner) {
//        this.listner = listner;
//
//    }

//    @Override
//    public void onClick(View view) {
//        listner.onClick(view, getAdapterPosition(), false);
//
//    }
}