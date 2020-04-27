package com.example.food_safari;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.model.CartModel;
import com.example.food_safari.ui.tools.ToolsFragment;
import com.example.food_safari.viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cart extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    String user_id;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;
    private Float overallPrice = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(layoutManager);
        CharSequence mTitle = getTitle();
        mTitle = "Cart";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount = findViewById(R.id.total_price);
        txtTotalAmount.setText("Total price= " + overallPrice);


        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTotalAmount.setText("Total price= " + overallPrice);

                Intent intent = new Intent(Cart.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overallPrice));
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        txtTotalAmount.setText("Total price= " + overallPrice);

        user_id = mFirebaseAuth.getCurrentUser().getUid();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cartlist");


        FirebaseRecyclerOptions<CartModel> options = new FirebaseRecyclerOptions.Builder<CartModel>()
                .setQuery(cartListRef.child(user_id).child("orders")
                        , CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final CartModel model) {
                        holder.txtProductQuantity.setText("Quantity= " + model.getQuantity());
                        holder.txtProductName.setText(model.getItemname());
                        holder.txtProductPrice.setText("Price= " + model.getPrice());

                        Float oneTyprProductTPrice = ((Float.valueOf(model.getPrice()))) * ((Float.valueOf(model.getQuantity())));
                        overallPrice = overallPrice + oneTyprProductTPrice;
                        txtTotalAmount.setText("Total price= " + overallPrice);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence[] options = new CharSequence[]{
                                        "Edit",
                                        "Remove"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                                builder.setTitle("Cart Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            Intent intent = new Intent(Cart.this, SimpleDisplayActivity.class);
                                            intent.putExtra("itemname", model.getItemname());
                                            intent.putExtra("description", model.getDescription());
                                            intent.putExtra("price", model.getPrice());

                                            startActivity(intent);
                                        }
                                        if (i == 1) {
                                            cartListRef.child(user_id)
                                                    .child("orders")
                                                    .child(model.getItemname())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(Cart.this, "Item removed", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(Cart.this, NavigationHome.class);
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });


                                        }

                                    }
                                });
                                builder.show();


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
