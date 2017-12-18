package com.deange.githubstatus.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class SpaceDecoration
    extends RecyclerView.ItemDecoration {

  public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
  public static final int VERTICAL = LinearLayout.VERTICAL;

  private static final String TAG = "SpaceDecoration";
  private static final int[] ATTRS = new int[]{android.R.attr.dividerHeight};

  private int mDividerSize;
  private int mOrientation;

  public SpaceDecoration(final Context context, @Orientation final int orientation) {
    final TypedArray a = context.obtainStyledAttributes(ATTRS);

    if (a.hasValue(0)) {
      mDividerSize = a.getDimensionPixelSize(0, 0);

    } else {
      Log.w(TAG, "@android:attr/dividerHeight was not set in the theme used for this "
          + "SpaceDecoration. Please set that attribute or call setDividerSize()");
    }

    a.recycle();
    setOrientation(orientation);
  }

  public void setOrientation(@Orientation final int orientation) {
    if (orientation != HORIZONTAL && orientation != VERTICAL) {
      throw new IllegalArgumentException(
          "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
    }
    mOrientation = orientation;
  }

  public void setDividerSize(final int dividerSize) {
    mDividerSize = dividerSize;
  }

  @Override
  public void onDraw(
      final Canvas canvas,
      final RecyclerView parent,
      final RecyclerView.State state) {
  }

  @Override
  public void getItemOffsets(
      final Rect outRect,
      final View view,
      final RecyclerView parent,
      final RecyclerView.State state) {

    final int position = parent.getChildAdapterPosition(view);
    final int firstPositionSize = (position == 0) ? mDividerSize : 0;

    if (mDividerSize == 0) {
      outRect.set(0, 0, 0, 0);

    } else {
      if (mOrientation == VERTICAL) {
        outRect.set(0, firstPositionSize, 0, mDividerSize);

      } else {
        outRect.set(firstPositionSize, 0, mDividerSize, 0);
      }
    }
  }

  @IntDef({VERTICAL, HORIZONTAL})
  public @interface Orientation {
  }
}
