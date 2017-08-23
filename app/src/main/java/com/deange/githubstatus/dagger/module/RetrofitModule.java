package com.deange.githubstatus.dagger.module;

import com.deange.githubstatus.net.GithubStatusApi;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class RetrofitModule {

    @Provides
    public Retrofit providesRetrofit(
            final Gson gson,
            final OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl("https://status.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public GithubStatusApi providesGithubStatusApi(final Retrofit retrofit) {
        return retrofit.create(GithubStatusApi.class);
    }

}
