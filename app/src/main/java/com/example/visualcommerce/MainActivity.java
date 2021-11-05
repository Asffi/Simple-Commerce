package com.example.visualcommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.visualcommerce.login.login;
import com.example.visualcommerce.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button cart,logout;
    private ImageView search;
    TextView userName;
    private RecyclerView recyclerView;
    //private String userNameString;
    private FirebaseRecyclerOptions<Products> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        userName=findViewById(R.id.textView5);
//        userNameString=getIntent().getStringExtra("Full Name");
        String userNme=Paper.book().read(CurrentUser.userName);

        if (userNme !=""){
            if (!TextUtils.isEmpty(userNme)){
                userName.setText(userNme);
            }
        }
        cart=findViewById(R.id.button);
        logout=findViewById(R.id.logout);
        search=findViewById(R.id.search_imageview);
        recyclerView= findViewById(R.id.product_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getData();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SearchActivity2.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });
    }

    private void getData() {
        options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(FirebaseDatabase.getInstance().getReference().child("products"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.prodName.setText(model.getName());
                holder.prodPrice.setText(model.getPrice());
                holder.prodQty.setText(model.getQuantity());
                Glide.with(holder.prodImage.getContext()).load(model.getImage()).into(holder.prodImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =new Intent(MainActivity.this, Productdetails.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row_item,parent,false);
                return new ProductViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



}
