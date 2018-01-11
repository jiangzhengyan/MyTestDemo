package com.jingcaiwang.mytestdemo.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jiang_yan on 2017/9/28.
 */

public class ActivitiesManager {
    private static volatile ActivitiesManager instance;
    private List<Activity> list = new ArrayList();

    public static ActivitiesManager getInstance() {
        if (instance == null) {
            instance = new ActivitiesManager();
        }

        return instance;
    }

    public ActivitiesManager() {
    }

    public void pushActivity(Activity activity) {
        this.list.add(activity);
    }

    public void popActivity(Class<?> cls) {
        ArrayList list2 = new ArrayList();
        Iterator var4 = this.list.iterator();

        while (var4.hasNext()) {
            Activity activity = (Activity) var4.next();
            if (cls.equals(activity.getClass())) {
                activity.finish();
            } else {
                list2.add(activity);
            }
        }

        this.list = list2;
    }

    public void popOtherActivity(Class... cls) {
        if (cls != null) {
            boolean i = false;
            ArrayList list2 = new ArrayList();
            Iterator var5 = this.list.iterator();

            while (var5.hasNext()) {
                Activity activity = (Activity) var5.next();

                int var6;
                for (var6 = 0; var6 < cls.length && !activity.getClass().equals(cls[var6]); ++var6) {
                    ;
                }

                if (var6 == cls.length) {
                    activity.finish();
                } else {
                    list2.add(activity);
                }
            }

            this.list = list2;
        }
    }

    public void popAllActivity() {
        Iterator var2 = this.list.iterator();

        while (var2.hasNext()) {
            Activity activity = (Activity) var2.next();
            activity.finish();
        }

        this.list.clear();
    }
}
