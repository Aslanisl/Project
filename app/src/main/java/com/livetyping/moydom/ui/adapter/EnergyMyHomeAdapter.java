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
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergyMyHomeAdapter extends RecyclerView.Adapter<EnergyMyHomeAdapter.ViewHolder> {

    private List<EnergySwitchModel> mEnergyModels = new ArrayList<>();

    private CurrentEnergyModel mCurrentEnergyModel;
    private TodayEnergyModel mTodayEnergyModel;
    private WeekEnergyModel mWeekEnergyModel;
    private MonthEnergyModel mMonthEnergyModel;

    private Context mContext;

    public EnergyMyHomeAdapter(Context context) {
        mContext = context;
    }

    public void addEnergyModels(List<EnergySwitchModel> models){
        mEnergyModels.clear();
        if (models != null){
            for (int i = 0; i < models.size(); i++){
                EnergySwitchModel model = models.get(i);
                if (model.isChecked()) mEnergyModels.add(model);
            }
        }
        notifyDataSetChanged();
    }

    public void addCurrentEnergy(CurrentEnergyModel model){
        mCurrentEnergyModel = model;
        updatePositionByType(EnergySwitchModel.ENERGY_TYPE_CURRENT);
    }

    public void addTodayEnergy(TodayEnergyModel model){
        mTodayEnergyModel = model;
        updatePositionByType(EnergySwitchModel.ENERGY_TYPE_TODAY);
    }

    public void addWeekEnergy(WeekEnergyModel model){
        mWeekEnergyModel = model;
        updatePositionByType(EnergySwitchModel.ENERGY_TYPE_WEEK);
    }

    public void addMonthEnergy(MonthEnergyModel model){
        mMonthEnergyModel = model;
        updatePositionByType(EnergySwitchModel.ENERGY_TYPE_THIS_MONTH);
    }

    private void updatePositionByType(int type){
        int position = - 1;
        for (int i = 0; i < mEnergyModels.size(); i++){
            if (mEnergyModels.get(i).getType() == type) position = i;
        }
        if (position != -1){
            notifyItemChanged(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_energy, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EnergySwitchModel model = mEnergyModels.get(position);
        switch (model.getType()) {
            case EnergySwitchModel.ENERGY_TYPE_CURRENT:
                holder.bindCurrentEnergyHolder(mCurrentEnergyModel);
                break;
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                holder.bindTodayEnergyHolder(mTodayEnergyModel);
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                holder.bindWeekEnergyHolder(mWeekEnergyModel);
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                holder.bindMonthEnergyHolder(mMonthEnergyModel);
                break;
        }
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

        public void bindHolderEmpty(){
            mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_loading_gray));
            mWave.setImageResource(R.drawable.wave_gray);
            mProgress.setVisibility(View.VISIBLE);
            mProgress.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.SRC_IN );
        }

        public void bindCurrentEnergyHolder(CurrentEnergyModel model){
            bindHolderEmpty();
            if (model != null) {
                mProgress.setVisibility(View.GONE);
                mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_yellow));
                mWave.setImageResource(R.drawable.wave_yellow);
                mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostNow()));
                mMeasure.setText(mContext.getString(R.string.rub));
                mUnit.setText(mContext.getString(R.string.energy_measure, model.getPowerNow()));
                mCurrentPeriod.setText(R.string.current_energy_power);
            }
        }

        public void bindTodayEnergyHolder(TodayEnergyModel model){
            bindHolderEmpty();
            if (model != null) {
                mProgress.setVisibility(View.GONE);
                mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_red));
                mWave.setImageResource(R.drawable.wave_red);
                mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getPowerCost()));
                mMeasure.setText(mContext.getString(R.string.rub));
                mUnit.setText(mContext.getString(R.string.energy_measure, model.getPower()));
                mCurrentPeriod.setText(R.string.today);
            }
        }

        public void bindWeekEnergyHolder(WeekEnergyModel model){
            bindHolderEmpty();
            if (model != null) {
                mProgress.setVisibility(View.GONE);
                mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_yellow));
                mWave.setImageResource(R.drawable.wave_yellow);
                mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getWeekPowerCost()));
                mMeasure.setText(mContext.getString(R.string.rub));
                mUnit.setText(mContext.getString(R.string.energy_measure, model.getWeekPower()));
                mCurrentPeriod.setText(R.string.this_week);
                mCurrentDate.setText(model.getWeekDate());
            }
        }

        public void bindMonthEnergyHolder(MonthEnergyModel model){
            bindHolderEmpty();
            if (model != null) {
                mProgress.setVisibility(View.GONE);
                mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green));
                mWave.setImageResource(R.drawable.wave_green);
                mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostTotal()));
                mMeasure.setText(mContext.getString(R.string.rub));
                mUnit.setText(mContext.getString(R.string.energy_measure, model.getPowerMonth()));
                mCurrentPeriod.setText(R.string.this_month);
                mCurrentDate.setText(CalendarUtils.getCurrentMonthText());
            }
        }
    }
}
