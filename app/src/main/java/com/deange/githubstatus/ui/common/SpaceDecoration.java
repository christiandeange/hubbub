package com.deange.githubstatus.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class SpaceDecoration
    extends RecyclerView.ItemDecoration {

  private static final String TAG = "SpaceDecoration";
  private static final int[] ATTRS = new int[]{android.R.attr.dividerHeight};

  private int dividerSize;

  public SpaceDecoration(Context context) {
    TypedArray a = context.obtainStyledAttributes(ATTRS);

    if (a.hasValue(0)) {
      dividerSize = a.getDimensionPixelSize(0, 0);

    } else {
      Log.w(TAG, "@android:attr/dividerHeight was not set in the theme used for this "
          + "SpaceDecoration. Please set that attribute or call setDividerSize()");
    }

    a.recycle();
  }

  @Override
  public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    // No-op
  }

  @Override
  public void getItemOffsets(
      Rect outRect,
      View view,
      RecyclerView parent,
      RecyclerView.State state) {

    int orientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
    int position = parent.getChildAdapterPosition(view);
    int offset = (position == 0) ? dividerSize : 0;

    if (dividerSize == 0) {
      outRect.set(0, 0, 0, 0);

    } else {
      if (orientation == VERTICAL) {
        outRect.set(0, offset, 0, dividerSize);

      } else {
        outRect.set(offset, 0, dividerSize, 0);
      }
    }
  }

}
