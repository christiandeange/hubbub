package com.deange.githubstatus.net;

import com.deange.githubstatus.dagger.MockMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Provider;

public class ServiceCreator {

  private final Provider<Boolean> mockProvider;

  @Inject
  public ServiceCreator(@MockMode final Provider<Boolean> mockProvider) {
    this.mockProvider = mockProvider;
  }

  public <T> T createService(T realService, T mockService, Class<T> clazz) {
    return clazz.cast(Proxy.newProxyInstance(
        clazz.getClassLoader(),
        new Class[]{clazz},
        new ServiceHandler<>(mockProvider, realService, mockService)));
  }

  private static final class ServiceHandler<T>
      implements
      InvocationHandler {

    final Provider<Boolean> mockMode;
    final T realService;
    final T mockService;

    public ServiceHandler(Provider<Boolean> mockMode, T realService, T mockService) {
      this.mockMode = mockMode;
      this.realService = realService;
      this.mockService = mockService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return method.invoke(service(), args);
    }

    private T service() {
      return mockMode.get() ? mockService : realService;
    }
  }
}
