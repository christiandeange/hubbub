package com.deange.githubstatus.dagger.module;

import com.deange.githubstatus.model.CurrentStatus;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.net.GithubStatusApi;
import com.nytimes.android.external.store3.base.impl.BarCode;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class StoreModule {

    @Provides
    @Singleton
    public Store<CurrentStatus, BarCode> providesStatusStore(final GithubStatusApi api) {
        return StoreBuilder.<CurrentStatus>barcode()
                .fetcher(barcode -> api.status())
                .open();
    }

    @Provides
    @Singleton
    public Store<Message, BarCode> providesMessageStore(final GithubStatusApi api) {
        return StoreBuilder.<Message>barcode()
                .fetcher(barcode -> api.messages())
                .open();
    }

}
