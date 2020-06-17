package com.jsrd.moviebuff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.jsrd.moviebuff.MovieResults.Result;
public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "9cfa1f217083aef20275dc6fa0a48587";
    private static final String BASE_URL = "https://api.themoviedb.org";

    private RecyclerView moviesListRecyclerView;
    private ProgressBar progressBar;
    private TextView refreshBtn;
    private LinearLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshBtn = findViewById(R.id.refreshBtn);

        moviesListRecyclerView = findViewById(R.id.moviesListRecyclerView);
        moviesListRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        showMoviesList();

        refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressBar.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
                showMoviesList();
            }
        });

    }

    private void showMoviesList(){
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<MovieResults> call = apiInterface.getMovies("popular", API_KEY, "en-US", 1);

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                MovieResults results = response.body();
                List<Result> moviesList = results.getResults();
                MoviesAdapter adapter = new MoviesAdapter(MainActivity.this,moviesList);
                moviesListRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            }
        });
    }

}