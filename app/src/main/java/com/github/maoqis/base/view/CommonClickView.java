package com.github.maoqis.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.maoqis.testcase.R;

public class CommonClickView extends androidx.appcompat.widget.AppCompatTextView {
    public CommonClickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 9, 0, 9);
        setMinHeight(context.getResources().getDimensionPixelSize(R.dimen.dp_48));
    }


    public ViewGroup.LayoutParams initCommonLayoutParams() {
        return initCommonLayoutParams(getContext());
    }

    public static ViewGroup.LayoutParams initCommonLayoutParams(Context context) {
        int w = ViewGroup.LayoutParams.MATCH_PARENT;
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        return new ViewGroup.LayoutParams(w, h);
    }
}
