package com.silhocompany.ideo.knews.Fragments.PresentNews;

/**
 * Created by Samuel on 20/03/2017.
 */

public class EntertainmentFragment extends NewsFragment{

    @Override
    protected String[] getUrl() {
        return new String[]{
                " https://newsapi.org/v1/articles?source=entertainment-weekly&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                " https://newsapi.org/v1/articles?source=mashable&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-lad-bible&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=buzzfeed&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
        };
    }
}
