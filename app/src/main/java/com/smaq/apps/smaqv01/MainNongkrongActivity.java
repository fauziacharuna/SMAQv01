package com.smaq.apps.smaqv01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainNongkrongActivity extends AppCompatActivity {

    ImageButton mainMenuMain, nongkrongButton, teamButton, mainMenuTradeButton, mainMenuPromotionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nongkrong);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        mainMenuMain = (ImageButton) findViewById(R.id.imageButton3);
        mainMenuTradeButton = (ImageButton) findViewById(R.id.imageButton5);
        mainMenuPromotionButton = (ImageButton) findViewById(R.id.imageButton7);
        nongkrongButton = (ImageButton) findViewById(R.id.bgMainNongkrongButton);
        teamButton = (ImageButton) findViewById(R.id.bgMainTeamButton);

        mainMenuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainNongkrongActivity.this, MainActivity.class);
                MainNongkrongActivity.this.startActivity(myIntent);
                MainNongkrongActivity.this.finish();
            }
        });

        mainMenuTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainNongkrongActivity.this, TradeActivity.class);
                MainNongkrongActivity.this.startActivity(myIntent);
                MainNongkrongActivity.this.finish();
            }
        });

        mainMenuPromotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainNongkrongActivity.this, PromotionActivity.class);
                MainNongkrongActivity.this.startActivity(myIntent);
                MainNongkrongActivity.this.finish();
            }
        });

        nongkrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainNongkrongActivity.this, NongkrongActivity.class);
                MainNongkrongActivity.this.startActivity(myIntent);
            }
        });

        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainNongkrongActivity.this, FindTeamActivity.class);
                MainNongkrongActivity.this.startActivity(myIntent);
            }
        });
    }
}
