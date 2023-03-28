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
import com.whitespace.unwalls.activity.MainDetailsActivity;
import com.whitespace.unwalls.bottomsheet.BottomSheetSetWallpaper;
import com.whitespace.unwalls.model.ImageModel;

import java.util.ArrayList;

public class MainDetailsViewPagerAdapter extends PagerAdapter {

    Context mContext;
    int itemPosition;
    ArrayList<ImageModel> list;
    LayoutInflater inflater;


    public MainDetailsViewPagerAdapter(Context mContext, ArrayList<ImageModel> list, int itemPosition) {
        this.mContext = mContext;
        this.list = list;
        this.itemPosition = itemPosition;
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
        View view = inflater.inflate(R.layout.details_layout,container,false);

        ImageView imageView;
        CardView back, apply;

        imageView = view.findViewById(R.id.imageView);
        back = view.findViewById(R.id.back);
        apply = view.findViewById(R.id.apply);

        try{
            Glide.with(mContext).load(list.get(position+itemPosition).getUrls().getRegular()).into(imageView);
        }catch (Exception e){

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainDetailsActivity)mContext).finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetSetWallpaper bottomSheetSetWallpaper = new BottomSheetSetWallpaper();
                Bundle bundle = new Bundle();
                bundle.putString("color", "Red");
                bundle.putString("image", list.get(position+itemPosition).getUrls().getFull());
                bottomSheetSetWallpaper.setArguments(bundle);
                bottomSheetSetWallpaper.show(((MainDetailsActivity)mContext).getSupportFragmentManager(), "Set wallpaper");
            }
        });


        container.addView(view);
        return  view;
    }
}
