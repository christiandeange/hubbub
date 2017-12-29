package com.deange.githubstatus.util;

import android.support.v7.util.DiffUtil;

import java.util.List;
import java.util.Objects;

public class SimpleDiffCallback<T>
    extends DiffUtil.Callback {

  private final List<T> oldList;
  private final List<T> newList;
  private final DiffComparator<T> idFunc;
  private final DiffComparator<T> eqFunc;

  public SimpleDiffCallback(List<T> oldList, List<T> newList) {
    this(oldList, newList, (o1, o2) -> o1.hashCode() == o1.hashCode());
  }

  public SimpleDiffCallback(List<T> oldList, List<T> newList, DiffComparator<T> idFunction) {
    this(oldList, newList, idFunction, Objects::equals);
  }

  public SimpleDiffCallback(
      List<T> oldList,
      List<T> newList,
      DiffComparator<T> idFunction,
      DiffComparator<T> equalsFunction) {
    this.oldList = oldList;
    this.newList = newList;
    idFunc = idFunction;
    eqFunc = equalsFunction;
  }

  @Override
  public int getOldListSize() {
    return oldList.size();
  }

  @Override
  public int getNewListSize() {
    return newList.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return idFunc.test(oldList.get(oldItemPosition), newList.get(newItemPosition));
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return eqFunc.test(oldList.get(oldItemPosition), newList.get(newItemPosition));
  }

  public interface DiffComparator<T> {
    /**
     * Returns true if both objects are "equal", in whatever context that applies
     */
    boolean test(T obj1, T obj2);
  }
}
