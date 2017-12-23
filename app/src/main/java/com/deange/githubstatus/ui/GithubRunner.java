package com.deange.githubstatus.ui;

import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.net.GithubStatusApi;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class GithubRunner {

  private final GithubStatusApi mApi;

  @Inject
  GithubRunner(final GithubStatusApi api) {
    mApi = api;
  }

  public Single<Response> getStatus() {
    return Single.zip(mApi.status(), mApi.messages(), Response::create)
                 .onErrorReturn(Response::error)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread());
  }

}
