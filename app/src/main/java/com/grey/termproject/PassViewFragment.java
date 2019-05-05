//DetailFragment from address
//uses fragment_pass_view

package com.grey.termproject;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grey.termproject.data.DatabaseDescription.Password;

import org.w3c.dom.Text;

public class PassViewFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>
{
    public interface PassViewFragmentListener
    {
        void onContactDeleted();
        void onEditContact(Uri contactUri);
    }

    private static final int CONTACT_LOADER = 0;
    private PassViewFragmentListener listener;
    private Uri contactUri;
    private TextView siteText;
    private TextView usernameText;
    private TextView passwordText;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PassViewFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // this fragment has menu items to display

        // get Bundle of arguments then extract the contact's Uri
        Bundle arguments = getArguments();

        if (arguments != null)
            contactUri = arguments.getParcelable(MainActivity.PASSWORD_URI);

        // inflate DetailFragment's layout
        View view =
                inflater.inflate(R.layout.fragment_pass_view, container, false);

        // get the EditTexts
        siteText = (TextView) view.findViewById(R.id.siteText);
        usernameText = (TextView) view.findViewById(R.id.usernameText);
        passwordText = (TextView) view.findViewById(R.id.passwordText);


        // load the contact
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_passview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditContact(contactUri); // pass Uri to listener
                return true;
            case R.id.action_delete:
                deleteContact();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteContact() {
        // modified from textbook, now using AlertDialog to do the checking instead of dialog fragment
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to delete?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(getActivity(),"You clicked yes button",Toast.LENGTH_LONG).show();
                        getActivity().getContentResolver().delete(
                                contactUri, null, null);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;

        switch (id) {
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        contactUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // if the contact exists in the database, display its data
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int site = data.getColumnIndex(Password.COLUMN_ACCOUNT);
            int user = data.getColumnIndex(Password.COLUMN_USERNAME);
            int pass = data.getColumnIndex(Password.COLUMN_PASSWORD);


            siteText.setText(data.getString(site));
            usernameText.setText(data.getString(user));
            passwordText.setText(data.getString(pass));


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }


}

