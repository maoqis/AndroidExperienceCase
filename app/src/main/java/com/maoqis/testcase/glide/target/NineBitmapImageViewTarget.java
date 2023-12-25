package com.maoqis.testcase.glide.target;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.maoqis.testcase.glide.utils.NinePngUtils;

public class NineBitmapImageViewTarget extends BitmapImageViewTarget {
    private static final String TAG = "NineBitmapImageViewTarg";
    Context contextApp;

    public NineBitmapImageViewTarget(ImageView view) {
        super(view);
        contextApp = view.getContext();
    }

    @Override
    protected void setResource(Bitmap resource) {
        boolean is9Png = NinePngUtils.is9png(resource);
        Log.d(TAG, "setResource: is9Png=" + is9Png + " resource=" + resource);
        if (is9Png) {
            Drawable drawable = NinePngUtils.createDrawable(resource, true, contextApp.getResources());
            view.setImageDrawable(drawable);
        } else {
            view.setImageBitmap(resource);
        }
    }
}
