package com.maoqis.testcase.component;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;

import com.maoqis.testcase.R;
import com.maoqis.testcase.view.CommonClickView;

public abstract class BaseCaseFragment extends BaseFragment {

    protected int getRLayout() {
        return R.layout.fragment_base_case;
    }

    protected CommonClickView addCommonTextView(String name, View.OnClickListener onClickListener) {
       return addCommonTextView(rootView, name, onClickListener);
    }

    public static CommonClickView addCommonTextView(ViewGroup rootView, String name, View.OnClickListener onClickListener) {
        CommonClickView child = new CommonClickView(rootView.getContext(), null);
        child.setOnClickListener(onClickListener);
        child.setText(name);
        rootView.addView(child);
        return child;
    }

    protected View findSetOnClickListener(@IdRes int id, View.OnClickListener l) {
        View viewById = rootView.findViewById(id);
        viewById.setOnClickListener(l);
        return viewById;
    }
}
