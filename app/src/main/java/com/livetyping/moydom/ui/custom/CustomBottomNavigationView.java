package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ivan on 09.01.2018.
 */

public class CustomBottomNavigationView extends LinearLayout {

    public enum Item{
        ITEM_NONE,
        ITEM_MY_HOME,
        ITEM_RESOURCES,
        ITEM_CAMERAS,
        ITEM_OTHER
    }

    @BindView(R.id.custom_bottom_view_my_home_icon) ImageView mMyHomeIcon;
    @BindView(R.id.custom_bottom_view_my_home_text) TextView mMyHomeText;

    @BindView(R.id.custom_bottom_view_resources_icon) ImageView mResourcesIcon;
    @BindView(R.id.custom_bottom_view_resources_text) TextView mResourcesText;

    @BindView(R.id.custom_bottom_view_cameras_icon) ImageView mCamerasIcon;
    @BindView(R.id.custom_bottom_view_cameras_text) TextView mCamerasText;

    @BindView(R.id.custom_bottom_view_other_icon) ImageView mOtherIcon;
    @BindView(R.id.custom_bottom_view_other_text) TextView mOtherText;

    private Item mCurrentItem;

    public CustomBottomNavigationView(Context context) {
        super(context);
        init(context);
    }

    public CustomBottomNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBottomNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.custom_bottom_navigation_view, this);
        ButterKnife.bind(this);
        mCurrentItem = Item.ITEM_NONE;
    }

    public interface OnItemClickListener{
        void onItemSelected(Item item);
    }

    private OnItemClickListener mListener;

    public void setItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @OnClick({R.id.custom_bottom_view_my_home, R.id.custom_bottom_view_resources,
            R.id.custom_bottom_view_cameras, R.id.custom_bottom_view_other})
    void onItemClicked(View view){
        Item selectedItem = Item.ITEM_NONE;
        switch (view.getId()){
            case R.id.custom_bottom_view_my_home:
                selectedItem = Item.ITEM_MY_HOME;
                break;
            case R.id.custom_bottom_view_resources:
                selectedItem = Item.ITEM_RESOURCES;
                break;
            case R.id.custom_bottom_view_cameras:
                selectedItem = Item.ITEM_CAMERAS;
                break;
            case R.id.custom_bottom_view_other:
                selectedItem = Item.ITEM_OTHER;
                break;
        }
        selectItem(selectedItem);
    }

    public void selectItem(Item item){
        changeItem(mCurrentItem, false);
        mCurrentItem = item;
        changeItem(mCurrentItem, true);
        if (mListener != null){
            mListener.onItemSelected(mCurrentItem);
        }
    }

    private void changeItem(Item item, boolean select){
        switch (item){
            case ITEM_MY_HOME:
                changeTextStyle(mMyHomeText, select);
                mMyHomeIcon.setImageResource(select ? R.drawable.home_active : R.drawable.home_inactive);
                break;
            case ITEM_RESOURCES:
                changeTextStyle(mResourcesText, select);
                mResourcesIcon.setImageResource(select ? R.drawable.resources_active : R.drawable.resources_inactive);
                break;
            case ITEM_CAMERAS:
                changeTextStyle(mCamerasText, select);
                mCamerasIcon.setImageResource(select ? R.drawable.cameras_active : R.drawable.cameras_inactive);
                break;
            case ITEM_OTHER:
                changeTextStyle(mOtherText, select);
                mOtherIcon.setImageResource(select ? R.drawable.more_active : R.drawable.more_inactive);
                break;
        }
    }

    private void changeTextStyle(TextView textView, boolean selected){
        if (selected){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(getContext(), R.style.Blue13CenteredRg);
            } else{
                textView.setTextAppearance(R.style.Blue13CenteredRg);
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(getContext(), R.style.Gray12CenteredRg);
            } else{
                textView.setTextAppearance(R.style.Gray12CenteredRg);
            }
        }
    }
}
