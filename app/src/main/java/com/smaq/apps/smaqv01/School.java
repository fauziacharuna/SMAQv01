package com.smaq.apps.smaqv01;

/**
 * Created by felix on 06/03/17.
 */

public class School {
    private String sch_id, sch_title, sch_address;

    public School()
    {

    }

    public School(String sch_id, String sch_title, String sch_address)
    {
        this.sch_id = sch_id;
        this.sch_title = sch_title;
        this.sch_address = sch_address;
    }

    public String getID()
    {
        return sch_id;
    }

    public void setID(String id)
    {
        this.sch_id = id;
    }

    public String getTitle()
    {
        return sch_title;
    }

    public void setTitle(String title)
    {
        this.sch_title = title;
    }

    public String getAddress()
    {
        return sch_address;
    }

    public void setAddress(String address)
    {
        this.sch_address = address;
    }

}
