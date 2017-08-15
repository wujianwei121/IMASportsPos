package com.imasports.pos.utils;

import android.content.Context;
import android.content.Intent;

import com.example.framwork.utils.BaseGoto;
import com.imasports.pos.course.ui.CourseListActivity;
import com.imasports.pos.main.MainActivity;
import com.imasports.pos.main.ui.MemeberRegActivity;

/**
 * Created by lenovo on 2017/8/10.
 */

public class Goto extends BaseGoto {
    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void toMemberRegActivity(Context context) {
        Intent intent = new Intent(context, MemeberRegActivity.class);
        context.startActivity(intent);
    }

    public static void toCourseListActivity(Context context) {
        Intent intent = new Intent(context, CourseListActivity.class);
        context.startActivity(intent);
    }
}
