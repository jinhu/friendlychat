package net.manatree.message;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.manatree.chat.FriendlyMessage;

/**
 * Created by jin on 5/28/16.
 */

public class MessageEditComponent {

    protected EditText mMessageEditText;
    private Button mSendButton;
    private String mPhotoUrl;
    private String mUsername;
    private MessageListener mListener;

    public MessageEditComponent(EditText aViewById, Button aButton, final MessageListener aListener, String aUsername, String aPhotoUrl) {
        mUsername = aUsername;
        mPhotoUrl = aPhotoUrl;
        mMessageEditText = aViewById;
        mListener = aListener;
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

        mSendButton = aButton;
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername,
                        mPhotoUrl);
                mMessageEditText.setText("");

                mListener.add(friendlyMessage);
            }
        });
    }

    public void setFilter(int aValue) {
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(aValue)});
    }
}

