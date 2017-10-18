package com.smaq.apps.smaqv01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by felix on 31/01/17.
 */

public class TradeNormalAdapter extends RecyclerView.Adapter{

    private String TAG = TradeNormalAdapter.class.getSimpleName();

    private List<Trade> tradeList;

    //private Context context;

    RecyclerView mRecyclerView;

    public class TradeNormalViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, price;
        public TradeNormalViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tradeListNormal1);
            price = (TextView) itemView.findViewById(R.id.tradeListNormalPrice1);
        }
    }

    public TradeNormalAdapter(List<Trade> tradeList)
    {
        this.tradeList = tradeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trade_normal, parent, false);

        //context = parent.getContext();

        return new TradeNormalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TradeNormalViewHolder viewHolder = (TradeNormalViewHolder) holder ;
        final Trade trade = tradeList.get(position);
        viewHolder.title.setText(trade.getTitle());
        viewHolder.price.setText(trade.getPrice());

        final Trade tradeClick = trade;
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.tradeDetailTitle = tradeClick.getTitle();
                MainActivity.tradeDetailPrice = tradeClick.getPrice();
                MainActivity.tradeDetailDescription = tradeClick.getDescription();

                loadImageFromURL(Constants.ROOT_URL+tradeClick.getImageUrl(), Constants.TRADE_DETAIL_IMAGE);

                Constants.TEXT_TRADE_DETAIL_TITLE.setText(tradeClick.getTitle());
                Constants.TEXT_TRADE_DETAIL_PRICE.setText(tradeClick.getPrice());
                Constants.TEXT_TRADE_DETAIL_DESCRIPTION.setText(tradeClick.getDescription());
                Constants.TRADE_DETAIL_OVERLAY.setVisibility(View.VISIBLE);

                //Intent myIntent = new Intent(context, ProfileActivity.class);
                //context.startActivity(myIntent);

                Log.e(TAG, "title: "+tradeClick.getTitle());
                Log.e(TAG, "description: "+tradeClick.getDescription());
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
}
