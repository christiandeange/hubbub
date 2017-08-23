package com.deange.githubstatus.net;

import com.deange.githubstatus.model.CurrentStatus;
import com.deange.githubstatus.model.Message;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface GithubStatusApi {

    @GET("/status.json")
    Single<CurrentStatus> status();

    @GET("/last-message.json")
    Single<Message> lastMessage();

    @GET("/messages.json")
    Single<Message> messages();

}
