package com.example.himsrana.camu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        ImageView imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        Uri imageUri = intent.getData();
        Picasso.get().load(imageUri).into(imageView);

    }
}
