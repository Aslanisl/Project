package com.livetyping.moydom.ui.activity.otherSettings;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.myTarget.MyTargetModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 21.12.2017.
 */

public class MyTargetRecyclerAdapter extends RecyclerView.Adapter<MyTargetRecyclerAdapter.ViewHolder> {
    private static final int POSITION_PERCENT_HIGH = 0;
    private static final int POSITION_PERCENT_NORMAL = 1;
    private static final int POSITION_PERCENT_LOW = 2;

    private static final int TARGETS_COUNT = 3;

    private float mCurrentCost;

    private int mSelectedPosition = -1;

    public MyTargetRecyclerAdapter(float currentCost) {
        mCurrentCost = currentCost;
    }

    public void setCurrentCost(float currentCost){
        mCurrentCost = currentCost;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_target, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position){
            case POSITION_PERCENT_HIGH:
                holder.bindHolder(MyTargetModel.PERCENT_HIGH, mCurrentCost);
                break;
            case POSITION_PERCENT_NORMAL:
                holder.bindHolder(MyTargetModel.PERCENT_NORMAL, mCurrentCost);
                break;
            case POSITION_PERCENT_LOW:
                holder.bindHolder(MyTargetModel.PERCENT_LOW, mCurrentCost);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return TARGETS_COUNT;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_my_target_container) RelativeLayout mContainer;
        @BindView(R.id.item_my_target_title) TextView mTitle;
        @BindView(R.id.item_my_target_description) TextView mDescription;
        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            bindHolder();
        }

        private void bindHolder(){
            mContainer.setOnClickListener(view -> {
                view.setSelected(true);
                view.setEnabled(false);
                mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                mDescription.setTextColor(ContextCompat.getColor(mContext, R.color.white_gray));
                int lastPosition = mSelectedPosition;
                mSelectedPosition = getAdapterPosition();
                if (lastPosition != -1) {
                    notifyItemChanged(lastPosition);
                }
            });
        }

        public void bindHolder(float percent, float value){
            int percentAbs = (int)(percent * 100);
            mTitle.setText(mContext.getString(R.string.energy_less_percent, percentAbs));
            int valueAbs = Math.round(value * percent);
            mDescription.setText(mContext.getString(R.string.about_cost_of_your, valueAbs));
            mContainer.setSelected(mSelectedPosition == getAdapterPosition());
            mTitle.setTextColor(mSelectedPosition == getAdapterPosition()
                    ? ContextCompat.getColor(mContext, R.color.white)
                    : ContextCompat.getColor(mContext, R.color.azul));
            mDescription.setTextColor(mSelectedPosition == getAdapterPosition()
                    ? ContextCompat.getColor(mContext, R.color.white_gray)
                    : ContextCompat.getColor(mContext, R.color.close_gray));
            mContainer.setEnabled(true);
        }
    }
}
