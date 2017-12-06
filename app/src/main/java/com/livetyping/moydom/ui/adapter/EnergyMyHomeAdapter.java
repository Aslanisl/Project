package com.livetyping.moydom.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergyMyHomeAdapter extends RecyclerView.Adapter<EnergyMyHomeAdapter.ViewHolder> {

    private List<EnergySwitchModel> mEnergyModels = new ArrayList<>();

    private Context mContext;

    public EnergyMyHomeAdapter(Context context) {
        mContext = context;
    }

    public void addEnergyModels(List<EnergySwitchModel> models){
        mEnergyModels.clear();
        mEnergyModels.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_energy, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EnergySwitchModel model = mEnergyModels.get(position);
        holder.bindHolder(model);
    }

    @Override
    public int getItemCount() {
        return mEnergyModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_energy_container) RelativeLayout mContainer;
        @BindView(R.id.item_energy_current_period) TextView mCurrentPeriod;
        @BindView(R.id.item_energy_current_date) TextView mCurrentDate;
        @BindView(R.id.item_energy_measure) TextView mMeasure;
        @BindView(R.id.item_energy_price) TextView mPrice;
        @BindView(R.id.item_energy_unit) TextView mUnit;
        @BindView(R.id.item_energy_progress_bar) ProgressBar mProgress;
        @BindView(R.id.item_energy_wave) ImageView mWave;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolder(EnergySwitchModel model){
            if (model.getPrice() == 0 && model.getEnergyWorth() == 0){
                mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_loading_gray));
                mWave.setImageResource(R.drawable.wave_gray);
                mProgress.setVisibility(View.VISIBLE);
                mProgress.getIndeterminateDrawable()
                        .setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.SRC_IN );
            }
        }
    }
}
