package com.livetyping.moydom.presentation.features.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.presentation.features.energyDetails.activity.ResourcesActivity;
import com.livetyping.moydom.presentation.features.main.holder.EnergyViewHolder;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.EnergySwitchModel;
import com.livetyping.moydom.presentation.utils.CalendarUtils;
import com.livetyping.moydom.presentation.utils.GlideApp;

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
    private CloseAdviceListener mListener;
    private List<EnergySwitchModel> mEnergyModels = new ArrayList<>();
    private CurrentEnergyModel mCurrentEnergyModel;
    private TodayEnergyModel mTodayEnergyModel;
    private WeekEnergyModel mWeekEnergyModel;
    private MonthEnergyModel mMonthEnergyModel;
    private AdviceModel mAdviceModel;
    private float mTargetCost;
    private boolean mWithAdvice;
    private Context mContext;
    private boolean mClickable = false;
    public EnergyMyHomeAdapter(Context context, boolean withAdvice) {
        mContext = context;
        mWithAdvice = withAdvice;
        mTargetCost = Prefs.getInstance().getTargetCost();
        if (withAdvice){
            addDefaultEnergyModels();
        }
        setHasStableIds(true);
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
        if (mCurrentEnergyModel == null || !mCurrentEnergyModel.equals(model)){
            mCurrentEnergyModel = model;
            updatePositionByType(EnergySwitchModel.ENERGY_TYPE_CURRENT);
        }
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

    public void addTodayEnergy(TodayEnergyModel model){
        if (mTodayEnergyModel == null || !mTodayEnergyModel.equals(model)){
            mTodayEnergyModel = model;
            updatePositionByType(EnergySwitchModel.ENERGY_TYPE_TODAY);
        }
    }

    public void addWeekEnergy(WeekEnergyModel model){
        if (mWeekEnergyModel == null || !mWeekEnergyModel.equals(model)){
            mWeekEnergyModel = model;
            updatePositionByType(EnergySwitchModel.ENERGY_TYPE_WEEK);
        }
    }

    public void addMonthEnergy(MonthEnergyModel model){
        if (mMonthEnergyModel == null || !mMonthEnergyModel.equals(model)){
            mMonthEnergyModel = model;
            updatePositionByType(EnergySwitchModel.ENERGY_TYPE_THIS_MONTH);
        }
    }

    public void setAdviceModel(AdviceModel model){
        mAdviceModel = model;
        notifyItemInserted(ADVICE_POSITION);
    }

    public void setAdviceListener(CloseAdviceListener listener){
        mListener = listener;
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
                energyHolder.init(mTargetCost);
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
                        energyHolder.bindMonthEnergyHolder(mMonthEnergyModel, mWithAdvice);
                        break;
                }
                if (mClickable){
                    View.OnClickListener listener = (v) -> {
                        Intent intent = new Intent(mContext, ResourcesActivity.class);
                        intent.putExtra(ResourcesActivity.EXTRA_TYPE, model.getType());
                        mContext.startActivity(intent);

                    };
                    energyHolder.mContainer.setOnClickListener(listener);
                    energyHolder.mContainer.setClickable(true);
                    energyHolder.mContainer.setFocusable(true);
                    energyHolder.mMonthContainer.setOnClickListener(listener);
                    energyHolder.mMonthContainer.setClickable(true);
                    energyHolder.mMonthContainer.setFocusable(true);
//                    TypedValue outValue = new TypedValue();
//                    mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
//                    energyHolder.mContainer.setBackgroundResource(outValue.resourceId);
                }
                break;
            case VIEW_TYPE_ADVICE:
                AdviceViewHolder adviceHolder = (AdviceViewHolder) holder;
                if (mAdviceModel != null) adviceHolder.bindViewHolder(mAdviceModel);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == ADVICE_POSITION && mAdviceModel != null){
            return VIEW_TYPE_ADVICE;
        }
        return VIEW_TYPE_ENERGY;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mAdviceModel == null && mEnergyModels.isEmpty()) return 0;
        if (mAdviceModel == null && !mEnergyModels.isEmpty()) return mEnergyModels.size();
        return mEnergyModels.size() + 1;
    }

    class AdviceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_advice_close_tip) ImageView mCloseAdvice;
        @BindView(R.id.item_advice_icon) ImageView mIcon;
        @BindView(R.id.item_advice_title) TextView mTitle;
        @BindView(R.id.item_advice_description) TextView mDescription;

        private int mAdviceId;
        private int mSpacingNormal;
        private int mSpacingLarge;

        public AdviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCloseAdvice.setOnClickListener(view -> {
                if (mListener != null) mListener.closeAdvice(mAdviceId);
                mAdviceModel = null;
                notifyItemRemoved(ADVICE_POSITION);
            });
            mSpacingNormal = (int)mContext.getResources().getDimension(R.dimen.spacing_normal);
            mSpacingLarge = (int)mContext.getResources().getDimension(R.dimen.spacing_large);
        }

        public void bindViewHolder(AdviceModel model){
            mAdviceId = model.getAdviceId();
            GlideApp.with(mContext).load(model.getIconUrl()).into(mIcon);
            mTitle.setText(model.getTitle() != null ? model.getTitle() : " ");
            boolean descriptionEmpty = TextUtils.isEmpty(model.getDescription());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
            if (descriptionEmpty){
                layoutParams.setMargins(mSpacingNormal, 0, mSpacingNormal, mSpacingLarge);
            } else {
                layoutParams.setMargins(mSpacingNormal, 0, mSpacingNormal, 0);
            }
            mDescription.setVisibility(descriptionEmpty ? View.GONE : View.VISIBLE);
            mDescription.setText(descriptionEmpty ? " " : model.getDescription());
        }
    }

    public interface CloseAdviceListener {
        void closeAdvice(int adviceId);
    }
}
