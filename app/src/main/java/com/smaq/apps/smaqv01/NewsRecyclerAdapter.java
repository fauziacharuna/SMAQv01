package com.smaq.apps.smaqv01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by felix on 10/03/17.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter {

    private final List<SchoolNews> newsList;
    private String TAG = NewsRecyclerAdapter.class.getSimpleName();
    private Context context;

    public class NewsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, description, author;
        public ImageView bgImg, type, favourite, authImg;
        public NewsViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.findteam_challenge_article_title_small);
            description = (TextView) itemView.findViewById(R.id.findteam_challenge_article_content_small);
            author = (TextView) itemView.findViewById(R.id.findteam_challenge_article_author_small);

            bgImg = (ImageView) itemView.findViewById(R.id.findteam_challenge_bg);
            type = (ImageView) itemView.findViewById(R.id.findteam_challenge_type);
            favourite = (ImageView) itemView.findViewById(R.id.findteam_challenge_favourite);
            authImg = (ImageView) itemView.findViewById(R.id.ic_findteam_challenge_article_author_small);
        }
    }

    public NewsRecyclerAdapter(List<SchoolNews> newsList)
    {
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_findteam_challenge, parent, false);

        context = parent.getContext();

        return new NewsRecyclerAdapter.NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SchoolNews news = newsList.get(position);

        NewsRecyclerAdapter.NewsViewHolder viewHolder = (NewsRecyclerAdapter.NewsViewHolder) holder;
        viewHolder.title.setText(news.getTitle());
        viewHolder.description.setText(news.getDescription());
        viewHolder.author.setVisibility(View.INVISIBLE);
        viewHolder.type.setVisibility(View.INVISIBLE);

        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+news.getImageUrl());
            Log.e(TAG, "image: "+Constants.ROOT_URL+news.getImageUrl());
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            Bitmap challengeBgImg = BitmapFactory.decodeStream(is);

            if(challengeBgImg != null)
            {
                viewHolder.bgImg.setImageBitmap(challengeBgImg);
            }
        }
        catch (MalformedURLException e)
        {
            //e.printStackTrace();
            viewHolder.bgImg.setImageResource(R.drawable.ic_image_unavailable);
            Log.e(TAG, "image: MalformedURLException ");
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            viewHolder.bgImg.setImageResource(R.drawable.ic_image_unavailable);
            Log.e(TAG, "image: Exception");
        }

        final String challenge_title = news.getTitle();
        final String challenge_description = news.getDescription();
        final String challenge_date = news.getDate();

        final String challenge_imgurl = news.getImageUrl();

        viewHolder.bgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, ChallengeDetailActivity.class);

                myIntent.putExtra("challenge_title", challenge_title);
                myIntent.putExtra("challenge_description", challenge_description);
                myIntent.putExtra("challenge_date", challenge_date);

                myIntent.putExtra("challenge_imgurl", challenge_imgurl);

                myIntent.putExtra("school_news", true);

                context.startActivity(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
