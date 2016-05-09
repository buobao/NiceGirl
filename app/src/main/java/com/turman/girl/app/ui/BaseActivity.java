package com.turman.girl.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.turman.girl.app.db.ImageDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by dqf on 2016/5/6.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected ImageDB mImageDB;
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            handleFunc(msg);
        }
    };
    protected List<Subscription> mSubscriptions = new ArrayList<>();

    protected abstract int getLayout();
    protected abstract void handleFunc(Message msg);
    protected abstract boolean hasToolbar();
    protected void initView(View view){}
    protected void initToolbar(){}
    protected void beforeCreate() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeCreate();
        mImageDB = new ImageDB(getApplicationContext());
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(getLayout(),null);
        setContentView(view);

        ButterKnife.bind(this);
        if (hasToolbar()){
            initToolbar();
        }
        initView(view);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mHandler.removeCallbacksAndMessages(null);

        if (mSubscriptions.size() > 0) {
            for (Subscription s : mSubscriptions) {
                if (s != null && !s.isUnsubscribed()) {
                    s.unsubscribe();
                }
            }
        }
    }
}
