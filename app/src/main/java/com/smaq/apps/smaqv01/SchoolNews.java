package com.smaq.apps.smaqv01;

/**
 * Created by felix on 10/03/17.
 */

public class SchoolNews {
    private String news_id, news_title, news_desc, news_date, news_picture;

    public SchoolNews()
    {

    }

    public SchoolNews(String news_id, String news_title, String news_desc, String news_date, String news_picture)
    {
        this.news_id = news_id;
        this.news_title = news_title;
        this.news_desc = news_desc;
        this.news_date = news_date;
        this.news_picture = news_picture;
    }

    public String getID()
    {
        return news_id;
    }

    public void setID(String id)
    {
        this.news_id = id;
    }

    public String getTitle()
    {
        return news_title;
    }

    public void setTitle(String title)
    {
        this.news_title = title;
    }

    public String getDescription()
    {
        return news_desc;
    }

    public void setDescription(String desc)
    {
        this.news_desc = desc;
    }

    public String getDate()
    {
        return news_date;
    }

    public void setDate(String date)
    {
        this.news_date = date;
    }

    public String getImageUrl()
    {
        return news_picture;
    }

    public void setImageUrl(String url)
    {
        this.news_picture = url;
    }

}
