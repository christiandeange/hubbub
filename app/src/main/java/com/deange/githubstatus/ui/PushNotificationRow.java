package com.deange.githubstatus.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.deange.githubstatus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static com.deange.githubstatus.util.ViewUtils.getDrawable;
import static com.deange.githubstatus.util.ViewUtils.inflateAndAttach;


public class PushNotificationRow
    extends LinearLayout
    implements
    Checkable {

  private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

  @BindView(R.id.list_item_setting_dot_outline) View dotOutline;
  @BindView(R.id.list_item_setting_dot) View dot;
  @BindView(R.id.list_item_setting_title) CheckedTextView title;
  @BindView(R.id.list_item_setting_description) CheckedTextView description;

  private final ColorDrawable colorDrawable;
  private boolean isChecked;
  private String topic;

  public PushNotificationRow(@NonNull Context context) {
    this(context, null);
  }

  public PushNotificationRow(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PushNotificationRow(
      @NonNull Context context,
      @Nullable AttributeSet attrs,
      int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public PushNotificationRow(
      @NonNull Context context,
      @Nullable AttributeSet attrs,
      int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    setOrientation(HORIZONTAL);
    setGravity(Gravity.CENTER_VERTICAL | GravityCompat.START);

    inflateAndAttach(this, R.layout.list_item_notification_setting);
    ButterKnife.bind(this);

    // Set up the background drawable selector
    // Has to be done programmatically so we can mutate the color in setColor()
    setClickable(true);
    colorDrawable = new ColorDrawable(Color.TRANSPARENT);
    StateListDrawable states = new StateListDrawable();
    states.addState(new int[]{android.R.attr.state_checked}, colorDrawable);
    states.addState(new int[]{}, getDrawable(R.attr.selectableItemBackground, context));
    setBackground(states);

    TypedArray a = context.obtainStyledAttributes(
        attrs, R.styleable.PushNotificationRow, defStyleAttr, defStyleRes);

    if (a != null) {
      setColor(a.getColor(R.styleable.PushNotificationRow_notificationColor, Color.WHITE));
      setDotSize(a.getDimensionPixelSize(
          R.styleable.PushNotificationRow_notificationDotSize,
          (int) (getResources().getDisplayMetrics().densityDpi * 16f)));

      setTitle(a.getString(R.styleable.PushNotificationRow_notificationTitle));
      setDescription(a.getString(R.styleable.PushNotificationRow_notificationDescription));
      setTopic(a.getString(R.styleable.PushNotificationRow_notificationTopic));
      a.recycle();
    }
  }

  public void setColor(int color) {
    dot.getBackground().setColorFilter(color, SRC_ATOP);
    colorDrawable.setColor(color);
    invalidate();
  }

  public void setDotSize(int dotSizePx) {
    ViewGroup.LayoutParams lp = dot.getLayoutParams();
    lp.width = dotSizePx;
    lp.height = dotSizePx;

    requestLayout();
    invalidate();
  }

  public void setTitle(CharSequence title) {
    this.title.setText(title);
  }

  public void setDescription(CharSequence description) {
    this.description.setText(description);
  }

  private void setTopic(String topic) {
    if (topic == null) topic = "";
    this.topic = topic;
  }

  @NonNull
  public String getTopic() {
    return topic;
  }

  @Override
  public boolean performClick() {
    toggle();
    return super.performClick();
  }

  @Override
  public int[] onCreateDrawableState(int extraSpace) {
    int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE_SET);
    }
    return drawableState;
  }

  @Override
  public void setChecked(boolean isChecked) {
    this.isChecked = isChecked;
    refreshDrawableState();

    title.setChecked(isChecked);
    description.setChecked(isChecked);
  }

  @Override
  public boolean isChecked() {
    return isChecked;
  }

  @Override
  public void toggle() {
    setChecked(!isChecked);
  }
}
