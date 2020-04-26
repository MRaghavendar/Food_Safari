package com.example.food_safari.ui.tools;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.R;

public class ToolsViewModel extends RecyclerView.ViewHolder {

    public TextView orderNumber,totalPrice,deliveryType,shippingAddress,time;



    public ToolsViewModel(View itemView) {
        super(itemView);
        deliveryType = itemView.findViewById(R.id.delivery_type);
        orderNumber = itemView.findViewById(R.id.order_number);
        totalPrice = itemView.findViewById(R.id.order_totalprice);
        shippingAddress = itemView.findViewById(R.id.addressOrder);
        time = itemView.findViewById(R.id.OrderTime);

    }
}