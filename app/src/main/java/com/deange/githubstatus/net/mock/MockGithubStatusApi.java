package com.deange.githubstatus.net.mock;

import com.deange.githubstatus.model.CurrentStatus;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.net.GithubStatusApi;

import java.util.List;

import io.reactivex.Single;

import static java.util.Arrays.asList;
import static org.joda.time.LocalDateTime.now;

public class MockGithubStatusApi
    implements
    GithubStatusApi {

  @Override
  public Single<CurrentStatus> status() {
    return Single.just(CurrentStatus.create(State.GOOD, now()));
  }

  @Override
  public Single<Message> lastMessage() {
    return Single.just(
        Message.create(State.GOOD, "Everything operating normally.", now()));
  }

  @Override
  public Single<List<Message>> messages() {
    return Single.just(asList(
        Message.create(State.GOOD, "Everything operating normally.", now()),
        Message.create(State.MINOR, "Something minor happened.", now().minusHours(1)),
        Message.create(State.MAJOR, "Major malfunction.", now().minusDays(1))
    ));
  }

}
