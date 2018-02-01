package com.deange.githubstatus.ui.scoping;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

import flow.path.Path;
import flow.path.PathContextFactory;
import mortar.MortarScope;

public final class MortarContextFactory implements PathContextFactory {

  @Override
  public Context setUpContext(Path path, Context parentContext) {
    MortarScope scope = MortarScope.findChild(parentContext, path.getClass().getName());
    if (scope == null) {
      final WithComponent withComponent = path.getClass().getAnnotation(WithComponent.class);
      if (withComponent == null) {
        throw new IllegalStateException(
            String.format("Missing WithComponent annotation on %s", path.getClass().getName()));
      }

      MortarScope parentScope = MortarScope.getScope(parentContext);
      Object component = parentScope.getService(ComponentBuilder.SERVICE_NAME);

      scope = MortarScope.buildChild(parentContext)
                         .withService(ComponentBuilder.SERVICE_NAME,
                                      ComponentBuilder.build(withComponent.value(), component))
                         .build(path.getClass().getName());
    }
    return new TearDownContext(parentContext, scope);
  }

  @Override
  public void tearDownContext(Context context) {
    TearDownContext.destroyScope(context);
  }

  public static MortarScope createRootScope(Object rootComponent, String name) {
    return MortarScope.buildRootScope()
                      .withService(ComponentBuilder.SERVICE_NAME, rootComponent)
                      .build(name);
  }

  static class TearDownContext extends ContextWrapper {
    private static final String SERVICE = "SNEAKY_MORTAR_PARENT_HOOK";
    private final MortarScope parentScope;
    private LayoutInflater inflater;

    static void destroyScope(Context context) {
      MortarScope scope = MortarScope.getScope(context);
      scope.destroy();
    }

    public TearDownContext(Context context, MortarScope scope) {
      super(scope.createContext(context));
      this.parentScope = MortarScope.getScope(context);
    }

    @Override
    public Object getSystemService(String name) {
      if (LAYOUT_INFLATER_SERVICE.equals(name)) {
        if (inflater == null) {
          inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
        }
        return inflater;
      }

      if (SERVICE.equals(name)) {
        return parentScope;
      }

      return super.getSystemService(name);
    }
  }
}
