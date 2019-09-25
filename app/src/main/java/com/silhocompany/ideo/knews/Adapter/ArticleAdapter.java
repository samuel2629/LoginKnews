package com.silhocompany.ideo.knews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Activities.LoginActivity;
import com.silhocompany.ideo.knews.Fragments.PresentNews.NewsFragment;
import com.silhocompany.ideo.knews.Models.Article;

import java.util.ArrayList;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private static final String TAGO = ArticleAdapter.class.getSimpleName();
    public static final String ARTICLE_POSITION = "position_article";
    private final NewsFragment.OnNewSelectedInterface mListener;
    private final ArticleAdapter.OnNewsInsertedInterface mListener2;
    private ArrayList<Article> mArticlesList;
    private Context mContext;
    private int lastPosition = -1;

    public interface OnNewsInsertedInterface{
        void onNewsSaved(Article article, FloatingActionButton saveButton);
    }

    public ArticleAdapter(Context context, ArrayList<Article> articles, NewsFragment.OnNewSelectedInterface listener,
                          OnNewsInsertedInterface listener2){
        mContext = context;
        mArticlesList = articles;
        mListener = listener;
        mListener2 = listener2;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

        if(holder != null) {
            holder.bindArticle(mArticlesList.get(holder.getAdapterPosition()));
            setAnimation(holder.itemView, holder.getAdapterPosition());
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return mArticlesList.size();
    }

    public void clear() {
        mArticlesList.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Article> articles) {
        mArticlesList.clear();
        mArticlesList.addAll(articles);
        notifyDataSetChanged();
    }


    protected class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final FloatingActionButton mCommentButton;
        private ImageView mImageView;
        private TextView mTitleTextView, mDescriptionTextView;
        private FloatingActionButton mSaveButton;

        private ArticleViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.titleWithoutImage);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            mSaveButton = (FloatingActionButton) itemView.findViewById(R.id.saveFloatingActionButton);
            mCommentButton = (FloatingActionButton) itemView.findViewById(R.id.commentFloatingActionButton);
            itemView.setOnClickListener(this);
        }

        private void bindArticle(final Article article) {

            Glide.with(mContext).load(article.getImageView()).into(mImageView);
            mTitleTextView.setText(article.getTitle());
            mDescriptionTextView.setText(article.getDescription());
            if(mDescriptionTextView.getText().equals("")){
                mDescriptionTextView.setVisibility(View.GONE);}

            if(article.getImageView().equals("null")){
                itemView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                mDescriptionTextView.setVisibility(View.GONE);
                mTitleTextView.setVisibility(View.GONE);
                mSaveButton.setVisibility(View.GONE);
                mCommentButton.setVisibility(View.GONE);}

            mSaveButton.setOnClickListener(view -> mListener2.onNewsSaved(article, mSaveButton));
            mCommentButton.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, LoginActivity.class);
                String articleTitle = article.getTitle();
                intent.putExtra(ARTICLE_POSITION, articleTitle);
                mContext.startActivity(intent);

            });

        }


        @Override
        public void onClick(View view) {
            mListener.onNewsSelected(getLayoutPosition(), mArticlesList);
        }

    }
}