package com.smaq.apps.smaqv01;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChallengeDetailActivity extends AppCompatActivity {

    private String challenge_title, challenge_description, challenge_date, challenge_authorname, challenge_imgurl, challenge_authorimage, challenge_type;
    private TextView challengeDetailToolbarTitle, challengeDetailTitle, challengeDetailDescription, challengeDetailDate, challengeDetailAuthorname, challengeDetailAuthorStatus;
    private ImageView challengeDetailImage, challengeDetailAuthorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        challenge_title = getIntent().getStringExtra("challenge_title");
        challenge_description = getIntent().getStringExtra("challenge_description");
        challenge_date = getIntent().getStringExtra("challenge_date");

        challenge_imgurl = getIntent().getStringExtra("challenge_imgurl");

        challengeDetailToolbarTitle = (TextView) findViewById(R.id.challengeDetailTitleText);
        challengeDetailTitle = (TextView) findViewById(R.id.challengeDetailTitle);
        challengeDetailDescription = (TextView) findViewById(R.id.textChallengeDetailContent);
        challengeDetailDate = (TextView) findViewById(R.id.textDateChallengeDetail);
        challengeDetailAuthorname = (TextView) findViewById(R.id.challengeDetailAuthorName);
        challengeDetailAuthorStatus = (TextView) findViewById(R.id.challengeDetailAuthorStatus);

        challengeDetailImage = (ImageView) findViewById(R.id.challengeDetailImage);
        challengeDetailAuthorImage = (ImageView) findViewById(R.id.challengeDetailAuthorImage);

        challengeDetailTitle.setText(challenge_title);
        challengeDetailDescription.setText(challenge_description);
        challengeDetailDate.setText(challenge_date);

        challengeDetailAuthorStatus.setVisibility(View.INVISIBLE);

        loadImageFromURL(challenge_imgurl, challengeDetailImage);

        boolean school_news = getIntent().getBooleanExtra("school_news", false);
        if(school_news)
        {
            challengeDetailToolbarTitle.setVisibility(View.INVISIBLE);
            challengeDetailAuthorname.setVisibility(View.INVISIBLE);
            challengeDetailAuthorImage.setVisibility(View.INVISIBLE);
        }
        else
        {
            challenge_authorname = getIntent().getStringExtra("challenge_authorname");
            challenge_type = getIntent().getStringExtra("challenge_type");
            challenge_authorimage = getIntent().getStringExtra("challenge_authorimage");

            challengeDetailToolbarTitle.setText(challenge_type);
            challengeDetailAuthorname.setText(challenge_authorname);
            loadImageFromURL(challenge_authorimage, challengeDetailAuthorImage);
        }

        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        ImageButton challengeDetailBackButton = (ImageButton) findViewById(R.id.challengeDetailBackButton);

        challengeDetailBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChallengeDetailActivity.this.finish();
            }
        });
    }

    private void loadImageFromURL(String fileUrl, ImageView iv)
    {
        try
        {
            URL myFileUrl = new URL (Constants.ROOT_URL+fileUrl);
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
