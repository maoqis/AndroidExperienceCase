package com.maoqis.testcase.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.NinePatchDrawable;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.module.LibraryGlideModule;
import com.maoqis.testcase.glide.decoder.ByteBufferBitmap9pngDecoder;
import com.maoqis.testcase.glide.decoder.NineBitmapDrawableDecoder;
import com.maoqis.testcase.glide.decoder.StreamBitmap9pngDecoder;
import com.maoqis.testcase.glide.encoder.NineBitmapEncoder;
import com.maoqis.testcase.glide.encoder.NineDrawableEncoder;

import java.io.InputStream;
import java.nio.ByteBuffer;

@GlideModule
public class NinePatchGlideModule extends LibraryGlideModule {
    private static final String TAG = "NineWebGlideModule";

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        Log.d(TAG, "registerComponents: registry=" + registry);
        // We should put our decoder before the build-in decoders,
        // because the Downsampler will consume arbitrary data and make the inputstream corrupt
        // on some devices
        final Resources resources = context.getResources();
        final BitmapPool bitmapPool = glide.getBitmapPool();
        final ArrayPool arrayPool = glide.getArrayPool();
        ByteBufferBitmap9pngDecoder byteBufferBitmapDecoder = new ByteBufferBitmap9pngDecoder(bitmapPool);
        StreamBitmap9pngDecoder streamBitmapDecoder = new StreamBitmap9pngDecoder(bitmapPool, arrayPool);
        ResourceDecoder<ParcelFileDescriptor, Bitmap> parcelFileDescriptorVideoDecoder = VideoDecoder.parcel(bitmapPool);

        NineBitmapEncoder bitmapEncoder = new NineBitmapEncoder(arrayPool);

        registry
                //Bitmap支持9png
                .prepend(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder)
                .prepend(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder)
                .prepend(Bitmap.class, bitmapEncoder)//加码器缓存时候用到
                //NinePatchDrawable
                .prepend(
                        Registry.BUCKET_BITMAP_DRAWABLE,
                        ByteBuffer.class,
                        NinePatchDrawable.class,
                        new NineBitmapDrawableDecoder<>(resources, byteBufferBitmapDecoder, arrayPool))
                .prepend(
                        Registry.BUCKET_BITMAP_DRAWABLE,
                        InputStream.class,
                        NinePatchDrawable.class,
                        new NineBitmapDrawableDecoder<>(resources, streamBitmapDecoder, arrayPool))
                .prepend(NinePatchDrawable.class, new NineDrawableEncoder(bitmapEncoder))//
        ;

    }


}
