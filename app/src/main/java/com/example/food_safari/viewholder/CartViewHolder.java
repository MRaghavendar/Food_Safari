package com.example.food_safari.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.interfaces.ItemClickListner;
import com.example.food_safari.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner subCategoryOnClickInterface;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById((R.id.cart_product_price));
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        subCategoryOnClickInterface.onClick(v, false);
    }

    public void SubCategoryInterfaceClick(ItemClickListner subCategoryOnClickInterface) {
        this.subCategoryOnClickInterface = subCategoryOnClickInterface;
    }
}