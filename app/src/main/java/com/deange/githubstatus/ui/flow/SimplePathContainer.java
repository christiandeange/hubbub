package com.deange.githubstatus.ui.flow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import flow.Flow;
import flow.path.Path;
import flow.path.PathContainer;
import flow.path.PathContext;
import flow.path.PathContextFactory;

/**
 * Uses {@link PathContext} to allow customized sub-containers. Saves and restores view state.
 */
public class SimplePathContainer extends PathContainer {
  private final PathContextFactory contextFactory;

  public SimplePathContainer(int tagKey, PathContextFactory contextFactory) {
    super(tagKey);
    this.contextFactory = contextFactory;
  }

  @Override
  protected void performTraversal(
      final ViewGroup containerView,
      final TraversalState traversalState,
      final Flow.Direction direction,
      final Flow.TraversalCallback callback) {

    final PathContext oldPath;
    if (containerView.getChildCount() > 0) {
      oldPath = PathContext.get(containerView.getChildAt(0).getContext());
    } else {
      oldPath = PathContext.root(containerView.getContext());
    }

    Path to = traversalState.toPath();
    PathContext context = PathContext.create(oldPath, to, contextFactory);
    View newView = LayoutInflater.from(context)
                                 .cloneInContext(context)
                                 .inflate(getLayout(to), containerView, false);

    if (traversalState.fromPath() != null) {
      View fromView = containerView.getChildAt(0);
      traversalState.saveViewState(fromView);
      containerView.removeView(fromView);
    }

    traversalState.restoreViewState(newView);
    containerView.addView(newView);
    oldPath.destroyNotIn(context, contextFactory);
    callback.onTraversalCompleted();
  }

  protected int getLayout(Path path) {
    if (!(path instanceof HasLayout)) {
      throw new IllegalArgumentException(
          String.format("%s does not implement HasLayout", path.getClass().getName()));
    }
    return ((HasLayout) path).layoutId();
  }

}