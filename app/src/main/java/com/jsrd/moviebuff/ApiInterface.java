package com.jsrd.moviebuff;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/movie/{category}")
    Call<MovieResults> getMovies(
            @Path("category") String category,
            @Query("api_key") String API_KEY,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("videos")
    Call<VideoResults> getVideo(
            @Query("api_key") String API_KEY,
            @Query("language") String language
    );

    @GET("reviews")
    Call<ReviewResults> getReviews(
            @Query("api_key") String API_KEY,
            @Query("language") String language,
            @Query("page") int page
    );
}
