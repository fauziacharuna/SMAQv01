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
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by felix on 22/02/17.
 */

public class LearningSubjectsAdapter extends RecyclerView.Adapter{

    private String TAG = ChallengeRecyclerAdapter.class.getSimpleName();

    private List<Subject> subjectList;
    private Context context;

    public class LearningSubjectsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView subImg;
        public LearningSubjectsViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.subjectTitle);

            subImg = (ImageView) itemView.findViewById(R.id.subjectImage);
        }
    }

    public LearningSubjectsAdapter(List<Subject> subjectList)
    {
        this.subjectList = subjectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.learning_subject_item, parent, false);

        context = parent.getContext();

        return new LearningSubjectsAdapter.LearningSubjectsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Subject subject = subjectList.get(position);
        final int pos = position;

        LearningSubjectsAdapter.LearningSubjectsViewHolder viewHolder = (LearningSubjectsAdapter.LearningSubjectsViewHolder) holder;
        viewHolder.title.setText(subject.getTitle());

        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+subject.getImageUrl());
            Log.e(TAG, "image: "+Constants.ROOT_URL+subject.getImageUrl());
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            Bitmap challengeBgImg = BitmapFactory.decodeStream(is);

            if(challengeBgImg != null)
            {
                viewHolder.subImg.setImageBitmap(challengeBgImg);
            }
            else
            {
                viewHolder.subImg.setImageResource(R.drawable.ic_image_unavailable);
            }
        }
        catch (MalformedURLException e)
        {
            //e.printStackTrace();
            viewHolder.subImg.setImageResource(R.drawable.ic_image_unavailable);
            Log.e(TAG, "image: MalformedURLException ");
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            viewHolder.subImg.setImageResource(R.drawable.ic_image_unavailable);
            Log.e(TAG, "image: Exception");
        }

        viewHolder.subImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, LearningSubjectActivity.class);

                myIntent.putExtra("sub_id", subject.getID());
                myIntent.putExtra("sub_title", subject.getTitle());

                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }
}
