package com.whitespace.unwalls.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitespace.unwalls.R;
import com.whitespace.unwalls.adapter.CategoryImageAdapter;
import com.whitespace.unwalls.adapter.ImageAdapter;
import com.whitespace.unwalls.api.ApiUtilities;
import com.whitespace.unwalls.model.ImageModel;
import com.whitespace.unwalls.model.SearchModel;
import com.whitespace.unwalls.utils.GridRecyclerDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryItemActivity extends AppCompatActivity {

    ImageView back;
    private RecyclerView recyclerView;
    TextView title;
    private ArrayList<ImageModel> list;
    private GridLayoutManager manager;
    private CategoryImageAdapter adapter;

    private int page = 1;
    private int pageSize = 30;

    private boolean isLoading, isLastItem;

    String category;


    private static final String TAG = "CategoryItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);

        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recycler_view);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        GridRecyclerDecoration decoration = new GridRecyclerDecoration(spacingInPixels);
        recyclerView.addItemDecoration(decoration);

        category = getIntent().getStringExtra("category");
        title.setText(category);

        list = new ArrayList<>();
        adapter = new CategoryImageAdapter(this, list, page, category);
        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstItemPosition = manager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastItem) {
                    if ((visibleItem + firstItemPosition >= totalItem)) {
                        Log.d(TAG, "onScrolled: New Page");
                        page++;
                        getData();
                    }
                }
            }
        });

    }

    private void getData() {
        isLoading = true;

        ApiUtilities.getApiInterface().searchImage(category, page, pageSize, "latest")
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        if (response.body() != null) {
                            list.addAll(response.body().getResults());
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;

                        if (list.size() > 0) {
                            isLastItem = list.size() < pageSize;
                        } else {
                            isLastItem = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {

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