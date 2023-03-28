package com.whitespace.unwalls.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UrlModel implements Parcelable {
    private String thumb, small, regular, full;

    public UrlModel(String thumb, String small, String regular, String full) {
        this.thumb = thumb;
        this.small = small;
        this.regular = regular;
        this.full = full;
    }

    protected UrlModel(Parcel in) {
        thumb = in.readString();
        small = in.readString();
        regular = in.readString();
        full = in.readString();
    }


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public static final Creator<UrlModel> CREATOR = new Creator<UrlModel>() {
        @Override
        public UrlModel createFromParcel(Parcel in) {
            return new UrlModel(in);
        }

        @Override
        public UrlModel[] newArray(int size) {
            return new UrlModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeString(small);
        dest.writeString(regular);
        dest.writeString(full);
    }
}
