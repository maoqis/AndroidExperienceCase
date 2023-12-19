package com.maoqis.testcase;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.maoqis.testcase.component.BaseCaseFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Glide9pngFragment extends BaseCaseFragment {
    private static final String TAG = "Glide9pngFragment";

    @Override
    protected int getRLayout() {
        return R.layout.fragment_glide_9png;
    }

    @Override
    protected void onInitView(ViewGroup rootView) {

        ImageView ivH48 = rootView.findViewById(R.id.iv_h48);
        ImageView ivAPPT = rootView.findViewById(R.id.iv_appt);

        //直接显示原9.png 有黑边，缺少Bitmap中的9.png 的chunk信息。

        String url = "https://raw.githubusercontent.com/vindolin/ninepatch/master/src/ninepatch/data/ninepatch_bubble.9.png";
        Glide.with(this).asBitmap()
                .dontTransform()//
                .load(url).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = get9pngBitmapDrawable(bitmap);
                        ivH48.setImageDrawable(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d(TAG, "onLoadCleared() called with: placeholder = [" + placeholder + "]");
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });

        //原因
        findSetOnClickListener(R.id.tv_not_appt, v -> {
            String fileName = "ninepatch_bubble.9.png";
            Bitmap assetBitmap = getAssetBitmap(fileName);
            Drawable pngBitmapDrawable = get9pngBitmapDrawable(assetBitmap);
            ivAPPT.setImageDrawable(pngBitmapDrawable);
        });


        //方案：https://www.jianshu.com/p/d3a7d5edb7bd中提到 ，使用 'appt s -i 来源 -o 目标' ，编译9.png添加块信息。
        findSetOnClickListener(R.id.tv_appt, v -> {
            String fileName = "ninepatch_bubble_chunk.9.png";
            Bitmap assetBitmap = getAssetBitmap(fileName);
            Drawable pngBitmapDrawable = get9pngBitmapDrawable(assetBitmap);

            ivAPPT.setImageDrawable(pngBitmapDrawable);

        });


    }


    @Nullable
    private Bitmap getAssetBitmap(String fileName) {

        InputStream is = null;
        try {
            is = getContext().getAssets().open(fileName);
            Rect outPadding = new Rect();
            Bitmap bitmap = BitmapFactory.decodeStream(is, outPadding, new BitmapFactory.Options());
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @NonNull
    private Drawable get9pngBitmapDrawable(@NonNull Bitmap bitmap) {
        byte[] chunk = bitmap.getNinePatchChunk();
        boolean is9png;
        if (chunk == null) {
            is9png = false;
            Log.d(TAG, "get9pngBitmapDrawable: chunk == null");
        } else {
            if (NinePatch.isNinePatchChunk(chunk)) {
                is9png = true;
            } else {
                is9png = false;
            }
        }


        Drawable drawable;
        Resources resources = getContext().getResources();
        if (is9png) {
            Log.d(TAG, "onResourceReady: is9png");
            Toast.makeText(getContext(), "is 9png", Toast.LENGTH_SHORT).show();
            drawable = new NinePatchDrawable(resources, bitmap, chunk, null, null);
        } else {
            Log.d(TAG, "onResourceReady: is not 9png");
            Toast.makeText(getContext(), "isn't 9png", Toast.LENGTH_SHORT).show();
            drawable = new BitmapDrawable(resources, bitmap);
        }
        return drawable;
    }
}
