package com.deange.githubstatus.ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import com.deange.githubstatus.R;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.util.ViewGroupIterable;
import com.f2prateek.rx.preferences2.Preference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PushNotificationDialog {

    private final Preference<String> mTopicPreference;
    private final List<PushNotificationRow> mToggles = new ArrayList<>();

    @Inject
    public PushNotificationDialog(TopicController controller) {
        mTopicPreference = controller.getPreference();
    }

    public void show(final Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(R.layout.content_push_notification_settings)
                .show();

        // Unregister ourselves as listeners
        for (final PushNotificationRow toggle : mToggles) {
            toggle.setOnClickListener(null);
        }
        mToggles.clear();

        final ViewGroup root = dialog.findViewById(R.id.push_notification_toggles_parent);
        for (final View view : ViewGroupIterable.on(root)) {
            if (view instanceof PushNotificationRow) {
                final PushNotificationRow row = (PushNotificationRow) view;

                row.setOnClickListener(this::onRowToggled);
                if (row.getTopic().equals(mTopicPreference.get())) {
                    row.setChecked(true);
                }

                mToggles.add(row);
            }
        }
    }

    private void onRowToggled(final View view) {
        final PushNotificationRow rowClicked = (PushNotificationRow) view;
        mTopicPreference.set(rowClicked.getTopic());

        for (final PushNotificationRow row : mToggles) {
            row.setChecked(row == rowClicked);
        }
    }

}
