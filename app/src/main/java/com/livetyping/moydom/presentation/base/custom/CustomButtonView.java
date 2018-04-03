package com.livetyping.moydom.presentation.features.base.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;

/**
 * Created by Ivan on 20.12.2017.
 */

public class CustomButtonView extends LinearLayout {

    private String mText;
    private TextView mTextView;

    public CustomButtonView(Context context) {
        super(context);
        init(context);
    }

    public CustomButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public CustomButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomButtonView,
                0, 0
        );

        try {
            mText = a.getString(R.styleable.CustomButtonView_buttonText);
        } finally {
            a.recycle();
        }
    }

    private void init(Context context){
        inflate(context, R.layout.custom_button_view, this);
        mTextView = findViewById(R.id.custom_button_view);
        mTextView.setText(mText != null ? mText : " ");
    }

    public void setText(String text){
        mTextView.setText(text);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1f : 0.54f);
    }
}
