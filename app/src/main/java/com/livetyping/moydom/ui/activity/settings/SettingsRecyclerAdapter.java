package com.livetyping.moydom.ui.activity.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.utils.ItemTouchMoveHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 04.12.2017.
 */

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder> implements ItemTouchMoveHelper {

    private List<EnergySwitchModel> mEnergyList;
    private List<CamerasSwitchModel> mCamerasList;

    private OnDragStartListener mDragStartListener;

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

    public SettingsRecyclerAdapter() {
        mEnergyList = new ArrayList<>();
        mCamerasList = new ArrayList<>();
    }

    public void addEnergySettings(List<EnergySwitchModel> models){
        mEnergyList.clear();
        mEnergyList.addAll(models);
        notifyDataSetChanged();
    }

    public void addCamerasSettings(List<CamerasSwitchModel> models){
        mCamerasList.clear();
        mCamerasList.addAll(models);
        notifyDataSetChanged();
    }

    public void setOnDragListener(OnDragStartListener listener){
        mDragStartListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings_switch, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!mEnergyList.isEmpty() && position < mEnergyList.size()) {
            EnergySwitchModel model = mEnergyList.get(position);
            holder.bindView(model);
        } else if(!mCamerasList.isEmpty() && position < mCamerasList.size()) {
            CamerasSwitchModel model = mCamerasList.get(position);
            holder.bindCamerasView(model);
        }
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder fromHolder, RecyclerView.ViewHolder toHolder) {
        int fromPosition = fromHolder.getAdapterPosition();
        int toPosition = toHolder.getAdapterPosition();
        if (!mEnergyList.isEmpty() && fromPosition < mEnergyList.size() && toPosition < mEnergyList.size()) {
            Collections.swap(mEnergyList, fromPosition, toPosition);
        } else if (!mCamerasList.isEmpty() && fromPosition < mCamerasList.size() && toPosition < mCamerasList.size()){
            Collections.swap(mCamerasList, fromPosition, toPosition);
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        if (!mEnergyList.isEmpty()){
            return mEnergyList.size();
        } else if (!mCamerasList.isEmpty()){
            return mCamerasList.size();
        } else return 0;
    }

    public List<EnergySwitchModel> getEnergyList() {
        return mEnergyList;
    }

    public List<CamerasSwitchModel> getCamerasList() {
        return mCamerasList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_settings_drag) ImageView mDrag;
        @BindView(R.id.item_settings_title) TextView mTitle;
        @BindView(R.id.item_settings_switch) Switch mSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindView(EnergySwitchModel model){
            mTitle.setText(model.getTitle());
            mSwitch.setChecked(model.isChecked());
            mDrag.setOnTouchListener((v, event) ->  {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (mDragStartListener != null) {
                            mDragStartListener.onDragStarted(ViewHolder.this);
                        }
                    }
                    return false;
            });
            mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> model.setChecked(isChecked));
        }

        private void bindCamerasView(CamerasSwitchModel model){
            mTitle.setText(model.getCameraTitle());
            mSwitch.setChecked(model.isCameraChecked());
            mDrag.setOnTouchListener((v, event) ->  {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mDragStartListener != null) {
                        mDragStartListener.onDragStarted(ViewHolder.this);
                    }
                }
                return false;
            });
            mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> model.setCameraChecked(isChecked));
        }
    }
}
