package com.smaq.apps.smaqv01;

/**
 * Created by felix on 23/02/17.
 */

public class Tryout {
    private String try_id, sub_id, try_title;

    public Tryout()
    {

    }

    public Tryout(String try_id, String sub_id, String try_title)
    {
        this.try_id = try_id;
        this.sub_id = sub_id;
        this.try_title = try_title;
    }

    public String getID()
    {
        return try_id;
    }

    public void setID(String id)
    {
        this.try_id = id;
    }

    public String getSubID()
    {
        return sub_id;
    }

    public void setSubID(String subID)
    {
        this.sub_id = subID;
    }

    public String getTitle()
    {
        return try_title;
    }

    public void setTitle(String title)
    {
        this.try_title = title;
    }

}
