package com.example.myapplication.helperClass;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

public class Helper extends LuminanceSource {
    private final byte[] luminances;

    public Helper(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());

        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        luminances = new byte[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int r = (pixel >> 16) & 0xff;
            int g = (pixel >> 8) & 0xff;
            int b = pixel & 0xff;
            luminances[i] = (byte) ((r + g + b) / 3);
        }
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        System.arraycopy(luminances, y * getWidth(), row, 0, getWidth());
        return row;
    }

    @Override
    public byte[] getMatrix() {
        return luminances;
    }
}