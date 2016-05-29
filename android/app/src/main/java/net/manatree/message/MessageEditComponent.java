package net.manatree.message;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import net.manatree.chat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageEditComponent extends LinearLayout {

    private final CircleImageView mAvatar;
    protected EditText mMessageEditText;
    //    private View mValue;
    private ImageView mImage;
    private Button mSendButton;
    //    private String mPhotoUrl;
//    private String mUsername;
    private MessageListener mListener;

    public MessageEditComponent(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_message_edit, this, true);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mAvatar = (CircleImageView) findViewById(R.id.avatar);
        mAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View aView) {
                if (mListener.getPhotoUrl() == null) {
                    mListener.signIn();
                } else {
                    mListener.signOut();
                    mAvatar.setImageResource(R.drawable.ic_account_circle_black_36dp);
                }
            }
        });
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

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.add(mMessageEditText.getText().toString());
                mMessageEditText.setText("");
            }
        });
    }


    public MessageEditComponent(Context context) {
        this(context, null);
    }

    public void setFilter(int aValue) {
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(aValue)});
    }

    public void setListener(MessageListener aListener) {
        mListener = aListener;
        Glide.with(getContext())
                .load(mListener.getPhotoUrl())
                .into(mAvatar);
    }

//    public String getUsername() {
//        return mUsername;
//    }
//
//    public void setUsername(String aUsername) {
//        mUsername = aUsername;
//    }
//
//    public String getPhotoUrl() {
//        return mPhotoUrl;
//    }
//
//    public void setPhotoUrl(String aPhotoUrl) {
//        mPhotoUrl = aPhotoUrl;
//    }
}

