package com.turman.girl.app.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.turman.girl.app.R;
import com.turman.girl.app.net.NetHelper;
import com.turman.girl.app.ui.BaseActivity;
import com.turman.girl.app.utils.FileUtil;
import com.turman.girl.app.widget.TouchImageView;

import butterknife.Bind;

/**
 * Created by dqf on 2016/5/6.
 */
@SuppressWarnings("ALL")
public class ShowActivity extends BaseActivity {
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String TYPE = "type";

    public static final String MENU_NORMAL = "normal";
    public static final String MENU_SHOW = "show";

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.show_img)
    protected TouchImageView mTouchImageView;

    protected Bundle mBundle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menu_type = mBundle.getString(TYPE);
        if (MENU_NORMAL.equals(menu_type)){
            getMenuInflater().inflate(R.menu.act_show_menu,menu);
        } else {
            getMenuInflater().inflate(R.menu.act_show_collect_menu,menu);
        }
        return true;
    }

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
        mToolbar.setNavigationOnClickListener(v ->{finish();});
        mToolbar.setOnMenuItemClickListener((item)->{
            switch (item.getItemId()) {
                case R.id.collection_img:
                    if (!mImageDB.queryByURL(mBundle.getString(URL))) {
                        mImageDB.insert(mBundle.getString(TITLE),mBundle.getString(URL));
                    }
                    Toast.makeText(ShowActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.save_img:
                    if (mTouchImageView.getDrawable() != null) {
                        mTouchImageView.setDrawingCacheEnabled(true);
                        FileUtil.saveImage(mTouchImageView.getDrawingCache(), mBundle.getString(TITLE), getApplicationContext());
                        mTouchImageView.setDrawingCacheEnabled(false);
                        Toast.makeText(ShowActivity.this,"已保存到本地相册",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShowActivity.this,"图片未加载,请稍后再试",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.remove_img:
                    mImageDB.deleteByUrl(mBundle.getString(URL));
                    Toast.makeText(ShowActivity.this,"已从收藏中移除",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.clear_img:
                    mImageDB.clear();
                    Toast.makeText(ShowActivity.this,"已清空收藏除",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
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
