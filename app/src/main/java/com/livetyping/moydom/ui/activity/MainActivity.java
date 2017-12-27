package com.livetyping.moydom.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.fragment.BaseFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.CamerasFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.MyHomeFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.OtherFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.ResourcesFragment;
import com.livetyping.moydom.utils.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.main_activity_container) RelativeLayout mContainer;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.bottom_navigation_view) BottomNavigationView mBottomNavigationView;

    private enum PageState{
        ITEM_MY_HOME,
        ITEM_RESOURSES,
        ITEM_CAMERAS,
        ITEM_OTHER
    }

    private PageState mPageState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setUpInternetView(mContainer, mToolbar);

        BottomNavigationViewHelper.removeShiftMode(mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            mBottomNavigationView.setSelectedItemId(R.id.action_my_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BaseFragment fragment = null;
        String tag = null;

        String toolBarTitle = null;
        switch (item.getItemId()) {
            case R.id.action_my_home:
                fragment = MyHomeFragment.newInstance();
                tag = MyHomeFragment.TAG;
                toolBarTitle = getString(R.string.my_home);
                break;
            case R.id.action_resources:
                fragment = ResourcesFragment.newInstance();
                tag = ResourcesFragment.TAG;
                toolBarTitle = getString(R.string.resources);
                break;
            case R.id.action_cameras:
                fragment = CamerasFragment.newInstance();
                tag = CamerasFragment.TAG;
                toolBarTitle = getString(R.string.cameras);
                break;
            case R.id.action_other:
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
        if (actionBar != null && toolBarTitle != null){
            actionBar.setTitle(toolBarTitle);
        }
        return true;
    }
}
