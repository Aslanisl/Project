package com.livetyping.moydom.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.custom.CustomBottomNavigationView;
import com.livetyping.moydom.ui.custom.CustomBottomNavigationView.Item;
import com.livetyping.moydom.ui.fragment.BaseFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.CamerasFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.MyHomeFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.OtherFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.ResourcesFragment;
import com.livetyping.moydom.utils.HelpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements CustomBottomNavigationView.OnItemClickListener{
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

        if (savedInstanceState == null){
            mBottomNavigationView.selectItem(Item.ITEM_MY_HOME);
        }
    }

    public void selectItemId(Item item){
        mBottomNavigationView.selectItem(item);
    }

    @Override
    public void onItemSelected(Item item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BaseFragment fragment = null;
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
        if (actionBar != null){
            if (toolBarTitle == null){
                actionBar.setTitle(" ");
                actionBar.setIcon(R.drawable.logotype);
            } else {
                actionBar.setTitle(toolBarTitle);
                actionBar.setIcon(null);
            }
        }
    }
}
