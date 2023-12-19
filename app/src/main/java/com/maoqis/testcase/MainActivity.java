package com.maoqis.testcase;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class MainActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissionAndLoadImg();
    }

    private void checkPermissionAndLoadImg() {
        int hasWriteExternalPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteExternalPermission == PackageManager.PERMISSION_GRANTED) {
            //TODO 有权限，做自己的后续操作

        } else {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }


    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        //加载选项列表布局
        loadHeadersFromResource(R.xml.main, target);
    }

    /**
     * 验证Preference是否有效
     *
     * @param fragmentName
     * @return
     */
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

}