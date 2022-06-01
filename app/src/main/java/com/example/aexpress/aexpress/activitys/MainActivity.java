package com.example.aexpress.aexpress.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aexpress.R;
import com.example.aexpress.aexpress.Adapters.CategoryAdapter;
import com.example.aexpress.aexpress.Adapters.ProductAdapter;
import com.example.aexpress.aexpress.Model.Category;
import com.example.aexpress.aexpress.Model.Product;
import com.example.aexpress.aexpress.utilits.Constants;
import com.example.aexpress.databinding.ActivityMainBinding;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
 ActivityMainBinding binding ;
 CategoryAdapter categoryAdapter;
 ArrayList<Category> categories;
 ProductAdapter productAdapter;
 ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("query",text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
       initcatagory();
       initproduct();
        initslider();

    }
    void initslider(){
        getRecentOffers();
    }
    void initcatagory(){
        categories=new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);
        getCategories();
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        binding.catagorylist.setLayoutManager(layoutManager);
        binding.catagorylist.setAdapter(categoryAdapter);

    }
    void initproduct(){
        products=new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.Productlist.setLayoutManager(layoutManager);
        binding.Productlist.setAdapter(productAdapter);


    }
    void getCategories(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")) {
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for(int i =0; i< categoriesArray.length(); i++) {
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category category = new Category(
                                    object.getString("name"),
                                    Constants.CATEGORIES_IMAGE_URL+object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }
                    else {
                        //Do not Show
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);


    }

    void  getRecentProducts(){
       RequestQueue queue = Volley.newRequestQueue(this);
       String Url = Constants.GET_PRODUCTS_URL+"?count=10";
       StringRequest request = new StringRequest(Request.Method.GET, Url, response -> {
           try {
               JSONObject object = new JSONObject(response);
               if(object.getString("status").equals("success")){
                   JSONArray productArray = object.getJSONArray("products");
                   for(int i=0; i<productArray.length(); i++){
                       JSONObject childObj = productArray.getJSONObject(i);
                       Product product = new Product(
                               childObj.getString("name"),
                               Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                               childObj.getString("status"),
                               childObj.getDouble("price"),
                               childObj.getDouble("price_discount"),
                               childObj.getInt("stock"),
                               childObj.getInt("id")
                       );
                      products.add(product);
                   }
                   productAdapter.notifyDataSetChanged();

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }, error -> {

       });
       queue.add(request);

    }
    void getRecentOffers(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray offerarray = object.getJSONArray("news_infos");
                    for (int i=0; i<offerarray.length();i++){
                        JSONObject childobj = offerarray.getJSONObject(i);

                        binding.carousel.addData(new CarouselItem(
                                Constants.NEWS_IMAGE_URL+ childobj.getString("image"),
                                childobj.getString("title")

                        ));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });

        queue.add(request);
    }
}