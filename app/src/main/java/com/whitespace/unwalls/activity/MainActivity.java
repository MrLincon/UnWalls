package com.whitespace.unwalls.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.whitespace.unwalls.R;
import com.whitespace.unwalls.adapter.ImageAdapter;
import com.whitespace.unwalls.api.ApiUtilities;
import com.whitespace.unwalls.model.ImageModel;
import com.whitespace.unwalls.model.SearchModel;
import com.whitespace.unwalls.utils.GridRecyclerDecoration;
import com.whitespace.unwalls.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    EditText etSearch;
    ImageView search;
    CardView category, trending;
    ChipGroup chipGroup;
    Chip searchChip;

    private ArrayList<ImageModel> list;
    private GridLayoutManager manager;
    private ImageAdapter adapter;

    String searchText;
    String selectedChipText;

    private int page = 1;
    private int pageSize = 30;

    private boolean isLoading, isLastItem, isSearch, isSearchClicked;

    Tools tools;
    Dialog popup;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.et_search);
        search = findViewById(R.id.search);
        category = findViewById(R.id.category);
        trending = findViewById(R.id.featured);
        chipGroup = findViewById(R.id.chip_group);
        searchChip = findViewById(R.id.search_chip);
        recyclerView = findViewById(R.id.recycler_view);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        GridRecyclerDecoration decoration = new GridRecyclerDecoration(spacingInPixels);
        recyclerView.addItemDecoration(decoration);

        tools = new Tools();
        popup = new Dialog(this);

        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list, page);
        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        tools.loading(popup, true);
        getData();

        setUpRecyclerViewScroll();


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {


                Chip selectedChip = group.findViewById(checkedId);
                if (selectedChip == null) {
                    return;
                }
                selectedChipText = (String) selectedChip.getChipText();
                if (isSearchClicked != true && !selectedChipText.equals(searchText)) {
                    tools.loading(popup, true);
                    if (selectedChipText.equals("All")) {
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(0);
                        list.clear();
                        page = 1;
                        getData();
                        setUpRecyclerViewScroll();
                    } else {
                        searchText = selectedChipText;
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(0);
                        list.clear();
                        page = 1;
                        searchData(searchText);
                        setUpRecyclerViewScroll();
                    }
                    selectedChip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            isSearchClicked = false;
                            if (isSearchClicked != true && !selectedChipText.equals(searchText)) {
                                etSearch.setText("");
                                tools.loading(popup, true);
                                if (selectedChipText.equals("All")) {
                                    adapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(0);
                                    list.clear();
                                    page = 1;
                                    getData();
                                    setUpRecyclerViewScroll();
                                } else {
                                    searchText = selectedChipText;
                                    adapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(0);
                                    list.clear();
                                    page = 1;
                                    searchData(searchText);
                                    setUpRecyclerViewScroll();
                                }

                            }
                        }
                    });
                }


                if (!selectedChipText.equals("Search")) {
                    searchChip.setVisibility(View.GONE);
                }


            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });

        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TrendingActivity.class));
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.loading(popup, true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                searchText = etSearch.getText().toString().trim();
                if (!searchText.equals("")) {
                    isSearchClicked = true;
                    Log.d(TAG, "onClick: " + searchText);
                    list.clear();
                    searchData(searchText);
                    searchChip.setVisibility(View.VISIBLE);
                    chipGroup.check(R.id.search_chip);
                }
//                else {
//                    Log.d(TAG, "onClick: "+searchText);
//                    list.clear();
//                    getData();
//                }

            }
        });

    }

    private void setUpRecyclerViewScroll() {
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
                        if (isSearch == true) {
                            searchData(searchText);
                        } else {
                            getData();
                        }
                    }
                }
            }
        });
    }

    private void getData() {
        isLoading = true;
        isSearch = false;
        ApiUtilities.getApiInterface().getImages(page, pageSize, "latest")
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                        if (response.body() != null) {
                            list.addAll(response.body());
                            adapter.notifyDataSetChanged();

                            tools.loading(popup, false);
                        }
                        isLoading = false;

                        if (list.size() > 0) {
                            isLastItem = list.size() < pageSize;
                        } else {
                            isLastItem = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                        tools.makeToast(getApplicationContext(), "Mo wallpaper found at this moment! :(");
                    }
                });
    }

    private void searchData(String query) {
        isLoading = true;
        isSearch = true;
        Log.d(TAG, "searchData: " + page);
        ApiUtilities.getApiInterface().searchImage(query, page, pageSize, "popular")
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        if (response.body() != null) {
                            list.addAll(response.body().getResults());
                            adapter.notifyDataSetChanged();
                            tools.loading(popup, false);
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
                        tools.makeToast(getApplicationContext(), "Mo wallpaper found at this moment! :(");
                    }
                });
    }
}