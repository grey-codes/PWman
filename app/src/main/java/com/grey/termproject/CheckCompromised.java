package com.grey.termproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

public class CheckCompromised extends AppCompatActivity {

    String hash;
    TextView compStat;
    TextView compDet;
    Button retBtn;

    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private class GetPasswordJSON
            extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line+"\n");
                        }
                    }
                    catch (IOException e) {
                        //Toast.makeText(getApplicationContext(),"READ ERROR",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    return builder.toString();
                }
                else {
                    //Toast.makeText(getApplicationContext(),"CONNECT ERROR",Toast.LENGTH_SHORT).show();
                    System.out.println("invalid http code");
                    System.out.println(response);
                }
            }
            catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"CONNECT ERROR",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            finally {
                connection.disconnect(); // close the HttpURLConnection
            }

            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(String data) {
            checkPassword(data); // repopulate weatherList
        }
    }

    private void checkPassword(String response) {
        response=response.toLowerCase();
        String hashSuffix=hash.substring(7);
        System.out.println("hash sub:");
        System.out.println(hashSuffix);
        String lines[] = response.split("[\r\n\t ]");
        System.out.println("Found " + lines.length + " passwords");
        int count=0;

        for (String line: lines) {
            String split[] = line.split(":");
            if (split.length<=1)
                continue;
            if (split[0].contains(hashSuffix)) {
                System.out.println("Found password!");
                count=Integer.parseInt(split[1]);
            }
            //System.out.println(split[0] + "//" + split[1]);
        }

        if (count>0) {
            compStat.setText("Compromised!");
            compDet.setText("Password appears in "+count+" breaches");
        } else {
            compStat.setText("Uncompromised!");
            compDet.setText("Password appears in "+0+" breaches");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_compromised);

        compStat = (TextView) findViewById(R.id.compHeader);
        compDet = (TextView) findViewById(R.id.compDetail);
        retBtn = (Button) findViewById(R.id.backButton);

                Intent i = getIntent();
        String pass = i.getStringExtra("password");
        if (pass == null)
            pass = "password";

        hash = encryptPassword(pass);
        hash=hash.toLowerCase();

        System.setProperty("http.agent", "android lmao");
        GetPasswordJSON pw = new GetPasswordJSON();
        try {
            System.out.println("https://api.pwnedpasswords.com/range/" + hash.substring(0, 5));
            pw.execute(new URL("https://api.pwnedpasswords.com/range/" + hash.substring(0, 5)));
        } catch (Exception e) {
            //do nothing
        }

        retBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
