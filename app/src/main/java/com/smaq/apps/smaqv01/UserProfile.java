package com.smaq.apps.smaqv01;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by felix on 07/01/17.
 */

public class UserProfile {

    SharedPreferences userProfile;

    final String userProfileName = "userSavedProfileName";
    final String userProfilePhone = "userSavedProfilePhone";
    final String userProfilePassword = "userSavedProfilePassword";
    final String userProfileEmail = "userSavedProfileEmail";

    final String userName = "userSavedName";
    final String userStatus = "userSavedStatus";

    final String userSchooltitle = "userSavedSchooltitle";
    final String userSchoolClass = "userSavedSchoolClass";
    final String userSchoolMajor = "userSavedSchoolMajor";
    final String userSchooladdress = "userSavedSchooladdress";

    final String userBioBirthdate = "userSavedBioBirthdate";
    final String userBioHistory = "userSavedBioHistory";
    final String userBioOrganization = "userSavedBioOrganization";
    final String userBioInterest = "userSavedBioInterest";
    //final String userBioPhone = "userSavedSchoolClass";
    //final String userBioEmail = "userSavedSchoolMajor";

    final String userID = "userSavedID";

    final String userProfilePictures = "userSavedProfilePictures";
    final String userCoverPictures = "userSavedCoverPictures";

    final String rootURL = Constants.ROOT_URL;
    final String updateInfoUrl = rootURL+"apanel/update_status.php";
    final String updateProfileInfoUrl = rootURL+"apanel/update_profile_info.php";
    final String updateBioUrl = rootURL+"apanel/update_bio.php";
    final String getIdUrl = rootURL+"apanel/get_account_id.php";

    final String defaultProfilePictureUrl = rootURL+"profile_pictures/default.png";
    final String defaultCoverPictureUrl = rootURL+"cover_pictures/default.png";

    String userSavedProfileName, userSavedProfilePhone, userSavedProfilePassword, userSavedProfileEmail;

    String userSavedName, userSavedStatus;

    String userSavedSchooltitle, userSavedSchoolClass, userSavedSchoolMajor, userSavedSchooladdress;

    String userSavedBioBirthdate, userSavedBioHistory, userSavedBioOrganization, userSavedBioInterest;

    String userSavedID;

    String userSavedProfilePictures, userSavedCoverPictures;

    String TAG = UserProfile.class.getSimpleName();

    public void getUserProfile(Context context)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedProfileName = userProfile.getString(userProfileName, "");
        userSavedProfilePhone = userProfile.getString(userProfilePhone, "");
        userSavedProfilePassword = userProfile.getString(userProfilePassword, "");
        userSavedProfileEmail = userProfile.getString(userProfileEmail, "");
    }

    public void saveUserProfile(Context context, String userName, String userPhone, String userPassword, String userEmail)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = userProfile.edit();

        editor.putString(userProfileName, userName);
        editor.putString(userProfilePhone, userPhone);
        editor.putString(userProfilePassword, userPassword);
        editor.putString(userProfileEmail, userEmail);
        editor.commit();
    }

    public void getUserStatus(Context context)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedName = userProfile.getString(userName, "Orkawas Kamtari");
        userSavedStatus = userProfile.getString(userStatus, "Insert Status");
    }

    public void saveUserStatus(Context context, String userCustomName, String userCustomStatus)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = userProfile.edit();

        userSavedID = getUserID(context);

        try
        {
            //URL urlCoverPicture = new URL("http://192.168.42.30/smaq/cover_picture.php");
            URL updateInfo = new URL(updateInfoUrl);
            String parameters = "&txtName="+userCustomName+"&txtStatus="+userCustomStatus+"&txtUserID="+userSavedID;
            //Log.e(TAG, parameters);
            loadDatabase(updateInfo,parameters);

            editor.putString(userName, userCustomName);
            editor.putString(userStatus, userCustomStatus);
            editor.commit();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public void getUserSchoolInfo(Context context)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedSchooltitle = userProfile.getString(userSchooltitle, "SMA XXX");
        userSavedSchoolClass = userProfile.getString(userSchoolClass, "Kelas X");
        userSavedSchoolMajor = userProfile.getString(userSchoolMajor, "");
        userSavedSchooladdress = userProfile.getString(userSchooladdress, "");
        //Log.e(TAG, "get userSavedSchooltitle: " + userProfile.getString(userSchooltitle,""));
    }

    public void saveUserSchoolInfo(Context context, String inputSchooltitle, String inputSchoolClass, String inputSchoolMajor, String inputSchooladdress)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = userProfile.edit();

        userSavedID = getUserID(context);

         try
         {
             //URL urlCoverPicture = new URL("http://192.168.42.30/smaq/cover_picture.php");
             URL updateInfo = new URL(updateProfileInfoUrl);
             String parameters = "&txtSchooltitle="+inputSchooltitle+"&txtSchoolcity="+inputSchooladdress+"&txtClass="+inputSchoolClass+"&txtMajor="+inputSchoolMajor+"&txtUserID="+userSavedID;
             //Log.e(TAG, parameters);
             loadDatabase(updateInfo,parameters);

             editor.putString(userSchooltitle, inputSchooltitle);
             editor.putString(userSchoolClass, inputSchoolClass);
             editor.putString(userSchoolMajor, inputSchoolMajor);
             editor.putString(userSchooladdress, inputSchooladdress);
             editor.commit();
         }
         catch (MalformedURLException e)
         {
             e.printStackTrace();
         }
    }

    public void getUserBio(Context context)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedBioBirthdate = userProfile.getString(userBioBirthdate, "");
        userSavedBioHistory = userProfile.getString(userBioHistory, "2001 - 2003 SMA Muhi 2 Sleman");
        userSavedBioOrganization = userProfile.getString(userBioOrganization, "2002 Top Skor Liga Futsal Lapangan Lumpur DIY");
        userSavedBioInterest = userProfile.getString(userBioInterest, "Filsafat, Fotografi, Fashion");
        //Log.e(TAG, "get userBioHistory: " + userProfile.getString(userBioHistory,""));
        //userSavedStatus = userProfile.getString(userStatus, "Insert Status");
    }

    public void saveUserBio(Context context, String userPhone, String userEmail, String userHistory, String userBirthdate, String userOrganization, String userInterest)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = userProfile.edit();

        userSavedID = getUserID(context);

        try
        {
            //URL urlCoverPicture = new URL("http://192.168.42.30/smaq/cover_picture.php");
            URL updateInfo = new URL(updateBioUrl);
            String parameters = "&txtPhone="+userPhone+"&txtEmail="+userEmail+"&txtHistory="+userHistory+"&txtBirthdate="+userBirthdate+"&txtOrganization="+userOrganization+"&txtInterest="+userInterest+"&txtUserID="+userSavedID;
            //Log.e(TAG, parameters);
            loadDatabase(updateInfo,parameters);

            editor.putString(userProfilePhone, userPhone);
            editor.putString(userProfileEmail, userEmail);

            editor.putString(userBioBirthdate, userBirthdate);
            editor.putString(userBioHistory, userHistory);
            editor.putString(userBioOrganization, userOrganization);
            editor.putString(userBioInterest, userInterest);
            editor.commit();
            //Log.e(TAG, "save userBioHistory: " + userProfile.getString(userBioHistory,""));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public void saveUserID(Context context, String id)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = userProfile.edit();

        editor.putString(userID, id);
        editor.commit();
    }

    public String getUserID(Context context)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);
        userSavedProfilePhone = userProfile.getString(userProfilePhone, "");
        String params = "&txtPhone="+userSavedProfilePhone;
        String acc_id = "";

        try
        {
            URL getID = new URL(getIdUrl);

            ArrayList<HashMap<String, String>> userProfile = new ArrayList<>();
            HttpURLConnection connection;
            OutputStreamWriter request = null;
            String response = null;

            try
            {
                // url = new URL("http://192.168.42.30/smaq/apanel/update_profile.php");
                connection = (HttpURLConnection) getID.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(params);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
//                    sb.append(line + "\n");
                    sb.append(line);
                }

                // Response from server after login process will be stored in response variable.
                response = sb.toString();

                // You can perform UI operations here
                if (!response.equals("invalid\n") && !response.equals("\n"))
                {
                    acc_id = response;
                }
                else
                {
                    Log.e(TAG, "Couldn't get json from server. error 1"+response);
                }
                /**
                 Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
                 */
                isr.close();
                reader.close();

            }
            catch (IOException e){
                // Error
                Log.e(TAG, "Couldn't get json from server. error 2");
                // Toast.makeText(context,"Error!!", Toast.LENGTH_LONG).show();

            }

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        /**
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedID = userProfile.getString(userID, "");

        return userSavedID;
         */
        return acc_id;
    }

    public void getUserPictures(Context context)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedProfilePictures = userProfile.getString(userProfilePictures, defaultProfilePictureUrl);
        userSavedCoverPictures = userProfile.getString(userCoverPictures, defaultCoverPictureUrl);
    }

    public void saveUserProfilePicture(Context context, String userCustomProfilePictures)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedID = userProfile.getString(userID, "");

        SharedPreferences.Editor editor = userProfile.edit();

        editor.putString(userProfilePictures, userCustomProfilePictures);
        editor.commit();

        /**
        try
        {
            URL urlProfilePicture = new URL("http://192.168.42.30/smaq/profile_picture.php");
            String parameters = "image="+userCustomProfilePictures+"&name="+userSavedID;
            loadDatabase(urlProfilePicture,parameters);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
         */
    }

    public void saveUserCoverPicture(Context context, String userCustomCoverPictures)
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(context);

        userSavedID = userProfile.getString(userID, "");

        SharedPreferences.Editor editor = userProfile.edit();

        editor.putString(userCoverPictures, userCustomCoverPictures);
        editor.commit();

        /**
        try
        {
            URL urlCoverPicture = new URL("http://192.168.42.30/smaq/cover_picture.php");
            String parameters = "image="+userCustomCoverPictures+"&name="+userSavedID;
            loadDatabase(urlCoverPicture,parameters);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
         */
    }

    public void loadDatabase(URL url, String parameters)
    {
        ArrayList<HashMap<String, String>> userProfile = new ArrayList<>();
        HttpURLConnection connection;
        OutputStreamWriter request = null;
        String response = null;

        try
        {
            // url = new URL("http://192.168.42.30/smaq/apanel/update_profile.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            /**
             Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
             */
            isr.close();
            reader.close();

        }
        catch (IOException e){
            // Error
            Log.e(TAG, "Couldn't get json from server. error 2");
            // Toast.makeText(context,"Error!!", Toast.LENGTH_LONG).show();

        }

    }
}
