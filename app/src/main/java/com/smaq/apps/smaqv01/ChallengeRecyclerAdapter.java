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
 * Created by felix on 16/02/17.
 */

public class ChallengeRecyclerAdapter extends RecyclerView.Adapter{

    private String TAG = ChallengeRecyclerAdapter.class.getSimpleName();

    private List<Challenge> challengeList;

    private Context context;

    RecyclerView mRecyclerView;

    public class ChallengeViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, description, author;
        public ImageView bgImg, type, favourite, authImg;
        public ChallengeViewHolder(View itemView) {
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

    public ChallengeRecyclerAdapter(List<Challenge> challengeList)
    {
        this.challengeList = challengeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_findteam_challenge, parent, false);

        context = parent.getContext();

        return new ChallengeRecyclerAdapter.ChallengeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);

        ChallengeRecyclerAdapter.ChallengeViewHolder viewHolder = (ChallengeRecyclerAdapter.ChallengeViewHolder) holder;
        viewHolder.title.setText(challenge.getTitle());
        viewHolder.description.setText(challenge.getDescription());
        viewHolder.author.setText(challenge.getAuthorName());

        loadImageFromURL(Constants.ROOT_URL+challenge.getAuthorPicture(), viewHolder.authImg);

        final String challengeType = challenge.getType();

        if(challengeType.equals("club")) {
            viewHolder.type.setImageResource(R.drawable.ic_team_club);
        }
        else if (challengeType.equals("game")) {
            viewHolder.type.setImageResource(R.drawable.ic_team_game);
        }
        else if (challengeType.equals("music")) {
            viewHolder.type.setImageResource(R.drawable.ic_team_music);
        }
        else if (challengeType.equals("sport")) {
            viewHolder.type.setImageResource(R.drawable.ic_team_sport);
        }
        else {
            viewHolder.type.setImageResource(R.drawable.ic_team_club);
        }

        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+challenge.getImageUrl());
            Log.e(TAG, "image: "+Constants.ROOT_URL+challenge.getImageUrl());
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

        final String challenge_title = challenge.getTitle();
        final String challenge_description = challenge.getDescription();
        final String challenge_date = challenge.getDate();
        final String challenge_authorname = challenge.getAuthorName();

        final String challenge_imgurl = challenge.getImageUrl();
        final String challenge_authorimage = challenge.getAuthorPicture();

        viewHolder.bgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, ChallengeDetailActivity.class);

                myIntent.putExtra("challenge_title", challenge_title);
                myIntent.putExtra("challenge_description", challenge_description);
                myIntent.putExtra("challenge_date", challenge_date);
                myIntent.putExtra("challenge_authorname", challenge_authorname);
                myIntent.putExtra("challenge_type", challengeType.toUpperCase());

                myIntent.putExtra("challenge_imgurl", challenge_imgurl);
                myIntent.putExtra("challenge_authorimage", challenge_authorimage);

                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    private void loadImageFromURL(String fileUrl, ImageView iv)
    {
        try
        {
            URL myFileUrl = new URL (fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            iv.setImageBitmap(BitmapFactory.decodeStream(is));
        }
        catch (MalformedURLException e)
        {
            /**
             String uri = "@drawable/ic_image_unavailable";  // where myresource (without the extension) is the file

             InputStream is = MainActivity.class.getResourceAsStream(uri);

             iv.setImageBitmap(BitmapFactory.decodeStream(is));
             */
            iv.setImageResource(R.drawable.ic_image_unavailable);
            //e.printStackTrace();
        }
        catch (Exception e)
        {
            iv.setImageResource(R.drawable.ic_image_unavailable);
            //e.printStackTrace();
        }
    }
}
