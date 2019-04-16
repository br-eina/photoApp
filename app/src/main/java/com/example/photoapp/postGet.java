package com.example.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.photoapp.Helper.BitmapHelper;

public class postGet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_get);

        ImageView imageView = findViewById(R.id.imageView2);

        Intent intent = getIntent();
        //Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        //imageView.setImageBitmap(bitmap);

        imageView.setImageBitmap(BitmapHelper.getInstance().getBitmap());

    }
}
