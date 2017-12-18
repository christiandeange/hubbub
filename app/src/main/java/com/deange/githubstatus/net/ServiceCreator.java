package com.deange.githubstatus.net;

import com.deange.githubstatus.dagger.MockMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Provider;

public class ServiceCreator {

  private final Provider<Boolean> mMockMode;

  @Inject
  public ServiceCreator(@MockMode final Provider<Boolean> mockMode) {
    mMockMode = mockMode;
  }

  public <T> T createService(T realService, T mockService, final Class<T> clazz) {
    return clazz.cast(Proxy.newProxyInstance(
        clazz.getClassLoader(),
        new Class[]{clazz},
        new ServiceHandler<>(mMockMode, realService, mockService)));
  }

  private static final class ServiceHandler<T>
      implements
      InvocationHandler {

    final Provider<Boolean> mMockMode;
    final T mRealService;
    final T mMockService;

    public ServiceHandler(
        final Provider<Boolean> mockMode,
        final T realService,
        final T mockService) {
      mMockMode = mockMode;
      mRealService = realService;
      mMockService = mockService;
    }

    @Override
    public Object invoke(
        final Object proxy,
        final Method method,
        final Object[] args) throws Throwable {
      return method.invoke(service(), args);
    }

    private T service() {
      return mMockMode.get() ? mMockService : mRealService;
    }
  }
}
