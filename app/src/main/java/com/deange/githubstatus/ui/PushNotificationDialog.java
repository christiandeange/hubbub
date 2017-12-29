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

  private final Preference<String> topicPreference;
  private final List<PushNotificationRow> toggles = new ArrayList<>();

  @Inject
  public PushNotificationDialog(TopicController controller) {
    topicPreference = controller.getPreference();
  }

  public void show(Context context) {
    AlertDialog dialog = new AlertDialog.Builder(context)
        .setView(R.layout.content_push_notification_settings)
        .show();

    // Unregister ourselves as listeners
    for (PushNotificationRow toggle : toggles) {
      toggle.setOnClickListener(null);
    }
    toggles.clear();

    ViewGroup root = dialog.findViewById(R.id.push_notification_toggles_parent);
    for (View view : ViewGroupIterable.childrenOf(root)) {
      if (view instanceof PushNotificationRow) {
        PushNotificationRow row = (PushNotificationRow) view;

        row.setOnClickListener(this::onRowToggled);
        if (row.getTopic().equals(topicPreference.get())) {
          row.setChecked(true);
        }

        toggles.add(row);
      }
    }
  }

  private void onRowToggled(View view) {
    PushNotificationRow rowClicked = (PushNotificationRow) view;
    topicPreference.set(rowClicked.getTopic());

    for (PushNotificationRow row : toggles) {
      row.setChecked(row == rowClicked);
    }
  }

}
