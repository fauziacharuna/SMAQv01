package com.smaq.apps.smaqv01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
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
 * Created by felix on 17/03/17.
 */

public class ProfileListRecyclerAdapter extends RecyclerView.Adapter {

    private String TAG = ChallengeRecyclerAdapter.class.getSimpleName();

    private List<ProfileList> profileLists;

    private Context context;

    private ViewGroup parent;

    public class ProfileListViewHolder extends RecyclerView.ViewHolder
    {
        public TextView profileName, profileSchool;
        public ImageView profileAvatar;

        public ProfileListViewHolder(View itemView) {
            super(itemView);

            profileName = (TextView) itemView.findViewById(R.id.nomineeName);
            profileSchool = (TextView) itemView.findViewById(R.id.nomineeSchool);

            profileAvatar = (ImageView) itemView.findViewById(R.id.nomineeAvatar);
        }
    }

    public ProfileListRecyclerAdapter(List<ProfileList> profileLists)
    {
        this.profileLists = profileLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mvp_nominee_item, parent, false);

        context = parent.getContext();

        return new ProfileListRecyclerAdapter.ProfileListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ProfileList profile = profileLists.get(position);

        ProfileListRecyclerAdapter.ProfileListViewHolder viewHolder = (ProfileListRecyclerAdapter.ProfileListViewHolder) holder;

        viewHolder.profileName.setText(profile.getProfileName());
        viewHolder.profileSchool.setText(profile.getProfileSchooltitle());

        Log.e(TAG, "profileName " + profile.getProfileName());
        Log.e(TAG, "profileSchool " + profile.getProfileSchooltitle());

        loadImageFromURL(Constants.ROOT_URL+profile.getProfileProfilepicture(), viewHolder.profileAvatar);

        viewHolder.profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NongkrongActivity.Transfer transfer = new NongkrongActivity.Transfer()
        /*
                View popupFriendProfileOverlay = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.popup_profile, parent, true);

                TextView textProfileName = (TextView) popupFriendProfileOverlay.findViewById(R.id.textProfileName);
                textProfileName.setText(profile.getProfileName());

                TextView textProfileStatus = (TextView) popupFriendProfileOverlay.findViewById(R.id.textProfileStatus);
                textProfileStatus.setText(profile.getProfileStatus());
        */

                Intent myIntent = new Intent(context, FriendProfileActivity.class);

                myIntent.putExtra("friend_name", profile.getProfileName());
                myIntent.putExtra("friend_email", profile.getProfileEmail());
                myIntent.putExtra("friend_phone", profile.getProfilePhone());
                myIntent.putExtra("friend_status", profile.getProfileStatus());
                myIntent.putExtra("friend_history", profile.getProfileHistory());
                myIntent.putExtra("friend_birthdate", profile.getProfileBirthdate());
                myIntent.putExtra("friend_organization", profile.getProfileOrganization());
                myIntent.putExtra("friend_interest", profile.getProfileInterest());
                myIntent.putExtra("friend_schooltitle", profile.getProfileSchooltitle());
                myIntent.putExtra("friend_schoolcity", profile.getProfileSchoolcity());
                myIntent.putExtra("friend_class", profile.getProfileClass());
                myIntent.putExtra("friend_major", profile.getProfileMajor());
                myIntent.putExtra("friend_profilepicture", profile.getProfileProfilepicture());
                myIntent.putExtra("friend_coverpicture", profile.getProfileCoverpicture());

                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileLists.size();
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
