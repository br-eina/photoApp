package com.example.photoapp.Helper;

import android.graphics.Bitmap;

public class BitmapHelper {

    private Bitmap bitmap = null;
    private static final BitmapHelper instance = new BitmapHelper();


    public static BitmapHelper getInstance() {
        return instance;
    }

    public BitmapHelper() {

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
