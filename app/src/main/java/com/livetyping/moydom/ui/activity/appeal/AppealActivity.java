package com.livetyping.moydom.ui.activity.appeal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.livetyping.moydom.BuildConfig;
import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.apiModel.appeal.AppealModel;
import com.livetyping.moydom.apiModel.appeal.AppealResponse;
import com.livetyping.moydom.ui.activity.BaseActivity;
import com.livetyping.moydom.utils.HelpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AppealActivity extends BaseActivity implements AppealPhotoSelectorFragment.AppealPhotoSelectorListener{

    private static final int REQUEST_CODE_SELECT_CATEGORY = 1;
    private static final int REQUEST_CODE_AVATAR_FROM_CAMERA = 2;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_appeal_categories) TextView mCategoryName;
    private CompositeDisposable mCompositeDisposable;
    private ArrayList<AppealModel> mCategories = new ArrayList<>();
    private AppealModel mSelectedModel;

    private List<File> mPhotoFiles = new ArrayList<>();

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

    @OnClick(R.id.activity_appeal_add_photo)
    void addPhoto(){
        AppealPhotoSelectorFragment fragment = new AppealPhotoSelectorFragment();
        fragment.show(getSupportFragmentManager(), AppealPhotoSelectorFragment.TAG);
    }

    @Override
    public void fromFile() {

    }

    @Override
    public void fromCamera() {
        if (checkCameraHardware()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File file = new File(
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "photo_" + mPhotoFiles.size() + ".png"
                );
                String providerAuthority = BuildConfig.APPLICATION_ID + ".fileprovider";
                Uri photoURI = FileProvider.getUriForFile(this, providerAuthority, file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_AVATAR_FROM_CAMERA);
            }
        } else {
            showToast(R.string.no_camera);
        }
    }

    private boolean checkCameraHardware() {
        return (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) ||
                (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_CATEGORY){
            mSelectedModel = data.getParcelableExtra("category");
            if (mSelectedModel != null){
                mCategoryName.setText(mSelectedModel.getName());
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AVATAR_FROM_CAMERA){
            Uri imageUri = data.getData();
            if (imageUri != null) {
                mPhotoFiles.add(new File(imageUri.getPath()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    }
}
