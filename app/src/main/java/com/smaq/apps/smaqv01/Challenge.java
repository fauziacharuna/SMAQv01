package com.smaq.apps.smaqv01;

/**
 * Created by felix on 16/02/17.
 */

public class Challenge {
    private String chal_id, chal_title, chal_description, chal_type, chal_date, chal_imageurl, chal_authorid, chal_authorname, chal_authorprofilepicture;

    public Challenge()
    {

    }

    public Challenge(String chal_id, String chal_title, String chal_description, String chal_type, String chal_date, String chal_imageurl, String chal_authorid, String chal_authorname, String chal_authorprofilepicture)
    {
        this.chal_id = chal_id;
        this.chal_title = chal_title;
        this.chal_description = chal_description;
        this.chal_type = chal_type;
        this.chal_date = chal_date;
        this.chal_imageurl = chal_imageurl;
        this.chal_authorid = chal_authorid;
        this.chal_authorname = chal_authorname;
        this.chal_authorprofilepicture = chal_authorprofilepicture;
    }

    public String getID()
    {
        return chal_id;
    }

    public void setID(String id)
    {
        this.chal_id = id;
    }

    public String getTitle()
    {
        return chal_title;
    }

    public void setTitle(String title)
    {
        this.chal_title = title;
    }

    public String getDescription()
    {
        return chal_description;
    }

    public void setDescription(String desc)
    {
        this.chal_description = desc;
    }

    public String getType()
    {
        return chal_type;
    }

    public void setType(String type)
    {
        this.chal_type = type;
    }

    public String getDate()
    {
        return chal_date;
    }

    public void setDate(String date)
    {
        this.chal_date = date;
    }

    public String getImageUrl()
    {
        return chal_imageurl;
    }

    public void setImageUrl(String url)
    {
        this.chal_imageurl = url;
    }

    public String getAuthorID()
    {
        return chal_authorid;
    }

    public void setAuthorID(String id)
    {
        this.chal_authorid = id;
    }

    public String getAuthorName()
    {
        return chal_authorname;
    }

    public void setAuthorName(String authorname)
    {
        this.chal_authorname = authorname;
    }

    public String getAuthorPicture()
    {
        return chal_authorprofilepicture;
    }

    public void setAuthorPicture(String authorprofilepicture)
    {
        this.chal_authorprofilepicture = authorprofilepicture;
    }
}
