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
import com.google.firebase.codelab.friendlychat.MainActivity;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.codelab.friendlychat.MainActivity.MESSAGES_CHILD;

/**
 * Created by jin on 5/22/16.
 */

public class MessageComponent {


    private RecyclerView mView;
    private MessageViewAdapter mAdapter;
    private MessageObserver mObserver;
    private ProgressBar mProgressBar;
    private Activity mContext;

    public MessageComponent(Activity aContext, DatabaseReference aFirebaseRef) {
        mContext = aContext;
        setView((RecyclerView) mContext.findViewById(R.id.messageRecyclerView));
        createAdapter(aFirebaseRef.child(MESSAGES_CHILD), R.layout.item_message);
        setProgressBar((ProgressBar) mContext.findViewById(R.id.progressBar));
        createLayoutManager();

    }

    public void setView(RecyclerView aView) {
        mView = aView;
    }

    public RecyclerView getView() {
        return mView;
    }

    public void createAdapter(DatabaseReference aChild, int aItemViewId) {
               mAdapter = new MessageViewAdapter(
                Message.class,
               aItemViewId,
                MessageComponent.MessageViewHolder.class,aChild);
        mAdapter.registerAdapterDataObserver(mObserver);
        mView.setAdapter(mAdapter);


    }

    public void createLayoutManager() {
        LinearLayoutManager manager= new LinearLayoutManager(mContext);
        manager.setStackFromEnd(true);
        mView.setLayoutManager(manager);
    }

    public void setProgressBar(ProgressBar aProgressBar) {
        mProgressBar = aProgressBar;
    }

    public class MessageObserver extends RecyclerView.AdapterDataObserver {
        private MessageViewAdapter mFirebaseAdapter;
        private LinearLayoutManager mLinearLayoutManager;
        private RecyclerView mMessageRecyclerView;

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            int friendlyMessageCount = mFirebaseAdapter.getItemCount();
            int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
            // to the bottom of the list to show the newly added message.
            if (lastVisiblePosition == -1 ||
                    (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                mMessageRecyclerView.scrollToPosition(positionStart);
            }
        }
    }

    public class MessageViewAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {


        private MainActivity mMainActivity;
        private ProgressBar mProgressBar;

        public MessageViewAdapter(Class<Message> modelClass, int modelLayout, Class<MessageViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(MessageViewHolder viewHolder, Message friendlyMessage, int position) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            viewHolder.messageTextView.setText(friendlyMessage.getText());
            viewHolder.messengerTextView.setText(friendlyMessage.getName());
            if (friendlyMessage.getPhotoUrl() == null) {
                viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(mMainActivity,
                        R.drawable.ic_account_circle_black_36dp));
            } else {
                Glide.with(mMainActivity)
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