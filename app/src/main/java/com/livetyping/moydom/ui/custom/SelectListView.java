package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;

/**
 * Created by Ivan on 20.12.2017.
 */

public class SelectListView extends LinearLayout {

    protected String mText;

    public SelectListView(Context context) {
        super(context);
        init(context);
    }

    public SelectListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, context);
        init(context);
    }

    public SelectListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, context);
        init(context);
    }

    private void initAttrs(AttributeSet attrs, Context context){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SelectListView,
                0, 0
        );

        try {
            mText = a.getString(R.styleable.SelectListView_selectText);
        } finally {
            a.recycle();
        }
    }

    private void init(Context context){
        inflate(context, R.layout.custom_select_list_view, this);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
        TextView textView = findViewById(R.id.custom_select_list_name);
        textView.setText(mText != null ? mText : " ");
    }
}
