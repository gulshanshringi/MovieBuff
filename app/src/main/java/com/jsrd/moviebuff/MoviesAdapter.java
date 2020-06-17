package com.jsrd.moviebuff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import com.jsrd.moviebuff.MovieResults.Result;
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private Context mContext;
    private List<Result> moviesList;

    public MoviesAdapter(Context context, List<Result> moviesList){
        mContext = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_list_item,parent,false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Result result = moviesList.get(position);
        Glide.with(mContext)
                .load(result.getPosterPath())
                .into(holder.coverImage);
        holder.titleTxt.setText(result.getTitle());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder{
        private ImageView coverImage;
        private TextView titleTxt;
        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.coverImageView);
            titleTxt = itemView.findViewById(R.id.titleTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat activityOptionsCompat =
                            ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) mContext,coverImage,"coverImage");



                    Intent movieDetailsIntent = new Intent(mContext,MovieDetailsActivity.class);
                    movieDetailsIntent.putExtra("Movie",moviesList.get(getAdapterPosition()));
                    mContext.startActivity(movieDetailsIntent,activityOptionsCompat.toBundle());
                }
            });
        }
    }

}
