package com.smaq.apps.smaqv01.Important;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smaq.apps.smaqv01.Constants;
import com.smaq.apps.smaqv01.MainActivity;
import com.smaq.apps.smaqv01.R;
import com.smaq.apps.smaqv01.UserProfile;

import org.json.JSONArray;
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
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    ImageButton loginBig, loginFb, loginGoogle, registerMainButton, registerNowMainButton;
    EditText registerName, registerEmail, registerPassword, registerConfirmPassword, registerPhone;
    AutoCompleteTextView registerSchool;

    SharedPreferences installationState;
    SharedPreferences userSavedProfile;

    final String welcomeState = "welcomeScreenShown";

    boolean registrationPage;

    private String TAG = LoginActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> userProfile;

    final String registerUrl = Constants.ROOT_URL+"apanel/register.php";
    final String loginUrl = Constants.ROOT_URL+"apanel/login.php";

    private ProgressDialog loading;
    private List<String> schoolList = new ArrayList<String>();
    private String getSchoolsUrl = Constants.ROOT_URL+"apanel/get_schools.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrationPage = false;

        userProfile = new ArrayList<>();

        SchoolList();

        addListenerOnButton();
        addListenerOnText();
    }

    @Override
    public void onBackPressed() {
        if(registrationPage)
        {
            final ConstraintLayout loginContainer = (ConstraintLayout) findViewById(R.id.loginContainer);
            final ConstraintLayout registerContainer = (ConstraintLayout) findViewById(R.id.registerContainer);
            final ConstraintLayout registerNowContainer = (ConstraintLayout) findViewById(R.id.registerNowContainer);

            loginContainer.setVisibility(View.VISIBLE);
            registerContainer.setVisibility(View.INVISIBLE);
            registerNowContainer.setVisibility(View.INVISIBLE);
            registrationPage = false;
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void addListenerOnButton()
    {
        loginBig = (ImageButton) findViewById(R.id.loginBig);
        loginFb = (ImageButton) findViewById(R.id.loginFb);
        loginGoogle = (ImageButton) findViewById(R.id.loginGoogle);
        registerMainButton = (ImageButton) findViewById(R.id.registerButtonMain);
        registerNowMainButton = (ImageButton) findViewById(R.id.registerNowButtonMain);

        final Context context = this;

        loginBig.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                EditText loginPhone = (EditText) findViewById(R.id.loginPhone);
                EditText loginPassword = (EditText) findViewById(R.id.loginPassword);
                final String phone = loginPhone.getText().toString();
                final String password = loginPassword.getText().toString();

//                new SigninActivity(context,1).execute(phone,password);
//                authenticateAccount(phone, password);
//                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
//                LoginActivity.this.startActivity(myIntent);
//                LoginActivity.this.finish();

                loading =  ProgressDialog.show(LoginActivity.this, "Working..", "Downloading Data...", true, false);

                Thread loginThread = new Thread(){
                    public void run(){
                        tryLogin(phone,password);
                    }
                };
                loginThread.start();
            }

        });

        loginFb.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
                LoginActivity.this.finish();
                setInstallationState();
            }

        });

        loginGoogle.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
                LoginActivity.this.finish();
                setInstallationState();
            }

        });

        registerMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRegistration();
            }
        });

        registerNowMainButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
                LoginActivity.this.finish();
                setInstallationState();
            }

        });
    }

    private void addListenerOnText()
    {
        TextView loginSignup = (TextView) findViewById(R.id.loginSignup);

        final ConstraintLayout loginContainer = (ConstraintLayout) findViewById(R.id.loginContainer);
        final ConstraintLayout registerContainer = (ConstraintLayout) findViewById(R.id.registerContainer);
        final ConstraintLayout registerNowContainer = (ConstraintLayout) findViewById(R.id.registerNowContainer);

        loginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginContainer.setVisibility(View.INVISIBLE);
                registerContainer.setVisibility(View.VISIBLE);
                registerNowContainer.setVisibility(View.INVISIBLE);
                registrationPage = true;
            }
        });
    }

    public void saveRegistration()
    {
        registerSchool = (AutoCompleteTextView) findViewById(R.id.autoCompleteSchool);
        registerName = (EditText) findViewById(R.id.registerName);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerConfirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPhone = (EditText) findViewById(R.id.registerPhone);

        final ConstraintLayout loginContainer = (ConstraintLayout) findViewById(R.id.loginContainer);
        final ConstraintLayout registerContainer = (ConstraintLayout) findViewById(R.id.registerContainer);
        final ConstraintLayout registerNowContainer = (ConstraintLayout) findViewById(R.id.registerNowContainer);

        String school = registerSchool.getText().toString();
        String name = registerName.getText().toString();
        String phone = registerPhone.getText().toString();
        String password = registerPassword.getText().toString();
        String confirmpassword = registerConfirmPassword.getText().toString();
        String email = registerEmail.getText().toString();
        String status = "insert status here";

        // saving to server database
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "txtName="+name+"&txtPhone="+phone+"&txtEmail="+email+"&txtPassword="+password+"&txtSchool="+school;

        if(password.equals(confirmpassword))
        {
            try {
                url = new URL(registerUrl);
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
//                else if (response != null && response != "invalid" && response != "")
                if (!response.equals("invalid\n") && !response.equals("\n"))
                {
                    if(!response.equals("existed\n"))
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
                            UserProfile userProfile = new UserProfile();
                            userProfile.saveUserProfile(this, name, phone, password, email);
                            userProfile.saveUserStatus(this, name, status);
                            userProfile.saveUserID(this, acc_id);
                            userSavedProfile = PreferenceManager.getDefaultSharedPreferences(this);
                            userSavedProfile.edit().putString("userSavedSchooltitle", school);
                            userSavedProfile.edit().putString("userSavedSchooladdress", acc_schoolcity);
                            Log.e(TAG, "userSavedSchooltitle: "+school);
                            Log.e(TAG, "userSavedSchooladdress: "+acc_schoolcity);

                            Toast.makeText(this," Registration Successful \n Pendaftaran Berhasil ", Toast.LENGTH_LONG).show();

                            // input sms code
                            /*
                            loginContainer.setVisibility(View.INVISIBLE);
                            registerContainer.setVisibility(View.INVISIBLE);
                            registerNowContainer.setVisibility(View.VISIBLE);
                            */

                            // TEMPORARY - waiting for SMS gateway
                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(myIntent);
                            LoginActivity.this.finish();
                            setInstallationState();
                        }
                        catch (final JSONException e)
                        {
                            final String echo = response;
                            Log.e(TAG, "Json parsing error: " + echo);
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getApplicationContext(),
                                            " Registration Failed \n" +
                                                    " Pendaftaran Gagal \n" + echo,
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }
                    }
                    else
                    {
                        Toast.makeText(this," Phone Number Already Registered \n Nomor Telepon Sudah Terdaftar ", Toast.LENGTH_LONG).show();
                        isr.close();
                        reader.close();
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
                                    "Couldn't retrieve data from server",
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

    protected void tryLogin(String mPhone, String mPassword)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;

        try
        {
            url = new URL(loginUrl);
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
//            if (response != null && response != "invalid" && response != "")
            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
//                    JSONArray profiles = jsonObj.getJSONArray("profile");

                    // looping through All Contacts
//                    for (int i = 0; i < profiles.length(); i++)
                    {
                        JSONObject c = jsonObj.getJSONObject("profile");
//                        JSONObject c = profiles.getJSONObject(i);

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

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

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

                        // save to local data
                        UserProfile userProfile = new UserProfile();
                        userProfile.saveUserProfile(this, acc_name, acc_phone, acc_password, acc_email);
                        userProfile.saveUserStatus(this, acc_name, acc_status);
                        userProfile.saveUserSchoolInfo(this, acc_schooltitle, acc_class, acc_major, acc_schoolcity);
                        userProfile.saveUserBio(this, acc_phone, acc_email, acc_history, acc_birthdate, acc_organization, acc_interest);
                        userProfile.saveUserID(this, acc_id);

                        //Toast.makeText(this," Login Successful \n Login Berhasil ", Toast.LENGTH_LONG).show();

                        LoginActivity.this.loading.dismiss();

                        // start main activity
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(myIntent);
                        LoginActivity.this.finish();
                        setInstallationState();
                    }
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Log.e(TAG, response);
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
                    LoginActivity.this.loading.dismiss();

                }
            }
            else if(response.equals("invalid\n"))
            {
                Log.e(TAG, "Invalid Input");
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),
                                " Wrong Login \n Login Salah ",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
                LoginActivity.this.loading.dismiss();

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
                                "Couldn't retrieve data from server",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
                LoginActivity.this.loading.dismiss();

            }
            /**
            Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
             */
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            Toast.makeText(this,"Error!!\n"+loginUrl, Toast.LENGTH_LONG).show();
        }
    }

    private void setInstallationState()
    {
        installationState = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = installationState.edit();
        editor.putBoolean(welcomeState, true);
        editor.commit(); // Very important to save the preference
    }

    protected void SchoolList()
    {
            GetSchoolList();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, schoolList);
            AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteSchool);
            textView.setAdapter(adapter);
    }

    protected void GetSchoolList()
    {
        schoolList.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "";

        try
        {
            url = new URL(getSchoolsUrl);
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

            response = sb.toString();

            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
                    JSONArray trades = new JSONArray(response);

                    for (int i = 0; i < trades.length(); i++)
                    {
                        JSONObject c = trades.getJSONObject(i);

                        String sch_title = c.getString("sch_title");

                        schoolList.add(sch_title);//temporary
                    }
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Log.e(TAG, response);
                }
            }
            else if(response.equals("invalid\n"))
            {
                Log.e(TAG, "Invalid Input");
            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
            }

            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

}
