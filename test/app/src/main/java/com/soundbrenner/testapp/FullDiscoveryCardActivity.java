package com.soundbrenner.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FullDiscoveryCardActivity extends AppCompatActivity {
    private Bundle data;
    private Bitmap bmp;
    private String title;
    private String text;
    private TextView textView;
    private TextView titleView;
    private ImageButton exitView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_discovery_card);
        Intent intent   = getIntent();
        data            = intent.getBundleExtra("bundle");

        text            = data.getString("text");
        textView        = (TextView) findViewById(R.id.discovery_text);
        textView.setText(text);

        title           = data.getString("title");
        titleView       = (TextView) findViewById(R.id.discovery_title);
        titleView.setText(title);

        byte[] bytes    = data.getByteArray("bitmap");
        bmp             = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView       = (ImageView) findViewById(R.id.discovery_image);
        imageView.setImageBitmap(bmp);

    }
}
