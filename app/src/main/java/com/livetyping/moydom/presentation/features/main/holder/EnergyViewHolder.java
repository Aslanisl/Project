package com.livetyping.moydom.presentation.features.main.holder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.EnergySwitchModel;
import com.livetyping.moydom.presentation.utils.CalendarUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnergyViewHolder extends RecyclerView.ViewHolder {
    public @BindView(R.id.item_energy_container) ViewGroup mContainer;
    @BindView(R.id.item_energy_current_period) TextView mCurrentPeriod;
    @BindView(R.id.item_energy_current_date) TextView mCurrentDate;
    @BindView(R.id.item_energy_measure) TextView mMeasure;
    @BindView(R.id.item_energy_price) TextView mPrice;
    @BindView(R.id.item_energy_unit) TextView mUnit;
    @BindView(R.id.item_energy_progress_bar) ProgressBar mProgress;
    @BindView(R.id.item_energy_wave) ImageView mWave;

    public @BindView(R.id.item_energy_month_container) ViewGroup mMonthContainer;
    @BindView(R.id.item_energy_month_current_period) TextView mMonthCurrentPeriod;
    @BindView(R.id.item_energy_month_current_date) TextView mMonthCurrentDate;
    @BindView(R.id.item_energy_month_measure) TextView mMonthMeasure;
    @BindView(R.id.item_energy_month_price) TextView mMonthPrice;
    @BindView(R.id.item_energy_month_unit) TextView mMonthUnit;
    @BindView(R.id.item_energy_month_wave) ImageView mMonthWave;
    @BindView(R.id.item_energy_month_prediction_unit) TextView mPredictionUnit;
    @BindView(R.id.item_energy_month_prediction_measure) TextView mPredictionMeasure;
    @BindView(R.id.item_energy_month_prediction_price) TextView mPredictionPrice;

    private Context mContext;
    private float mTargetCost;

    public EnergyViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void init(float targetCost) {
        mTargetCost = targetCost;
    }

    public void bindCurrentEnergyHolder(CurrentEnergyModel model) {
        bindHolderEmpty();
        if (model != null) {
            mProgress.setVisibility(View.GONE);
            @DrawableRes int background;
            Drawable wave;
            if (model.getCostNowStatus() <= 1) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    background = R.drawable.background_energy_green_ripple;
                } else {
                    background = R.drawable.background_energy_green;
                }
                wave = ContextCompat.getDrawable(mContext, R.drawable.wave_green);
            } else if (model.getCostNowStatus() > 1 && model.getCostNowStatus() <= 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    background = R.drawable.background_energy_yellow_ripple;
                } else {
                    background = R.drawable.background_energy_yellow;
                }
                wave = ContextCompat.getDrawable(mContext, R.drawable.wave_yellow);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    background = R.drawable.background_energy_red_ripple;
                } else {
                    background = R.drawable.background_energy_red;
                }
                wave = ContextCompat.getDrawable(mContext, R.drawable.wave_red);
            }

            mContainer.setBackgroundResource(background);
            mWave.setImageDrawable(wave);
            mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostNow()));
            mMeasure.setText(mContext.getString(R.string.rub_hour));
            mUnit.setText(getEnergyUnit(model.getPowerNow()));
            mCurrentPeriod.setText(R.string.current_energy_power);
            mCurrentDate.setVisibility(View.GONE);
        }
    }

    public void bindHolderEmpty() {
        mContainer.setVisibility(View.VISIBLE);
        mMonthContainer.setVisibility(View.GONE);
        mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_loading_gray));
        mWave.setImageResource(R.drawable.wave_gray);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(mContext, R.color.gray), PorterDuff.Mode.SRC_IN);
    }

    private String getEnergyUnit(float value) {
        String temp = mContext.getString(R.string.energy_measure, value);
        return temp.replace(",", ".");
    }

    public void bindTodayEnergyHolder(TodayEnergyModel model) {
        bindHolderEmpty();
        if (model != null) {
            mProgress.setVisibility(View.GONE);
            bindContainerWaveColor(mContainer, mWave, model.getPowerCost(), EnergySwitchModel.ENERGY_TYPE_TODAY);
            mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getPowerCost()));
            mMeasure.setText(mContext.getString(R.string.rub));
            mUnit.setText(getEnergyUnit(model.getPower()));
            mCurrentPeriod.setText(R.string.today);
            mCurrentDate.setText(CalendarUtils.getCurrentDayString());
            mCurrentDate.setVisibility(View.VISIBLE);
        }
    }

    private void bindContainerWaveColor(ViewGroup container, ImageView wave,
                                        float cost, int type) {
        float targetCost = 0;
        switch (type) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                // cuz 30 days in month approx
                targetCost = mTargetCost / 30;
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                // cuz 4 week in month approx
                targetCost = mTargetCost / 4;
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                targetCost = mTargetCost;
                break;
        }
        if (targetCost != 0 && cost != 0) {
            float percent = cost / targetCost;
            if (percent >= 1) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_red_ripple));
                } else {
                    container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_red));
                }
                wave.setImageResource(R.drawable.wave_red);
            } else if (percent < 1 && percent >= 0.6) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_yellow_ripple));
                } else {
                    container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_yellow));
                }
                wave.setImageResource(R.drawable.wave_yellow);
            } else if (percent < 0.6) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green_ripple));
                } else {
                    container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green));
                }
                wave.setImageResource(R.drawable.wave_green);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green_ripple));
            } else {
                container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green));
            }
            wave.setImageResource(R.drawable.wave_green);
        }
    }

    public void bindWeekEnergyHolder(WeekEnergyModel model) {
        bindHolderEmpty();
        if (model != null) {
            mProgress.setVisibility(View.GONE);
            bindContainerWaveColor(mContainer, mWave, model.getWeekPowerCost(), EnergySwitchModel.ENERGY_TYPE_WEEK);
            mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getWeekPowerCost()));
            mMeasure.setText(mContext.getString(R.string.rub));
            mUnit.setText(getEnergyUnit(model.getWeekPower()));
            mCurrentPeriod.setText(R.string.this_week);
            mCurrentDate.setText(model.getWeekDate());
            mCurrentDate.setVisibility(View.VISIBLE);
        }
    }

    public void bindMonthEnergyHolder(MonthEnergyModel model, boolean withAdvice) {
        bindHolderEmpty();
        if (model != null) {
            if (!withAdvice) {
                mProgress.setVisibility(View.GONE);
                bindContainerWaveColor(mContainer, mWave, model.getCostMonth(), EnergySwitchModel.ENERGY_TYPE_THIS_MONTH);
                mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostMonth()));
                mMeasure.setText(mContext.getString(R.string.rub));
                mUnit.setText(getEnergyUnit(model.getPowerMonth()));
                mCurrentPeriod.setText(R.string.this_month);
                mCurrentDate.setText(CalendarUtils.getCurrentMonthText());
                mCurrentDate.setVisibility(View.VISIBLE);
            } else {
                mContainer.setVisibility(View.GONE);
                mMonthContainer.setVisibility(View.VISIBLE);
                bindContainerWaveColor(mMonthContainer, mMonthWave, model.getCostMonth(), EnergySwitchModel.ENERGY_TYPE_THIS_MONTH);
                mMonthPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostMonth()));
                mMonthMeasure.setText(mContext.getString(R.string.rub));
                mMonthUnit.setText(mContext.getString(R.string.energy_measure, model.getPowerMonth()));
                mMonthCurrentPeriod.setText(R.string.this_month);
                mMonthCurrentDate.setText(CalendarUtils.getCurrentMonthText());
                mPredictionPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostTotal()));
                mPredictionMeasure.setText(mContext.getString(R.string.rub));
                mPredictionUnit.setText(getEnergyUnit(model.getPowerTotal()));
            }
        }
    }
}
