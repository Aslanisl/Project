package com.livetyping.moydom.presentation.features.main.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.cameras.CameraModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 08.12.2017.
 */

public class CameraMyHomeAdapter extends RecyclerView.Adapter<CameraMyHomeAdapter.ViewHolder> {

    private List<CameraModel> mCameras = new ArrayList<>();
    private Context mContext;

    public CameraMyHomeAdapter(Context context) {
        mContext = context;
    }

    public void addCameras(List<CameraModel> cameras){
        mCameras.clear();
        mCameras.addAll(cameras);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CameraModel model = mCameras.get(position);
        holder.bindHolder(model);
    }

    @Override
    public int getItemCount() {
        return mCameras.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_camera_preview) ImageView mPreview;
        @BindView(R.id.item_camera_play) ImageView mPlay;
        @BindView(R.id.item_camera_location) TextView mLocation;
        @BindView(R.id.item_camera_online) TextView mOnline;
        @BindView(R.id.item_camera_loading) ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolder(CameraModel model){
            if (model.getCameraStatus() == CameraModel.CAMERA_ONLINE){
                bindOnline(true);
            } else {
                bindOnline(false);
            }

            mLocation.setText(model.getCameraName());
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_IN );
        }

        private void bindOnline(boolean isOnline){
            if (isOnline){
                mOnline.setText(R.string.camera_online);
                mOnline.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.indicator), null, null, null);
            } else {
                mOnline.setText(R.string.camera_offline);
                mOnline.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }
}
