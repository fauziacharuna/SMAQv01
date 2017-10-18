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

public class PromoNormalAdapter extends RecyclerView.Adapter{

    private String TAG = PromoNormalAdapter.class.getSimpleName();

    private List<Promo> promoList;

    public class PromoNormalViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, date;
        public PromoNormalViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tradeListNormal1);
            date = (TextView) itemView.findViewById(R.id.tradeListNormalPrice1);
        }
    }

    public PromoNormalAdapter(List<Promo> promoList)
    {
        this.promoList = promoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trade_normal, parent, false);

        return new PromoNormalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PromoNormalViewHolder viewHolder = (PromoNormalViewHolder) holder ;
        Promo promo = promoList.get(position);
        viewHolder.title.setText(promo.getTitle());
        viewHolder.date.setText(promo.getDate());

        final Promo promoClick = promo;
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "title: "+promoClick.getTitle());
                Log.e(TAG, "description: "+promoClick.getDescription());

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