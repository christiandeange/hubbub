package com.deange.githubstatus.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import com.deange.githubstatus.R;

import java.util.ArrayList;
import java.util.List;

public class PushNotificationDialog {

    private final Context mContext;
    private ViewGroup mRoot;
    private final List<PushNotificationRow> mToggles = new ArrayList<>();

    public PushNotificationDialog(@NonNull final Context context) {
        mContext = context;
    }

    public void show() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(R.layout.content_push_notification_settings)
                .show();

        mToggles.clear();
        mRoot = (ViewGroup) dialog.findViewById(R.id.push_notification_toggles_parent);

        if (mRoot != null) {
            for (int i = 0; i < mRoot.getChildCount(); ++i) {
                final View child = mRoot.getChildAt(i);
                if (child instanceof PushNotificationRow) {
                    mToggles.add((PushNotificationRow) child);
                }
            }
        }

        for (final PushNotificationRow row : mToggles) {
            row.setOnClickListener(this::onRowToggled);
        }
    }

    private void onRowToggled(final View view) {
        for (final PushNotificationRow row : mToggles) {
            row.setChecked(row == view);
        }
    }

}
