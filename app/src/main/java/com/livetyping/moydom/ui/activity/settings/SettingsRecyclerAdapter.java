package com.livetyping.moydom.ui.activity.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.livetyping.moydom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 04.12.2017.
 */

public class SettingsRecyclerAdapter extends RecyclerView.Adapter<SettingsRecyclerAdapter.ViewHolder>{

    private List<SettingsSwitchModel> mSettingsList;

    public SettingsRecyclerAdapter(List<SettingsSwitchModel> settingsList) {
        mSettingsList = settingsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings_switch, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SettingsSwitchModel model = mSettingsList.get(position);
        holder.bindView(model);
    }

    @Override
    public int getItemCount() {
        return mSettingsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_settings_drag) ImageView mDrag;
        @BindView(R.id.item_settings_title) TextView mTitle;
        @BindView(R.id.item_settings_switch) Switch mSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindView(SettingsSwitchModel model){
            mTitle.setText(model.getTitle());
            mSwitch.setChecked(model.isChecked());
        }
    }
}
