package com.livetyping.moydom.ui.fragment.mainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.MainActivity;
import com.livetyping.moydom.ui.activity.appeal.AppealActivity;
import com.livetyping.moydom.ui.activity.otherSettings.OtherSettingsActivity;
import com.livetyping.moydom.ui.custom.CustomBottomNavigationView;
import com.livetyping.moydom.ui.fragment.BaseFragment;
import com.livetyping.moydom.utils.AlertDialogUtils;
import com.livetyping.moydom.utils.HelpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class OtherFragment extends BaseFragment {
    public static final String TAG = OtherFragment.class.getSimpleName();

    private static final int REQUEST_CODE_SEND_APPEAL = 1;

    private Unbinder mUnbinder;

    public static OtherFragment newInstance(){
        OtherFragment fragment = new OtherFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.fragment_other_settings)
    void settingsClicked(){
        Intent intent = new Intent(getContext(), OtherSettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fragment_other_appeal)
    void appealClicked(){
        Intent intent = new Intent(getContext(), AppealActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SEND_APPEAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEND_APPEAL && resultCode == RESULT_OK){
            Activity activity = getActivity();
            if (activity instanceof MainActivity){
                AlertDialogUtils.showAlertDone(activity);
                ((MainActivity) activity).selectItemId(CustomBottomNavigationView.Item.ITEM_MY_HOME);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
