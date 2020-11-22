package com.cjz.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class MainActivity extends AppCompatActivity {

    private final static String userName = "HE2005082205431654";            //用户名
    private final static String key = "e683b71c02744dffb4dc5936e2a509b9";   //自己申请的key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用 SDK 时，需提前进行账户初始化（全局执行一次即可）
        HeConfig.init(userName, key);
        //个人开发者、企业开发者、普通用户等所有使用免费数据的用户需要切换到免费服务域名 即 https://free-api.heweather.net/s6/sdk/
        HeConfig.switchToFreeServerNode();
    }

}
