package com.shyj.shop.shunheapp.base;

import android.content.Context;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * Created by dllo on 16/5/23.
 */
public class App extends LitePalApplication {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        LitePalApplication.initialize(this);

        //第一：默认初始化
        Bmob.initialize(this, "759dd361dbc4197fe636742790272384");
        // 第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("759dd361dbc4197fe636742790272384")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }
}
