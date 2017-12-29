package com.deange.githubstatus.controller;

import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.net.GithubStatusApi;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class GithubController {

  private final GithubStatusApi api;

  @Inject
  GithubController(final GithubStatusApi api) {
    this.api = api;
  }

  public Single<Response> getStatus() {
    return Single.zip(api.status(), api.messages(), Response::create)
                 .onErrorReturn(Response::error)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread());
  }

}
