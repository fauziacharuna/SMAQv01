package com.smaq.apps.smaqv01;

/**
 * Created by felix on 01/02/17.
 */
public class Trade {
    private String sale_id, sale_title, sale_price, sale_description, sale_imageurl, sale_posterid, sale_priority;

    public Trade()
    {

    }

    public Trade(String sale_id, String sale_title, String sale_price, String sale_description, String sale_imageurl, String sale_posterid, String sale_priority)
    {
        this.sale_id = sale_id;
        this.sale_title = sale_title;
        this.sale_price = sale_price;
        this.sale_description = sale_description;
        this.sale_imageurl = sale_imageurl;
        this.sale_posterid = sale_posterid;
        this.sale_priority = sale_priority;
    }

    public String getID()
    {
        return sale_id;
    }

    public void setID(String id)
    {
        this.sale_id = id;
    }

    public String getTitle()
    {
        return sale_title;
    }

    public void setTitle(String title)
    {
        this.sale_title = title;
    }

    public String getPrice()
    {
        return sale_price;
    }

    public void setPrice(String price)
    {
        this.sale_price = price;
    }

    public String getDescription()
    {
        return sale_description;
    }

    public void setDescription(String desc)
    {
        this.sale_description = desc;
    }

    public String getImageUrl()
    {
        return sale_imageurl;
    }

    public void setImageUrl(String url)
    {
        this.sale_imageurl = url;
    }

    public String getPosterID()
    {
        return sale_posterid;
    }

    public void setPosterID(String id)
    {
        this.sale_posterid = id;
    }

    public String getPriority()
    {
        return sale_priority;
    }

    public void setPriority(String priority)
    {
        this.sale_priority = priority;
    }
}
