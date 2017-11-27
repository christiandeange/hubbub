package com.deange.githubstatus.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;

import com.deange.githubstatus.R;
import com.deange.githubstatus.dagger.MockMode;
import com.f2prateek.rx.preferences2.Preference;
import com.jakewharton.processphoenix.ProcessPhoenix;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static com.deange.githubstatus.MainApplication.component;

public class DevSettingsDialog {

    @Inject @MockMode Preference<Boolean> mMockModePreference;

    @BindView(R.id.dev_mock_mode) SwitchCompat mMockSwitch;

    private final Context mContext;
    private boolean mWasMockMode;

    public DevSettingsDialog(@NonNull final Context context) {
        mContext = context;
        component(context).inject(this);
    }

    public void show() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(R.layout.dev_settings)
                .setOnDismissListener(d -> onDismissed())
                .show();

        ButterKnife.bind(this, dialog);

        mWasMockMode = mMockModePreference.get();
        mMockSwitch.setChecked(mWasMockMode);
    }

    private void onDismissed() {
        if (mWasMockMode != mMockModePreference.get()) {
            ProcessPhoenix.triggerRebirth(mContext);
        }
    }

    @OnCheckedChanged(R.id.dev_mock_mode)
    void onMockModeToggled(final boolean isChecked) {
        mMockModePreference.set(isChecked);
    }

}
