package com.silhocompany.ideo.knews.Fragments.PresentNews;

/**
 * Created by Samuel on 20/03/2017.
 */

public class BusinessFragment extends NewsFragment{

    @Override
    protected String[] getUrl() {
        return new String[]{
                " https://newsapi.org/v1/articles?source=cnbc&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                " https://newsapi.org/v1/articles?source=fortune&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-economist&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                " https://newsapi.org/v1/articles?source=the-wall-street-journal&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
        };
    }
}
