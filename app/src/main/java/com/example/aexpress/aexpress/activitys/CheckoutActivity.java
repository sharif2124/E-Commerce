package com.example.aexpress.aexpress.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aexpress.R;
import com.example.aexpress.aexpress.Adapters.CartAdapter;
import com.example.aexpress.aexpress.Model.Product;
import com.example.aexpress.aexpress.utilits.Constants;
import com.example.aexpress.databinding.ActivityCheckoutBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    ArrayList<Product> products;
    CartAdapter adapter;
    double totalprice = 0;
    int tax = 5;
    ProgressDialog progressDialog;
    Cart cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        products = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing..");

         cart = TinyCartHelper.getCart();
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
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);
        binding.subtotal.setText(String.format("USD:%.2f",cart.getTotalPrice()));

       totalprice = (cart.getTotalPrice().doubleValue()*tax/100)+cart.getTotalPrice().doubleValue();
       binding.total.setText("USD:"+totalprice);

       binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               processOrder();
           }
       });
    }

    void processOrder(){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
              productOrder.put("address",binding.addressBox.getText().toString());
              productOrder.put("buyer",binding.commentBox.getText().toString());
              productOrder.put("comment",binding.commentBox.getText().toString());
              productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
              productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
              productOrder.put("date_ship",Calendar.getInstance().getTimeInMillis());
              productOrder.put("email",binding.emailBox.getText().toString());
              productOrder.put("phone",binding.phoneBox.getText().toString());
              productOrder.put("serial", "cab8c1a4e4421a3b");
              productOrder.put("shipping", "");
              productOrder.put("shipping_location", "");
              productOrder.put("shipping_rate", "0.0");
              productOrder.put("status", "WAITING");
              productOrder.put("tax", tax);
              productOrder.put("total_fees", totalprice);

            JSONArray product_order_detail = new JSONArray();
            for(Map.Entry<Item, Integer> item: cart.getAllItemsWithQty().entrySet()){
                Product product = (Product) item.getKey();
                int quantity =item.getValue();
                product.setQuntity(quantity);

                JSONObject productobj = new JSONObject();
                productobj.put("amount",quantity);
                productobj.put("price_item", product.getPrice());
                productobj.put("product_id", product.getId());
                productobj.put("product_name", product.getName());
                product_order_detail.put(productobj);
            }
           dataObject.put("product_order",productOrder);
            dataObject.put("product_order_detail",product_order_detail);

            Log.e("err", dataObject.toString());


        }catch (JSONException e){

        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                      if(response.getString("status").equals("success"))
                      {
                          Toast.makeText(CheckoutActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                          String Ordernumber = response.getJSONObject("data").getString("code");
                          new AlertDialog.Builder(CheckoutActivity.this)
                                  .setTitle("Order Successful")
                                  .setCancelable(false)
                                          .setMessage("Your Order Number is"+Ordernumber)
                                                  .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                                      @Override
                                                      public void onClick(DialogInterface dialogInterface, int i) {

                                                      }
                                                  }).show();
                          Log.e("res",response.toString());
                      }
                      else {
                          Toast.makeText(CheckoutActivity.this, "Fail.", Toast.LENGTH_SHORT).show();
                          new AlertDialog.Builder(CheckoutActivity.this)
                                  .setTitle("Order Failed")
                                  .setCancelable(false)
                                  .setMessage("Something went Wrong!! Please try Again")
                                  .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialogInterface, int i) {

                                      }
                                  }).show();
                          Log.e("res",response.toString());
                      }
                      progressDialog.dismiss();
                }
                catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> headers = new HashMap<>();
                headers.put("Security","secure_code");
                return headers;
            }
        };
        queue.add(request);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}