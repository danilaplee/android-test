package com.soundbrenner.testapp;

/**
 * Created by danilapuzikov on 19/09/2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private String imageUrl;

    private BaseAdapter adapter;

    private ImageCache cache;

    private int desiredWidth, desiredHeight;

    private int inSampleSize = 1;

    private Bitmap image = null;

    public DownloadImageTask(ImageCache c, int desiredWidth, int desiredHeight) {
        this.cache = c;

        this.desiredWidth = desiredWidth;

        this.desiredHeight = desiredHeight;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        return getImage(imageUrl);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.e("DONE LOADING", imageUrl);
        super.onPostExecute(result);
        cache.addImageToWarehouse(imageUrl, image);
        cache.notifyDataSetChanged();
    }

    private Bitmap getImage(String imageUrl) {
        Log.e("LOADING IMAGE", imageUrl);
        Bitmap fromWareHouse = cache.getImageFromWarehouse(imageUrl);
        if (fromWareHouse == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = false;

            options.inSampleSize = inSampleSize;
            int TIMEOUT_VALUE = 10000;

            try {
                URL url = new URL(imageUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(TIMEOUT_VALUE);
                connection.setReadTimeout(TIMEOUT_VALUE);

                InputStream stream = connection.getInputStream();

                image = BitmapFactory.decodeStream(stream, null, options);

                stream.close();
                if(image == null) {
                    Log.e("NO IMAGE FOUND", "TRUE");
                    return null;
                }
                int imageWidth = image.getWidth();

                int imageHeight = image.getHeight();

                Double maxAllowedDiff = 1.5;
                int Diff = imageWidth/desiredWidth;
                Log.e("IMAGE DIFF:", String.valueOf(Diff));

                if (Diff > maxAllowedDiff) {
                    Bitmap bm;
                    bm = Bitmap.createScaledBitmap(image, (image.getWidth()/Diff), (image.getHeight()/Diff), true);
                    onPostExecute(bm);
                    return bm;
                }
                onPostExecute(image);
                return image;
            } catch (SocketTimeoutException e) {
                Log.e("TIMEOUT EXCEPTION1", "TRUE");
                return null;
            } catch (Exception e) {
                Log.e("TIMEOUT EXCEPTION2", e.toString());
                return null;
            }
        }
        else
        {
            return fromWareHouse;
        }

    }



}
