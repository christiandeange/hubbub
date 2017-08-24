package com.deange.githubstatus.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.R;
import com.deange.githubstatus.model.CurrentStatus;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.Response;
import com.nytimes.android.external.store3.base.impl.BarCode;
import com.nytimes.android.external.store3.base.impl.Store;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity
        extends BaseActivity {

    private static final BarCode BARCODE = BarCode.empty();

    @Inject Store<CurrentStatus, BarCode> mStatusStore;
    @Inject Store<List<Message>, BarCode> mMessageStore;

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication.get(this).getAppComponent().inject(this);

        Single.zip(
                mStatusStore.get(BARCODE),
                mMessageStore.get(BARCODE),
                Response::create)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::onCurrentStatusReceived, this::onCurrentStatusFailed);

        setSupportActionBar(mToolbar);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Snackbar.make(mFab, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    void onCurrentStatusReceived(final Response response) {
        mToolbarLayout.setTitle(getString(response.status().state().getStringResId()));
    }

    void onCurrentStatusFailed(final Throwable error) {
        error.printStackTrace();
    }
}
