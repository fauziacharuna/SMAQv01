package com.smaq.apps.smaqv01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PromotionActivity extends AppCompatActivity {

    ImageButton mainMenuMain, mainMenuNongkrongButton, mainMenuTradeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        mainMenuMain = (ImageButton) findViewById(R.id.imageButton3);
        mainMenuNongkrongButton = (ImageButton) findViewById(R.id.imageButton4);
        mainMenuTradeButton = (ImageButton) findViewById(R.id.imageButton5);

        mainMenuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(PromotionActivity.this, MainActivity.class);
                PromotionActivity.this.startActivity(myIntent);
                PromotionActivity.this.finish();
            }
        });

        mainMenuNongkrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(PromotionActivity.this, MainNongkrongActivity.class);
                PromotionActivity.this.startActivity(myIntent);
                PromotionActivity.this.finish();
            }
        });

        mainMenuTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(PromotionActivity.this, TradeActivity.class);
                PromotionActivity.this.startActivity(myIntent);
                PromotionActivity.this.finish();
            }
        });
    }
}
