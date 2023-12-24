package com.maoqis.testcase.glide.encode;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.maoqis.testcase.BuildConfig;
import com.maoqis.testcase.glide.Constants;
import com.maoqis.testcase.glide.utils.NinePngUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NineBitmapEncoder extends BitmapEncoder {
    private static final String TAG = "NineBitmapEncoder";

    public NineBitmapEncoder(@NonNull ArrayPool arrayPool) {
        super(arrayPool);
    }

    @Override
    public boolean encode(@NonNull Resource<Bitmap> resource, @NonNull File file, @NonNull Options options) {
        Bitmap bitmap = resource.get();
        boolean is9png = NinePngUtils.is9png(bitmap);
        Log.d(TAG, "encode: bitmap is9png=" + is9png + "不保存文件，后续块缓存文件什么时候创建 to=" + file.getPath());
        if (is9png) {
            return true;
        }


        return super.encode(resource, file, options);
    }

    private void saveBitmapToFile(File file, Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
        Boolean is9png = options.get(Constants.IS_9PNG);
        EncodeStrategy source;
        if (is9png) {
            source = EncodeStrategy.SOURCE;
        } else {
            source = EncodeStrategy.TRANSFORMED;
        }
        Log.d(TAG, "getEncodeStrategy: is9png=" + is9png + " source=" + source);
        return source;
    }
}
