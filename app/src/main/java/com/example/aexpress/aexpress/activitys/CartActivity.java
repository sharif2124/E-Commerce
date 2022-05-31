package com.example.aexpress.aexpress.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.aexpress.aexpress.Adapters.CartAdapter;
import com.example.aexpress.aexpress.Model.Product;
import com.example.aexpress.databinding.ActivityCartBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
  ActivityCartBinding binding;
  ArrayList<Product> products;
  CartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        products = new ArrayList<>();



        Cart cart = TinyCartHelper.getCart();
        for(Map.Entry<Item, Integer> item: cart.getAllItemsWithQty().entrySet()){
            Product product = (Product) item.getKey();
            int quantity =item.getValue();
            product.setQuntity(quantity);
            products.add(product);
        }
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("USD:%.2f",cart.getTotalPrice()));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartlist.setLayoutManager(layoutManager);
        binding.cartlist.addItemDecoration(itemDecoration);
        binding.cartlist.setAdapter(adapter);
        binding.subtotal.setText(String.format("USD:%.2f",cart.getTotalPrice()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this,CheckoutActivity.class));
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}