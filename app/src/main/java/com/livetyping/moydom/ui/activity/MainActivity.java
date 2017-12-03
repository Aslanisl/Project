package com.livetyping.moydom.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.ui.fragment.BaseFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.CamerasFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.MyHomeFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.OtherFragment;
import com.livetyping.moydom.ui.fragment.mainScreen.ResourcesFragment;
import com.livetyping.moydom.utils.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
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
        switch (item.getItemId()) {
            case R.id.action_my_home:
                fragment = MyHomeFragment.newInstance();
                tag = MyHomeFragment.TAG;
                break;
            case R.id.action_resources:
                fragment = ResourcesFragment.newInstance();
                tag = ResourcesFragment.TAG;
                break;
            case R.id.action_cameras:
                fragment = CamerasFragment.newInstance();
                tag = CamerasFragment.TAG;
                break;
            case R.id.action_other:
                fragment = OtherFragment.newInstance();
                tag = OtherFragment.TAG;
                break;
            default:
                break;
        }
        fragmentTransaction.replace(R.id.main_activity_fragment_container, fragment, tag);
        fragmentTransaction.commit();
        return true;
    }
}
