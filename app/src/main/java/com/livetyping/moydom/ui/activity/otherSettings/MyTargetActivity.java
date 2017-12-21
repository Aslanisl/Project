package com.livetyping.moydom.ui.activity.otherSettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTargetActivity extends BaseActivity {

    private static final int REQUEST_CODE_NEW_TARGET = 1;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.item_my_target_title) TextView mMyTargetTitle;
    @BindView(R.id.item_my_target_description) TextView mMyTargetDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_target);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.my_target);
        mToolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @OnClick(R.id.activity_my_target_target)
    void changeTarget(){
        Intent intent = new Intent(this, NewTargetActivity.class);
        intent.putExtra(NewTargetActivity.EDIT, true);
        startActivityForResult(intent, REQUEST_CODE_NEW_TARGET);
    }
}
