package com.smaq.apps.smaqv01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class QuizResultActivity extends AppCompatActivity {

    private ImageButton resultBackButton;

    private SharedPreferences score;
    private SharedPreferences.Editor editor;

    private int totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        getScore();

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        resultBackButton = (ImageButton) findViewById(R.id.resultBackButton);

        resultBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizResultActivity.this.finish();
            }
        });
    }

    private void getScore()
    {
        score = PreferenceManager.getDefaultSharedPreferences(QuizResultActivity.this);
        totalScore = score.getInt("totalScore", 0);

        String finalScore = String.valueOf(totalScore);

        TextView textScore = (TextView) findViewById(R.id.textScore);
        textScore.setText(finalScore);

        editor = score.edit();
        editor.putInt("totalScore", 0);
        editor.commit();
    }

}
