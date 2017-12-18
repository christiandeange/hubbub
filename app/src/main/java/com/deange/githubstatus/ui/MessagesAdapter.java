package com.deange.githubstatus.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deange.githubstatus.R;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.util.Formatter;
import com.deange.githubstatus.util.SimpleDiffCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class MessagesAdapter
    extends RecyclerView.Adapter<MessagesAdapter.VH> {

  private final Context mContext;
  private final LayoutInflater mInflater;
  @NonNull private List<Message> mMessages = Collections.emptyList();

  public MessagesAdapter(final Context context) {
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public VH onCreateViewHolder(final ViewGroup parent, final int viewType) {
    return new VH(mInflater.inflate(R.layout.list_item_message, parent, false));
  }

  @Override
  public void onBindViewHolder(final VH holder, final int position) {
    final Message message = mMessages.get(position);
    final State state = message.state();
    final int color = ContextCompat.getColor(mContext, state.getColorResId());

    ((LinearLayout.LayoutParams) holder.mTitle.getLayoutParams()).weight = state.getWeight();

    holder.mTitle.setBackgroundColor(color);
    holder.mTitle.setText(mContext.getString(state.getTitleResId()).toLowerCase());
    holder.mBody.setText(message.bodyForNotification(mContext));
    holder.mDate.setText(Formatter.formatLocalDateTime(message.createdOn()));
  }

  @Override
  public int getItemCount() {
    return mMessages.size();
  }

  public Disposable setResponse(final Single<Response> response) {
    return response.subscribe(this::onResponseReceived, this::onResponseFailed);
  }

  private void onResponseReceived(final Response response) {
    setMessages(response.messages());
  }

  private void onResponseFailed(final Throwable throwable) {
    setMessages(Collections.emptyList());
  }

  void setMessages(@NonNull final List<Message> messages) {
    final List<Message> oldMessages = mMessages;
    mMessages = messages;

    final DiffUtil.Callback callback = new SimpleDiffCallback<>(
        oldMessages, mMessages,
        (m1, m2) -> m1.id() == m2.id());

    DiffUtil.calculateDiff(callback, false).dispatchUpdatesTo(this);
  }

  static class VH extends RecyclerView.ViewHolder {
    @BindView(R.id.message_title) TextView mTitle;
    @BindView(R.id.message_body) TextView mBody;
    @BindView(R.id.message_date) TextView mDate;

    public VH(final View root) {
      super(root);
      ButterKnife.bind(this, root);
    }
  }
}
