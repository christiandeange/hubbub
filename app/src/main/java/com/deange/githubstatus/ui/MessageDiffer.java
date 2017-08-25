package com.deange.githubstatus.ui;

import android.support.v7.util.DiffUtil;

import com.deange.githubstatus.model.Message;

import java.util.List;

public class MessageDiffer
        extends DiffUtil.Callback {

    private final List<Message> mOldMessages;
    private final List<Message> mNewMessages;

    public MessageDiffer(final List<Message> oldMessages, final List<Message> newMessages) {
        mOldMessages = oldMessages;
        mNewMessages = newMessages;
    }

    @Override
    public int getOldListSize() {
        return mOldMessages.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMessages.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return mOldMessages.get(oldItemPosition).id() == mNewMessages.get(newItemPosition).id();
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return mOldMessages.get(oldItemPosition).equals(mNewMessages.get(newItemPosition));
    }
}
