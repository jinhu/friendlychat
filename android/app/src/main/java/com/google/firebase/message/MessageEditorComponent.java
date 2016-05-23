package com.google.firebase.message;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.MainActivity;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DatabaseReference;

import static com.google.firebase.codelab.friendlychat.MainActivity.ANONYMOUS;
import static com.google.firebase.codelab.friendlychat.MainActivity.DEFAULT_MSG_LENGTH_LIMIT;
import static com.google.firebase.codelab.friendlychat.MainActivity.MESSAGE_SENT_EVENT;

/**
 * Created by jin on 5/22/16.
 */
public class MessageEditorComponent {
    private final EditText mMessageEditText;
    private final Button mSendButton;
    private final Activity mContext;
    private String mUsername=ANONYMOUS;
    private String mPhotoUrl=null;

    public static final String INSTANCE_ID_TOKEN_RETRIEVED = "iid_token_retrieved";
    public static final String FRIENDLY_MSG_LENGTH = "friendly_msg_length";

    public MessageEditorComponent(Activity aContext, SharedPreferences mSharedPreferences, DatabaseReference aFirebaseDatabaseReference, FirebaseAnalytics aFirebaseAnalytics, FirebaseUser aFirebaseUser) {
        mContext = aContext;

        mMessageEditText = (EditText) mContext.findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) mContext.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener((view) -> {
            Message friendlyMessage = new Message(mMessageEditText.getText().toString(), mUsername,
                    mPhotoUrl);
            aFirebaseDatabaseReference.child(MainActivity.MESSAGES_CHILD).push().setValue(friendlyMessage);
            mMessageEditText.setText("");
            aFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
        });

        setUsername(aFirebaseUser.getDisplayName());
        setPhotoUrl(aFirebaseUser.getPhotoUrl().toString());

    }


    public void setUsername(String aUsername) {
        mUsername = aUsername;
    }

    public void setPhotoUrl(String aPhotoUrl) {
        mPhotoUrl = aPhotoUrl;
    }

    public void setFilters(InputFilter[] aInputFilters) {
        mMessageEditText.setFilters(aInputFilters);
    }
}
