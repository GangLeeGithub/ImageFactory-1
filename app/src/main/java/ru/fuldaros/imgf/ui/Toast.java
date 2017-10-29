package ru.fuldaros.imgf.ui;

import ru.fuldaros.imgf.core.ImageFactory;

public class Toast {
    public static void makeLongText(CharSequence text) {
        makeText(text, android.widget.Toast.LENGTH_LONG);
    }

    public static void makeShortText(CharSequence text) {
        makeText(text, android.widget.Toast.LENGTH_SHORT);
    }

    public static void makeText(CharSequence text, int duration) {
        android.widget.Toast.makeText(ImageFactory.APP, text, duration).show();
    }
}
