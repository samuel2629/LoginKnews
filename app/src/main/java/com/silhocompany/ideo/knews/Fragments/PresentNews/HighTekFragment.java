package com.silhocompany.ideo.knews.Fragments.PresentNews;

/**
 * Created by Samuel on 05/03/2017.
 */

public class HighTekFragment extends NewsFragment {

    @Override
    public String[] getUrl() {
        return new String[]
        {"https://newsapi.org/v1/articles?source=techradar&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                ,"https://newsapi.org/v1/articles?source=ars-technica&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                , "https://newsapi.org/v1/articles?source=engadget&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                , "https://newsapi.org/v1/articles?source=recode&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                , "https://newsapi.org/v1/articles?source=techcrunch&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                , "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=04e9f0afe05f427e9b2c03399acfacd5"
                , "https://newsapi.org/v1/articles?source=the-verge&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5"};
    }
}
