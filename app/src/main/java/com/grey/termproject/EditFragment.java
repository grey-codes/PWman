//from addressbook, AddEditFragment

package com.grey.termproject;

import android.content.ContentValues;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.grey.termproject.data.DatabaseDescription;

public class EditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //declare in main
    public interface EditFragmentListener {
        void onEditCompleted(Uri passwordUri);
    }

    private static final int PASSWORD_LOADER = 0;
    private EditFragmentListener listener;
    private Uri passwordUri;
    private boolean addPassword = true;

    private TextInputLayout siteText;
    private TextInputLayout usernameText;
    private TextInputLayout passwordText;
    private FloatingActionButton savePasswordFAB;
    private FloatingActionButton checkPasswordFAB;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (EditFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true); // fragment has menu items to display

        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        siteText = (TextInputLayout) view.findViewById(R.id.siteText);
        usernameText = (TextInputLayout) view.findViewById(R.id.usernameText);
        passwordText = (TextInputLayout) view.findViewById(R.id.passwordText);

        savePasswordFAB = (FloatingActionButton) view.findViewById(R.id.saveFAB);
        //checkPasswordFAB = (FloatingActionButton) view.findViewById(R.id.checkFAB);

        savePasswordFAB.setOnClickListener(saveFABclicked);
        //checkPasswordFAB.setOnClickListener(checkFABclicked);//this will be the JSON thing
        updateSaveFAB();

        Bundle arguments = getArguments();

        if (arguments != null) {
            addPassword = false;
            passwordUri = arguments.getParcelable(MainActivity.PASSWORD_URI);
        }

        if (passwordUri != null)
            getLoaderManager().initLoader(PASSWORD_LOADER, null, this);

        return view;
    }

    private final TextWatcher nameChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            updateSaveFAB();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void updateSaveFAB() {
        String input = siteText.getEditText().getText().toString();

        if (input.trim().length() != 0)
            savePasswordFAB.show();
        else
            savePasswordFAB.hide();
    }

    private final View.OnClickListener saveFABclicked =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hide the virtual keyboard
                    ((InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getView().getWindowToken(), 0);
                    save(); // save password to the database
                }
            };

    private void save() {
        // create ContentValues object containing password's key-value pairs
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseDescription.Password.COLUMN_ACCOUNT,
                siteText.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Password.COLUMN_USERNAME,
                usernameText.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Password.COLUMN_PASSWORD,
                passwordText.getEditText().getText().toString());



        if (addPassword) {
            // use Activity's ContentResolver to invoke
            // insert on the AddressBookContentProvider
            Uri newPasswordUri = getActivity().getContentResolver().insert(
                    DatabaseDescription.Password.CONTENT_URI, contentValues);

            if (newPasswordUri != null) {
                //in main
                listener.onEditCompleted(newPasswordUri);
            }
        }
        else {
            // use Activity's ContentResolver to invoke
            // insert on the AddressBookContentProvider
            int updatedRows = getActivity().getContentResolver().update(
                    passwordUri, contentValues, null, null);

            if (updatedRows > 0) {
                listener.onEditCompleted(passwordUri);
            }

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case PASSWORD_LOADER:
                return new CursorLoader(getActivity(),
                        passwordUri, // Uri of password to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // if the password exists in the database, display its data
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int account = data.getColumnIndex(DatabaseDescription.Password.COLUMN_ACCOUNT);
            int username = data.getColumnIndex(DatabaseDescription.Password.COLUMN_USERNAME);
            int password = data.getColumnIndex(DatabaseDescription.Password.COLUMN_PASSWORD);


            // fill EditTexts with the retrieved data
            siteText.getEditText().setText(
                    data.getString(account));
            usernameText.getEditText().setText(
                    data.getString(username));
            passwordText.getEditText().setText(
                    data.getString(password));

            updateSaveFAB();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }


}
