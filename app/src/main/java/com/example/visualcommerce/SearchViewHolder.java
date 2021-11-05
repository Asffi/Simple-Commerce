package com.example.visualcommerce;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchViewHolder extends RecyclerView.ViewHolder  {

        ImageView prodImage;
        TextView prodName, prodQty, prodPrice;
        public SearchViewHolder(@NonNull View itemView) {
        super(itemView);

                prodImage = itemView.findViewById(R.id.prod_image);
                prodName = itemView.findViewById(R.id.prod_name);
                prodPrice = itemView.findViewById(R.id.prod_price);
                prodQty = itemView.findViewById(R.id.prod_qty);
        }


}
