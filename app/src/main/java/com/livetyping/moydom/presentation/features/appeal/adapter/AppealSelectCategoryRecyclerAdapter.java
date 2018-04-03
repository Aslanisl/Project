package com.livetyping.moydom.presentation.features.appeal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.appeal.AppealModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 13.12.2017.
 */

public class AppealSelectCategoryRecyclerAdapter extends RecyclerView.Adapter<AppealSelectCategoryRecyclerAdapter.ViewHolder> {

    public interface AppealCategorySelected{
        void categorySelected(AppealModel model);
    }

    private AppealCategorySelected mListener;

    private List<AppealModel> mModelList;
    private AppealModel mSelectedModel;

    public AppealSelectCategoryRecyclerAdapter(List<AppealModel> modelList, AppealModel selectedModel) {
        mModelList = modelList;
        mSelectedModel = selectedModel;
    }

    public void setAppealCategoryListener(AppealCategorySelected listener){
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal_select_category, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppealModel model = mModelList.get(position);
        holder.bindHolder(model);
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_appeal_select_category_container) RelativeLayout mContainer;
        @BindView(R.id.item_appeal_select_category_name) TextView mCategoryName;
        @BindView(R.id.item_appeal_select_category_selected) ImageView mCategorySelected;
        private AppealModel mModel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            bindHolder();
        }

        private void bindHolder(){
            mContainer.setOnClickListener(view -> {
                if (mListener != null && mModel != null){
                    mSelectedModel = mModel;
                    notifyDataSetChanged();
                    mListener.categorySelected(mModel);
                }
            });
        }

        public void bindHolder(AppealModel model){
            mModel = model;
            mCategorySelected.setVisibility(mModel.equals(mSelectedModel) ? View.VISIBLE : View.GONE);
            mCategoryName.setText(model.getName());
        }
    }
}
