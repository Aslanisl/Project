package com.livetyping.moydom.presentation.features.appeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.appeal.AppealModel;
import com.livetyping.moydom.presentation.features.appeal.adapter.AppealSelectCategoryRecyclerAdapter;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppealSelectCategoryActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_appeal_select_category_recycler) RecyclerView mSelectCategoryRecycler;
    private AppealSelectCategoryRecyclerAdapter mAdapter;
    private ArrayList<AppealModel> mCategories;
    private AppealModel mSelectedModel;

    private boolean mEnableDoneButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_select_category);
        ButterKnife.bind(this);
        initToolBar();
        Intent intent = getIntent();
        if (intent != null){
            mCategories = intent.getParcelableArrayListExtra("categories");
            mSelectedModel = intent.getParcelableExtra("selected");
            if (mCategories != null){
                initModels();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_done);
        if (mEnableDoneButton){
            item.setEnabled(true);
            item.setIcon(R.drawable.accept_active);
        } else {
            item.setEnabled(false);
            item.setIcon(R.drawable.accept_disable);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                choseCategory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolBar(){
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.chose_reason);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initModels(){
        mAdapter = new AppealSelectCategoryRecyclerAdapter(mCategories, mSelectedModel);
        mAdapter.setAppealCategoryListener(this::appealModelSelected);
        mSelectCategoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        mSelectCategoryRecycler.setAdapter(mAdapter);
        if (mSelectedModel != null){
            mEnableDoneButton = true;
            invalidateOptionsMenu();
        }
    }

    private void appealModelSelected(AppealModel model){
        mEnableDoneButton = true;
        invalidateOptionsMenu();
        mSelectedModel = model;
    }

    private void choseCategory(){
        Intent intent = new Intent();
        intent.putExtra("category", mSelectedModel);
        setResult(RESULT_OK, intent);
        finish();
    }
}
