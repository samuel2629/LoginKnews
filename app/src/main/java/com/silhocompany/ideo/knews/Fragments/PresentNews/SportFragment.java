package com.silhocompany.ideo.knews.Fragments.PresentNews;

/**
 * Created by Samuel on 05/03/2017.
 */

public class SportFragment extends NewsFragment{

    @Override
    public String[] getUrl() {
        return new String[]
                {"https://newsapi.org/v1/articles?source=four-four-two&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                ,"https://newsapi.org/v1/articles?source=bbc-sport&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                " https://newsapi.org/v1/articles?source=fox-sports&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                " https://newsapi.org/v1/articles?source=talksport&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-sport-bible&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"};
    }
}
