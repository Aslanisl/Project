package com.livetyping.moydom.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.livetyping.moydom.apiModel.advice.AdviceModel;
import com.livetyping.moydom.apiModel.energy.model.CurrentEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.MonthEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.TodayEnergyModel;
import com.livetyping.moydom.apiModel.energy.model.WeekEnergyModel;
import com.livetyping.moydom.ui.activity.ResourcesActivity;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.utils.CalendarUtils;
import com.livetyping.moydom.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 06.12.2017.
 */

public class EnergyMyHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_ENERGY = 0;
    private final static int VIEW_TYPE_ADVICE = 1;
    private final static int ADVICE_POSITION = 2;

    public interface CloseAdviceListener {
        void closeAdvice(int adviceId);
    }

    private CloseAdviceListener mListener;

    private List<EnergySwitchModel> mEnergyModels = new ArrayList<>();

    private CurrentEnergyModel mCurrentEnergyModel;
    private TodayEnergyModel mTodayEnergyModel;
    private WeekEnergyModel mWeekEnergyModel;
    private MonthEnergyModel mMonthEnergyModel;
    private AdviceModel mAdviceModel;

    private boolean mWithAdvice;

    private Context mContext;
    private boolean mClickable = false;

    public EnergyMyHomeAdapter(Context context, boolean withAdvice) {
        mContext = context;
        mWithAdvice = withAdvice;
        if (withAdvice){
            addDefaultEnergyModels();
        }
    }

    private void addDefaultEnergyModels(){
        mEnergyModels.clear();
        EnergySwitchModel currentMonth = new EnergySwitchModel();
        currentMonth.setType(EnergySwitchModel.ENERGY_TYPE_THIS_MONTH);
        mEnergyModels.add(currentMonth);
        EnergySwitchModel currentWeek = new EnergySwitchModel();
        currentWeek.setType(EnergySwitchModel.ENERGY_TYPE_WEEK);
        mEnergyModels.add(currentWeek);
        EnergySwitchModel today = new EnergySwitchModel();
        today.setType(EnergySwitchModel.ENERGY_TYPE_TODAY);
        mEnergyModels.add(today);
        EnergySwitchModel current = new EnergySwitchModel();
        current.setType(EnergySwitchModel.ENERGY_TYPE_CURRENT);
        mEnergyModels.add(current);
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

    public void setAdviceModel(AdviceModel model){
        mAdviceModel = model;
        notifyItemInserted(ADVICE_POSITION);
    }

    public void setAdviceListener(CloseAdviceListener listener){
        mListener = listener;
    }

    private void updatePositionByType(int type){
        int position = - 1;
        for (int i = 0; i < mEnergyModels.size(); i++){
            if (mEnergyModels.get(i).getType() == type) position = i;
        }
        if (position != -1){
            if (position >= ADVICE_POSITION && mAdviceModel != null){
                position ++;
            }
            notifyItemChanged(position);
        }
    }

    /**
     * Sets whether children should be clickable
     * @param clickable
     */
    public void setIsItemClickable(boolean clickable){
        this.mClickable = clickable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        switch (viewType) {
            case VIEW_TYPE_ENERGY:
                rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_energy, parent, false);
                return new EnergyViewHolder(rootView);
            case VIEW_TYPE_ADVICE:
                rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advice, parent, false);
                return new AdviceViewHolder(rootView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_ENERGY:
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition >= ADVICE_POSITION && mAdviceModel != null){
                    currentPosition --;
                }
                EnergyViewHolder energyHolder = (EnergyViewHolder) holder;
                EnergySwitchModel model = mEnergyModels.get(currentPosition);
                switch (model.getType()) {
                    case EnergySwitchModel.ENERGY_TYPE_CURRENT:
                        energyHolder.bindCurrentEnergyHolder(mCurrentEnergyModel);
                        break;
                    case EnergySwitchModel.ENERGY_TYPE_TODAY:
                        energyHolder.bindTodayEnergyHolder(mTodayEnergyModel);
                        break;
                    case EnergySwitchModel.ENERGY_TYPE_WEEK:
                        energyHolder.bindWeekEnergyHolder(mWeekEnergyModel);
                        break;
                    case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                        energyHolder.bindMonthEnergyHolder(mMonthEnergyModel);
                        break;
                }
                if (mClickable){
                    energyHolder.itemView.setOnClickListener((v) -> {
                        Intent intent = new Intent(mContext, ResourcesActivity.class);
                        intent.putExtra(ResourcesActivity.EXTRA_TYPE, model.getType());
                        mContext.startActivity(intent);
                    });
                }
                break;
            case VIEW_TYPE_ADVICE:
                AdviceViewHolder adviceHolder = (AdviceViewHolder) holder;
                if (mAdviceModel != null) adviceHolder.bindViewHolder(mAdviceModel);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mAdviceModel == null && mEnergyModels.isEmpty()) return 0;
        if (mAdviceModel == null && !mEnergyModels.isEmpty()) return mEnergyModels.size();
        return mEnergyModels.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == ADVICE_POSITION && mAdviceModel != null){
            return VIEW_TYPE_ADVICE;
        }
        return VIEW_TYPE_ENERGY;
    }

    class EnergyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_energy_container) RelativeLayout mContainer;
        @BindView(R.id.item_energy_current_period) TextView mCurrentPeriod;
        @BindView(R.id.item_energy_current_date) TextView mCurrentDate;
        @BindView(R.id.item_energy_measure) TextView mMeasure;
        @BindView(R.id.item_energy_price) TextView mPrice;
        @BindView(R.id.item_energy_unit) TextView mUnit;
        @BindView(R.id.item_energy_progress_bar) ProgressBar mProgress;
        @BindView(R.id.item_energy_wave) ImageView mWave;

        @BindView(R.id.item_energy_month_container) RelativeLayout mMonthContainer;
        @BindView(R.id.item_energy_month_current_period) TextView mMonthCurrentPeriod;
        @BindView(R.id.item_energy_month_current_date) TextView mMonthCurrentDate;
        @BindView(R.id.item_energy_month_measure) TextView mMonthMeasure;
        @BindView(R.id.item_energy_month_price) TextView mMonthPrice;
        @BindView(R.id.item_energy_month_unit) TextView mMonthUnit;
        @BindView(R.id.item_energy_month_wave) ImageView mMonthWave;
        @BindView(R.id.item_energy_month_prediction_unit) TextView mPredictionUnit;
        @BindView(R.id.item_energy_month_prediction_measure) TextView mPredictionMeasure;
        @BindView(R.id.item_energy_month_prediction_price) TextView mPredictionPrice;

        public EnergyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHolderEmpty(){
            mContainer.setVisibility(View.VISIBLE);
            mMonthContainer.setVisibility(View.GONE);
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
                if (!mWithAdvice) {
                    mProgress.setVisibility(View.GONE);
                    mContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green));
                    mWave.setImageResource(R.drawable.wave_green);
                    mPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostMonth()));
                    mMeasure.setText(mContext.getString(R.string.rub));
                    mUnit.setText(mContext.getString(R.string.energy_measure, model.getPowerMonth()));
                    mCurrentPeriod.setText(R.string.this_month);
                    mCurrentDate.setText(CalendarUtils.getCurrentMonthText());
                } else {
                    mContainer.setVisibility(View.GONE);
                    mMonthContainer.setVisibility(View.VISIBLE);
                    mMonthContainer.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_energy_green));
                    mMonthWave.setImageResource(R.drawable.wave_green);
                    mMonthPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostMonth()));
                    mMonthMeasure.setText(mContext.getString(R.string.rub));
                    mMonthUnit.setText(mContext.getString(R.string.energy_measure, model.getPowerMonth()));
                    mMonthCurrentPeriod.setText(R.string.this_month);
                    mMonthCurrentDate.setText(CalendarUtils.getCurrentMonthText());
                    mPredictionPrice.setText(String.format(Locale.getDefault(), "%.0f", model.getCostTotal()));
                    mPredictionMeasure.setText(mContext.getString(R.string.rub));
                    mPredictionUnit.setText(mContext.getString(R.string.energy_measure, model.getPowerTotal()));
                }
            }
        }
    }

    class AdviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_advice_close_tip) ImageView mCloseAdvice;
        @BindView(R.id.item_advice_icon) ImageView mIcon;
        @BindView(R.id.item_advice_title) TextView mTitle;
        @BindView(R.id.item_advice_description) TextView mDescription;

        private int mAdviceId;

        public AdviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCloseAdvice.setOnClickListener(view -> {
                if (mListener != null) mListener.closeAdvice(mAdviceId);
                mAdviceModel = null;
                notifyItemRemoved(ADVICE_POSITION);
            });
        }

        public void bindViewHolder(AdviceModel model){
            mAdviceId = model.getAdviceId();
            GlideApp.with(mContext).load(model.getIconUrl()).into(mIcon);
            mTitle.setText(model.getTitle() != null ? model.getTitle() : " ");
            mDescription.setText(model.getDescription() != null ? model.getDescription() : " ");
        }
    }
}
