package com.livetyping.moydom.ui.activity.appeal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.livetyping.moydom.App;
import com.livetyping.moydom.BuildConfig;
import com.livetyping.moydom.R;
import com.livetyping.moydom.api.Api;
import com.livetyping.moydom.api.ApiUrlService;
import com.livetyping.moydom.api.CallbackWrapper;
import com.livetyping.moydom.api.RetryApiCallWithDelay;
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

import static com.livetyping.moydom.api.Api.API_RETRY_CALL_COUNT;
import static com.livetyping.moydom.api.Api.API_RETRY_CALL_TIME;

public class AppealActivity extends BaseActivity implements AppealPhotoSelectorFragment.AppealPhotoSelectorListener{

    private static final int REQUEST_CODE_SELECT_CATEGORY = 1;
    private static final int REQUEST_CODE_AVATAR_FROM_CAMERA = 2;
    private static final int REQUEST_CODE_AVATAR_FROM_GALLERY = 3;
    private static final int REQUEST_PERMISSION_READ_IMAGES = 4;
    private static final int REQUEST_PERMISSION_READ_IMAGES_ACTIVITY = 5;

    private final int DELAY_TIME = 20;

    private boolean mEnableSendMenu = false;

    @BindView(R.id.activity_appeal_container) RelativeLayout mContainer;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_appeal_categories) TextView mCategoryName;
    @BindView(R.id.activity_appeal_body) EditText mAppealBody;
    @BindView(R.id.activity_appeal_photos_recycler) RecyclerView mPhotosRecycler;
    private View mMenuView;
    private Handler mMenuItemHandler;
    private Runnable mChangeMenuItemRunnable;
    private int mChangeCounter;
    private AppealPhotoRecyclerAdapter mPhotoAdapter;
    private CompositeDisposable mCompositeDisposable;
    private ArrayList<AppealModel> mCategories = new ArrayList<>();
    private AppealModel mSelectedModel;

    private List<File> mPhotoFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        ButterKnife.bind(this);
        mMenuView = findViewById(R.id.action_send);
        setUpInternetView(mContainer, mToolbar);
        initViews();
        initCategories();
    }

    private void initToolBar(){
        mToolbar.setNavigationIcon(R.drawable.close);
        mToolbar.setTitle(R.string.appeal);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initViews(){
        initToolBar();
        mPhotoAdapter = new AppealPhotoRecyclerAdapter();
        mPhotosRecycler.setAdapter(mPhotoAdapter);
        //width photo view in dp
        int width = (int)(getResources().getDimension(R.dimen.appeal_photo_width_height) / getResources().getDisplayMetrics().density);
        mPhotosRecycler.setLayoutManager(new GridLayoutManager(this, HelpUtils.calculateNoOfColumns(this, width)));
        HelpUtils.focusEditSoft(mAppealBody, this);
        mAppealBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeEnableMenu();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mMenuItemHandler = new Handler();
        mChangeMenuItemRunnable = this::changeMenuEnableColor;
    }

    private void initCategories(){
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(Api.getApiService().getAddresses(ApiUrlService.getAddressesUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryApiCallWithDelay(API_RETRY_CALL_COUNT, API_RETRY_CALL_TIME))
                .subscribeWith(new CallbackWrapper<AppealResponse>(this){
                    @Override
                    protected void onSuccess(AppealResponse appealResponse) {
                        if (appealResponse.containsErrors()){
                            showToast(appealResponse.getErrorMessage());
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenuView == null){
            mMenuView = findViewById(R.id.action_send);
        }
        if (mMenuItemHandler != null && mChangeMenuItemRunnable != null) {
            mMenuItemHandler.post(mChangeMenuItemRunnable);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void changeMenuEnableColor(){
        if (mMenuView != null) {
            mChangeCounter = 0;
            if (mEnableSendMenu) {
                if (mMenuView instanceof TextView) {
                    ((TextView) mMenuView).setTextColor(Color.WHITE);
                    mMenuView.setEnabled(true);
                }
            } else {
                if (mMenuView instanceof TextView) {
                    ((TextView) mMenuView).setTextColor(ContextCompat.getColor(this, R.color.blue_gray));
                    mMenuView.setEnabled(false);
                }
            }
        } else if (mChangeCounter < 5){
            mChangeCounter++;
            if (mMenuView == null){
                mMenuView = findViewById(R.id.action_send);
            }
            mMenuItemHandler.postDelayed(mChangeMenuItemRunnable, DELAY_TIME);
        }
    }

    private void changeEnableMenu(){
        boolean enable = mSelectedModel != null && mAppealBody.getText().length() > 0;
        if (enable != mEnableSendMenu){
            mEnableSendMenu = enable;
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:
                if (mSelectedModel == null){
                    showToast(R.string.chose_category);
                } else {
                    sendAppeal();
                }
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
            intent.putExtra("selected", mSelectedModel);
            startActivityForResult(intent, REQUEST_CODE_SELECT_CATEGORY);
        } else {
            showToast(R.string.categories_loading);
        }
    }

    @OnClick(R.id.activity_appeal_add_photo)
    void addPhoto(){
        AppealPhotoSelectorFragment fragment = new AppealPhotoSelectorFragment();
        fragment.show(getSupportFragmentManager(), AppealPhotoSelectorFragment.TAG);
    }

    @Override
    public void fromFile() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_IMAGES);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.chose_picture)), REQUEST_CODE_AVATAR_FROM_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_IMAGES){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fromFile();
            } else {
                showNoStoragePermissionSnackbar();
            }
        }
    }

    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(mContainer, getString(R.string.gallery_permission), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.settings), v -> {
                    openApplicationSettings();
                    Toast.makeText(App.getAppContext(),
                            getString(R.string.gallery_permission_request),
                            Toast.LENGTH_SHORT)
                            .show();
                })
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, REQUEST_PERMISSION_READ_IMAGES_ACTIVITY);
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
                mCategoryName.setTextColor(ContextCompat.getColor(this, R.color.close_black));
                changeEnableMenu();
                HelpUtils.focusEditSoft(mAppealBody, this);
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AVATAR_FROM_CAMERA){
            File file = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "photo_" + mPhotoFiles.size() + ".png"
            );
            addFile(file);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AVATAR_FROM_GALLERY){
            Uri imageUri = data.getData();
            if (imageUri != null) {
                String path = getPath(imageUri);
                if (path != null) {
                    File file = new File(path);
                    addFile(file);
                }
            }
        } else if (requestCode == REQUEST_PERMISSION_READ_IMAGES_ACTIVITY){
            fromFile();
        }
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        String filePath = cursor.getString(columnIndexData);
        cursor.close();
        return filePath;
    }

    private void addFile(File file){
        mPhotoFiles.add(file);
        mPhotoAdapter.addFile(file);
    }

    private void sendAppeal(){
        HelpUtils.hideSoftKeyborad(this);
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, mSelectedModel.getEmail() != null ? new String[] { mSelectedModel.getEmail() } : " ");
        intent.putExtra(Intent.EXTRA_SUBJECT, mSelectedModel.getTypeName() != null
                ? getString(R.string.appeal_from_category, mSelectedModel.getTypeName())
                : " ");
        intent.putExtra(Intent.EXTRA_TEXT, mAppealBody.getText().toString());
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : mPhotoFiles){
            Uri uri = Uri.fromFile(file);
            uris.add(uri);
        }
        if (!uris.isEmpty()) {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        startActivity(Intent.createChooser(intent, "Send appeal"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
        if (mMenuItemHandler != null && mChangeMenuItemRunnable != null) mMenuItemHandler.removeCallbacks(mChangeMenuItemRunnable);
    }
}
