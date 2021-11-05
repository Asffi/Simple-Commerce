package com.example.visualcommerce;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartViewHolder extends RecyclerView.ViewHolder {
    TextView productName,productQuantity,productPrice;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }

    private void findViews() {
        productName=itemView.findViewById(R.id.product_name_cartlist_textView);
        productQuantity=itemView.findViewById(R.id.product_quantity_cartlist_textView);
        productPrice=itemView.findViewById(R.id.product_price_cartlist_textView);
    }


}
