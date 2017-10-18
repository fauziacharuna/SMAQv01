package com.smaq.apps.smaqv01.Important;

/**
 * Created by felix on 01/02/17.
 */
public class Promo {
    private String promo_id, promo_title, promo_description, promo_date, promo_imageurl, promo_priority;

    public Promo()
    {

    }

    public Promo(String promo_id, String promo_title, String promo_description, String promo_date, String promo_imageurl, String promo_priority)
    {
        this.promo_id = promo_id;
        this.promo_title = promo_title;
        this.promo_description = promo_description;
        this.promo_imageurl = promo_imageurl;
        this.promo_date = promo_date;
        this.promo_priority = promo_priority;
    }

    public String getID()
    {
        return promo_id;
    }

    public void setID(String id)
    {
        this.promo_id = id;
    }

    public String getTitle()
    {
        return promo_title;
    }

    public void setTitle(String title)
    {
        this.promo_title = title;
    }

    public String getDescription()
    {
        return promo_description;
    }

    public void setDescription(String desc)
    {
        this.promo_description = desc;
    }

    public String getDate()
    {
        return promo_date;
    }

    public void setPosterID(String date)
    {
        this.promo_date = date;
    }

    public String getImageUrl()
    {
        return promo_imageurl;
    }

    public void setImageUrl(String url)
    {
        this.promo_imageurl = url;
    }

    public String getPriority()
    {
        return promo_priority;
    }

    public void setPriority(String priority)
    {
        this.promo_priority = priority;
    }
}
