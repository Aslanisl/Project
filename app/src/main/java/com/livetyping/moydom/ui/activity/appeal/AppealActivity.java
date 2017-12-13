package com.livetyping.moydom.ui.activity.appeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.apiModel.appeal.AppealModel;
import com.livetyping.moydom.apiModel.appeal.AppealResponse;
import com.livetyping.moydom.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AppealActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_CATEGORY = 1;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    private CompositeDisposable mCompositeDisposable;
    private ArrayList<AppealModel> mCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        ButterKnife.bind(this);
        initToolBar();
        initCategories();
    }

    private void initToolBar(){
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.appeal);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initCategories(){
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(Api.getApiService().getAddresses(ApiUrlService.getAddressesUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<AppealResponse>(this){
                    @Override
                    protected void onSuccess(AppealResponse appealResponse) {
                        if (appealResponse.containsErrors()){
                            appealResponse.getErrorMessage();
                        } else {
                            initAddresses(appealResponse.getAppealModels());
                        }
                    }
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:
                //TODO send appeal
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAddresses(List<AppealModel> models){
        mCategories.addAll(models);
    }

    @OnClick(R.id.activity_appeal_categories)
    void showCategories(){
        if (!mCategories.isEmpty()){
            Intent intent = new Intent(this, AppealCategoryActivity.class);
            intent.putExtra("categories", mCategories);
            startActivityForResult(intent, REQUEST_CODE_SELECT_CATEGORY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    }
}
