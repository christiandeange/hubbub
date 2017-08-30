package com.deange.githubstatus.ui;

import android.content.Context;
import android.content.res.ColorStateList;
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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PushNotificationRow
        extends LinearLayout
        implements
        Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    @BindView(R.id.list_item_setting_dot_outline) View mDotOutline;
    @BindView(R.id.list_item_setting_dot) View mDot;
    @BindView(R.id.list_item_setting_title) CheckedTextView mTitle;
    @BindView(R.id.list_item_setting_description) CheckedTextView mDescription;

    @BindDimen(R.dimen.push_notification_dot_margin) float mDotMargin;

    private final ColorDrawable mColorDrawable;
    private boolean mIsChecked;

    public PushNotificationRow(@NonNull final Context context) {
        this(context, null);
    }

    public PushNotificationRow(
            @NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PushNotificationRow(
            @NonNull final Context context,
            @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PushNotificationRow(
            @NonNull final Context context,
            @Nullable final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL | GravityCompat.START);

        inflate(getContext(), R.layout.list_item_notification_setting, this);
        ButterKnife.bind(this);

        // Set up the background drawable selector
        // Has to be done programmatically so we can mutate the color in setColor()
        setClickable(true);
        mColorDrawable = new ColorDrawable(Color.TRANSPARENT);
        final StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_checked}, mColorDrawable);
        states.addState(new int[]{}, ViewUtils.getDrawable(R.attr.selectableItemBackground, context));
        setBackground(states);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.PushNotificationRow, defStyleAttr, defStyleRes);

        if (a != null) {
            setColor(a.getColor(R.styleable.PushNotificationRow_notificationColor, Color.WHITE));
            setDotSize(a.getDimensionPixelSize(
                    R.styleable.PushNotificationRow_notificationDotSize,
                    (int) (getResources().getDisplayMetrics().densityDpi * 16f)));

            setTitle(a.getString(R.styleable.PushNotificationRow_notificationTitle));
            setDescription(a.getString(R.styleable.PushNotificationRow_notificationDescription));
            a.recycle();
        }
    }

    public void setColor(final int color) {
        mDot.setBackgroundTintList(ColorStateList.valueOf(color));
        mColorDrawable.setColor(color);
        invalidate();
    }

    public void setDotSize(final int dotSizePx) {
        ViewGroup.LayoutParams lp = mDot.getLayoutParams();
        lp.width = dotSizePx;
        lp.height = dotSizePx;

        ViewGroup.LayoutParams lp2 = mDotOutline.getLayoutParams();
        lp2.width = (int) (dotSizePx + mDotMargin);
        lp2.height = (int) (dotSizePx + mDotMargin);

        requestLayout();
        invalidate();
    }

    public void setTitle(final CharSequence title) {
        mTitle.setText(title);
    }

    public void setDescription(final CharSequence description) {
        mDescription.setText(description);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(final boolean isChecked) {
        mIsChecked = isChecked;
        refreshDrawableState();

        mTitle.setChecked(isChecked);
        mDescription.setChecked(isChecked);
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }
}
