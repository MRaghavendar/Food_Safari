package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    FirebaseRecyclerAdapter<CategoryTwo, CategoryTwoViewHolder> adapter2;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProductsRef = FirebaseDatabase.getInstance().getReference("Restaurants");
        recyclerView = findViewById(R.id.restRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();
        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Timings " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                FirebaseRecyclerOptions<CategoryTwo> options2 = new FirebaseRecyclerOptions.Builder<CategoryTwo>()
                        .setQuery(ProductsRef.child(model.getPname()).child("data"), CategoryTwo.class)
                        .build();
                adapter2 = new FirebaseRecyclerAdapter<CategoryTwo, CategoryTwoViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryTwoViewHolder categoryTwoViewHolder, int i, @NonNull final CategoryTwo categoryTwo) {
                        categoryTwoViewHolder.dataId.setText(categoryTwo.getDataId());
                        categoryTwoViewHolder.dataName.setText(categoryTwo.getDataName());
                        categoryTwoViewHolder.dataAge.setText(categoryTwo.getDataAge());
                        categoryTwoViewHolder.SubCategoryInterfaceClick(new ItemClickListner() {
                            @Override
                            public void onClick(View view, boolean isLongPressed) {
                                Intent intent = new Intent(Home.this, SimpleDisplayActivity.class);
                                intent.putExtra("userId", categoryTwo.getDataId());
                                intent.putExtra("userName", categoryTwo.getDataName());
                                intent.putExtra("userAge", categoryTwo.getDataAge());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CategoryTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v2 = LayoutInflater.from(getBaseContext())
                                .inflate(R.layout.menu_view, parent, false);
                        return new CategoryTwoViewHolder(v2);
                    }
                };
                adapter2.startListening();
                adapter2.notifyDataSetChanged();
                holder.category_recyclerView.setAdapter(adapter2);
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;

            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }

}