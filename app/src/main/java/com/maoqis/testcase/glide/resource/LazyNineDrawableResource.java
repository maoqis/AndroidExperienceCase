package com.maoqis.testcase.glide.resource;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Preconditions;
import com.maoqis.testcase.glide.utils.NinePngUtils;

/**
 * LazyBitmapDrawableResource final 无法继承
 */
public class LazyNineDrawableResource implements Resource<NinePatchDrawable>, Initializable {
    private static final String TAG = "LazyNineDrawableResource";

    private final Resources resources;
    private final Resource<Bitmap> bitmapResource;

    /**
     * @deprecated Use {@link #obtain(Resources, Resource)} instead, it can be unsafe to extract
     * {@link Bitmap}s from their wrapped {@link Resource}.
     */
    @Deprecated
    public static LazyNineDrawableResource obtain(Context context, Bitmap bitmap) {
        return (LazyNineDrawableResource)
                obtain(
                        context.getResources(),
                        BitmapResource.obtain(bitmap, Glide.get(context).getBitmapPool()));
    }

    /**
     * @deprecated Use {@link #obtain(Resources, Resource)} instead, it can be unsafe to extract
     * {@link Bitmap}s from their wrapped {@link Resource}.
     */
    @Deprecated
    public static LazyNineDrawableResource obtain(
            Resources resources, BitmapPool bitmapPool, Bitmap bitmap) {
        return (LazyNineDrawableResource)
                obtain(resources, BitmapResource.obtain(bitmap, bitmapPool));
    }

    @Nullable
    public static Resource<NinePatchDrawable> obtain(
            @NonNull Resources resources, @Nullable Resource<Bitmap> bitmapResource) {
        if (bitmapResource == null) {
            return null;
        }
        return new LazyNineDrawableResource(resources, bitmapResource);
    }

    private LazyNineDrawableResource(
            @NonNull Resources resources, @NonNull Resource<Bitmap> bitmapResource) {
        this.resources = Preconditions.checkNotNull(resources);
        this.bitmapResource = Preconditions.checkNotNull(bitmapResource);
    }

    @NonNull
    @Override
    public Class<NinePatchDrawable> getResourceClass() {
        return NinePatchDrawable.class;
    }

    @NonNull
    @Override
    public NinePatchDrawable get() {
        Bitmap bitmap = bitmapResource.get();
        boolean is9png = NinePngUtils.is9png(bitmap);
        Log.d(TAG, "get: is9png=" + is9png);
        return (NinePatchDrawable)NinePngUtils.createDrawable(bitmap,is9png,resources);
    }

    @Override
    public int getSize() {
        return bitmapResource.getSize();
    }

    @Override
    public void recycle() {
        bitmapResource.recycle();
    }

    @Override
    public void initialize() {
        if (bitmapResource instanceof Initializable) {
            ((Initializable) bitmapResource).initialize();
        }
    }
}
