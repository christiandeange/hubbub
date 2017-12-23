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

  public SimpleDiffCallback(
      final List<T> oldList,
      final List<T> newList) {
    this(oldList, newList, (o1, o2) -> o1.hashCode() == o1.hashCode());
  }

  public SimpleDiffCallback(
      final List<T> oldList,
      final List<T> newList,
      final DiffComparator<T> idFunction) {
    this(oldList, newList, idFunction, Objects::equals);
  }

  public SimpleDiffCallback(
      final List<T> oldList,
      final List<T> newList,
      final DiffComparator<T> idFunction,
      final DiffComparator<T> equalsFunction) {

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
  public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
    return idFunc.test(oldList.get(oldItemPosition), newList.get(newItemPosition));
  }

  @Override
  public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
    return eqFunc.test(oldList.get(oldItemPosition), newList.get(newItemPosition));
  }

  public interface DiffComparator<T> {
    boolean test(final T obj1, final T obj2);
  }
}
