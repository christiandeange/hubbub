package com.deange.githubstatus.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deange.githubstatus.R;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.util.Formatter;
import com.deange.githubstatus.util.SimpleDiffCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v7.util.DiffUtil.calculateDiff;
import static com.deange.githubstatus.util.ViewUtils.inflate;
import static java.util.Collections.emptyList;

public class MessagesAdapter
    extends RecyclerView.Adapter<MessagesAdapter.VH> {

  @NonNull private List<Message> messages = emptyList();

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VH(inflate(parent, R.layout.list_item_message));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    Context context = holder.itemView.getContext();
    Message message = messages.get(position);
    State state = message.state();

    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.title.getLayoutParams();
    lp.weight = state.getWeight();
    holder.title.setLayoutParams(lp);

    holder.title.setBackgroundColor(getColor(context, state.getColorResId()));
    holder.title.setText(context.getString(state.getTitleResId()).toLowerCase());
    holder.body.setText(message.bodyForNotification(context));
    holder.date.setText(Formatter.formatLocalDateTime(message.createdOn()));
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  public void setMessages(@NonNull List<Message> messages) {
    List<Message> oldMessages = this.messages;
    this.messages = messages;

    DiffUtil.Callback callback = new SimpleDiffCallback<>(
        oldMessages, this.messages,
        (m1, m2) -> m1.id() == m2.id());

    calculateDiff(callback, false).dispatchUpdatesTo(this);
  }

  static class VH extends RecyclerView.ViewHolder {
    @BindView(R.id.message_title) TextView title;
    @BindView(R.id.message_body) TextView body;
    @BindView(R.id.message_date) TextView date;

    public VH(View root) {
      super(root);
      ButterKnife.bind(this, root);
    }
  }
}
