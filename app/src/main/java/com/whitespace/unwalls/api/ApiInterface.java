package com.whitespace.unwalls.api;

import static com.whitespace.unwalls.api.ApiUtilities.API_KEY;

import com.whitespace.unwalls.model.ImageModel;
import com.whitespace.unwalls.model.SearchModel;
import com.whitespace.unwalls.model.UrlModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/photos")
    Call<List<ImageModel>> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String order
    );

    @Headers("Authorization: Client-ID " + API_KEY)
    @GET("/search/photos")
    Call<SearchModel> searchImage(
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("order_by") String order
    );
}