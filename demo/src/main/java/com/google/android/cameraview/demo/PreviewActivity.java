/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.cameraview.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;

/**
 * <p>***********************************************************************
 * <p> Author: Michael
 * <p> CreateData: 2017-03-29 17:44
 * <p> Version: xx
 * <p> Description: xx
 * <p>
 * <p>***********************************************************************
 */

public class PreviewActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        image = (ImageView) findViewById(R.id.image);
        String imageUrl = getIntent().getStringExtra("image_url");
        Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
        boolean mirror_X = getIntent().getBooleanExtra("mirror_X", false);
        boolean mirror_Y = getIntent().getBooleanExtra("mirror_Y", false);
        bmp = convertBmp(bmp, mirror_X, mirror_Y);
        image.setImageBitmap(bmp);
    }

    /**
     * 查询图片旋转角度
     */
    public static int getExifOrientation(String filepath) {// YOUR MEDIA PATH AS STRING
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filepath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 做镜像翻转
     */
    public static Bitmap convertBmp(Bitmap bmp, boolean mirror_X, boolean mirror_Y) {
        Bitmap bitmap = bmp;
        if (mirror_X) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1); // 镜像水平翻转
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            bmp.recycle();
        }

        if (mirror_Y) {
            Matrix matrix = new Matrix();
            matrix.postScale(1, -1); // 镜像垂直翻转
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            bmp.recycle();
        }
        return bitmap;
    }
}
