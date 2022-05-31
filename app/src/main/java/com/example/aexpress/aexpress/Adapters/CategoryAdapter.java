package com.example.aexpress.aexpress.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aexpress.R;
import com.example.aexpress.aexpress.Model.Category;
import com.example.aexpress.databinding.ItemCatagoriesBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewholder> {

    Context context;
    ArrayList<Category> catagories;
    public CategoryAdapter(Context context,ArrayList<Category> catagories){
        this.context = context;
        this.catagories = catagories;
    }
    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewholder(LayoutInflater.from(context).inflate(R.layout.item_catagories,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {
  Category category = catagories.get(position);
  holder.binding.label.setText(Html.fromHtml(category.getName()));
  Glide.with(context)
          .load((category.getIcon()))
          .into(holder.binding.image);

  holder.binding.image.setBackgroundColor(Color.parseColor(category.getColour()));

    }

    @Override
    public int getItemCount() {
        return catagories.size();
    }

    public class CategoryViewholder extends RecyclerView.ViewHolder{

        ItemCatagoriesBinding binding ;
        public CategoryViewholder(@NonNull View itemView) {
            super(itemView);
            binding=ItemCatagoriesBinding.bind(itemView);


        }
    }
}
