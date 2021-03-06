package com.livetyping.moydom.presentation.features.target.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.livetyping.moydom.R;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.AverageEnergyCostRepository;
import com.livetyping.moydom.presentation.base.custom.CustomButtonView;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;
import com.livetyping.moydom.presentation.features.main.activity.MainActivity;
import com.livetyping.moydom.presentation.features.target.adapter.MyTargetRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTargetActivity extends BaseActivity implements AverageEnergyCostRepository.AverageCostCallback {
    public static final String EDIT = "edit";

    public static final int STANDARD_AVERAGE_COST = 600;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.activity_new_target) CustomButtonView mNewTargetButton;
    @BindView(R.id.activity_new_target_targets) RecyclerView mTargetsRecycler;
    private MyTargetRecyclerAdapter mTargetsAdapter;
    private float mPercentSelected;

    private boolean mEdit;

    private AverageEnergyCostRepository mCostRepository;

    private float mAverageCost;

    private Prefs mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_target);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null){
            mEdit = intent.getBooleanExtra(EDIT, false);
            if (mEdit){
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.setNavigationIcon(R.drawable.close);
                mToolbar.setTitle(R.string.new_target);
                setSupportActionBar(mToolbar);
                mToolbar.setNavigationOnClickListener(view -> onBackPressed());
            } else {
                mToolbar.setVisibility(View.GONE);
            }
        }

        mPrefs = Prefs.getInstance();
        initViews();

        mCostRepository = AverageEnergyCostRepository.getInstance();
    }

    private void initViews(){
        mPercentSelected = mPrefs.getTargetPercent();
        mNewTargetButton.setEnabled(mPercentSelected != 0);
        mTargetsAdapter = new MyTargetRecyclerAdapter(0, mPercentSelected);
        mTargetsRecycler.setAdapter(mTargetsAdapter);
        mTargetsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mTargetsAdapter.setPercentListener(percent -> {
            mPercentSelected = percent;
            if (!mNewTargetButton.isEnabled()) mNewTargetButton.setEnabled(true);
        });
        mAverageCost = STANDARD_AVERAGE_COST;
        mTargetsAdapter.setCurrentCost(mAverageCost);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCostRepository.setAverageCostCallback(this);
        mCostRepository.getAverageCost();
    }

    @Override
    public void onAverageCostResponse(float averageCost) {
        int temp = Math.round(averageCost * 1000) / 1000;
        if (temp != 0) {
            mAverageCost = averageCost;
            mTargetsAdapter.setCurrentCost(mAverageCost);
        }
    }

    @Override
    public void onErrorResponse(String error) {
        showToast(error);
    }

    @OnClick(R.id.activity_new_target)
    void addNewTarget(CustomButtonView view){
        mPrefs.saveTargetPercent(mPercentSelected);
        mPrefs.saveTargetCost(mAverageCost * (1 - mPercentSelected));
        if (mEdit) {
            setResult(RESULT_OK);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCostRepository.removeAverageCostCallback();
    }
}
