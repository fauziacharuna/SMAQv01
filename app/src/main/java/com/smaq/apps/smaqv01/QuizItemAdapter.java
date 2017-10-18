package com.smaq.apps.smaqv01;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by felix on 23/02/17.
 */

public class QuizItemAdapter extends RecyclerView.Adapter{

    private String TAG = QuizItemAdapter.class.getSimpleName();

    private List<Quiz> quizList;
    private Context context;

    private SharedPreferences answers;
    private SharedPreferences.Editor editor;

    private int totalScore = 0;
    private float addedScore = 0;

    private boolean correctAnswer = false;
    private boolean seeAnswer = Constants.QUIZ_STATE_QUESTION;

    public class QuizItemViewHolder extends RecyclerView.ViewHolder
    {
        public TextView question;
        public RadioGroup quizAnswerGroup;
        public RadioButton answer1, answer2, answer3, answer4, answer5;
        public QuizItemViewHolder(View itemView) {
            super(itemView);

            question = (TextView) itemView.findViewById(R.id.quizQuestion);
            quizAnswerGroup = (RadioGroup) itemView.findViewById(R.id.quizAnswerGroup);
            answer1 = (RadioButton) itemView.findViewById(R.id.quizAnswer1);
            answer2 = (RadioButton) itemView.findViewById(R.id.quizAnswer2);
            answer3 = (RadioButton) itemView.findViewById(R.id.quizAnswer3);
            answer4 = (RadioButton) itemView.findViewById(R.id.quizAnswer4);
            answer5 = (RadioButton) itemView.findViewById(R.id.quizAnswer5);
        }
    }

    public QuizItemAdapter(List<Quiz> quizList, boolean seeAnswer)
    {
        this.seeAnswer = seeAnswer;
        this.quizList = quizList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_item, parent, false);

        context = parent.getContext();

        answers = PreferenceManager.getDefaultSharedPreferences(context);

        return new QuizItemAdapter.QuizItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Quiz quiz = quizList.get(position);
        final int pos = position;

        String[] choices = new String[5];
        choices[0] = quiz.getAnswer();
        choices[1] = quiz.getDummy1();
        choices[2] = quiz.getDummy2();
        choices[3] = quiz.getDummy3();
        choices[4] = quiz.getDummy4();

        Random rgen = new Random();  // Random number generator

        for (int i=0; i<choices.length; i++) {
            Log.e(TAG, "choices[i]: "+choices[i]);
            int randomPosition = rgen.nextInt(choices.length);
            String temp = choices[i];
            choices[i] = choices[randomPosition];
            choices[randomPosition] = temp;
            Log.e(TAG, "choices[i]: "+choices[i]);
        }

        QuizItemAdapter.QuizItemViewHolder viewHolder = (QuizItemAdapter.QuizItemViewHolder) holder;
        viewHolder.question.setText(quiz.getQuestion());
        viewHolder.answer1.setText(choices[0]);
        viewHolder.answer2.setText(choices[1]);
        viewHolder.answer3.setText(choices[2]);
        viewHolder.answer4.setText(choices[3]);
        viewHolder.answer5.setText(choices[4]);

        if(seeAnswer == Constants.QUIZ_STATE_ANSWERS)
        {
            viewHolder.answer1.setText(quiz.getAnswer());
            viewHolder.answer1.setChecked(true);

            viewHolder.answer2.setEnabled(false);
            viewHolder.answer3.setEnabled(false);
            viewHolder.answer4.setEnabled(false);
            viewHolder.answer5.setEnabled(false);

            viewHolder.answer2.setVisibility(View.GONE);
            viewHolder.answer3.setVisibility(View.GONE);
            viewHolder.answer4.setVisibility(View.GONE);
            viewHolder.answer5.setVisibility(View.GONE);
        }

        editor = answers.edit();
        editor.putBoolean("answer"+pos, false);
        editor.commit();

        viewHolder.quizAnswerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                for(int idx=0; idx<radioGroup.getChildCount(); idx++) {
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(idx);
                    if(btn.getId() == i) {
                        String text = btn.getText().toString();
                        //Log.e(TAG, "getItemCount: "+quizList.size());

                        String answer = "answer"+pos;
                        correctAnswer = answers.getBoolean(answer, false);
                        Log.e(TAG, "correctAnswer: "+correctAnswer);

                        if(text.equals(quiz.getAnswer()) && !correctAnswer)
                        {
                            Log.e(TAG, "Correct!!");
                            addedScore = 100/quizList.size();

                            editor = answers.edit();
                            editor.putBoolean(answer, true);
                            editor.commit();
                            //correctAnswer = true;
                        }
                        else
                        {
                            if(correctAnswer)
                            {
                                Log.e(TAG, "kurangi!!");
                                totalScore -= 100/quizList.size();
                                addedScore = 0;

                                editor = answers.edit();
                                editor.putBoolean(answer, false);
                                editor.commit();
                                //correctAnswer = false;
                            }
                            else
                            {
                                addedScore = 0;
                            }
                        }

                        totalScore += addedScore;

                        editor = answers.edit();
                        editor.putInt("totalScore", totalScore);
                        editor.commit();

                        Log.e(TAG, "totalScore: "+totalScore);

                        return;
                    }
                }

                //String selectedValue = radioGroup.getChildAt(i).toString();
                //Log.e(TAG, "selectedValue: "+selectedValue);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }
}
