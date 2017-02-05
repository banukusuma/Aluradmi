package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by banu on 03/02/17.
 */

public class DenahActivity extends AppCompatActivity {
    private ImageView denah;
    private ProgressBar progressBar;
    private TextView textView_Denah;
    private PhotoViewAttacher photoViewAttacher;
    private static final String EXTRA_LINK = "com.spp.banu.aluradmi.link.denah";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        denah = (ImageView) findViewById(R.id.fullscreen_imageview);
        progressBar = (ProgressBar) findViewById(R.id.progressBarDenah);
        textView_Denah = (TextView) findViewById(R.id.download_denah_textview);
        progressBar.setVisibility(View.VISIBLE);
        textView_Denah.setVisibility(View.VISIBLE);
        String link_Denah = getIntent().getStringExtra(EXTRA_LINK);
        Callback imageCallback = new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                textView_Denah.setVisibility(View.GONE);
                if (photoViewAttacher != null){
                    photoViewAttacher.update();
                }else {
                    photoViewAttacher = new PhotoViewAttacher(denah);
                }
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.INVISIBLE);
                textView_Denah.setText("Gagal Mengunduh Denah");
            }
        };
        Picasso.with(this).load(link_Denah).into(denah, imageCallback);

    }

    public static Intent newIntent(Context packageContext, String link){
        Intent intent = new Intent(packageContext, DenahActivity.class);
        intent.putExtra(EXTRA_LINK, link);
        return intent;
    }

}
