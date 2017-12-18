package com.deange.githubstatus.util;

import android.support.v7.util.DiffUtil;

import java.util.List;
import java.util.Objects;

public class SimpleDiffCallback<T>
    extends DiffUtil.Callback {

  private final List<T> mOldList;
  private final List<T> mNewList;
  private final DiffComparator<T> mIdFunction;
  private final DiffComparator<T> mEqualsFunction;

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

    mOldList = oldList;
    mNewList = newList;
    mIdFunction = idFunction;
    mEqualsFunction = equalsFunction;
  }

  @Override
  public int getOldListSize() {
    return mOldList.size();
  }

  @Override
  public int getNewListSize() {
    return mNewList.size();
  }

  @Override
  public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
    return mIdFunction.test(mOldList.get(oldItemPosition), mNewList.get(newItemPosition));
  }

  @Override
  public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
    return mEqualsFunction.test(mOldList.get(oldItemPosition), mNewList.get(newItemPosition));
  }

  public interface DiffComparator<T> {
    boolean test(final T obj1, final T obj2);
  }
}
