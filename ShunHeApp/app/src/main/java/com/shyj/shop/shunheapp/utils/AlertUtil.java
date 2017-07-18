package com.shyj.shop.shunheapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shyj.shop.shunheapp.R;


public class AlertUtil {

    private static Toast mToast;

    public static void t(Context context, String msg) {
        if (StringUtil.isEmpty(msg)){
            return;
        }
        if (mToast == null) {
            mToast = new MyToast(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void t(Context context, int msgRes) {
        if (StringUtil.isEmpty( String.valueOf(msgRes))){
            return;
        }

        if (mToast == null) {
            mToast = new MyToast(context, String.valueOf(msgRes), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msgRes);
        }
        mToast.show();
    }

    public static void t(Context context, int msgRes, int duration) {
        if (StringUtil.isEmpty( String.valueOf(msgRes))){
            return;
        }

        if (mToast == null) {
            mToast = new MyToast(context, String.valueOf(msgRes), duration);
        } else {
            mToast.setText(msgRes);
        }
        mToast.show();
    }

    public static void t(Context context, String msg, int duration) {
        if (StringUtil.isEmpty(msg)){
            return;
        }

        if (mToast == null) {
            mToast = new MyToast(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }


    public static void  cancel(){
        if (mToast != null){
            mToast.cancel();
        }
    }

    public static class MyToast extends Toast {

        public MyToast(Context context, String msg, int duration) {
            super(context);
            LayoutInflater inflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflate.inflate(R.layout.view_toast, null);
            TextView tv = (TextView) v.findViewById(android.R.id.message);
            tv.setText(msg);
            setView(v);
            // setGravity方法用于设置位置，此处为垂直居中
            setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            setDuration(duration);
        }
    }

}
