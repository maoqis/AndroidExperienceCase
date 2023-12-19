package com.maoqis.testcase.component;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;

import com.maoqis.testcase.R;

public abstract class BaseFragment extends Fragment {
    protected ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(getRLayout(), container, false);
        rootView = initRootView(inflate);
        return inflate;
    }

    protected abstract int getRLayout();

    private ViewGroup initRootView(View inflate) {
        return inflate.findViewById(R.id.ll_root);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onInitView(rootView);
    }

    protected abstract void onInitView(ViewGroup rootView);

}
