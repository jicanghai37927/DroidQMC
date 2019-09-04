package io.bunnyblue.droidncm.finder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.haiyunshan.droidqmc.R;
import io.bunnyblue.droidncm.finder.LocalFileFragment.OnListFragmentInteractionListener;
import io.bunnyblue.droidncm.finder.dummy.NCMFileContent;
import io.bunnyblue.droidncm.finder.dummy.NCMFileContent.NCMLocalFile;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NCMLocalFile} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLocalFileRecyclerViewAdapter extends RecyclerView.Adapter<MyLocalFileRecyclerViewAdapter.ViewHolder> {

    private final List<NCMLocalFile> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyLocalFileRecyclerViewAdapter(NCMFileContent ncmFileContent, OnListFragmentInteractionListener listener) {
        mValues = ncmFileContent == null ? new ArrayList<NCMLocalFile>() : ncmFileContent.getITEMS();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_localfile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NCMLocalFile ncmLocalFile = mValues.get(position);
        holder.mItem = ncmLocalFile;
        holder.mIdView.setText(ncmLocalFile.id);
        if (TextUtils.isEmpty(ncmLocalFile.error)) {
            if (TextUtils.isEmpty(ncmLocalFile.targetPath)) {
                holder.mContentView.setText(ncmLocalFile.content);
            } else {
                holder.mContentView.setText(ncmLocalFile.targetPath);
            }
        }else {
            holder.mContentView.setText(ncmLocalFile.error);
        }
        holder.mPath.setText(ncmLocalFile.localPath);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteractionLongClick(holder.mItem);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mPath;
        public NCMLocalFile mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mPath = view.findViewById(R.id.ncmPath);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
