package com.example.arafat.e_commerce.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arafat.e_commerce.Interface.ItemClickListener;
import com.example.arafat.e_commerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.products_name);
        txtProductPrice = itemView.findViewById(R.id.products_price);
        txtProductDescription = itemView.findViewById(R.id.products_description);
        imageView = itemView.findViewById(R.id.products_image);

    }

    public void setItemClickListener(ItemClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(), false);
    }
}
