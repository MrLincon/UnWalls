package com.whitespace.unwalls.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.whitespace.unwalls.R;
import com.whitespace.unwalls.activity.MainDetailsActivity;
import com.whitespace.unwalls.model.ImageModel;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private int page;
    private ArrayList<ImageModel> list;

    public ImageAdapter(Context context, ArrayList<ImageModel> list, int page) {
        this.context = context;
        this.list = list;
        this.page = page;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_layout,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUrls().getSmall()).into(holder.imageView);
        int color = Color.parseColor(list.get(position).getColor());
        holder.cardView.setCardBackgroundColor(color);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> urlList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    urlList.add(list.get(i).getUrls().getRegular());
                }
                Log.d("imageView", "onClick: "+list.size());
                Intent intent = new Intent(context, MainDetailsActivity.class);
                intent.putParcelableArrayListExtra("list", list);
                intent.putExtra("page", page);
                intent.putExtra("itemPosition", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private CardView cardView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
