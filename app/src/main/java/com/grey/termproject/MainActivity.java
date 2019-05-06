package com.grey.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;

import com.grey.termproject.data.DatabaseDescription;

public class MainActivity extends AppCompatActivity
        implements ListFragment.PasswordsFragmentListener,
        PassViewFragment.PassViewFragmentListener,
        EditFragment.EditFragmentListener {
    // key for storing a password's Uri in a Bundle passed to a fragment
    public static final String PASSWORD_URI = "password_uri";

    private ListFragment passwordsFragment; // displays password list

    // display PasswordsFragment when MainActivity first loads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // if layout contains fragment_container, the phone layout is in use;
        // create and display a PasswordsFragment
        if (savedInstanceState == null &&
                findViewById(R.id.fragment_container) != null) {
            // create PasswordsFragment
            passwordsFragment = new ListFragment();

            // add the fragment to the FrameLayout
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, passwordsFragment);
            transaction.commit(); // display PasswordsFragment
        }
    }


    // display DetailFragment for selected password
    @Override
    public void onPasswordSelected(Uri passwordUri) {
        if (findViewById(R.id.fragment_container) != null) // phone
            displayPassword(passwordUri, R.id.fragment_container);
    }

    // display AddEditFragment to add a new password
    @Override
    public void onAddPassword() {
        if (findViewById(R.id.fragment_container) != null) // phone
            displayAddEditFragment(R.id.fragment_container, null);
    }


    // display a password

    private void displayPassword(Uri passwordUri, int viewID) {
        PassViewFragment detailFragment = new PassViewFragment();

        // specify password's Uri as an argument to the DetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(PASSWORD_URI, passwordUri);
        detailFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
    }


    // display fragment for adding a new or editing an existing password
    private void displayAddEditFragment(int viewID, Uri passwordUri) {
        EditFragment addEditFragment = new EditFragment();

        // if editing existing password, provide passwordUri as an argument
        if (passwordUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(PASSWORD_URI, passwordUri);
            addEditFragment.setArguments(arguments);
        }

        // use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes AddEditFragment to display
    }


    // return to password list when displayed password deleted
    @Override
    public void onPasswordDeleted() {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        passwordsFragment.updatePasswordList(); // refresh passwords
    }

    // update GUI after new password or updated password saved
    @Override
    public void onEditCompleted(Uri passwordUri) {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        passwordsFragment.updatePasswordList(); // refresh passwords
    }

    @Override
    public void onEditPassword(Uri passwordUri) {
        if (findViewById(R.id.fragment_container) != null)
            displayAddEditFragment(R.id.fragment_container, passwordUri);
    }

    @Override
    public void onValidatePassword(Uri passwordUri) {
        Intent i = new Intent(getBaseContext(),CheckCompromised.class);
        CursorLoader cursorLoader = new CursorLoader(this,
                passwordUri, // Uri of password to display
                null, // null projection returns all columns
                null, // null selection returns all rows
                null, // no selection arguments
                null); // sort order
        Cursor cur = cursorLoader.loadInBackground();
        // if the password exists in the database, display its data
        if (cur != null && cur.moveToFirst()) {
            // get the column index for each data item
            int pass = cur.getColumnIndex(DatabaseDescription.Password.COLUMN_PASSWORD);

            final String passwd=cur.getString(pass);
            i.putExtra("password",passwd);
            startActivity(i);
        }
    }

}
