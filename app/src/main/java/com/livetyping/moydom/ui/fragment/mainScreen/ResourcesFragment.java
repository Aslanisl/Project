package com.livetyping.moydom.ui.fragment.mainScreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livetyping.moydom.R;
import com.livetyping.moydom.ui.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ResourcesFragment extends BaseFragment {
    public static final String TAG = ResourcesFragment.class.getSimpleName();

    private Unbinder mUnbinder;

    public static ResourcesFragment newInstance() {
        ResourcesFragment fragment = new ResourcesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resourses, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
