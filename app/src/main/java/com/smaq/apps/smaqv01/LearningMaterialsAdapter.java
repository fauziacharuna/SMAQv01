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

public class LearningMaterialsAdapter extends RecyclerView.Adapter{

    private String TAG = ChallengeRecyclerAdapter.class.getSimpleName();

    private List<Material> materialList;
    private Context context;

    public class LearningMaterialsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView matImg;
        public LearningMaterialsViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textMaterialSubjectTopicTitle);

            matImg = (ImageView) itemView.findViewById(R.id.imageMaterialSubjectTopicThumbnail);
        }
    }

    public LearningMaterialsAdapter(List<Material> materialList)
    {
        this.materialList = materialList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_material_item, parent, false);

        context = parent.getContext();

        return new LearningMaterialsAdapter.LearningMaterialsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Material material = materialList.get(position);
        final int pos = position;

        LearningMaterialsAdapter.LearningMaterialsViewHolder viewHolder = (LearningMaterialsAdapter.LearningMaterialsViewHolder) holder;
        viewHolder.title.setText(material.getTitle());

        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+material.getImageUrl());
            Log.e(TAG, "image: "+Constants.ROOT_URL+material.getImageUrl());
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            Bitmap challengeBgImg = BitmapFactory.decodeStream(is);

            if(challengeBgImg != null)
            {
                viewHolder.matImg.setImageBitmap(challengeBgImg);
            }
            else
            {
                viewHolder.matImg.setImageResource(R.drawable.ic_image_unavailable);
            }
        }
        catch (MalformedURLException e)
        {
            //e.printStackTrace();
            viewHolder.matImg.setImageResource(R.drawable.ic_image_unavailable);
            Log.e(TAG, "image: MalformedURLException ");
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            viewHolder.matImg.setImageResource(R.drawable.ic_image_unavailable);
            Log.e(TAG, "image: Exception");
        }

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, MaterialActivity.class);

                myIntent.putExtra("mat_title", material.getTitle());
                myIntent.putExtra("mat_contents", material.getContents());

                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }
}
