package com.example.aexpress.aexpress.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aexpress.R;
import com.example.aexpress.aexpress.Model.Product;
import com.example.aexpress.databinding.ItemCartBinding;
import com.example.aexpress.databinding.QuantitydialogBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;
    public interface CartListener{
        public void onQuantityChanged();

    }
    public CartAdapter(Context context,ArrayList<Product> products,CartListener cartListener){
        this.context=context;
        this.products=products;
        this.cartListener=cartListener;
         cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
             Product product = products.get(position);


        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        holder.binding.name.setText(product.getName());
        holder.binding.Price.setText("USD:"+product.getPrice());
        holder.binding.productstock.setText(product.getQuntity()+"item(s)");
        //holder.binding.quantity.setText(product.getQuantity() + " item(s)");
        //holder.binding..setText(String.valueOf(product.getQuntity()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                QuantitydialogBinding quantitydialogBinding = QuantitydialogBinding.inflate(LayoutInflater.from(context));

                AlertDialog alertDialog  = new AlertDialog.Builder(context)
                        .setView(quantitydialogBinding.getRoot())
                        .create();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                quantitydialogBinding.productname.setText(product.getName());
                quantitydialogBinding.productstock.setText("Stock:"+product.getStock());
                int Stock = product.getStock();

                quantitydialogBinding.quantity.setText(String.valueOf(product.getQuntity()));

                quantitydialogBinding.plusbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = product.getQuntity();
                        quantity++;
                        if(quantity>product.getStock()) {
                            Toast.makeText(context, "Max stock available: " + product.getStock(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            product.setQuntity(quantity);
                            quantitydialogBinding.quantity.setText(String.valueOf(quantity));
                        }
                        cart.updateItem(product,product.getQuntity());
                        cartListener.onQuantityChanged();
                        notifyDataSetChanged();


                    }
                });
                quantitydialogBinding.minusbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      int quantity = product.getQuntity();
                      if(quantity>1){
                          quantity--;
                          product.setQuntity(quantity);
                          quantitydialogBinding.quantity.setText(String.valueOf(quantity));
                      }
                        cart.updateItem(product,product.getQuntity());
                        cartListener.onQuantityChanged();
                        notifyDataSetChanged();
                    }
                });
                quantitydialogBinding.save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         alertDialog.dismiss();
                        /* cart.updateItem(product,product.getQuntity());
                         cartListener.onQuantityChanged();
                         notifyDataSetChanged();
                         */

                    }
                });
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ItemCartBinding binding ;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemCartBinding.bind(itemView);
        }
    }
}
