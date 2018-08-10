package com.soundbrenner.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import layout.DiscoveryCard;


/**
 * Created by danilapuzikov on 19/09/2017.
 */

public class ViewFactory {
    private static Context mContext;
    private static ImageCache cache;
    private static int defaultGap = 90;
    private static int halfGap = 40;
    private static int topBottomPadding = 20;

    private static RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    private static FrameLayout.LayoutParams    fllp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);

    public ViewFactory(Context context, ImageCache c){
        mContext    = context;
        cache       = c;
    }

    public static ViewPager createDiscoveryCardSwiper(final JSONArray data)
    {
        ViewPager   swiper = new ViewPager(mContext);
                    swiper.setLayoutParams(rllp);
                    swiper.setPadding(defaultGap, topBottomPadding, defaultGap, topBottomPadding);
                    swiper.setClipToPadding(false);
                    swiper.setPageMargin(halfGap);
                    swiper.setAdapter(new PagerAdapter()
                    {
                        int virtualPosition = 1;

                        final int count = (int) data.length() + 1;

                        @Override
                        public Object instantiateItem(ViewGroup container, int position) {
                            LayoutInflater inflater = LayoutInflater.from(mContext);
                            int counted = virtualPosition;
                            if(virtualPosition >= count) counted = virtualPosition % count;
                            if(counted == 3) counted = 1;
                            virtualPosition += 1;
                            final int c = counted;
                            try {
                                JSONObject dd = data.getJSONObject(counted);
                                ViewGroup discovery_card = (ViewGroup) new DiscoveryCard().onCreateView(inflater, container, createDiscoveryCardBundle(dd));
                                discovery_card.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.e("Clicked card", "#"+c);
                                        Intent  intent = new Intent(mContext, FullDiscoveryCardActivity.class);
                                        try {
                                            Bundle intent_bundle = createDiscoveryCardBundle(data.getJSONObject(c));
                                            intent.putExtra("bundle", intent_bundle);
                                            ((Activity) mContext).startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                container.addView(discovery_card);
                                return discovery_card;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public int getCount() {
                            return Integer.MAX_VALUE;
                        }

                        @Override
                        public boolean isViewFromObject(View view, Object object) {
                            return view == object;
                        }

                        @Override
                        public void destroyItem(ViewGroup container, int position, Object object) {
                            container.removeView((View)object);
                        }
                    });
                    swiper.setCurrentItem(16);

        return      swiper;
    }

    private static Bundle createDiscoveryCardBundle(JSONObject data) throws JSONException, IOException {

        String title    = data.getString("title");
        String txt      = data.getString("text");
        String img_url  = data.getString("image");
        Bitmap bmp      = cache.getImageFromWarehouse(img_url);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        Bundle  args = new Bundle();
                args.putString("title", title);
                args.putString("text", txt);
                args.putByteArray("bitmap", bytes);
        return args;
    }
}
