package com.deange.githubstatus.dagger;

import dagger.Component;


@AppScope
@Component(modules = AppModule.class)
public interface AppComponent extends AppComponentExports {

  @Component.Builder
  interface Builder {
    Builder appModule(AppModule appModule);
    AppComponent build();
  }

}
