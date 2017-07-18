package com.shyj.shop.shunheapp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shyj.shop.shunheapp.utils.CustomClipLoading;

/**
 * Created by dllo on 16/5/18.
 */
public abstract class BaseFragment extends Fragment {

    private CustomClipLoading dialog;// 加载
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }

    public abstract int setLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public abstract void initView(View view);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract void initData();

    protected <T extends View> T findView(int id) {
        return (T) getView().findViewById(id);
    }

    /**
     * 显示进度条
     */
    protected void showLoadingDialog() {
        if (dialog == null) {
            dialog = new CustomClipLoading(getActivity());
            //dialog.setOnOutSide(true);
        }
        if (!getActivity().isFinishing()) {
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
