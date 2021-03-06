package com.livetyping.moydom.presentation.features.appeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.appeal.AppealModel;
import com.livetyping.moydom.presentation.features.appeal.adapter.AppealCategoryRecyclerAdapter;
import com.livetyping.moydom.presentation.features.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppealCategoryActivity extends BaseActivity {
    private static final int REQUEST_CODE_SELECT_CATEGORY = 3;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_appeal_category_recycler) RecyclerView mCategoryRecycler;
    private AppealCategoryRecyclerAdapter mCategoryRecyclerAdapter;

    private Map<String, List<AppealModel>> mCategoriesMap;
    private AppealModel mSelectedModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_category);
        ButterKnife.bind(this);
        initToolBar();
        Intent intent = getIntent();
        if (intent != null){
            ArrayList<AppealModel> models = intent.getParcelableArrayListExtra("categories");
            mSelectedModel = intent.getParcelableExtra("selected");
            if (models != null) {
                initCategoriesMap(models);
            }
        }
    }

    private void initToolBar(){
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.chose_category);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initCategoriesMap(ArrayList<AppealModel> models){
        mCategoriesMap = new HashMap<>();
        int modelsSize = models.size();
        for (int i = 0; i < modelsSize; i++) {
            AppealModel model = models.get(i);
            if (mCategoriesMap.containsKey(model.getTypeName())){
                mCategoriesMap.get(model.getTypeName()).add(model);
            } else {
                List<AppealModel> appealModels = new ArrayList<>();
                appealModels.add(model);
                mCategoriesMap.put(model.getTypeName(), appealModels);
            }
        }
        String selectedName = null;
        if (mSelectedModel != null){
            selectedName = mSelectedModel.getTypeName();
        }
        mCategoryRecyclerAdapter = new AppealCategoryRecyclerAdapter(mCategoriesMap.keySet(), selectedName);
        mCategoryRecyclerAdapter.setAppealCategoryListener(this::categorySelected);
        mCategoryRecycler.setAdapter(mCategoryRecyclerAdapter);
        mCategoryRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void categorySelected(String name){
        List<AppealModel> selectedModels = mCategoriesMap.get(name);
        if (selectedModels != null) {
            Intent intent = new Intent(this, AppealSelectCategoryActivity.class);
            ArrayList<AppealModel> models = new ArrayList<>();
            models.addAll(selectedModels);
            intent.putExtra("categories", models);
            intent.putExtra("selected", mSelectedModel);
            startActivityForResult(intent, REQUEST_CODE_SELECT_CATEGORY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CATEGORY && resultCode == RESULT_OK){
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
