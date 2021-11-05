package com.example.visualcommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.visualcommerce.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity2 extends AppCompatActivity {
    private EditText productName;
    private ImageView search;
    private RecyclerView searchedItem;
    private String product;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerOptions<Products> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        getViews();
        searchedItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        searchedItem.setLayoutManager(layoutManager);
        setOnClickListener();
    }

    private void getViews() {
        productName=findViewById(R.id.editTextSearch);
        search=findViewById(R.id.buttonSearch);
        searchedItem=findViewById(R.id.recycler_search);
    }

    private void setOnClickListener() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               product= productName.getText().toString();
               onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("products");

        options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(firebaseDatabase.orderByChild("name").startAt(product),Products.class).build();
        FirebaseRecyclerAdapter<Products, SearchViewHolder> adapter=new FirebaseRecyclerAdapter<Products, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull Products model) {
                holder.prodName.setText(model.getName());
                holder.prodPrice.setText(model.getPrice());
                holder.prodQty.setText(model.getQuantity());
                Glide.with(holder.prodImage.getContext()).load(model.getImage()).into(holder.prodImage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(SearchActivity2.this, Productdetails.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row_item,parent,false);
                return new SearchViewHolder(view);
            }
        };
        searchedItem.setAdapter(adapter);
        adapter.startListening();
    }
}