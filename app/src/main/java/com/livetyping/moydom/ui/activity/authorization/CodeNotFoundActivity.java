package com.livetyping.moydom.ui.activity.authorization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeNotFoundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_not_found);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_code_leave_number)
    void leaveNumber(){
        Intent intent = new Intent(this, EnterPhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_code_not_found_again)
    void tryAgain(){
        setResult(RESULT_OK);
        finish();
    }
}
