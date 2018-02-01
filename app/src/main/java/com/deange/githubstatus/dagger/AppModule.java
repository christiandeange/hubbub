package com.deange.githubstatus.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.deange.githubstatus.BuildConfig;
import com.deange.githubstatus.converter.LocalDateTimeConverterFactory;
import com.deange.githubstatus.converter.ModelGsonFactory;
import com.deange.githubstatus.net.GithubStatusApi;
import com.deange.githubstatus.net.ServiceCreator;
import com.deange.githubstatus.net.mock.MockGithubStatusApi;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class AppModule {

  private final Application application;

  public static AppModule create(Application application) {
    return new AppModule(application);
  }

  private AppModule(Application application) {
    this.application = application;
  }

  @Provides
  public Context providesApplicationContext() {
    return application.getApplicationContext();
  }

  @Provides
  public Resources res(Context context) {
    return context.getResources();
  }

  @Provides
  public static Gson providesGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(ModelGsonFactory.create())
        .registerTypeAdapterFactory(new LocalDateTimeConverterFactory())
        .create();
  }

  @Provides
  public static OkHttpClient providesOkHttpClient() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(BuildConfig.DEBUG
                                    ? HttpLoggingInterceptor.Level.BODY
                                    : HttpLoggingInterceptor.Level.NONE);

    return new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();
  }

  @Provides
  public static Retrofit providesRetrofit(Gson gson, OkHttpClient client) {
    return new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .baseUrl("https://status.github.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  @Provides
  public static GithubStatusApi providesGithubStatusApi(
      Retrofit retrofit,
      ServiceCreator serviceCreator) {
    return serviceCreator.createService(
        retrofit.create(GithubStatusApi.class),
        new MockGithubStatusApi(),
        GithubStatusApi.class);
  }

  @Provides
  public static SharedPreferences providesSharedPreferences(Context context) {
    return context.getSharedPreferences("default", Context.MODE_PRIVATE);
  }

  @Provides
  public static RxSharedPreferences providesRxSharedPreferences(SharedPreferences sharedPrefs) {
    return RxSharedPreferences.create(sharedPrefs);
  }

  @Provides
  @MockMode
  public static Preference<Boolean> providesMockModePreference(RxSharedPreferences preferences) {
    return preferences.getBoolean("mock_mode");
  }

}
