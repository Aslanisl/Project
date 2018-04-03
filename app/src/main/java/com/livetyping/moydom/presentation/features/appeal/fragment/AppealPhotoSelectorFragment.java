package com.livetyping.moydom.presentation.features.appeal.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.livetyping.moydom.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Ivan on 13.12.2017.
 */

public class AppealPhotoSelectorFragment extends BottomSheetDialogFragment {
    public static final String TAG = AppealPhotoSelectorFragment.class.getSimpleName();

    public interface AppealPhotoSelectorListener{
        void fromFile();
        void fromCamera();
    }

    private AppealPhotoSelectorListener mListener;

    private Unbinder mUnbinder;

    private BottomSheetBehavior mBottomSheerBehavior;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppealPhotoSelectorListener){
            mListener = (AppealPhotoSelectorListener) context;
        }
    }

    @SuppressWarnings("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_dialog_photo_selector, null);
        dialog.setContentView(contentView);
        mUnbinder = ButterKnife.bind(this, contentView);

        View parentView = (View) contentView.getParent();
        if (parentView != null) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parentView.getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            if (behavior != null && behavior instanceof BottomSheetBehavior) {
                mBottomSheerBehavior = (BottomSheetBehavior) behavior;
                mBottomSheerBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
                parentView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            }
        }
    }

    @OnClick(R.id.fragment_dialog_photo_check_file)
    void checkFile(){
        if (mListener != null){
            mListener.fromFile();
        }
        dismiss();
    }

    @OnClick(R.id.fragment_dialog_photo_check_photo)
    void checkPhoto(){
        if (mListener != null){
            mListener.fromCamera();
        }
        dismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
