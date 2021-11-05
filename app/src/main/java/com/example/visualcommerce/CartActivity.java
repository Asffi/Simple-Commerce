package com.example.visualcommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visualcommerce.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
    private TextView totalPrice;
    private Button proceed;
    private String userName;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Cart> options;
    private int totalPriceCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Paper.init(this);
        getViews();
        Paper.init(this);
        userName=Paper.book().read(CurrentUser.userName);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this,ConfirmFinalOrderAcivity.class);
                intent.putExtra("total price",String.valueOf(totalPriceCount));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference cartListRefernce= FirebaseDatabase.getInstance().getReference().child("cart list");


        options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(FirebaseDatabase.getInstance().getReference().child("cart list").child(userName).child("products"),Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.productName.setText("Product: "+model.getProductName());
                holder.productQuantity.setText("Quantity: "+model.getProductQuantity());
                holder.productPrice.setText("Price: "+model.getProductPrice());
                int oneProductPrice = Integer.valueOf(model.getProductPrice()) * Integer.valueOf(model.getProductQuantity());
                totalPriceCount = totalPriceCount + oneProductPrice;
                totalPrice.setText("Total price is = "+Integer.toString(totalPriceCount));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "Edit","Remove"
                        };
                        AlertDialog.Builder builder= new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if (i==0){
                                    Intent intent =new Intent(CartActivity.this, Productdetails.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if (i==1){
                                    cartListRefernce.child(userName).child("products").child(model.getCartId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(CartActivity.this,"Data is successfully deleted",Toast.LENGTH_SHORT).show();
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
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                return new CartViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    

    private void getViews() {
        totalPrice=findViewById(R.id.total_price_textview);
        proceed=findViewById(R.id.proceed_btn);
        recyclerView=findViewById(R.id.cartList_recyclerview);
    }
}