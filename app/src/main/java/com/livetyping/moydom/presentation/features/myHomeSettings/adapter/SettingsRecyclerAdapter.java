package com.livetyping.moydom.presentation.features.myHomeSettings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.CamerasSwitchModel;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.EnergySwitchModel;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 04.12.2017.
 */

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder>
        implements DraggableItemAdapter<SettingsRecyclerAdapter.ViewHolder> {

    public interface OnChangeListener{
        void changedSettings();
    }

    private OnChangeListener mListener;

    private List<EnergySwitchModel> mEnergyList = new LinkedList<>();
    private List<CamerasSwitchModel> mCamerasList = new LinkedList<>();

    public SettingsRecyclerAdapter() {
        super();
        setHasStableIds(true);
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

    @Override
    public long getItemId(int position) {
        // requires static value, it means need to keep the same value
        // even if the item position has been changed.
        if (!mEnergyList.isEmpty() && position < mEnergyList.size()) {
            return mEnergyList.get(position).getType();
        } else if(!mCamerasList.isEmpty() && position < mCamerasList.size()) {
            return mCamerasList.get(position).getCameraId();
        }
        return 0;
    }

    public void setOnChangeListener(OnChangeListener listener){
        mListener = listener;
    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        View dragHandle = holder.mDrag;

        int handleWidth = dragHandle.getWidth();
        int handleHeight = dragHandle.getHeight();
        int handleLeft = dragHandle.getLeft();
        int handleTop = dragHandle.getTop();

        return (x >= handleLeft) && (x < handleLeft + handleWidth) &&
                (y >= handleTop) && (y < handleTop + handleHeight);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (mListener != null) mListener.changedSettings();
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        if (fromPosition == toPosition) {
            return;
        }
        if (!mEnergyList.isEmpty() && fromPosition < mEnergyList.size() && toPosition < mEnergyList.size()) {
            final EnergySwitchModel item = mEnergyList.remove(fromPosition);
            mEnergyList.add(toPosition, item);
        } else if (!mCamerasList.isEmpty() && fromPosition < mCamerasList.size() && toPosition < mCamerasList.size()) {
            final CamerasSwitchModel item = mCamerasList.remove(fromPosition);
            mCamerasList.add(toPosition, item);
        }
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

    class ViewHolder extends AbstractDraggableItemViewHolder {

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
            mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                model.setChecked(isChecked);
                if (mListener != null) mListener.changedSettings();
            });
        }

        private void bindCamerasView(CamerasSwitchModel model){
            mTitle.setText(model.getCameraTitle());
            mSwitch.setChecked(model.isCameraChecked());
            mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                model.setCameraChecked(isChecked);
                if (mListener != null) mListener.changedSettings();
            });
        }
    }
}
