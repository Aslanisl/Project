package com.livetyping.moydom.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.adapter.ResourcesPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourcesActivity extends BaseActivity {
    public static final String EXTRA_TYPE = "extra_energy_type";
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.resources_activity_view_pager) ViewPager mViewPager;
    @BindView(R.id.resources_activity_tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.energy_label);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new ResourcesPagerAdapter(getSupportFragmentManager()));

    }

}
