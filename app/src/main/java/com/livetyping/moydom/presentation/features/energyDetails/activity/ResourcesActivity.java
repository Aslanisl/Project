package com.livetyping.moydom.presentation.features.energyDetails.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.EnergySwitchModel;
import com.livetyping.moydom.presentation.features.energyDetails.adapter.ResourcesPagerAdapter;

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
        getSupportActionBar().setTitle(R.string.electric_energy);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

        mToolbar.setTitle(R.string.energy_label);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new ResourcesPagerAdapter(getSupportFragmentManager()));

        if (getIntent().getExtras() != null) {
            int type = getIntent().getIntExtra(EXTRA_TYPE, EnergySwitchModel.ENERGY_TYPE_TODAY);
            int page = 0;
            switch (type){
                case EnergySwitchModel.ENERGY_TYPE_WEEK:
                    page = 1;
                    break;
                case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                    page = 2;
                    break;
                case EnergySwitchModel.ENERGY_TYPE_YEAR:
                    page = 3;
                    break;
                case EnergySwitchModel.ENERGY_TYPE_TODAY:
                default:
                    page = 0;
                    break;
            }

            mViewPager.setCurrentItem(page);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
