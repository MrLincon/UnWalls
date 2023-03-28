package com.whitespace.unwalls.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.whitespace.unwalls.R;
import com.whitespace.unwalls.activity.CategoryDetailsActivity;
import com.whitespace.unwalls.activity.TrendingActivity;
import com.whitespace.unwalls.bottomsheet.BottomSheetSetWallpaper;
import com.whitespace.unwalls.model.ImageModel;

import java.util.ArrayList;

public class TrendingViewPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<ImageModel> list;
    LayoutInflater inflater;

    public TrendingViewPagerAdapter(Context mContext, ArrayList<ImageModel> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.trending_layout,container,false);

        ImageView imageView;
        CardView back, apply;

        imageView = view.findViewById(R.id.imageView);
        back = view.findViewById(R.id.back);
        apply = view.findViewById(R.id.apply);

        Glide.with(mContext).load(list.get(position).getUrls().getRegular()).into(imageView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrendingActivity)mContext).finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetSetWallpaper bottomSheetSetWallpaper = new BottomSheetSetWallpaper();
                Bundle bundle = new Bundle();
                bundle.putString("color", "Yellow");
                bundle.putString("image", list.get(position).getUrls().getFull());
                bottomSheetSetWallpaper.setArguments(bundle);
                bottomSheetSetWallpaper.show(((TrendingActivity)mContext).getSupportFragmentManager(), "Set wallpaper");
            }
        });

        container.addView(view);
        return  view;
    }
}
