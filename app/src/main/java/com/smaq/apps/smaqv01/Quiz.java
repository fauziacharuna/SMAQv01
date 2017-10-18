package com.smaq.apps.smaqv01;

/**
 * Created by felix on 23/02/17.
 */

public class Quiz {
    private String qui_id, try_id, qui_question, qui_answer, qui_dummy1, qui_dummy2, qui_dummy3, qui_dummy4;

    public Quiz()
    {

    }

    public Quiz(String qui_id, String try_id, String qui_question, String qui_answer, String qui_dummy1, String qui_dummy2, String qui_dummy3, String qui_dummy4)
    {
        this.qui_id = qui_id;
        this.try_id = try_id;
        this.qui_question = qui_question;
        this.qui_answer = qui_answer;
        this.qui_dummy1 = qui_dummy1;
        this.qui_dummy2 = qui_dummy2;
        this.qui_dummy3 = qui_dummy3;
        this.qui_dummy4 = qui_dummy4;
    }

    public String getID()
    {
        return qui_id;
    }

    public void setID(String id)
    {
        this.qui_id = id;
    }

    public String getSubID()
    {
        return try_id;
    }

    public void setSubID(String subID)
    {
        this.try_id = subID;
    }

    public String getQuestion()
    {
        return qui_question;
    }

    public void setQuestion(String question)
    {
        this.qui_question = question;
    }

    public String getAnswer()
    {
        return qui_answer;
    }

    public void setAnswer(String answer)
    {
        this.qui_answer = answer;
    }

    public String getDummy1()
    {
        return qui_dummy1;
    }

    public void setDummy1(String dummy1)
    {
        this.qui_dummy1 = dummy1;
    }

    public String getDummy2()
    {
        return qui_dummy2;
    }

    public void setDummy2(String dummy2)
    {
        this.qui_dummy2 = dummy2;
    }

    public String getDummy3()
    {
        return qui_dummy3;
    }

    public void setDummy3(String dummy3)
    {
        this.qui_dummy3 = dummy3;
    }

    public String getDummy4()
    {
        return qui_dummy4;
    }

    public void setDummy4(String dummy4)
    {
        this.qui_dummy4 = dummy4;
    }

}
