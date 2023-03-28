package com.whitespace.unwalls.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.whitespace.unwalls.R;
import com.whitespace.unwalls.adapter.MainDetailsViewPagerAdapter;
import com.whitespace.unwalls.api.ApiUtilities;
import com.whitespace.unwalls.model.ImageModel;
import com.whitespace.unwalls.utils.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDetailsActivity extends AppCompatActivity {

    VerticalViewPager viewPager;
    MainDetailsViewPagerAdapter adapter;

    int itemPosition;

    private ArrayList<ImageModel> list;
    private int page= 0;
    private int pageSize = 30;

    private boolean isLoading, isLastItem;

    private static final String TAG = "MainDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        viewPager = findViewById(R.id.viewPager);

        list = new ArrayList<>();

        itemPosition = getIntent().getIntExtra("itemPosition",0);
        page = getIntent().getIntExtra("page",0);
        list = getIntent().getParcelableArrayListExtra("list");


        adapter = new MainDetailsViewPagerAdapter(MainDetailsActivity.this, list, itemPosition);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!isLoading && !isLastItem && position+itemPosition == list.size() - 1 && positionOffset == 0) {
                    page++;
                    Log.d(TAG, "onPageScrolled: Page "+page);
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
        ApiUtilities.getApiInterface().getImages(page,pageSize,"latest")
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