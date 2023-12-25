package com.maoqis.testcase.glide;

import android.util.Log;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.BaseRequestOptions;
import com.maoqis.testcase.glide.trans.NineTransformationWrap;

import java.lang.reflect.Method;

/**
 * 要复写into 时候的 Transform，使用NineTransformationWrap 包装了层。
 */
@GlideExtension
public class NinePngGlideExtension {
    private static final String TAG = "NinePngGlideExtension";

    /**
     * 将构造方法设为私有，作为工具类使用
     */
    private NinePngGlideExtension() {
    }

    /**
     * 1.自己新增的方法的第一个参数必须是RequestOptions options
     * 2.方法必须是静态的
     *
     * @param options
     */
    @GlideOption(override = GlideOption.OVERRIDE_REPLACE)
    public static BaseRequestOptions<?> optionalCenterCrop(BaseRequestOptions<?> options) {
        Log.d(TAG, "optionalCenterCrop() called with: options = [" + options + "]");
        //需要和BaseRequestOptions在 相同包名下才能直接调用
        try {
            Method method = options.getClass().getDeclaredMethod("optionalTransform",
                    DownsampleStrategy.class, Transformation.class);
            method.setAccessible(true);
            BaseRequestOptions<?> ret = (BaseRequestOptions<?>) method.invoke(options,
                    DownsampleStrategy.CENTER_OUTSIDE, new NineTransformationWrap(new CenterCrop()));
            method.setAccessible(false);
            return ret;
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "optionalCenterCrop: ", e);
            return options;
        } catch (Exception e) {
            Log.e(TAG, "optionalCenterCrop: ", e);
            return options;
        }
    }

    @GlideOption(override = GlideOption.OVERRIDE_REPLACE)
    public static BaseRequestOptions<?> optionalCenterInside(BaseRequestOptions<?> options) {
        Log.d(TAG, "optionalCenterInside() called with: options = [" + options + "]");

        //optionalScaleOnlyTransform 私有，需要用反射
        try {
            Method method = BaseRequestOptions.class.getDeclaredMethod("optionalScaleOnlyTransform",
                    DownsampleStrategy.class, Transformation.class);
            method.setAccessible(true);
            BaseRequestOptions<?> ret = (BaseRequestOptions<?>) method.invoke(options,
                    DownsampleStrategy.CENTER_INSIDE, new NineTransformationWrap(new CenterInside()));
            method.setAccessible(false);
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "optionalCenterInside: ", e);
            return options;
        }
    }


    @GlideOption(override = GlideOption.OVERRIDE_REPLACE)
    public static BaseRequestOptions<?> optionalFitCenter(BaseRequestOptions<?> options) {
        Log.d(TAG, "optionalCenterCrop() called with: options = [" + options + "]");
        //optionalScaleOnlyTransform 私有，需要用反射
        try {
            Method t = options.getClass().getMethod("optionalFitCenter");
            Log.d(TAG, "optionalFitCenter: t=" + t.getName());
            Method method = BaseRequestOptions.class.getDeclaredMethod("optionalScaleOnlyTransform",
                    DownsampleStrategy.class, Transformation.class);

            Log.d(TAG, "optionalFitCenter: t=" + method.getName());
            method.setAccessible(true);
            BaseRequestOptions<?> ret = (BaseRequestOptions<?>) method.invoke(options,
                    DownsampleStrategy.FIT_CENTER, new NineTransformationWrap(new FitCenter()));
            method.setAccessible(false);
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "optionalFitCenter: ", e);
            return options;
        }
    }



}