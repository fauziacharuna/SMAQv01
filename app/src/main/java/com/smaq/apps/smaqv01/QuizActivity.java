package com.smaq.apps.smaqv01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerQuiz;
    private QuizItemAdapter qAdapter;
    private List<Quiz> quizList = new ArrayList<>();
    private String try_id, try_title;
    private boolean seeResult, seeAnswer;

    private String TAG = QuizActivity.class.getSimpleName();
    private Button buttonQuizDone;
    private ImageButton quizBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        try_id = getIntent().getStringExtra("try_id");
        try_title = getIntent().getStringExtra("try_title");
        seeResult = getIntent().getBooleanExtra("seeResult", false);
        seeAnswer = getIntent().getBooleanExtra("seeAnswer", Constants.QUIZ_STATE_QUESTION);

        quizBackButton = (ImageButton) findViewById(R.id.quizBackButton);
        buttonQuizDone = (Button) findViewById(R.id.buttonQuizDone);
        TextView quizTitleText = (TextView) findViewById(R.id.quizTitleText);

        if(seeAnswer == Constants.QUIZ_STATE_ANSWERS)
        {
            buttonQuizDone.setEnabled(false);
            buttonQuizDone.setVisibility(View.GONE);
            quizTitleText.setText("Kunci jawaban "+try_title);
        }
        else
        {
            buttonQuizDone.setEnabled(true);
            buttonQuizDone.setVisibility(View.VISIBLE);
            quizTitleText.setText(try_title);
        }

        //Log.e(TAG, "try_id: "+try_id);
        //Toast.makeText(this,"mlebu quiz: \n", Toast.LENGTH_LONG).show();

        addListenerOnButton();
        LoadQuizes();
    }

    private void addListenerOnButton() {

        quizBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizActivity.this.finish();
            }
        });

        buttonQuizDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.e(TAG, "seeResult: "+seeResult);

                Intent myIntent = new Intent(QuizActivity.this, QuizResultActivity.class);
                QuizActivity.this.startActivity(myIntent);
                QuizActivity.this.finish();
                /*
                if (seeResult)
                {
                    Intent myIntent = new Intent(QuizActivity.this, QuizResultActivity.class);

                    //myIntent.putExtra("mat_title", material.getTitle());
                    //myIntent.putExtra("mat_contents", material.getContents());

                    QuizActivity.this.startActivity(myIntent);
                    QuizActivity.this.finish();
                }
                else
                {
                    QuizActivity.this.finish();
                }
                */
            }
        });
    }

    protected void LoadQuizes()
    {
        recyclerQuiz = (RecyclerView) findViewById(R.id.quiz_list_recycler);
        qAdapter = new QuizItemAdapter(quizList, seeAnswer);
        RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerLearning.setLayoutManager(new GridLayoutManager(LearningSubjectActivity.this, 3));
        recyclerQuiz.setLayoutManager(normalLayoutManager);
        recyclerQuiz.setItemAnimator(new DefaultItemAnimator());
        recyclerQuiz.setAdapter(qAdapter);

        Log.e(TAG, "LoadQuizes: ");

        GetQuizesData();

        OverScrollDecoratorHelper.setUpOverScroll(recyclerQuiz, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    protected void GetQuizesData()
    {
        quizList.clear();

        Log.e(TAG, "GetQuizesData: ");

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "tryID="+try_id;

        try
        {
            url = new URL(Constants.ROOT_URL+"apanel/get_quizes.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            response = sb.toString();

            Log.e(TAG, "response: "+response);

            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
                    JSONArray trades = new JSONArray(response);

                    for (int i = 0; i < trades.length(); i++)
                    {
                        JSONObject c = trades.getJSONObject(i);

                        String qui_id = c.getString("qui_id");
                        String try_id = c.getString("try_id");
                        String qui_question = c.getString("qui_question");
                        String qui_answer = c.getString("qui_answer");
                        String qui_dummy1 = c.getString("qui_dummy1");
                        String qui_dummy2 = c.getString("qui_dummy2");
                        String qui_dummy3 = c.getString("qui_dummy3");
                        String qui_dummy4 = c.getString("qui_dummy4");

                        Log.e(TAG, "qui_id " + qui_id);
                        Log.e(TAG, "qui_question " + qui_question);

                        Quiz quiz = new Quiz(qui_id, try_id, qui_question, qui_answer, qui_dummy1, qui_dummy2, qui_dummy3, qui_dummy4);

                        quizList.add(quiz);
                    }

                    QuizActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qAdapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Log.e(TAG, response);
                }
            }
            else if(response.equals("invalid\n"))
            {
                Log.e(TAG, "Invalid Input");
            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
            }
            /**
             Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
             */
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            // Toast.makeText(this,"Error!!\n"+tradeUrl, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

}
