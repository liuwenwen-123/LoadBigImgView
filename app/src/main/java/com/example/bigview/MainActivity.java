package com.example.bigview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BigView viewById = findViewById(R.id.img);
        AssetManager assetManager= getAssets();

       InputStream open = null;
        try {
            open = assetManager.open("bbb.jpg");
            viewById.setImg(open);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Rect rect=new Rect();
        Bitmap bitmap = BitmapFactory.decodeStream(open);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int allocationByteCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            allocationByteCount = bitmap.getAllocationByteCount();
            Log.e("eee",width+"---  "+height+"----"+allocationByteCount);
        }

        options.inJustDecodeBounds=false;*/

//        viewById.setImageBitmap(bitmap);

    }
}