package com.maoqis.testcase.glide;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.maoqis.testcase.glide.target.NinePatchImageViewTargetFactory;

import java.lang.reflect.Field;

public class GlideNinePngApi {
    private static final String TAG = "GlideNinePngApi";


    public static void afterGlideInit(Glide glide) {
        GlideContext glideContext = getGlideContext(glide);
        replaceImageViewTargetFactory(glideContext);
    }


    /**
     * called after your application.onCreate
     */
    private static void replaceImageViewTargetFactory(GlideContext glideContext) {
        if (glideContext == null) {
            Log.w(TAG, "replaceImageViewTargetFactory: glideContext == null");
            return;
        }
        //反射设置，NinePatchImageViewTargetFactory ，适配ImageView
        try {
            Field field;
            field = glideContext.getClass().getDeclaredField("imageViewTargetFactory");
            field.setAccessible(true);
            ImageViewTargetFactory imageViewTargetFactory = (ImageViewTargetFactory) field.get(glideContext);
            ImageViewTargetFactory factory = new NinePatchImageViewTargetFactory(imageViewTargetFactory);
            field.set(glideContext, factory);
            field.setAccessible(false);
            Log.d(TAG, "已设置NinePatchImageViewTargetFactory: 原imageViewTargetFactory=" + imageViewTargetFactory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    private static GlideContext getGlideContext(@NonNull Glide glide) {
        try {
            Field field = glide.getClass().getDeclaredField("glideContext");
            field.setAccessible(true);
            GlideContext glideContext = (GlideContext) field.get(glide);
            field.setAccessible(false);
            return glideContext;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
