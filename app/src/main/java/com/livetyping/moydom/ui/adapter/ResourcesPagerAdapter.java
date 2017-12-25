package com.livetyping.moydom.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.livetyping.moydom.App;
import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.fragment.ResourceByPeriodFragment;

/**
 * Created by XlebNick for MoyDom.
 */

public class ResourcesPagerAdapter extends FragmentStatePagerAdapter{
    public ResourcesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        int periodType = 0;
        switch (position){
            case 0:
                periodType = R.string.day;
                break;
            case 1:
                periodType = R.string.week;
                break;
            case 2:
                periodType = R.string.month;
                break;
            case 3:
                periodType = R.string.year;
                break;

        }
        return ResourceByPeriodFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        int id = 0;
        switch (position){
            case 0:
                id = R.string.day;
                break;
            case 1:
                id = R.string.week;
                break;
            case 2:
                id = R.string.month;
                break;
            case 3:
                id = R.string.year;
                break;
        }
        return App.getAppContext().getString(id);
    }
}
