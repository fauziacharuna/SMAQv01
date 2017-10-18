package com.smaq.apps.smaqv01;

/**
 * Created by felix on 17/03/17.
 */

public class ProfileList {
    private String acc_id, acc_studentid, acc_name, acc_email, acc_phone, acc_fullname, acc_status, acc_history, acc_birthdate, acc_organization, acc_interest, acc_schooltitle, acc_schoolcity, acc_class, acc_major, acc_profilepicture, acc_coverpicture;

    public ProfileList()
    {

    }

    public ProfileList(String acc_id, String acc_studentid, String acc_name, String acc_email, String acc_phone, String acc_fullname, String acc_status, String acc_history, String acc_birthdate, String acc_organization, String acc_interest, String acc_schooltitle, String acc_schoolcity, String acc_class, String acc_major, String acc_profilepicture, String acc_coverpicture)
    {
        this.acc_name = acc_name;
        this.acc_schooltitle = acc_schooltitle;
        this.acc_profilepicture = acc_profilepicture;
        this.acc_id = acc_id;
        this.acc_studentid = acc_studentid;
        this.acc_email = acc_email;
        this.acc_phone = acc_phone;
        this.acc_fullname = acc_fullname;
        this.acc_status = acc_status;
        this.acc_history = acc_history;
        this.acc_birthdate = acc_birthdate;
        this.acc_organization = acc_organization;
        this.acc_interest = acc_interest;
        this.acc_schoolcity = acc_schoolcity;
        this.acc_class = acc_class;
        this.acc_major = acc_major;
        this.acc_coverpicture = acc_coverpicture;
    }
    
    public String getProfileID()
    {
        return acc_id;
    }
    public String getProfileStudentID()
    {
        return acc_studentid;
    }

    public String getProfileName()
    {
        return acc_name;
    }

    public String getProfileEmail()
    {
        return acc_email;
    }
    public String getProfilePhone()
    {
        return acc_phone;
    }
    public String getProfileFullname()
    {
        return acc_fullname;
    }
    public String getProfileStatus()
    {
        return acc_status;
    }
    public String getProfileHistory()
    {
        return acc_history;
    }
    public String getProfileBirthdate()
    {
        return acc_birthdate;
    }
    public String getProfileOrganization()
    {
        return acc_organization;
    }
    public String getProfileInterest()
    {
        return acc_interest;
    }

    public String getProfileSchooltitle()
    {
        return acc_schooltitle;
    }

    public String getProfileSchoolcity()
    {
        return acc_schoolcity;
    }
    public String getProfileClass()
    {
        return acc_class;
    }
    public String getProfileMajor()
    {
        return acc_major;
    }

    public String getProfileProfilepicture()
    {
        return acc_profilepicture;
    }

    public String getProfileCoverpicture()
    {
        return acc_coverpicture;
    }

    public void setProfileID(String id)
    {
        this.acc_id = id;
    }
    public void setProfileStudentID(String studentid)
    {
        this.acc_studentid = studentid;
    }

    public void setProfileName(String name)
    {
        this.acc_name = name;
    }

    public void setProfileEmail(String email)
    {
        this.acc_email = email;
    }
    public void setProfilePhone(String phone)
    {
        this.acc_phone = phone;
    }
    public void setProfileFullname(String fullname)
    {
        this.acc_fullname = fullname;
    }
    public void setProfileStatus(String status)
    {
        this.acc_status = status;
    }
    public void setProfileHistory(String history)
    {
        this.acc_history = history;
    }
    public void setProfileBirthdate(String birthdate)
    {
        this.acc_birthdate = birthdate;
    }
    public void setProfileOrganization(String organization)
    {
        this.acc_organization = organization;
    }
    public void setProfileInterest(String interest)
    {
        this.acc_interest = interest;
    }

    public void setProfileSchooltitle(String schooltitle)
    {
        this.acc_schooltitle = schooltitle;
    }

    public void setProfileSchoolcity(String schoolcity)
    {
        this.acc_schoolcity = schoolcity;
    }
    public void setProfileClass(String accClass)
    {
        this.acc_class = accClass;
    }
    public void setProfileMajor(String major)
    {
        this.acc_major = major;
    }

    public void setProfileProfilepicture(String profilepicture)
    {
        this.acc_profilepicture = profilepicture;
    }

    public void setProfileCoverpicture(String coverpicture)
    {
        this.acc_coverpicture = coverpicture;
    }

}
