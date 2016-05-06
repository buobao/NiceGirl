package com.turman.girl.app.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.turman.girl.app.R;
import com.turman.girl.app.bean.ImageEntity;
import com.turman.girl.app.bean.ImageListResult;
import com.turman.girl.app.net.NetHelper;
import com.turman.girl.app.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dqf on 2016/5/6.
 */
public class HomeActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.fabButton)
    protected ImageButton mFabButton;

    protected int curr_page;
    protected int page_size;
    protected List<ImageEntity> imgUrlList;
    protected RecyclerAdapter recyclerAdapter;

    @Override
    protected void beforeCreate() {
        setTheme(R.style.AppThemeRed);
        curr_page = 1;
        page_size = 10;
        imgUrlList = new ArrayList<>();
    }

    @Override
    protected int getLayout() {
        return R.layout.act_home;
    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    protected void initView(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(imgUrlList);
        recyclerAdapter.setOnClickItemListener(new RecyclerAdapter.OnClickItemListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(HomeActivity.this, ShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ShowActivity.TITLE, imgUrlList.get(position).title);
                bundle.putString(ShowActivity.URL, imgUrlList.get(position).img);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(recyclerAdapter);

        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }

            @Override
            public void onBottom() {
                curr_page++;
                loadData();
            }
        });

        //初始化加载数据
        loadData();
    }

    private void loadData() {
        Map<String, Object> params = new HashMap<>();
        params.put("page",curr_page);
        params.put("size",page_size);
        mSubscriptions.add(
                Observable.just(params)
                        .flatMap(new Func1<Map<String, Object>, Observable<ImageListResult>>() {
                            @Override
                            public Observable<ImageListResult> call(Map<String, Object> stringObjectMap) {
                                return NetHelper.getImgService().getList((int)stringObjectMap.get("page"),(int)stringObjectMap.get("size"));
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ImageListResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Toast.makeText(HomeActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(ImageListResult imageListResult) {
                                List<ImageEntity> list = imageListResult.tngou;
                                if (list != null && list.size()> 0) {
                                    for (ImageEntity entity : list) {
                                        imgUrlList.add(entity);
                                    }
                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        })
        );
    }

    @Override
    protected void handleFunc(Message msg) {

    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void hideViews() {
        //toolbar隐藏的动画
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        //底部浮动按钮的隐藏动画
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
