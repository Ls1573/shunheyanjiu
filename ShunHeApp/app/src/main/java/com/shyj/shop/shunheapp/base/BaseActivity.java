package com.shyj.shop.shunheapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shyj.shop.shunheapp.utils.CustomClipLoading;


/**
 * Created by ls on 16/5/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private CustomClipLoading dialog;// 加载


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    protected abstract void initView();

    public abstract void initData();

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 显示进度条
     */
    protected void showLoadingDialog() {
        if (dialog == null) {
            dialog = new CustomClipLoading(this);
            //dialog.setOnOutSide(true);
        }
        if (!isFinishing()) {
            dialog.show();
        }
    }

    /**
     * 隐藏进度条
     */
    protected void hideLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
