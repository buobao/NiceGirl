package com.turman.girl.app.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.turman.girl.app.ui.activity.ShowActivity;

/**
 * Created by dqf on 2016/5/10.
 */
public class ExampleTest extends InstrumentationTestCase {

    private ShowActivity activity;

    @Override
    protected void setUp() throws Exception {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setClassName("com.turman.girl.app.ui.activity",ShowActivity.class.getName());
        System.out.println("Intent has defined......");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(ShowActivity.URL,"/ext/160417/fc9d6b190c5300159d82b4579378bcbf.jpg");
        bundle.putString(ShowActivity.TITLE,"媚娘花枝招展的青春");
        bundle.putString(ShowActivity.TYPE,ShowActivity.MENU_SHOW);
        intent.putExtras(bundle);
        activity = (ShowActivity) getInstrumentation().startActivitySync(intent);
    }

    @Override
    protected void tearDown() throws Exception {
        activity.finish();
        try {
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 活动功能测试
    */
    public void testActivity() throws Exception {
        Log.v("testActivity", "test the Activity");
        SystemClock.sleep(1500);
        SystemClock.sleep(3000);
    }


}
