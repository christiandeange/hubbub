package mortar;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.text.TextUtils.join;

public final class MortarScopeSpy {
  private static final String TAG = "MortarScopeSpy";

  private MortarScopeSpy() {
    throw new AssertionError();
  }

  public static MortarScope parentScope(MortarScope mortarScope) {
    return mortarScope.parent;
  }

  public static void printScope(MortarScope scope) {
    while (scope.parent != null) scope = scope.parent;
    Log.d(TAG, join("\n", renderDirectoryTreeLines(scope)));
  }

  private static List<StringBuilder> renderDirectoryTreeLines(MortarScope scope) {
    List<StringBuilder> result = new LinkedList<>();
    result.add(new StringBuilder().append(scope.getName()));
    List<MortarScope> childScopes = new ArrayList<>(scope.children.values());

    for (int i = 0; i < childScopes.size(); i++) {
      List<StringBuilder> subscopes = renderDirectoryTreeLines(childScopes.get(i));
      if (i < childScopes.size() - 1) {
        addChildScope(result, subscopes);
      } else {
        addLastChildScope(result, subscopes);
      }
    }
    return result;
  }

  private static void addChildScope(List<StringBuilder> result, List<StringBuilder> subtree) {
    for (int i = 0; i < subtree.size(); i++) {
      StringBuilder sb = subtree.get(i);
      result.add(sb.insert(0, (i == 0) ? "├── " : "│   "));
    }
  }

  private static void addLastChildScope(List<StringBuilder> result, List<StringBuilder> subtree) {
    for (int i = 0; i < subtree.size(); i++) {
      StringBuilder sb = subtree.get(i);
      result.add(sb.insert(0, (i == 0) ? "└── " : "    "));
    }
  }

}
