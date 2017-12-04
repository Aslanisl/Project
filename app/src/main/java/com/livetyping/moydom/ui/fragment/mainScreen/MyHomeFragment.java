package com.livetyping.moydom.ui.fragment.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.activity.settings.SettingsActivity;
import com.livetyping.moydom.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class MyHomeFragment extends BaseFragment {
    public static final String TAG = MyHomeFragment.class.getSimpleName();

    private static final int SETTINGS_REQUEST_CODE = 2;

    @BindView(R.id.fragment_my_home_cameras_recycler) RecyclerView mCamerasRecycler;

    private Unbinder mUnbinder;

    public static MyHomeFragment newInstance() {
        MyHomeFragment fragment = new MyHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_home, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK){
            //TODO apply new settings
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
