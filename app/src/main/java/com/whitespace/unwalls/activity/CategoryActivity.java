package com.whitespace.unwalls.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.whitespace.unwalls.R;
import com.whitespace.unwalls.adapter.CategoryAdapter;
import com.whitespace.unwalls.adapter.ImageAdapter;
import com.whitespace.unwalls.utils.GridRecyclerDecoration;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<Integer> category;
    ImageView back;

    private GridLayoutManager manager;

    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycler_view);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        GridRecyclerDecoration decoration = new GridRecyclerDecoration(spacingInPixels);
        recyclerView.addItemDecoration(decoration);

        category = new ArrayList<>();
        category.add(R.drawable.img_car);
        category.add(R.drawable.img_bike);
        category.add(R.drawable.img_abastract);
        category.add(R.drawable.img_architecture);
        category.add(R.drawable.img_space);
        category.add(R.drawable.img_tech);
        category.add(R.drawable.img_art);
        category.add(R.drawable.img_dark);
        category.add(R.drawable.img_food);
        category.add(R.drawable.img_fruits);
        category.add(R.drawable.img_plant);
        category.add(R.drawable.img_nature);
        category.add(R.drawable.img_animal);
        category.add(R.drawable.img_cat);
        category.add(R.drawable.img_dog);
        category.add(R.drawable.img_rgb);
        String[] categories = {"Car", "Bike", "Abstract", "Architecture", "Space", "Tech",
                "Art", "Dark", "Food", "Fruits", "Plant", "Nature", "Animal", "Cat", "Dog", "RGB"};


        adapter = new CategoryAdapter(this, category);
        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                Intent intent = new Intent(CategoryActivity.this, CategoryItemActivity.class);
                intent.putExtra("category",categories[position]);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}