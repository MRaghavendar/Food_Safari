package com.example.food_safari;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.account.Account;
import com.example.food_safari.account.LoginActivity;
import com.example.food_safari.interfaces.ItemClickListner;
import com.example.food_safari.model.CategoryTwo;
import com.example.food_safari.model.Products;
import com.example.food_safari.viewholder.CategoryTwoViewHolder;
import com.example.food_safari.viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    FirebaseRecyclerAdapter<CategoryTwo, CategoryTwoViewHolder> adapter2;
    FloatingActionButton btn_add_item;
    FirebaseRecyclerOptions<CategoryTwo> options2;
    ArrayList<CategoryTwo> categoryTwos;
    NavigationView navigationView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ProductsRef = FirebaseDatabase.getInstance().getReference("Restaurants");
        recyclerView = findViewById(R.id.restRecycler);
        recyclerView.setHasFixedSize(true);


        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        categoryTwos = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btn_add_item = findViewById(R.id.addCart);
        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(Home.this, Cart.class));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                                Intent intent = new Intent(Home.this, SimpleDisplayActivity.class);
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


    private void search(String newText) {
        final ArrayList<CategoryTwo> search = new ArrayList<>();
        for (CategoryTwo a : categoryTwos) {
            if ((a.getDataName().toLowerCase().contains(newText.toLowerCase())) || (a.getDataId().toLowerCase().contains(newText.toLowerCase()))) {
                search.add(a);
            }
        }
        RecyclerView.Adapter adapterf = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v2 = LayoutInflater.from(getBaseContext())
                        .inflate(R.layout.menu_view, parent, false);
                return new CategoryTwoViewHolder(v2);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
                CategoryTwoViewHolder t = ((CategoryTwoViewHolder) holder);
                t.dataId.setText(search.get(position).getDataId());
                t.dataName.setText(search.get(position).getDataName());
                t.dataAge.setText(search.get(position).getDataAge());
                t.SubCategoryInterfaceClick(new ItemClickListner() {
                    @Override
                    public void onClick(View view, boolean isLongPressed) {
                        Intent intent = new Intent(Home.this, SimpleDisplayActivity.class);
                        intent.putExtra("itemname", search.get(position).getDataId());
                        intent.putExtra("description", search.get(position).getDataName());
                        intent.putExtra("price", search.get(position).getDataAge());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return search.size();
            }
        };
        recyclerView.setAdapter(adapterf);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Deleting this account will completely removes your account and you won't be able to access this app");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Home.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Log.d("deleted", "User account deleted.");
                                    Intent intent = new Intent(Home.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Home.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().length() == 0) {
                    adapter.startListening();
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                } else {
                    search(newText);

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountMenu:
                Toast.makeText(this, "account", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Account.class));
//                return true;
            case R.id.restaurantMenu:
//                startActivity(new Intent(Home.this, Home.class));
//                return true;
            case R.id.cartMenu:
//                startActivity(new Intent(Home.this, Cart.class));
//                return true;
            case R.id.chatMenu:
//                startActivity(new Intent(Home.this, OnlineSupport.class));
//                return true;
            case R.id.deleteMenu:
                delete();
//                return true;
            case R.id.aboutMenu:
//                startActivity(new Intent(Home.this, AboutUs.class));

//                return true;

        }
        return true;
    }

}