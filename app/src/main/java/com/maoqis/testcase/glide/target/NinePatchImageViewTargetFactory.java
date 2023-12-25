package com.maoqis.testcase.glide.target;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;

public class NinePatchImageViewTargetFactory extends ImageViewTargetFactory {
    private static final String TAG = "ImageViewTargetFactory";
    ImageViewTargetFactory imageViewTargetFactory;

    public NinePatchImageViewTargetFactory(ImageViewTargetFactory real) {
        imageViewTargetFactory = real;
    }

    @NonNull
    @Override
    public <Z> ViewTarget<ImageView, Z> buildTarget(@NonNull ImageView view, @NonNull Class<Z> clazz) {
        if (imageViewTargetFactory == null) {
            Log.w(TAG, "buildTarget: 原imageViewTargetFactory == null");
            return super.buildTarget(view, clazz);
        }

        if (Bitmap.class.equals(clazz)) {
            return (ViewTarget<ImageView, Z>) new NineBitmapImageViewTarget(view);
        } else {
            return imageViewTargetFactory.buildTarget(view, clazz);
        }
    }
}
