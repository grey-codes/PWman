//contactsAdapter
package com.grey.termproject;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grey.termproject.data.DatabaseDescription.Password;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{


    public interface ContactClickListener {
        void onClick(Uri contactUri);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView textView;
        private long rowID;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            // attach listener to itemView
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        // executes when the contact in this ViewHolder is clicked
                        @Override
                        public void onClick(View view) {
                            clickListener.onClick(Password.buildPasswordUri(rowID));
                        }
                    }
            );
        }
        public void setRowID(long rowID) {
            this.rowID = rowID;
        }
    }
    private Cursor cursor = null;
    private final ContactClickListener clickListener;

    public Adapter(ContactClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the android.R.layout.simple_list_item_1 layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view); // return current item's ViewHolder
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(Password._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(
                Password.COLUMN_ACCOUNT)));
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    // swap this adapter's current Cursor for a new one
    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }


}
