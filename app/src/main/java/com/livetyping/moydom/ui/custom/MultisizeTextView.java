package com.livetyping.moydom.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;

import com.livetyping.moydom.R;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by XlebNick for MoyDom.
 */

public class MultisizeTextView extends android.support.v7.widget.AppCompatTextView {

    private int mFirstPartSize = 14;
    private int mSecondPartSize = 14;

    public MultisizeTextView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        if (attrs == null)
            return;

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MultisizeTextView, 0, 0);
        try {
            mFirstPartSize =
                    ta.getDimensionPixelSize(R.styleable.MultisizeTextView_firstPartTextSize, 57);
            mSecondPartSize =
                    ta.getDimensionPixelSize(R.styleable.MultisizeTextView_secondPartTextSize, 57);
        } finally {
            ta.recycle();
        }
    }

    public MultisizeTextView(Context context,
                             @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultisizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String[] textParts = text.toString().split(" ");

        SpannableString span1 = new SpannableString(textParts[0]);
        SpannableString span2 = new SpannableString(textParts[1]);

        span1.setSpan(new AbsoluteSizeSpan(mFirstPartSize), 0, textParts[0].length(), SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new AbsoluteSizeSpan(mSecondPartSize), 0, textParts[1].length(), SPAN_INCLUSIVE_INCLUSIVE);

        super.setText(TextUtils.concat(span1, " ", span2), type);
    }


}
