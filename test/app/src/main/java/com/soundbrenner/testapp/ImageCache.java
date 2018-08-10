package com.soundbrenner.testapp;

/**
 * Created by danilapuzikov on 19/09/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.json.JSONArray;


public class ImageCache
{
    private  LruCache<String, Bitmap> imagesWarehouse;

    private JSONArray downloadTasks;

    private static ImageCache cache;

    private static Context mContext;

    private int downloadedImages = 0;

    private int dds              = 0;

    public static ImageCache getInstance()
    {
        if(cache == null)
        {
            cache = new ImageCache();
        }

        return cache;
    }

    public void addImageToWarehouse(String key, Bitmap value) {
        imagesWarehouse.put(key, value);
    }

    public Bitmap getImageFromWarehouse(String key) {
        return imagesWarehouse.get(key);
    }

    public void removeImageFromWarehouse(String key) {
        imagesWarehouse.remove(key);
    }

    public void initializeCache(Context context)
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() /256);

        final int cacheSize = maxMemory;

        mContext = context;

        downloadTasks = new JSONArray();

        imagesWarehouse = new LruCache<String, Bitmap>(cacheSize)
        {
            protected int sizeOf(String key, Bitmap value)
            {
                // The cache size will be measured in kilobytes rather than number of items.

                int bitmapByteCount = value.getRowBytes() * value.getHeight();

                return bitmapByteCount / 1024;
            }
        };
    }

    public void DownloadImage(String url) {

        dds += 1;
        DownloadImageTask DLTask = new DownloadImageTask(this, 300, 300);
        downloadTasks.put(DLTask);
        DLTask.doInBackground(url);
    }


    public void notifyDataSetChanged(){
        downloadedImages += 1;

        Log.e("DATA SET CHANGED", "IMAGES:"+downloadedImages+"/TASKS:"+dds);

        if(downloadedImages == dds) cacheDone();
    }

    private void cacheDone() {
        Log.e("DONE CACHING", "IMAGES:"+downloadedImages+"/TASKS:"+dds);

        ((MainActivity) mContext).DrawCards();
    }

}
