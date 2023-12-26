package com.bumptech.glide.ninepng;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.ninepng.target.NinePngImageViewTargetFactory;

import java.lang.reflect.Field;

public class NinePngGlideApi {
    private static final String TAG = "GlideNinePngApi";


    public static void afterGlideInit(Glide glide) {
        GlideContext glideContext = NinePngGlideModule.getGlideContext(glide);
        NinePngGlideModule.replaceImageViewTargetFactory(glideContext);
    }


}
