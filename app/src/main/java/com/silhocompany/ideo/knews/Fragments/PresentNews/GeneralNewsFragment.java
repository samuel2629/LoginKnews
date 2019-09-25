package com.silhocompany.ideo.knews.Fragments.PresentNews;
/**
 * Created by Samuel on 05/03/2017.
 */

public class GeneralNewsFragment extends NewsFragment {

    @Override
    public String[] getUrl() {
        return new String[]{
                "https://newsapi.org/v1/articles?source=bbc-news&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=time&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-guardian-uk&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-guardian-au&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=metro&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=independent&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=associated-press&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",};
    }
}
