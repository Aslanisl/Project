package com.livetyping.moydom.presentation.features.main.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.presentation.features.base.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CamerasFragment extends BaseMainFragment {
    public static final String TAG = CamerasFragment.class.getSimpleName();

    private Unbinder mUnbinder;

    public static CamerasFragment newInstance() {
        CamerasFragment fragment = new CamerasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cameras, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
