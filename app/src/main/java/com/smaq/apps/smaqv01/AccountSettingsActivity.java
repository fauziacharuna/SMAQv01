package com.smaq.apps.smaqv01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.smaq.apps.smaqv01.Important.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AccountSettingsActivity extends AppCompatActivity {

    ImageButton cancelButton, saveButton, logoutButton;
    String userSavedProfileName, userSavedProfilePhone, userSavedProfilePassword, userSavedProfileEmail;
    EditText fieldName, fieldPassword, fieldConfirmPassword, fieldPhone, fieldEmail;

    SharedPreferences installationState;
    final String welcomeState = "welcomeScreenShown";

    private String TAG = AccountSettingsActivity.class.getSimpleName();

    final String updateProfileUrl = Constants.ROOT_URL+"apanel/update_profile.php";

    ArrayList<HashMap<String, String>> userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        userProfile = new ArrayList<>();
        getUserProfile();

        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        cancelButton = (ImageButton) findViewById(R.id.profileSettingCancel);
        saveButton = (ImageButton) findViewById(R.id.profileSettingSave);
        logoutButton = (ImageButton) findViewById(R.id.profileSettingLogout);

        cancelButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
//                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
//                MainActivity.this.startActivity(myIntent);
                AccountSettingsActivity.this.finish();
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
//                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
//                MainActivity.this.startActivity(myIntent);
                saveUserProfile();
            }

        });

        logoutButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                setInstallationState();
                Intent myIntent = new Intent(AccountSettingsActivity.this, LoginActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                AccountSettingsActivity.this.startActivity(myIntent);
                AccountSettingsActivity.this.finish();
            }

        });
    }

    public void getUserProfile()
    {
        UserProfile profile = new UserProfile();
        profile.getUserProfile(this);
        userSavedProfileName = profile.userSavedProfileName;
        userSavedProfilePassword = profile.userSavedProfilePassword;
        userSavedProfilePhone = profile.userSavedProfilePhone;
        userSavedProfileEmail = profile.userSavedProfileEmail;

        fieldName = (EditText) findViewById(R.id.settingFieldUsername);
        fieldPassword = (EditText) findViewById(R.id.settingFieldPassword);
        fieldConfirmPassword = (EditText) findViewById(R.id.settingFieldPasswordConfirm);
        fieldPhone = (EditText) findViewById(R.id.settingFieldPhone);
        fieldEmail = (EditText) findViewById(R.id.settingFieldEmail);

        fieldName.setText(userSavedProfileName);
        fieldPassword.setText(userSavedProfilePassword);
        fieldConfirmPassword.setText(userSavedProfilePassword);
        fieldPhone.setText(userSavedProfilePhone);
        fieldEmail.setText(userSavedProfileEmail);
    }

    public void saveUserProfile()
    {
        fieldName = (EditText) findViewById(R.id.settingFieldUsername);
        fieldPassword = (EditText) findViewById(R.id.settingFieldPassword);
        fieldConfirmPassword = (EditText) findViewById(R.id.settingFieldPasswordConfirm);
        fieldPhone = (EditText) findViewById(R.id.settingFieldPhone);
        fieldEmail = (EditText) findViewById(R.id.settingFieldEmail);

        String name = fieldName.getText().toString();
        String phone = fieldPhone.getText().toString();
        String password = fieldPassword.getText().toString();
        String confirmpassword = fieldConfirmPassword.getText().toString();
        String email = fieldEmail.getText().toString();

//        UserProfile profile = new UserProfile();
//        profile.saveUserProfile(this, name, phone, password, email);

        // load user ID
        UserProfile accountProfile = new UserProfile();
        String userID = accountProfile.getUserID(this);

        // saving to server database
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "txtName="+name+"&txtPhone="+phone+"&txtEmail="+email+"&txtPassword="+password+"&txtUserID="+userID;

        if(password.equals(confirmpassword))
        {
            try {
                url = new URL(updateProfileUrl);
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

                // You can perform UI operations here
                if (!response.equals("invalid\n") && !response.equals("\n"))
                {
                    try
                    {
                        JSONObject jsonObj = new JSONObject(response);

                        JSONObject c = jsonObj.getJSONObject("profile");

                        String acc_id = c.getString("acc_id");
                        String acc_studentid = c.getString("acc_studentid");
                        String acc_name = c.getString("acc_name");
                        String acc_password = c.getString("acc_password");
                        String acc_email = c.getString("acc_email");
                        String acc_phone = c.getString("acc_phone");
                        String acc_fullname = c.getString("acc_fullname");
                        String acc_status = c.getString("acc_status");
                        String acc_history = c.getString("acc_history");
                        String acc_birthdate = c.getString("acc_birthdate");
                        String acc_organization = c.getString("acc_organization");
                        String acc_interest = c.getString("acc_interest");
                        String acc_schooltitle = c.getString("acc_schooltitle");
                        String acc_schoolcity = c.getString("acc_schoolcity");
                        String acc_class = c.getString("acc_class");
                        String acc_major = c.getString("acc_major");
                        String acc_profilepicture = c.getString("acc_profilepicture");
                        String acc_coverpicture = c.getString("acc_coverpicture");

                        // tmp hash map for single contact
                        HashMap<String, String> profile = new HashMap<>();

                        // adding each child node to HashMap key => value
                        profile.put("acc_id", acc_id);
                        profile.put("acc_studentid", acc_studentid);
                        profile.put("acc_name", acc_name);
                        profile.put("acc_password", acc_password);
                        profile.put("acc_email", acc_email);
                        profile.put("acc_phone", acc_phone);
                        profile.put("acc_fullname", acc_fullname);
                        profile.put("acc_status", acc_status);
                        profile.put("acc_history", acc_history);
                        profile.put("acc_birthdate", acc_birthdate);
                        profile.put("acc_organization", acc_organization);
                        profile.put("acc_interest", acc_interest);
                        profile.put("acc_schooltitle", acc_schooltitle);
                        profile.put("acc_schoolcity", acc_schoolcity);
                        profile.put("acc_class", acc_class);
                        profile.put("acc_major", acc_major);
                        profile.put("acc_profilepicture", acc_profilepicture);
                        profile.put("acc_coverpicture", acc_coverpicture);

                        // adding contact to contact list
                        userProfile.add(profile);

                        // save to local preference
                        accountProfile.saveUserProfile(this, name, phone, password, email);
                        accountProfile.saveUserID(this, acc_id);

                        Toast.makeText(this," Profile Changed \n Profil Berhasil Diubah ", Toast.LENGTH_LONG).show();
                        AccountSettingsActivity.this.finish();
                    }
                    catch (final JSONException e)
                    {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't retrieve data from server: \n" + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                }
                else
                {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(),
                                    " Failed to Update Profile \n Gagal Mengubah Profil ",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
                /**
                Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
                 */
                isr.close();
                reader.close();

            }
            catch (IOException e){
                // Error
                Toast.makeText(this,"Error!!", Toast.LENGTH_LONG).show();

            }
        }
        else
        {
            Toast.makeText(this,"Password mismatch", Toast.LENGTH_LONG).show();
        }
    }

    private void setInstallationState()
    {
        installationState = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = installationState.edit();
        editor.putBoolean(welcomeState, false);
        editor.commit(); // Very important to save the preference
    }
}
