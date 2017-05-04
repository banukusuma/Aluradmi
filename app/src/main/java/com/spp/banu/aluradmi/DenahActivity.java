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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by banu on 03/02/17.
 */

public class DenahActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView denah;
    private ProgressBar progressBar;
    private TextView textView_Denah;
    private Button btn_coba_lagi;
    private PhotoViewAttacher photoViewAttacher;
    private static final String EXTRA_LINK = "com.spp.banu.aluradmi.link.denah";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        denah = (ImageView) findViewById(R.id.fullscreen_imageview);
        progressBar = (ProgressBar) findViewById(R.id.progressBarDenah);
        textView_Denah = (TextView) findViewById(R.id.download_denah_textview);
        btn_coba_lagi = (Button) findViewById(R.id.button_downloan_denah);
        btn_coba_lagi.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textView_Denah.setVisibility(View.VISIBLE);
        downloadDenah();
        /*
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
                btn_coba_lagi.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                textView_Denah.setText("Gagal Mengunduh Denah");
            }
        };
        Picasso.with(this).load(link_Denah).into(denah, imageCallback);
        */
        btn_coba_lagi.setOnClickListener(this);
    }

    private void downloadDenah(){
        String link_Denah = getIntent().getStringExtra(EXTRA_LINK);
        Callback imageCallback = new Callback() {
            @Override
            public void onSuccess() {
                btn_coba_lagi.setVisibility(View.GONE);
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
                btn_coba_lagi.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        textView_Denah.setVisibility(View.VISIBLE);
        textView_Denah.setText(R.string.unduh_denah);
        btn_coba_lagi.setVisibility(View.INVISIBLE);
        downloadDenah();
    }
}
