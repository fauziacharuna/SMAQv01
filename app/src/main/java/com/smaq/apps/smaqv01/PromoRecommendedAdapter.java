package com.smaq.apps.smaqv01;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaq.apps.smaqv01.Important.Promo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by felix on 01/02/17.
 */

public class PromoRecommendedAdapter extends RecyclerView.Adapter  {

    private String TAG = PromoNormalAdapter.class.getSimpleName();

    private List<Promo> promoList;

    public class PromoRecommendedViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView image;
        public PromoRecommendedViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tradeRecommendedText1);
            image = (ImageView) itemView.findViewById(R.id.tradeRecommendedImage1);
        }
    }

    public PromoRecommendedAdapter(List<Promo> promoList)
    {
        this.promoList = promoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trade_recommended, parent, false);

        return new PromoRecommendedAdapter.PromoRecommendedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Promo promo = promoList.get(position);

        PromoRecommendedAdapter.PromoRecommendedViewHolder viewHolder = (PromoRecommendedAdapter.PromoRecommendedViewHolder) holder ;
        viewHolder.title.setText(promo.getTitle());

        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+promo.getImageUrl());
            Log.e(TAG, "image: "+Constants.ROOT_URL+promo.getImageUrl());
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            viewHolder.image.setImageBitmap(BitmapFactory.decodeStream(is));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        final Promo promoClick = promo;
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "image: "+promoClick.getImageUrl());
                Log.e(TAG, "title: "+promoClick.getTitle());

                loadImageFromURL(Constants.ROOT_URL+promoClick.getImageUrl(), Constants.PROMO_DETAIL_IMAGE);

                Constants.TEXT_PROMO_DETAIL_TITLE.setText(promoClick.getTitle());
                Constants.TEXT_PROMO_DETAIL_DATE.setText(promoClick.getDate());
                Constants.TEXT_PROMO_DETAIL_DESCRIPTION.setText(promoClick.getDescription());
                Constants.PROMO_DETAIL_OVERLAY.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return promoList.size();
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
