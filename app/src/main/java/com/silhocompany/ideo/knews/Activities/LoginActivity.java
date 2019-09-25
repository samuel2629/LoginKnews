package com.silhocompany.ideo.knews.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Adapter.ArticleAdapter;
import com.silhocompany.ideo.knews.Adapter.ChatMessagesAdapter;

import com.silhocompany.ideo.knews.Models.ChatMessage;

import java.util.ArrayList;

public class LoginActivity extends MainActivity {

    private static final int RC_SIGN_IN = 119;

    private static final String ANONYMOUS = "anonymous";
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 240;
    private String mUsername;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseReference;
    private ChatMessagesAdapter mChatMessagesAdapter;
    private ChildEventListener mChildEventListener;

    private EditText mEditText;
    private ChatMessage mChatMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String stringTitle = getIntent().getStringExtra(ArticleAdapter.ARTICLE_POSITION);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference().child("messages").child(stringTitle);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        mEditText = (EditText) findViewById(R.id.editTextChat);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarChat);
        RecyclerView chatMessageRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);

        mProgressBar.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);

        chatMessageRecyclerView.setHasFixedSize(false);
        chatMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mChatMessagesAdapter = new ChatMessagesAdapter(this, new ArrayList<>());
        chatMessageRecyclerView.setAdapter(mChatMessagesAdapter);
        setTitle(stringTitle);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        sendButton.setOnClickListener(view -> {
            mChatMessage = new ChatMessage(mEditText.getText().toString(), mUsername);
                    mDatabaseReference.push().setValue(mChatMessage);

            mEditText.setText("");
                }
            );

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                onSignedInInitialized(user.getDisplayName());
            } else {
                onSignedOut();
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder().build(),
                        RC_SIGN_IN);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, R.string.error_signing_in, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void attachDataBaseReadListener() {
        if(mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    mChatMessagesAdapter.add(chatMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mDatabaseReference.addChildEventListener(mChildEventListener);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void detachDataBaseReadListener() {
        if(mChildEventListener != null){
        mDatabaseReference.removeEventListener(mChildEventListener);
        mChildEventListener = null;}
    }

    private void onSignedOut() {
        mUsername = ANONYMOUS;
        mChatMessagesAdapter.clear();
        detachDataBaseReadListener();
    }

    private void onSignedInInitialized(String userName) {
        mUsername = userName ;
        attachDataBaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null){
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                AlertDialog alertDialog = getDialogQuit(this);
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private AlertDialog getDialogQuit(LoginActivity loginActivity) {
        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(loginActivity)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_message)
                .setPositiveButton(R.string.confirm_button, (dialogInterface, i) -> AuthUI.getInstance().signOut(loginActivity))
                .setNegativeButton(R.string.cancel_button, null);
        return dialogFragment.create();
    }
}
