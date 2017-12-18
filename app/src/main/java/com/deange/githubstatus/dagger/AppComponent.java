package com.deange.githubstatus.dagger;

import com.deange.githubstatus.dagger.module.AppModule;
import com.deange.githubstatus.dagger.module.GsonModule;
import com.deange.githubstatus.dagger.module.OkHttpModule;
import com.deange.githubstatus.dagger.module.RetrofitModule;
import com.deange.githubstatus.dagger.module.SharedPrefsModule;
import com.deange.githubstatus.dagger.module.StoreModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {
    AppModule.class,
    GsonModule.class,
    StoreModule.class,
    OkHttpModule.class,
    RetrofitModule.class,
    SharedPrefsModule.class,
})
public interface AppComponent
    extends BaseAppComponent {

  @Component.Builder
  interface Builder {
    Builder appModule(final AppModule appModule);

    AppComponent build();
  }

}
