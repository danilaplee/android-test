package com.soundbrenner.testapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ViewFactory vFactory;
    private FrameLayout content;
    private ProgressBar loader;
    private Context mContext;
    private Handler handler;
    private JSONArray DiscoveryData;
    private ImageCache cache;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    return true;
            }
            return false;
        }

    };

    private JSONArray createDummyData() throws JSONException {
        String lorem = getString(R.string.lorem_ipsum);
        JSONObject  a = new JSONObject();
                    a.put("image", "https://starpy.me/img_2429.jpg");
                    a.put("title", "Test A");
                    a.put("text", lorem);
        JSONObject  b = new JSONObject();
                    b.put("image", "https://starpy.me/dsc_02439.jpg");
                    b.put("title", "Test B");
                    b.put("text", lorem);
        JSONObject  c = new JSONObject();
                    c.put("image", "https://starpy.me/dsc_00871.jpg");
                    c.put("title", "Test C");
                    c.put("text", lorem);
        JSONArray   arr = new JSONArray();
                    arr.put(a);
                    arr.put(b);
                    arr.put(c);
        return arr;
    }

    private void cacheImages() throws JSONException {
        JSONArray dd = DiscoveryData;
        for (int i = 0; i < dd.length(); i++)
        {
                final JSONObject d = dd.getJSONObject(i);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cache.DownloadImage(d.getString("image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
        }
    }

    public void DrawCards() {
        final ViewPager swiper = ViewFactory.createDiscoveryCardSwiper(DiscoveryData);
        swiper.setVisibility(View.GONE);

        handler.post(new Runnable() {
            @Override
            public void run() {
                content.addView(swiper);
                swiper.animate().translationY(content.getHeight()*-1).start();
                loader.animate().translationY(content.getHeight()).start();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiper.setVisibility(View.VISIBLE);
                        swiper.animate().translationY(0).start();
                    }
                }, 300);
            }
        });
    }

    private void startDiscovery() {
        try {
            DiscoveryData = createDummyData();
            Log.e("LOADED_DISCOVERY_DATA", DiscoveryData.toString());
            cacheImages();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        content     = (FrameLayout) findViewById(R.id.content);
        loader      = (ProgressBar) findViewById(R.id.progressBar);

        mContext    = this;
        handler     = new Handler(mContext.getMainLooper());
        cache       = new ImageCache();
        cache.initializeCache(mContext);

        vFactory    = new ViewFactory(mContext, cache);

        startDiscovery();
    }

}
