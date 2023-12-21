package com.maoqis.testcase.glide.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.maoqis.testcase.glide.NinePngUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes Webp {@link Bitmap Bitmaps} from {@link InputStream InputStreams}.
 * For static lossless and transparent webp
 *
 * @author liuchun
 */
public class StreamBitmap9pngDecoder implements ResourceDecoder<InputStream, Bitmap> {
    private static final String TAG = "StreamBitmapWebpDecoder";
    private final BitmapPool bitmapPool;


    public StreamBitmap9pngDecoder(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        boolean is9png = isIs9png(source);
        Log.d(TAG, "handles: is9png=" + is9png);
//        options.set(IS_9PNG, is9png);
        return is9png;
    }

    @Override
    public Resource<Bitmap> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        Log.d(TAG, "decode: ");
        Bitmap bitmap = BitmapFactory.decodeStream(source, null, new BitmapFactory.Options());
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    public static boolean isIs9png(@NonNull InputStream source) throws IOException {
        return NinePngUtils.is9png(source);
    }
}
