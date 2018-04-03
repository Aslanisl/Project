package com.livetyping.moydom.presentation.features.main.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.livetyping.moydom.presentation.base.custom.CustomBottomNavigationView;
import com.livetyping.moydom.presentation.features.base.fragment.BaseFragment;
import com.livetyping.moydom.presentation.utils.SelectMenuItemListener;

public class BaseMainFragment extends BaseFragment {

    private SelectMenuItemListener mMenuListener;

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMenuListener = (SelectMenuItemListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @CallSuper
    public void onDetach() {
        super.onDetach();
        mMenuListener = null;
    }

    protected void selectMenuItem(CustomBottomNavigationView.Item item){
        if (mMenuListener != null) mMenuListener.selectItemId(item);
    }
}
