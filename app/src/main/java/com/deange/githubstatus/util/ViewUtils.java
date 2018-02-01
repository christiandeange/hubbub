package com.deange.githubstatus.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import static android.view.LayoutInflater.from;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewUtils {

  private ViewUtils() {
    throw new AssertionError();
  }

  public static void setVisible(View view, boolean visible) {
    view.setVisibility(visible ? VISIBLE : GONE);
  }

  public static View inflate(ViewGroup parent, @LayoutRes int layoutId) {
    return from(parent.getContext()).inflate(layoutId, parent, false);
  }

  public static View inflateAndAttach(ViewGroup parent, @LayoutRes int layoutId) {
    return from(parent.getContext()).inflate(layoutId, parent, true);
  }

  public static Activity unwrap(Context context) {
    while (true) {
      if (context instanceof Activity) {
        return (Activity) context;
      } else if (context instanceof ContextWrapper) {
        context = ((ContextWrapper) context).getBaseContext();
      } else {
        String className = (context == null) ? "null" : context.getClass().getName();
        throw new IllegalArgumentException(
            "Provided Context of type " + className + " does not wrap Activity");
      }
    }
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
