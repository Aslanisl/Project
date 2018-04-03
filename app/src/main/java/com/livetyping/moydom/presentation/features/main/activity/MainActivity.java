package com.livetyping.moydom.presentation.features.main.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.base.custom.CustomBottomNavigationView;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.base.custom.CustomBottomNavigationView.Item;
import com.livetyping.moydom.presentation.features.base.fragment.BaseFragment;
import com.livetyping.moydom.presentation.features.main.fragment.BaseMainFragment;
import com.livetyping.moydom.presentation.features.main.fragment.CamerasFragment;
import com.livetyping.moydom.presentation.features.main.fragment.MyHomeFragment;
import com.livetyping.moydom.presentation.features.main.fragment.OtherFragment;
import com.livetyping.moydom.presentation.features.main.fragment.ResourcesFragment;
import com.livetyping.moydom.presentation.utils.SelectMenuItemListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements CustomBottomNavigationView.OnItemClickListener, SelectMenuItemListener {
    @BindView(R.id.main_activity_container) RelativeLayout mContainer;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.bottom_navigation_view) CustomBottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpInternetView(mContainer, mToolbar);

        mBottomNavigationView.setItemClickListener(this);

        if (savedInstanceState == null) {
            mBottomNavigationView.selectItem(Item.ITEM_MY_HOME);
        }
    }

    @Override
    public void selectItemId(Item item) {
        mBottomNavigationView.selectItem(item);
    }

    @Override
    public void onItemSelected(Item item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BaseMainFragment fragment = null;
        String tag = null;

        String toolBarTitle = null;
        switch (item) {
            case ITEM_MY_HOME:
                fragment = MyHomeFragment.newInstance();
                tag = MyHomeFragment.TAG;
                break;
            case ITEM_RESOURCES:
                fragment = ResourcesFragment.newInstance();
                tag = ResourcesFragment.TAG;
                toolBarTitle = getString(R.string.resources);
                break;
            case ITEM_CAMERAS:
                fragment = CamerasFragment.newInstance();
                tag = CamerasFragment.TAG;
                toolBarTitle = getString(R.string.cameras);
                break;
            case ITEM_OTHER:
                fragment = OtherFragment.newInstance();
                tag = OtherFragment.TAG;
                toolBarTitle = getString(R.string.other);
                break;
            default:
                break;
        }
        fragmentTransaction.replace(R.id.main_activity_fragment_container, fragment, tag);
        fragmentTransaction.commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (toolBarTitle == null) {
                actionBar.setTitle(" ");
                actionBar.setIcon(R.drawable.logotype);
            } else {
                actionBar.setTitle(toolBarTitle);
                actionBar.setIcon(null);
            }
        }
    }
}
