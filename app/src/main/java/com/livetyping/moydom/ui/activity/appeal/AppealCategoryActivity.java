package com.livetyping.moydom.ui.activity.appeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.appeal.AppealModel;
import com.livetyping.moydom.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppealCategoryActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_appeal_category_recycler) RecyclerView mCategoryRecycler;
    private AppealCategoryRecyclerAdapter mCategoryRecyclerAdapter;

    private Map<String, List<AppealModel>> mCategoriesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_category);
        ButterKnife.bind(this);
        initToolBar();
        Intent intent = getIntent();
        if (intent != null){
            ArrayList<AppealModel> models = intent.getParcelableArrayListExtra("categories");
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
    }
}
