package com.turman.girl.app.ui.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.turman.girl.app.R;
import com.turman.girl.app.glide.CustomImageSizeModel;
import com.turman.girl.app.glide.CustomImageSizeModelFutureStudio;
import com.turman.girl.app.net.NetHelper;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

    private final ImageView itemImageView;

    public RecyclerItemViewHolder(final View parent, ImageView itemImageView) {
        super(parent);
        this.itemImageView = itemImageView;
    }

    public static RecyclerItemViewHolder newInstance(View parent) {
        ImageView itemImageView = (ImageView) parent.findViewById(R.id.itemImageView);
        return new RecyclerItemViewHolder(parent, itemImageView);
    }

    public void setItemImage(Context context,CharSequence path) {
        String img_url = NetHelper.IMG_BASE_URL + path;
        CustomImageSizeModel customImageRequest = new CustomImageSizeModelFutureStudio( img_url );

        Glide.with(context)
                .load(customImageRequest)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(itemImageView);
    }










}
