package com.turman.girl.app.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.turman.girl.app.R;
import com.turman.girl.app.net.NetHelper;
import com.turman.girl.app.ui.BaseActivity;
import com.turman.girl.app.widget.TouchImageView;

import butterknife.Bind;

/**
 * Created by dqf on 2016/5/6.
 */
public class ShowActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String URL = "url";

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.show_img)
    protected TouchImageView mTouchImageView;

    protected Bundle mBundle;

    @Override
    protected int getLayout() {
        return R.layout.act_show;
    }

    @Override
    protected void beforeCreate() {
        setTheme(R.style.AppThemeRed);
        mBundle = getIntent().getExtras();
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(mBundle.getString(TITLE));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setNavigationIcon(R.mipmap.back32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView(View view) {
        String img_url = NetHelper.IMG_BASE_URL + mBundle.getString(URL);

        Glide.with(getApplicationContext())
                .load(img_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mTouchImageView);
    }

    @Override
    protected void handleFunc(Message msg) {

    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }
}
