package com.example.framwork.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.framwork.R;


/**
 * Dialog对话框生成工具类
 *
 * @author zhuyanfei
 */
public class DialogUtils {
    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     *
     * @author xuzhaohu
     */
    private static class SingletonHolder {
        private static DialogUtils instance = new DialogUtils();
    }

    /**
     * 私有的构造函数
     */
    private DialogUtils() {

    }

    public static DialogUtils getInstance() {
        return DialogUtils.SingletonHolder.instance;
    }

    /**
     * 中间Dialog
     *
     * @param context
     * @param
     * @return
     */
    public Dialog getCenterDialog(Context context, int id) {
        Dialog dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(id);

        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setWindowAnimations(R.style.mystyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }


    /**
     * 点击对话框外可取消
     *
     * @param context
     * @param id
     * @return
     */
    public Dialog getCenterCancelDialog(Context context, int id) {
        Dialog dialog = new Dialog(context, R.style.custom_cancel_dialog);
        dialog.setContentView(id);

        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setWindowAnimations(R.style.mystyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    /**
     * 中间Dialog
     *
     * @param context
     * @param
     * @return
     */
    public Dialog getCenterDialog(Context context, int id, double width) {
        Dialog dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(id);

        Window dialogWindow = dialog.getWindow();
        //dialogWindow.setWindowAnimations(R.style.mystyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) ((context.getResources().getDisplayMetrics().widthPixels) * width);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * 底部Dialog 有动画
     *
     * @param context
     * @param id      资源ID
     * @return
     */
    public Dialog getBottomDialog(Context context, int id) {
        Dialog dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(id);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.Dialog_Bottom);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    /**
     * 底部Dialog，有动画
     *
     * @param context
     * @param view    View对象
     * @return
     */
    public Dialog getBottomDialog(Context context, View view) {
        Dialog dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.Dialog_Bottom);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }
}
