package ru.fuldaros.imgf.core;

import android.app.Application;
import android.content.Context;

import java.io.File;

public class ImageFactory extends Application {
    public static Application APP;
    public static File DATA_PATH;
    public static File KERNEL_BACKUPS;
    public static File KERNEL_UNPACKED;
    public static File KERNEL_REPACKED;
    public static File IMAGE_CONVERTED;

    @Override
    public void onCreate() {
        // TODO: Implement this method
        super.onCreate();
        APP = this;
    }

    public static Context getApp() {
        // TODO: Implement this method
        return APP;
    }
}
