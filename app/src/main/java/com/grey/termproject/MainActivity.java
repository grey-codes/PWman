package com.grey.termproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity
        implements ListFragment.OnFragmentInteractionListener,
        EditFragment.EditFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null)
                return;
            ListFragment firstFrag = new ListFragment();
            firstFrag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFrag).commit();
        }
    }


    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onEditCompleted(Uri contactUri) {

    }



   /* @Override
    public void onEditCompleted(Uri uri) {
        // removes top of back stack
        getSupportFragmentManager().popBackStack();
        //Need to change
        contactsFragment.updateContactList(); // refresh contacts
    }*/
}
