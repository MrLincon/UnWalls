package com.whitespace.unwalls.bottomsheet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.whitespace.unwalls.R;
import com.whitespace.unwalls.utils.StorageChecker;
import com.whitespace.unwalls.utils.WallpaperSetterBoth;
import com.whitespace.unwalls.utils.WallpaperSetterHome;
import com.whitespace.unwalls.utils.WallpaperSetterLock;

import java.net.URL;


public class BottomSheetSetWallpaper extends BottomSheetDialogFragment {

    String color, imageUrl;
    ImageView imgHome, imgLock, imgHomeLock, imgSave;
    LinearLayout home, lock, homeLock, save;
    URL url;
    AsyncTask mMyTask;
    Dialog popup;

    private Bitmap bitmap;

    public BottomSheetSetWallpaper() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_set_wallpaper, container, false);

        imgHome = view.findViewById(R.id.img_set_home);
        imgLock = view.findViewById(R.id.img_set_lock);
        imgHomeLock = view.findViewById(R.id.img_set_home_lock);
        imgSave = view.findViewById(R.id.img_download);
        home = view.findViewById(R.id.set_home);
        lock = view.findViewById(R.id.set_lock);
        homeLock = view.findViewById(R.id.set_home_lock);
        save = view.findViewById(R.id.download);

        popup = new Dialog(getContext());

        color = this.getArguments().getString("color");
        imageUrl = this.getArguments().getString("image");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperSetterHome wallpaperSetter = new WallpaperSetterHome(getActivity());
                wallpaperSetter.execute(imageUrl);
                Log.d("imageUrl", "onClick: "+imageUrl);
                dismiss();
                Toast.makeText(getActivity(), "Setting wallpaper...", Toast.LENGTH_SHORT).show();
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperSetterLock wallpaperSetter = new WallpaperSetterLock(getActivity());
                wallpaperSetter.execute(imageUrl);
                Log.d("imageUrl", "onClick: "+imageUrl);
                dismiss();
                Toast.makeText(getActivity(), "Setting wallpaper...", Toast.LENGTH_SHORT).show();
            }
        });

        homeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperSetterBoth wallpaperSetter = new WallpaperSetterBoth(getActivity());
                wallpaperSetter.execute(imageUrl);
                Log.d("imageUrl", "onClick: "+imageUrl);
                dismiss();
                Toast.makeText(getActivity(), "Setting wallpaper...", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageChecker.checkStorageAvailability(getContext());
                if (isStoragePermissionGranted()) {
                   downloadImage(imageUrl);
                   dismiss();
                }else{

                }
            }
        });


        if (color.equals("Yellow")){
            setColor(R.color.colorLightIconYellow);
        } if (color.equals("Red")){
            setColor(R.color.colorAccent);
        }if (color.equals("Blue")){
            setColor(R.color.colorLightIconBlue);
        }

        return view;
    }

    private void setColor(int color) {
        imgHome.setColorFilter(ContextCompat.getColor(getContext(),color), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgLock.setColorFilter(ContextCompat.getColor(getContext(), color), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgHomeLock.setColorFilter(ContextCompat.getColor(getContext(), color), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgSave.setColorFilter(ContextCompat.getColor(getContext(), color), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public boolean isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionChecker.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED){
                Log.v("LOG", "Permission granted");
                return true;
            } else {
                Log.v("LOG", "Permission revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else{
            Log.v("LOG", "Permission is granted");
            return true;
        }
    }

    private void downloadImage(String imageUrl) {
        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(imageUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "UnWalls_"+System.currentTimeMillis()+".jpg");
        downloadManager.enqueue(request);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        long downloadId = downloadManager.enqueue(request);

        // Create a new BroadcastReceiver to receive the download progress updates
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Check if the download is complete
                DownloadManager.Query queryCompleted = new DownloadManager.Query();
                queryCompleted.setFilterById(downloadId);
                Cursor cursorCompleted = downloadManager.query(queryCompleted);
                if (cursorCompleted.moveToFirst()) {
                    @SuppressLint("Range") int status = cursorCompleted.getInt(cursorCompleted.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // The download is complete, so unregister the BroadcastReceiver
                        context.unregisterReceiver(this);

                        // Display a Toast to indicate that the download is complete
                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        // The download failed, so unregister the BroadcastReceiver
                        context.unregisterReceiver(this);

                        // Display a Toast to indicate that the download failed
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
                    }
                }
                cursorCompleted.close();
            }
        };

        // Register the BroadcastReceiver to receive download progress updates
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(receiver, filter);
    }

}
