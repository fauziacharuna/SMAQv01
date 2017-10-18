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
 * Created by felix on 20/02/17.
 */

public class MvpNomineeRecyclerAdapter extends RecyclerView.Adapter{

    private String TAG = ChallengeRecyclerAdapter.class.getSimpleName();

    private List<MvpNominee> mvpNomineeList;

    private Context context;

    public class MvpNomineeViewHolder extends RecyclerView.ViewHolder
    {
        public TextView nomineeName, nomineeSchool;
        public ImageView nomineeAvatar;

        public MvpNomineeViewHolder(View itemView) {
            super(itemView);

            nomineeName = (TextView) itemView.findViewById(R.id.nomineeName);
            nomineeSchool = (TextView) itemView.findViewById(R.id.nomineeSchool);

            nomineeAvatar = (ImageView) itemView.findViewById(R.id.nomineeAvatar);
        }
    }

    public MvpNomineeRecyclerAdapter(List<MvpNominee> mpvNomineeList)
    {
        this.mvpNomineeList = mpvNomineeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mvp_nominee_item, parent, false);

        context = parent.getContext();

        return new MvpNomineeRecyclerAdapter.MvpNomineeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MvpNominee nominee = mvpNomineeList.get(position);

        MvpNomineeRecyclerAdapter.MvpNomineeViewHolder viewHolder = (MvpNomineeRecyclerAdapter.MvpNomineeViewHolder) holder;

        viewHolder.nomineeName.setText(nominee.getMvpName());
        viewHolder.nomineeSchool.setText(nominee.getMvpSchooltitle());

        Log.e(TAG, "mvpnom_name " + nominee.getMvpName());
        Log.e(TAG, "getMvpSchooltitle " + nominee.getMvpSchooltitle());

        loadImageFromURL(Constants.ROOT_URL+nominee.getMvpProfilepicture(), viewHolder.nomineeAvatar);

        viewHolder.nomineeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, FriendProfileActivity.class);

                myIntent.putExtra("friend_name", nominee.getMvpName());
                myIntent.putExtra("friend_email", nominee.getMvpEmail());
                myIntent.putExtra("friend_phone", nominee.getMvpPhone());
                myIntent.putExtra("friend_status", nominee.getMvpStatus());
                myIntent.putExtra("friend_history", nominee.getMvpHistory());
                myIntent.putExtra("friend_birthdate", nominee.getMvpBirthdate());
                myIntent.putExtra("friend_organization", nominee.getMvpOrganization());
                myIntent.putExtra("friend_interest", nominee.getMvpInterest());
                myIntent.putExtra("friend_schooltitle", nominee.getMvpSchooltitle());
                myIntent.putExtra("friend_schoolcity", nominee.getMvpSchoolcity());
                myIntent.putExtra("friend_class", nominee.getMvpClass());
                myIntent.putExtra("friend_major", nominee.getMvpMajor());
                myIntent.putExtra("friend_profilepicture", nominee.getMvpProfilepicture());
                myIntent.putExtra("friend_coverpicture", nominee.getMvpCoverpicture());

                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mvpNomineeList.size();
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
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if(bitmap != null) {
                iv.setImageBitmap(bitmap);
            }
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
