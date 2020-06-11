package com.example.moovittext.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moovittext.R;

public class PhotoActivity extends AppCompatActivity {
    private final String EXTRA_DATA_URL = "image_url" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        String url = getIntent().getStringExtra(EXTRA_DATA_URL);

        Glide.with(this).load(url)
                .into((ImageView)findViewById(R.id.second_activity_image));
    }
}
