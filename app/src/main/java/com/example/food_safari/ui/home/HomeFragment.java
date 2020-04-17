package com.example.food_safari.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.Cart;
import com.example.food_safari.R;
import com.example.food_safari.SimpleDisplayActivity;
import com.example.food_safari.interfaces.ItemClickListner;
import com.example.food_safari.model.CategoryTwo;
import com.example.food_safari.model.Products;
import com.example.food_safari.viewholder.CategoryTwoViewHolder;
import com.example.food_safari.viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static RecyclerView.LayoutManager layoutManager;
    public static FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    public static FirebaseRecyclerAdapter<CategoryTwo, CategoryTwoViewHolder> adapter2;
    public static FirebaseRecyclerOptions<CategoryTwo> options2;
    public static ArrayList<CategoryTwo> categoryTwos;
    public static RecyclerView recyclerView;
    FloatingActionButton btn_add_item;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseReference ProductsRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_home, container, false);


        ProductsRef = FirebaseDatabase.getInstance().getReference("Restaurants");
        recyclerView = root.findViewById(R.id.restRecycler);
        recyclerView.setHasFixedSize(true);


        categoryTwos = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        FloatingActionButton fab = root.findViewById(R.id.addCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplication(), Cart.class);
                startActivity(intent);
            }
        });

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();
        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Timings " + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                options2 = new FirebaseRecyclerOptions.Builder<CategoryTwo>()
                        .setQuery(ProductsRef.child(model.getPname()).child("data"), CategoryTwo.class)
                        .build();
                adapter2 = new FirebaseRecyclerAdapter<CategoryTwo, CategoryTwoViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryTwoViewHolder categoryTwoViewHolder, int i, @NonNull final CategoryTwo categoryTwo) {
                        categoryTwoViewHolder.dataId.setText(categoryTwo.getDataId());
                        categoryTwoViewHolder.dataName.setText(categoryTwo.getDataName());
                        categoryTwoViewHolder.dataAge.setText(categoryTwo.getDataAge());
                        categoryTwos.add(categoryTwo);
                        categoryTwoViewHolder.SubCategoryInterfaceClick(new ItemClickListner() {
                            @Override
                            public void onClick(View view, boolean isLongPressed) {
                                Intent intent = new Intent(getActivity().getApplication(), SimpleDisplayActivity.class);
                                intent.putExtra("itemname", categoryTwo.getDataId());
                                intent.putExtra("description", categoryTwo.getDataName());
                                intent.putExtra("price", categoryTwo.getDataAge());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CategoryTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v2 = LayoutInflater.from(getActivity().getBaseContext())
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

        return root;
    }


}