package com.deange.githubstatus.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.util.TypedValue;
import android.view.View;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewUtils {

  private ViewUtils() {
    throw new AssertionError();
  }

  public static void setVisibility(View view, boolean visible) {
    view.setVisibility(visible ? VISIBLE : GONE);
  }

  public static int getAttrIdValue(@AttrRes int attr, Context context) {
    TypedValue value = new TypedValue();
    context.getTheme().resolveAttribute(attr, value, true);
    return value.resourceId;
  }

  public static Drawable getDrawable(@AttrRes int attr, Context context) {
    return context.getDrawable(getAttrIdValue(attr, context));
  }

  public static String getString(@AttrRes int attr, Context context) {
    return context.getString(getAttrIdValue(attr, context));
  }

  public static int getInteger(@AttrRes int attr, Context context) {
    return context.getResources().getInteger(getAttrIdValue(attr, context));
  }

  public static boolean getBoolean(@AttrRes int attr, Context context) {
    return context.getResources().getBoolean(getAttrIdValue(attr, context));
  }

  public static int getColor(@AttrRes int attr, Context context) {
    return context.getResources().getColor(getAttrIdValue(attr, context));
  }

}
