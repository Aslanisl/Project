package com.livetyping.moydom.presentation.features.target.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.livetyping.moydom.R;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.AverageEnergyCostRepository;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.target.adapter.MyTargetRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTargetActivity extends BaseActivity implements AverageEnergyCostRepository.AverageCostCallback {

    private static final int REQUEST_CODE_NEW_TARGET = 1;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.activity_my_target_container) RelativeLayout mContainer;
    @BindView(R.id.activity_my_target_targets) RecyclerView mMyTargetRecycler;
    private MyTargetRecyclerAdapter mAdapter;

    private Prefs mPrefs;
    private AverageEnergyCostRepository mCostRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_target);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.my_target);
        mToolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mPrefs = Prefs.getInstance();
        initViews();
        showProgress(mContainer);
        mCostRepository = AverageEnergyCostRepository.getInstance();
    }

    private void initViews(){
        float targetPercent = mPrefs.getTargetPercent();
        mAdapter = new MyTargetRecyclerAdapter(0, targetPercent, true);
        mMyTargetRecycler.setAdapter(mAdapter);
        mMyTargetRecycler.setLayoutManager(new LinearLayoutManager(this));
        if (targetPercent == 0) mMyTargetRecycler.setVisibility(View.GONE);
        mAdapter.setCurrentCost(mPrefs.getTargetCost());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCostRepository.setAverageCostCallback(this);
        mCostRepository.getAverageCost();
    }

    @Override
    public void onAverageCostResponse(float averageCost) {
        removeProgress(mContainer);
        mAdapter.setCurrentCost(averageCost);
    }

    @Override
    public void onErrorResponse(String error) {
        removeProgress(mContainer);
        showToast(error);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCostRepository.removeAverageCostCallback();
    }

    @OnClick(R.id.activity_my_target_target)
    void changeTarget(){
        Intent intent = new Intent(this, NewTargetActivity.class);
        intent.putExtra(NewTargetActivity.EDIT, true);
        startActivityForResult(intent, REQUEST_CODE_NEW_TARGET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_NEW_TARGET){
            mAdapter.setCurrentPercent(mPrefs.getTargetPercent());
            mMyTargetRecycler.setVisibility(View.VISIBLE);
        }
    }
}
