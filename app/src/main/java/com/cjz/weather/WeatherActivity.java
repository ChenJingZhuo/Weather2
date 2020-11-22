package com.cjz.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherActivity extends AppCompatActivity {

    private final static String TAG = "WeatherActivity";

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private Button navButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private String mWeatherId;

    String location;
    String loc;
    String tmp;
    String cond_txt;
    List<ForecastBase> daily_forecast;
    List<LifestyleBase> lifestyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        mWeatherId = getIntent().getStringExtra("weather_id");
        requestWeather(mWeatherId);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        mWeatherId=weatherId;
        /**
         * 和风天气
         *
         * @param context  上下文
         * @param location 地址详解
         * @param lang     多语言，默认为简体中文，海外城市默认为英文
         * @param unit     单位选择，公制（m）或英制（i），默认为公制单位
         * @param listener 网络访问回调接口
         */
        HeWeather.getWeather(WeatherActivity.this, weatherId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(final interfaces.heweather.com.interfacesmodule.bean.weather.Weather weather) {
                //basic 基础信息
                Basic basic = weather.getBasic();
                location = basic.getLocation();              //地区／城市名称          卓资
//                Log.d(TAG, basic.toString());

                //update 接口更新时间
                Update update = weather.getUpdate();
                loc = update.getLoc();                       //当地时间，24小时制，格式yyyy-MM-dd HH:mm     2017-10-25 12:34
//                Log.d(TAG, update.toString());

                //satuts 接口状态
                //String status = weather.getStatus();       //接口状态，具体含义请参考接口状态码及错误码     ok

                //now 实况天气
                NowBase now = weather.getNow();
                tmp = now.getTmp();                          //温度                21
                cond_txt = now.getCond_txt();                //实况天气状况描述      晴
//                Log.d(TAG, now.toString());

                //daily_forecast 天气预报
                daily_forecast = weather.getDaily_forecast();

                //hourly 逐小时预报
                //List<HourlyBase> hourly = weather.getHourly();

                //lifestyle 生活指数
                lifestyle = weather.getLifestyle();

                //lifestyle_forecast 生活指数预报
                //List<LifestyleForecastBase> lifestyle_forecast = weather.getLifestyle_forecast();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherInfo();
                    }
                });

            }
        });

    }

    private void showWeatherInfo() {
        titleCity.setText(location);
        titleUpdateTime.setText(loc.trim().split(" ")[1]);
        degreeText.setText(tmp+"℃");
        weatherInfoText.setText(cond_txt);
        forecastLayout.removeAllViews();
        for (ForecastBase forecast : daily_forecast) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.getDate());           //预报日期  2013-12-30
            infoText.setText(forecast.getCond_txt_d());     //白天天气状况描述  晴
            maxText.setText(forecast.getTmp_max());         //最高温度  4
            minText.setText(forecast.getTmp_min());         //最低温度  -5
            forecastLayout.addView(view);
        }
        for (LifestyleBase lifestyleBase : lifestyle) {
            String brf = lifestyleBase.getBrf();    //生活指数简介
            String txt = lifestyleBase.getTxt();    //生活指数详细描述
            switch (lifestyleBase.getType()){
                case "comf":
                    comfortText.setText("舒适度："+brf+"。"+txt);
                    break;
                case "cw":
                    carWashText.setText("洗车指数："+brf+"。"+txt);
                    break;
                case "sport":
                    sportText.setText("运动建议："+brf+"。"+txt);
                    break;
            }
        }
        swipeRefresh.setRefreshing(false);
    }

}
