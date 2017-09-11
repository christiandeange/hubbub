package com.deange.githubstatus.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deange.githubstatus.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.HasTypeface;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public final class FontUtils {

    public static final int THIN = 0;
    public static final int LIGHT = 1;
    public static final int REGULAR = 2;
    public static final int BOLD = 3;
    public static final int THIN_ITALIC = 4;
    public static final int LIGHT_ITALIC = 5;
    public static final int REGULAR_ITALIC = 6;
    public static final int BOLD_ITALIC = 7;

    public static final int DEFAULT = REGULAR;

    private static AssetManager sAssets;
    private static final SparseArray<String> FONTS = new SparseArray<>();

    private FontUtils() {
        throw new AssertionError();
    }

    public static void init(final Context context) {
        sAssets = context.getResources().getAssets();

        FONTS.put(THIN, context.getString(R.string.font_thin));
        FONTS.put(LIGHT, context.getString(R.string.font_light));
        FONTS.put(REGULAR, context.getString(R.string.font_regular));
        FONTS.put(BOLD, context.getString(R.string.font_bold));
        FONTS.put(THIN_ITALIC, context.getString(R.string.font_thin_italic));
        FONTS.put(LIGHT_ITALIC, context.getString(R.string.font_light_italic));
        FONTS.put(REGULAR_ITALIC, context.getString(R.string.font_regular_italic));
        FONTS.put(BOLD_ITALIC, context.getString(R.string.font_bold_italic));

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath(FONTS.get(DEFAULT))
                        .setFontAttrId(R.attr.fontName)
                        .build());
    }

    public static void apply(final View view, final @Font int type) {
        apply(view, getFont(type));
    }

    public static void apply(
            final View view,
            final Typeface typeface) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);

        } else if (view instanceof CollapsingToolbarLayout) {
            final CollapsingToolbarLayout layout = (CollapsingToolbarLayout) view;
            layout.setExpandedTitleTypeface(typeface);
            layout.setCollapsedTitleTypeface(typeface);

        } else if (view instanceof HasTypeface) {
            ((HasTypeface) view).setTypeface(typeface);
        }

        if (view instanceof ViewGroup) {
            for (final View child : ViewGroupIterable.on((ViewGroup) view)) {
                apply(child, typeface);
            }
        }
    }

    public static Typeface getFont(final @Font int font) {
        final String fontPath = FONTS.get(font);
        return TypefaceUtils.load(sAssets, fontPath);
    }

    @IntDef({
            THIN,
            LIGHT,
            REGULAR,
            BOLD,
            THIN_ITALIC,
            LIGHT_ITALIC,
            REGULAR_ITALIC,
            BOLD_ITALIC,
    })
    public @interface Font {
    }

}
