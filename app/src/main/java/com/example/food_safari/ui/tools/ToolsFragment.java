package com.example.food_safari.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.R;
import com.example.food_safari.model.OrderOne;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ToolsFragment extends Fragment {

    FirebaseAuth mFirebaseAuth;
    String user_id;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_details, container, false);
        recyclerView = root.findViewById(R.id.orderRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mFirebaseAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(layoutManager);



        user_id = mFirebaseAuth.getCurrentUser().getUid();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("orderlist");


        FirebaseRecyclerOptions<OrderOne> options = new FirebaseRecyclerOptions.Builder<OrderOne>()
                .setQuery(cartListRef.child(user_id).child("orders")
                        , OrderOne.class).build();

        FirebaseRecyclerAdapter<OrderOne, ToolsViewModel> adapter =
               new FirebaseRecyclerAdapter<OrderOne, ToolsViewModel>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull ToolsViewModel toolsViewModel, int i, @NonNull OrderOne orderOne) {
                       toolsViewModel.totalPrice.setText("Price: " + orderOne.getTotalPrice());
                       toolsViewModel.time.setText("Order placed on "+orderOne.getTime());
                       toolsViewModel.shippingAddress.setText("Address: "+ orderOne.getAddress());
                       toolsViewModel.orderNumber.setText("Order number:"+orderOne.getOrderNumber());
                       toolsViewModel.deliveryType.setText("type of delivery: "+orderOne.getTypeOfDelivery());
                   }

                   @NonNull
                   @Override
                   public ToolsViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_model,parent,false);
                       return new ToolsViewModel(view);
                   }
               };
        recyclerView.setAdapter(adapter);
        adapter.startListening();




        return root;
    }
}