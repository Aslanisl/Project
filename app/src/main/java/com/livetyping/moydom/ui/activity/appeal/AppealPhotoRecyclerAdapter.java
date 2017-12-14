package com.livetyping.moydom.ui.activity.appeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.utils.GlideApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 14.12.2017.
 */

public class AppealPhotoRecyclerAdapter extends RecyclerView.Adapter<AppealPhotoRecyclerAdapter.ViewHolder> {

    private List<File> mPhotoFiles = new ArrayList<>();

    public AppealPhotoRecyclerAdapter() {
    }

    public void addFiles(List<File> files){
        int fromPosition = mPhotoFiles.size();
        mPhotoFiles.addAll(files);
        notifyItemRangeInserted(fromPosition, mPhotoFiles.size());
    }

    public void addFile(File file){
        int position = mPhotoFiles.size();
        mPhotoFiles.add(file);
        notifyItemInserted(position);
    }

    public void removeFile(File file){
        int position = mPhotoFiles.indexOf(file);
        if (position != -1){
            mPhotoFiles.remove(file);
            notifyItemRemoved(position);
        }
    }

    public List<File> getPhotoFiles() {
        return mPhotoFiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal_photo, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = mPhotoFiles.get(holder.getAdapterPosition());
        holder.bindHolder(file);
    }

    @Override
    public int getItemCount() {
        return mPhotoFiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_appeal_photo) ImageView mPhoto;
        private Context mContext;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindHolder(File file){
            GlideApp.with(mContext).load(file).into(mPhoto);
        }
    }
}
