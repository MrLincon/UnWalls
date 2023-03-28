package com.whitespace.unwalls.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.whitespace.unwalls.R;
import com.whitespace.unwalls.adapter.TrendingViewPagerAdapter;
import com.whitespace.unwalls.api.ApiUtilities;
import com.whitespace.unwalls.model.ImageModel;
import com.whitespace.unwalls.utils.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingActivity extends AppCompatActivity {

    VerticalViewPager viewPager;
    TrendingViewPagerAdapter adapter;

    private ArrayList<ImageModel> list;
    private int page = 1;
    private int pageSize = 30;

    private boolean isLoading, isLastItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        viewPager = findViewById(R.id.viewPager);

        list = new ArrayList<>();
        adapter = new TrendingViewPagerAdapter(TrendingActivity.this, list);
        viewPager.setAdapter(adapter);

        getData();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!isLoading && !isLastItem && position == list.size() - 1 && positionOffset == 0) {
                    page++;
                    getData();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getData() {
        isLoading = true;
        ApiUtilities.getApiInterface().getImages(page,pageSize,"popular")
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                        if (response.body() != null) {
                            list.addAll(response.body());
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
                    public void onFailure(Call<List<ImageModel>> call, Throwable t) {

                    }
                });
    }

}