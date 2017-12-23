package com.deange.githubstatus.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;

import com.deange.githubstatus.R;
import com.deange.githubstatus.dagger.MockMode;
import com.deange.githubstatus.util.Unit;
import com.f2prateek.rx.preferences2.Preference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class DevSettingsDialog {

  @Inject @MockMode Preference<Boolean> mMockModePreference;

  @BindView(R.id.dev_mock_mode) SwitchCompat mMockSwitch;

  private final Subject<Unit> mUpdateObservable = PublishSubject.create();

  @Inject
  public DevSettingsDialog() {
  }

  public void show(@NonNull final Context context) {
    final AlertDialog dialog = new AlertDialog.Builder(context)
        .setView(R.layout.dev_settings)
        .setOnDismissListener(d -> onDismissed())
        .setPositiveButton(android.R.string.ok, null)
        .show();

    ButterKnife.bind(this, dialog);

    mMockSwitch.setChecked(mMockModePreference.get());
  }

  private void onDismissed() {
    mUpdateObservable.onNext(Unit.INSTANCE);
  }

  @OnCheckedChanged(R.id.dev_mock_mode)
  void onMockModeToggled(final boolean isChecked) {
    mMockModePreference.set(isChecked);
  }

  public Observable<Unit> onDevSettingsChanged() {
    return mUpdateObservable;
  }
}
