//from addressbook, AddEditFragment

package com.grey.termproject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class EditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    //declare in main
    public interface EditFragmentListener{
        void onEditCompleted(Uri contactUri);
    }

    private static final int CONTACT_LOADER = 0;
    private EditFragmentListener listener;
    private Uri contactUri;
    private boolean addPassword = true;

    private TextInputLayout siteText;
    private TextInputLayout usernameText;
    private TextInputLayout passwordText;

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

        View view = inflater.inflate(R.layout.fragment_edit,container,false);
        siteText= (TextInputLayout)view.findViewById(R.id.editText3);
        usernameText=(TextInputLayout)view.findViewById(R.id.editText);
        passwordText = (TextInputLayout)view.findViewById(R.id.editText2);


    }
}
