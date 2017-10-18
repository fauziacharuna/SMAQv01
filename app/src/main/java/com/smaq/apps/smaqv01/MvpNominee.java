package com.smaq.apps.smaqv01;

/**
 * Created by felix on 20/02/17.
 */

public class MvpNominee {
    //private String mvp_description, mvp_id, mvp_studentid, mvp_name, mvp_email, mvp_phone, mvp_fullname, mvp_status, mvp_history, mvp_birthdate, mvp_organization, mvp_interest, mvp_schooltitle, mvp_schoolcity, mvp_class, mvp_major, mvp_profilepicture, mvp_coverpicture;
    private String mvp_description, mvp_id, mvp_studentid, mvp_name, mvp_email, mvp_phone, mvp_fullname, mvp_status, mvp_history, mvp_birthdate, mvp_organization, mvp_interest, mvp_schooltitle, mvp_schoolcity, mvp_class, mvp_major, mvp_profilepicture, mvp_coverpicture;

    public MvpNominee()
    {

    }

    //public MvpNominee(String mvp_description, String mvp_name, String mvp_schooltitle, String mvp_profilepicture)
    public MvpNominee(String mvp_description, String mvp_id, String mvp_studentid, String mvp_name, String mvp_email, String mvp_phone, String mvp_fullname, String mvp_status, String mvp_history, String mvp_birthdate, String mvp_organization, String mvp_interest, String mvp_schooltitle, String mvp_schoolcity, String mvp_class, String mvp_major, String mvp_profilepicture, String mvp_coverpicture)
    {
        this.mvp_description = mvp_description;
        this.mvp_name = mvp_name;
        this.mvp_schooltitle = mvp_schooltitle;
        this.mvp_profilepicture = mvp_profilepicture;
        this.mvp_id = mvp_id;
        this.mvp_studentid = mvp_studentid;
        this.mvp_email = mvp_email;
        this.mvp_phone = mvp_phone;
        this.mvp_fullname = mvp_fullname;
        this.mvp_status = mvp_status;
        this.mvp_history = mvp_history;
        this.mvp_birthdate = mvp_birthdate;
        this.mvp_organization = mvp_organization;
        this.mvp_interest = mvp_interest;
        this.mvp_schoolcity = mvp_schoolcity;
        this.mvp_class = mvp_class;
        this.mvp_major = mvp_major;
        this.mvp_coverpicture = mvp_coverpicture;
    }

    public String getMvpDescription()
    {
        return mvp_description;
    }

    public String getMvpID()
    {
        return mvp_id;
    }
    public String getMvpStudentID()
    {
        return mvp_studentid;
    }

    public String getMvpName()
    {
        return mvp_name;
    }

    public String getMvpEmail()
    {
        return mvp_email;
    }
    public String getMvpPhone()
    {
        return mvp_phone;
    }
    public String getMvpFullname()
    {
        return mvp_fullname;
    }
    public String getMvpStatus()
    {
        return mvp_status;
    }
    public String getMvpHistory()
    {
        return mvp_history;
    }
    public String getMvpBirthdate()
    {
        return mvp_birthdate;
    }
    public String getMvpOrganization()
    {
        return mvp_organization;
    }
    public String getMvpInterest()
    {
        return mvp_interest;
    }

    public String getMvpSchooltitle()
    {
        return mvp_schooltitle;
    }

    public String getMvpSchoolcity()
    {
        return mvp_schoolcity;
    }
    public String getMvpClass()
    {
        return mvp_class;
    }
    public String getMvpMajor()
    {
        return mvp_major;
    }

    public String getMvpProfilepicture()
    {
        return mvp_profilepicture;
    }

    public String getMvpCoverpicture()
    {
        return mvp_coverpicture;
    }

    public void setMvpDescription(String description)
    {
        this.mvp_description = description;
    }

    public void setMvpID(String id)
    {
        this.mvp_id = id;
    }
    public void setMvpStudentID(String studentid)
    {
        this.mvp_studentid = studentid;
    }

    public void setMvpName(String name)
    {
        this.mvp_name = name;
    }

    public void setMvpEmail(String email)
    {
        this.mvp_email = email;
    }
    public void setMvpPhone(String phone)
    {
        this.mvp_phone = phone;
    }
    public void setMvpFullname(String fullname)
    {
        this.mvp_fullname = fullname;
    }
    public void setMvpStatus(String status)
    {
        this.mvp_status = status;
    }
    public void setMvpHistory(String history)
    {
        this.mvp_history = history;
    }
    public void setMvpBirthdate(String birthdate)
    {
        this.mvp_birthdate = birthdate;
    }
    public void setMvpOrganization(String organization)
    {
        this.mvp_organization = organization;
    }
    public void setMvpInterest(String interest)
    {
        this.mvp_interest = interest;
    }

    public void setMvpSchooltitle(String schooltitle)
    {
        this.mvp_schooltitle = schooltitle;
    }

    public void setMvpSchoolcity(String schoolcity)
    {
        this.mvp_schoolcity = schoolcity;
    }
    public void setMvpClass(String mvpClass)
    {
        this.mvp_class = mvpClass;
    }
    public void setMvpMajor(String major)
    {
        this.mvp_major = major;
    }

    public void setMvpProfilepicture(String profilepicture)
    {
        this.mvp_profilepicture = profilepicture;
    }

    public void setMvpCoverpicture(String coverpicture)
    {
        this.mvp_coverpicture = coverpicture;
    }

}
