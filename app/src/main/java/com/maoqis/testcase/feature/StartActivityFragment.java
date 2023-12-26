package com.maoqis.testcase.feature;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maoqis.testcase.R;
import com.maoqis.testcase.component.BaseFragment;

public class StartActivityFragment extends BaseFragment {
    private static final String TAG = "StartActivityFragment";

    @Override
    protected int getRLayout() {
        return R.layout.activity_start_activity;
    }

    @Override
    protected void onInitView(View rootView) {
        rootView.findViewById(R.id.tv_must_result).setOnClickListener( v -> {
            Log.d(TAG, "tv_must_result startActivityForResult: mv://Main");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("mv://Main"));
            startActivityForResult(intent, 101);
            Toast.makeText(getContext(), "一定会返回，默认值为0", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        Toast.makeText(getContext(), "收到onActivityResult", Toast.LENGTH_SHORT).show();
    }
}
