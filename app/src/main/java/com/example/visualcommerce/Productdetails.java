package com.example.visualcommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.visualcommerce.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class Productdetails extends AppCompatActivity {

    private ImageView product,plus,minus;
    private TextView count,description,productName,price, trialroom;
    private Button addToCart;

    private String productId,date,time,randomKey,userName;
    private int counts=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        productId =getIntent().getStringExtra("pid");
        Paper.init(this);
        userName=Paper.book().read(CurrentUser.userName);

        if (userName !=""){
            if (!TextUtils.isEmpty(userName)){
            }
        }
        getViews();
        getDetail(productId);
        onClick();
    }

    private void onClick()  {
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counts++;
                    count.setText(Integer.toString(counts));
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counts--;
                    if (counts<=0){
                    counts++;
                    }else {
                        count.setText(Integer.toString(counts));
                    }
                }
            });
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addingCartList();
                }
            });
            trialroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Productdetails.this, CartActivity.class);
                    startActivity(intent);
                }
            });
        }

    private void addingCartList() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDateFormat= new SimpleDateFormat("MMM dd, yyyy");
        date=currentDateFormat.format(calendar.getTime());
        SimpleDateFormat currentTimeFormat= new SimpleDateFormat("HH:mm:ss a");
        time=currentTimeFormat.format(calendar.getTime());
        randomKey=date+time;

        DatabaseReference cartReference= FirebaseDatabase.getInstance().getReference().child("cart list");
        HashMap<String,Object> cartMap= new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("cartId",randomKey);
        cartMap.put("productName",productName.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("productQuantity",count.getText().toString());

        cartReference.child(userName).
                child("products").
                child(randomKey).
                updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Productdetails.this,"Data has been uploaded.",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Productdetails.this,CartActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void getViews() {
        trialroom=findViewById(R.id.trialroom);
        product=findViewById(R.id.imageView7);
        plus=findViewById(R.id.imageView9);
        minus=findViewById(R.id.imageView8);
        count=findViewById(R.id.textView14);
        description=findViewById(R.id.textView13);
        productName=findViewById(R.id.textView11);
        price=findViewById(R.id.textView12);
        addToCart=findViewById(R.id.button2);
    }
    private void getDetail(String pId) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()) {
                        Products products = snapshot1.getValue(Products.class);
                        if (pId.equals(products.getPid())) {
                            productName.setText(products.getName());
                            description.setText(products.getDescription());
                            price.setText(products.getPrice());
                            Glide.with(product).load(products.getPng()).into(product);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
