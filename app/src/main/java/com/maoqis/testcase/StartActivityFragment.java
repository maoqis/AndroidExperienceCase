package com.maoqis.testcase;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class StartActivityFragment extends Fragment {
    private static final String TAG = "StartActivityFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_start_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tv_must_result).setOnClickListener( v -> {
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
