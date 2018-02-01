package com.deange.githubstatus.ui.scoping;

import android.content.Context;

import mortar.MortarScope;
import mortar.MortarScopeSpy;

public final class Components {

  private Components() {
    throw new AssertionError();
  }

  @SuppressWarnings("unchecked")
  public static <T> T component(Context context, Class<T> componentClass) {
    MortarScope scope = MortarScope.getScope(context);
    try {
      return (T) scope.getService(ComponentBuilder.SERVICE_NAME);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("No component found in context " + context);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(
          "Expected component of type" +
              componentClass +
              ", found " +
              scope.getClass() +
              " instead.");
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T componentInParent(Context context, Class<T> componentClass) {
    MortarScope originalScope = MortarScope.getScope(context);
    MortarScope scope = originalScope;
    while (scope != null) {
      Object component;
      try {
        component = scope.getService(ComponentBuilder.SERVICE_NAME);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("No component found in context " + context);
      }
      if (componentClass.isInstance(component)) {
        return (T) component;
      } else {
        scope = MortarScopeSpy.parentScope(scope);
      }
    }

    throw new IllegalArgumentException(
        "No components of type " + componentClass + " found in scope " + originalScope);
  }
}
