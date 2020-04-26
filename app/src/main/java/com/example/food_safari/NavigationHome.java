package com.example.food_safari;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_safari.interfaces.ItemClickListner;
import com.example.food_safari.model.CategoryTwo;
import com.example.food_safari.ui.home.HomeFragment;
import com.example.food_safari.viewholder.CategoryTwoViewHolder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.food_safari.ui.home.HomeFragment.recyclerView;

public class NavigationHome extends AppCompatActivity {

    DatabaseReference databaseReference;
    String name;
    NavController navController;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_restaurant, R.id.nav_account, R.id.nav_chat,
                R.id.nav_tools, R.id.nav_about, R.id.nav_cart, R.id.logout)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //header layout
        final View navView = navigationView.getHeaderView(0);
        final TextView profilenameET = navView.findViewById(R.id.profilenameET);
        TextView profileEmailET = navView.findViewById(R.id.profileEmailET);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("userdata").child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    profilenameET.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            String email = user.getEmail();
            profileEmailET.setText(email);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().length() == 0) {
                    HomeFragment.adapter.startListening();
                    HomeFragment.adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(HomeFragment.adapter);
                } else {
                    search(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void search(String newText) {
        final ArrayList<CategoryTwo> search = new ArrayList<>();

        for (CategoryTwo a : HomeFragment.categoryTwos) {
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
                        Intent intent = new Intent(NavigationHome.this, SimpleDisplayActivity.class);
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

}
