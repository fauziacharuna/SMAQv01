package com.smaq.apps.smaqv01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class NewsArticleActivity extends AppCompatActivity {

    ImageButton newsArticleBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        newsArticleBackButton = (ImageButton) findViewById(R.id.newsArticleBackButton);

        newsArticleBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                NewsArticleActivity.this.finish();
            }

        });
    }
}
