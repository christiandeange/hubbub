package com.deange.githubstatus.net;

import com.deange.githubstatus.model.CurrentStatus;
import com.deange.githubstatus.model.Message;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface GithubStatusApi {

    @GET("/api/status.json")
    Single<CurrentStatus> status();

    @GET("/api/last-message.json")
    Single<Message> lastMessage();

    @GET("/api/messages.json")
    Single<List<Message>> messages();

}
