package com.google.firebase.message;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jin on 5/22/16.
 */

public class MessageComponent {


    private RecyclerView mView;
    private MessageViewAdapter mAdapter;
    private MessageObserver mObserver;
    private ProgressBar mProgressBar;
    private Activity mContext;
    private final LinearLayoutManager mManager;

    public MessageComponent(Activity aContext, DatabaseReference aFirebaseRef) {
        mContext = aContext;
        mView = (RecyclerView)mContext.findViewById(R.id.messageRecyclerView);

        mAdapter = new MessageViewAdapter(aFirebaseRef);
        mAdapter.registerAdapterDataObserver(mObserver);

        mView.setAdapter(mAdapter);

        setProgressBar((ProgressBar) mContext.findViewById(R.id.progressBar));

        mManager = new LinearLayoutManager(mContext);
        mManager.setStackFromEnd(true);
        mView.setLayoutManager(mManager);

    }

    public void setView(RecyclerView aView) {
        mView = aView;
    }

    public RecyclerView getView() {
        return mView;
    }



    public void setProgressBar(ProgressBar aProgressBar) {
        mProgressBar = aProgressBar;
    }

    public class MessageObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            int count = mAdapter.getItemCount();
            int position = mManager.findLastCompletelyVisibleItemPosition();
            if (position == -1 || (positionStart >= (count - 1) && position == (positionStart - 1))) {
                mView.scrollToPosition(positionStart);
            }
        }
    }

    public class MessageViewAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {
        public MessageViewAdapter(Query aRef) {
            super(Message.class, R.layout.item_message, MessageViewHolder.class, aRef);
        }

        @Override
        protected void populateViewHolder(MessageViewHolder viewHolder, Message friendlyMessage, int position) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            viewHolder.messageTextView.setText(friendlyMessage.getText());
            viewHolder.messengerTextView.setText(friendlyMessage.getName());
            if (friendlyMessage.getPhotoUrl() == null) {
                viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.ic_account_circle_black_36dp));
            } else {
                Glide.with(mContext)
                        .load(friendlyMessage.getPhotoUrl())
                        .into(viewHolder.messengerImageView);
            }
        }
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }
}