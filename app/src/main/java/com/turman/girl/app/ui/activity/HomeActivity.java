package com.turman.girl.app.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.turman.girl.app.R;
import com.turman.girl.app.bean.ImageEntity;
import com.turman.girl.app.bean.ImageListResult;
import com.turman.girl.app.db.ImageDB;
import com.turman.girl.app.net.NetHelper;
import com.turman.girl.app.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static final String FLAG_NET = "show_net";
    public static final String FLAG_COLLECTION = "show_collection";

    @Bind(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.fabButton)
    protected ImageButton mFabButton;
    @Bind(R.id.refresh_layout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected int curr_page;
    protected int page_size;

    protected int coll_page;
    protected int coll_size;

    protected boolean isAllLoad_net;
    protected boolean isAllLoad_coll;

    protected List<ImageEntity> imgUrlList;
    protected List<ImageEntity> collectedList;
    protected RecyclerAdapter recyclerAdapter;
    protected RecyclerAdapter collRecyclerAdapter;

    RecyclerAdapter.OnClickItemListener listener = new RecyclerAdapter.OnClickItemListener() {
        @Override
        public void onClick(int position) {
            Intent intent = new Intent(HomeActivity.this, ShowActivity.class);
            Bundle bundle = new Bundle();
            if (FLAG_COLLECTION.equals(flag)) {
                bundle.putString(ShowActivity.TYPE, ShowActivity.MENU_SHOW);
                bundle.putString(ShowActivity.TITLE, collectedList.get(position).title);
                bundle.putString(ShowActivity.URL, collectedList.get(position).img);
            } else {
                bundle.putString(ShowActivity.TITLE, imgUrlList.get(position).title);
                bundle.putString(ShowActivity.URL, imgUrlList.get(position).img);
                bundle.putString(ShowActivity.TYPE, ShowActivity.MENU_NORMAL);
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    protected String flag;

    @Override
    protected void beforeCreate() {
        setTheme(R.style.AppThemeRed);
        curr_page = 1;
        page_size = 10;
        coll_page = 1;
        coll_size = 10;
        flag = FLAG_NET;

        isAllLoad_net = false;
        isAllLoad_coll = false;
        imgUrlList = new ArrayList<>();
        collectedList = new ArrayList<>();
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
        collRecyclerAdapter = new RecyclerAdapter(collectedList);
        recyclerAdapter.setOnClickItemListener(listener);
        collRecyclerAdapter.setOnClickItemListener(listener);

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
                if (FLAG_NET.equals(flag)) {
                    curr_page++;
                } else {
                    coll_page++;
                }
                loadData();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(HomeActivity.this,"refresh",Toast.LENGTH_SHORT).show();
                if (FLAG_COLLECTION.equals(flag)){
                    isAllLoad_coll = false;
                    coll_page = 1;
                    collectedList.clear();
                } else {
                    isAllLoad_net = false;
                    curr_page = 1;
                    imgUrlList.clear();
                }
                loadData();
            }
        });

        //初始化加载数据
        if (imgUrlList == null || imgUrlList.size() == 0 ) {
            loadData();
        }

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG_NET.equals(flag)) {
                    flag = FLAG_COLLECTION;
                    if (collectedList.size() == 0) {
                        loadData();
                    }
                    mRecyclerView.setAdapter(collRecyclerAdapter);
                    mToolbar.setTitle(getString(R.string.collection_title));
                } else {
                    flag = FLAG_NET;
                    mRecyclerView.setAdapter(recyclerAdapter);
                    mToolbar.setTitle(getString(R.string.app_name));
                }
            }
        });
    }

    private void loadData() {
        if (FLAG_NET.equals(flag) && !isAllLoad_net) {
            Map<String, Object> params = new HashMap<>();
            params.put("page",curr_page);
            params.put("size",page_size);
            mSubscriptions.add(
                    Observable.just(params)
                            .flatMap(new Func1<Map<String, Object>, Observable<ImageListResult>>() {
                                @Override
                                public Observable<ImageListResult> call(Map<String, Object> stringObjectMap) {
                                    Random ra =new Random();
                                    return NetHelper.getImgService().getList((int) stringObjectMap.get("page"), (int) stringObjectMap.get("size"),ra.nextInt(7)+1);
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
//                                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(ImageListResult imageListResult) {
                                    List<ImageEntity> list = imageListResult.tngou;
                                    if (list != null && list.size() > 0) {
                                        for (ImageEntity entity : list) {
                                            imgUrlList.add(entity);
                                        }
                                        recyclerAdapter.notifyDataSetChanged();
                                    } else {
                                        isAllLoad_net = true;
                                    }
                                }
                            })
            );
        }

        if(FLAG_COLLECTION.equals(flag) && !isAllLoad_coll) {
            //从本地读取收藏的记录
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = mImageDB.select(coll_page, coll_size);
                    if (cursor.getCount() < coll_size) {
                        isAllLoad_coll = true;
                    }
                    while (cursor.moveToNext()) {
                        ImageEntity entity = new ImageEntity();
                        entity.title =cursor.getString(cursor.getColumnIndex(ImageDB.IMAGE_TITLE));
                        entity.img = cursor.getString(cursor.getColumnIndex(ImageDB.IMAGE_URL));
                        collectedList.add(entity);
                    }
                    cursor.close();
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void handleFunc(Message msg) {
        switch (msg.what) {
            case 0:
                collRecyclerAdapter.notifyDataSetChanged();
                break;
        }
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
