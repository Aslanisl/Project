package com.livetyping.moydom.ui.activity.appeal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ivan on 13.12.2017.
 */

public class AppealCategoryRecyclerAdapter extends RecyclerView.Adapter<AppealCategoryRecyclerAdapter.ViewHolder> {

    public interface AppealCategorySelectListener{
        void appealCategorySelected(String name);
    }

    private AppealCategorySelectListener mListener;
    private List<String> mCategories;
    private String mSelectedName;

    public AppealCategoryRecyclerAdapter(Set<String> categories, String selectedName) {
        mCategories = new ArrayList<>();
        mCategories.addAll(categories);
        mSelectedName = selectedName;
    }

    public void setAppealCategoryListener(AppealCategorySelectListener listener){
        mListener = listener;
    }

    public void removeAppealCategoryListener(){
        mListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal_category, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mCategories.get(position);
        holder.bindHolder(name);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_appeal_category_container) RelativeLayout mContainer;
        @BindView(R.id.item_appeal_category_name) TextView mCategoryName;
        @BindView(R.id.item_appeal_category_selected) ImageView mSelectedCategory;
        private String mString;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            bindHolder();
        }

        private void bindHolder(){
            mContainer.setOnClickListener(view -> {
                if (mListener != null && mString != null){
                    mListener.appealCategorySelected(mString);
                }
            });
        }

        public void bindHolder(String name){
            mCategoryName.setText(name);
            mString = name;
            mSelectedCategory.setVisibility(
                    mSelectedName != null && mSelectedName.equals(name)
                    ? View.VISIBLE
                    : View.GONE
            );
        }
    }
}
