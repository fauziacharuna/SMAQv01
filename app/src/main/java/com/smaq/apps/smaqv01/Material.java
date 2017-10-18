package com.smaq.apps.smaqv01;

/**
 * Created by felix on 22/02/17.
 */

public class Material {
    private String mat_id, sub_id, mat_title, mat_contents, mat_imageurl;

    public Material()
    {

    }

    public Material(String mat_id, String sub_id, String mat_title, String mat_contents, String mat_imageurl)
    {
        this.mat_id = mat_id;
        this.sub_id = sub_id;
        this.mat_title = mat_title;
        this.mat_contents = mat_contents;
        this.mat_imageurl = mat_imageurl;
    }

    public String getID()
    {
        return mat_id;
    }

    public void setID(String id)
    {
        this.mat_id = id;
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
        return mat_title;
    }

    public void setTitle(String title)
    {
        this.mat_title = title;
    }

    public String getContents()
    {
        return mat_contents;
    }

    public void setContents(String contents)
    {
        this.mat_contents = contents;
    }

    public String getImageUrl()
    {
        return mat_imageurl;
    }

    public void setImageUrl(String url)
    {
        this.mat_imageurl = url;
    }

}
