package com.livetyping.moydom.ui.activity.otherSettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.apiModel.myTarget.AverageEnergyCostResponse;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.ui.custom.CustomButtonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewTargetActivity extends BaseActivity {
    public static final String EDIT = "edit";

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.activity_new_target) CustomButtonView mNewTargetButton;
    @BindView(R.id.activity_new_target_targets) RecyclerView mTargetsRecycler;
    private MyTargetRecyclerAdapter mTargetsAdapter;

    private CompositeDisposable mCompositeDisposable;

    private boolean mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_target);
        ButterKnife.bind(this);

        mCompositeDisposable = new CompositeDisposable();

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

        mNewTargetButton.setEnabled(false);

        mCompositeDisposable.add(Api.getApiService()
                .getAverageEnergyCost(ApiUrlService.getAverageEnergyCost())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new CallbackWrapper<AverageEnergyCostResponse>(this){
                    @Override
                    protected void onSuccess(AverageEnergyCostResponse averageEnergyCostResponse) {
                        if (averageEnergyCostResponse.containsErrors()){
                            //TODO send error
                        } else {
                            initTargets(averageEnergyCostResponse.getAverageCost());
                        }
                    }
                }));
    }

    private void initTargets(float averageCost){
        if (mTargetsAdapter == null){
            mTargetsAdapter = new MyTargetRecyclerAdapter(averageCost);
            mTargetsRecycler.setAdapter(mTargetsAdapter);
            mTargetsRecycler.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mTargetsAdapter.setCurrentCost(averageCost);
        }
    }

    @OnClick(R.id.activity_new_target)
    void addNewTarget(CustomButtonView view){
        view.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}
