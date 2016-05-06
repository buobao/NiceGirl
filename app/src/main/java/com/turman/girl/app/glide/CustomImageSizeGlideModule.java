package com.turman.girl.app.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by dqf on 2016/5/6.
 */
public class CustomImageSizeGlideModule implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        // nothing to do here
    }

    @Override public void registerComponents(Context context, Glide glide) {
        glide.register(CustomImageSizeModel.class, InputStream.class, new CustomImageSizeModelFactory());
    }
}