package com.livetyping.moydom.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.livetyping.moydom.R;
import com.livetyping.moydom.utils.NetworkUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Ivan on 25.11.2017.
 */

public class NoInternetDialogFragment extends DialogFragment {
    public static final String TAG = NoInternetDialogFragment.class.getSimpleName();

    public interface OnInternetDialogListener{
        void tryInternetCallAgain();
    }

    private OnInternetDialogListener mOnInternetDialogListener;

    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInternetDialogListener = (OnInternetDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NoInternetDialogFragment newInstance(){
        NoInternetDialogFragment fragment = new NoInternetDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_no_internet, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.fragment_dialog_no_internet_check_again)
    void checkInternet(){
        if (NetworkUtil.isConnected(getContext())) {
            if (mOnInternetDialogListener != null){
                mOnInternetDialogListener.tryInternetCallAgain();
            }
            dismiss();
        } else {
            Toast.makeText(getContext(), R.string.check_internet_settings, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnInternetDialogListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
