package com.smaq.apps.smaqv01;

/**
 * Created by felix on 22/02/17.
 */

public class Subject {
    private String sub_id, sub_title, sub_imageurl;

    public Subject()
    {

    }

    public Subject(String sub_id, String sub_title, String sub_imageurl)
    {
        this.sub_id = sub_id;
        this.sub_title = sub_title;
        this.sub_imageurl = sub_imageurl;
    }

    public String getID()
    {
        return sub_id;
    }

    public void setID(String id)
    {
        this.sub_id = id;
    }

    public String getTitle()
    {
        return sub_title;
    }

    public void setTitle(String title)
    {
        this.sub_title = title;
    }

    public String getImageUrl()
    {
        return sub_imageurl;
    }

    public void setImageUrl(String url)
    {
        this.sub_imageurl = url;
    }

}
