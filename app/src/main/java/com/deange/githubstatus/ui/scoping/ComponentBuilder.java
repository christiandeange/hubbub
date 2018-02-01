package com.deange.githubstatus.ui.scoping;

import java.lang.reflect.Method;

public class ComponentBuilder {

  public static final String SERVICE_NAME = "COMPONENT_BUILDER";

  public static <T> T build(Class<T> componentClass, Object parentComponent) {
    for (Method method : parentComponent.getClass().getMethods()) {
      if (method.getReturnType() == componentClass) {
        try {
          //noinspection unchecked
          return (T) method.invoke(parentComponent);
        } catch (ReflectiveOperationException e) {
          throw new RuntimeException(e);
        }
      }
    }

    String parentComponentName = parentComponent.getClass().getName();
    String childComponentName = componentClass.getName();
    throw new IllegalArgumentException(
        "Component " + parentComponentName + " cannot create a child " + childComponentName);
  }

}
