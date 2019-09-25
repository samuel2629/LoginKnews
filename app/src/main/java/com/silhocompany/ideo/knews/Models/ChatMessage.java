package com.silhocompany.ideo.knews.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samuel on 12/06/2017.
 */

public class ChatMessage implements Parcelable {

    public ChatMessage(){}

    private String message;
    private String author;
    private int agreeDisagree;

    public int getAgreeDisagree() {
        return agreeDisagree;
    }

    public void setAgreeDisagree(int agreeDisagree) {
        this.agreeDisagree = agreeDisagree;
    }

    public ChatMessage(String s, String username) {
        message = s;
        author = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String text) {
        message = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    protected ChatMessage(Parcel in) {
        message = in.readString();
        author = in.readString();
        agreeDisagree = in.readInt();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(author);
        parcel.writeInt(agreeDisagree);
    }
}
