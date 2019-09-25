package com.silhocompany.ideo.knews.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Samuel on 29/01/2017.
 */

public class Article extends ArrayList<Article> implements Parcelable {

    private String mTitle, mDescription, mImageView, mArticleUrl, mUrlToImage;

    public Article(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mImageView = in.readString();
        mArticleUrl = in.readString();
        mUrlToImage = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Article() {

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageView() {
        return mImageView;
    }

    public void setImageView(String imageView) {
        mImageView = imageView;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        mArticleUrl = articleUrl;
    }


    public String getUrlToImage() {
        return mUrlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        mUrlToImage = urlToImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mImageView);
        parcel.writeString(mArticleUrl);
        parcel.writeString(mUrlToImage);
    }
}
