package com.jsrd.moviebuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jsrd.moviebuff.MainActivity.API_KEY;

public class MovieDetailsActivity extends AppCompatActivity {


    String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private ImageView posterImage, coverImage, movieThumbnailImg1, movieThumbnailImg2;
    private TextView titleTxt, ratingTxt, releaseDateTxt, overviewTxt, reviewTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        posterImage = findViewById(R.id.moviePosterImageView);
        coverImage = findViewById(R.id.movieCoverImageView);
        titleTxt = findViewById(R.id.movieTitleTxt);
        ratingTxt = findViewById(R.id.movieRatingTxt);
        releaseDateTxt = findViewById(R.id.movieReleaseDateTxt);
        overviewTxt = findViewById(R.id.overviewTxt);
        movieThumbnailImg1 = findViewById(R.id.movieThumbnailImg1);
        movieThumbnailImg2 = findViewById(R.id.movieThumbnailImg2);
        reviewTxt = findViewById(R.id.reviewTxt);


        Intent intent = getIntent();
        MovieResults.Result movie = (MovieResults.Result) intent.getSerializableExtra("Movie");

        Glide.with(this).load(movie.getPosterPath()).into(posterImage);
        Glide.with(this).load(movie.getBackdropPath()).into(coverImage);

        titleTxt.setText(movie.getTitle());
        ratingTxt.setText(Double.toString(movie.getVoteAverage()));
        releaseDateTxt.setText(movie.getReleaseDate());
        overviewTxt.setText(movie.getOverview());

        showMovieVideoThumbnails(movie.getId());

        showMovieReview(movie.getId());

    }

    private void showMovieVideoThumbnails(int movieId) {

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL + movieId + "/").
                addConverterFactory(GsonConverterFactory.create()).
                build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<VideoResults> call = apiInterface.getVideo(API_KEY, "en-US");

        call.enqueue(new Callback<VideoResults>() {

            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                VideoResults results = response.body();
                List<VideoResults.Video> videos = results.getResults();
                String thumbnailUrl = "https://img.youtube.com/vi/" + videos.get(0).getKey() + "/1.jpg";
                String thumbnailUrl2 = "https://img.youtube.com/vi/" + videos.get(0).getKey() + "/2.jpg";
                Glide.with(MovieDetailsActivity.this).
                        load(thumbnailUrl).
                        into(movieThumbnailImg1);
                Glide.with(MovieDetailsActivity.this).
                        load(thumbnailUrl2).
                        into(movieThumbnailImg2);
            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {

            }
        });
    }

    private void showMovieReview(int movieId) {

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL + movieId + "/").
                addConverterFactory(GsonConverterFactory.create()).
                build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ReviewResults> call = apiInterface.getReviews(API_KEY, "en-US", 1);

        call.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                ReviewResults results = response.body();
                List<ReviewResults.Review> reviews = results.getResults();
                if (reviews.size() > 0) {
                    String review = reviews.get(0).getContent();
                    reviewTxt.setText(review);
                } else {
                    reviewTxt.setText("No Review Found");
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {

            }
        });
    }
}