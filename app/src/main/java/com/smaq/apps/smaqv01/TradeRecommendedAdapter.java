package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.content.Context;
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
 * Created by felix on 01/02/17.
 */

public class TradeRecommendedAdapter extends RecyclerView.Adapter {

    private String TAG = TradeNormalAdapter.class.getSimpleName();

    private List<Trade> tradeList;

    public boolean tradeLoaded = false;

    private Context context;

    public class TradeRecommendedViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView image;
        public TradeRecommendedViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tradeRecommendedText1);
            image = (ImageView) itemView.findViewById(R.id.tradeRecommendedImage1);
        }
    }

    public TradeRecommendedAdapter(List<Trade> tradeList)
    {
        this.tradeList = tradeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trade_recommended, parent, false);

        context = parent.getContext();
        return new TradeRecommendedAdapter.TradeRecommendedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Trade trade = tradeList.get(position);

        TradeRecommendedAdapter.TradeRecommendedViewHolder viewHolder = (TradeRecommendedAdapter.TradeRecommendedViewHolder) holder ;
        viewHolder.title.setText(trade.getTitle());

        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+trade.getImageUrl());
            Log.e(TAG, "image: "+Constants.ROOT_URL+trade.getImageUrl());
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

        final Trade tradeClick = trade;
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "image: "+tradeClick.getImageUrl());
                Log.e(TAG, "title: "+tradeClick.getTitle());

                loadImageFromURL(Constants.ROOT_URL+tradeClick.getImageUrl(), Constants.TRADE_DETAIL_IMAGE);

                Constants.TEXT_TRADE_DETAIL_TITLE.setText(tradeClick.getTitle());
                Constants.TEXT_TRADE_DETAIL_PRICE.setText(tradeClick.getPrice());
                Constants.TEXT_TRADE_DETAIL_DESCRIPTION.setText(tradeClick.getDescription());
                Constants.TRADE_DETAIL_OVERLAY.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tradeList.size();
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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        tradeLoaded = true;
        Log.e(TAG, "tradeLoaded: "+tradeLoaded);
    }

}
