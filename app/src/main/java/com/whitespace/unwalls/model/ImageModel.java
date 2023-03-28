package com.whitespace.unwalls.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    String color;
    private UrlModel urls;

    public ImageModel(String color, UrlModel urls) {
        this.color = color;
        this.urls = urls;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public UrlModel getUrls() {
        return urls;
    }

    public void setUrls(UrlModel urls) {
        this.urls = urls;
    }

    // Parcelable implementation
    protected ImageModel(Parcel in) {
        color = in.readString();
        urls = in.readParcelable(UrlModel.class.getClassLoader());
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        dest.writeParcelable(urls, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
