package com.silhocompany.ideo.knews.Fragments.Container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Fragments.PresentNews.BusinessFragment;
import com.silhocompany.ideo.knews.Fragments.PresentNews.EntertainmentFragment;
import com.silhocompany.ideo.knews.Fragments.PresentNews.GeneralNewsFragment;
import com.silhocompany.ideo.knews.Fragments.PresentNews.HighTekFragment;
import com.silhocompany.ideo.knews.Fragments.PresentNews.SportFragment;


public class ViewPagerFragment extends Fragment {

    public static final String KEY_NEW_INDEX = "key_new_index";
    private SportFragment mSportFragment;
    private HighTekFragment mHighTekFragment;
    private GeneralNewsFragment mGeneralNewsFragment;
    private BusinessFragment mBusinessFragment;
    private EntertainmentFragment mEntertainmentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_pager, container, false);

        mSportFragment = new SportFragment();
        mHighTekFragment = new HighTekFragment();
        mGeneralNewsFragment = new GeneralNewsFragment();
        mBusinessFragment = new BusinessFragment();
        mEntertainmentFragment = new EntertainmentFragment();

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mGeneralNewsFragment;
                    case 1:
                        return mSportFragment;
                    case 2:
                        return mEntertainmentFragment;
                    case 3:
                        return mHighTekFragment;
                    case 4:
                        return mBusinessFragment;
                    default:
                        return mGeneralNewsFragment;
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.general);
                    case 1:
                        return getString(R.string.sport);
                    case 2:
                        return getString(R.string.entertainment);
                    case 3:
                        return getString(R.string.high_tech);
                    case 4:
                        return getString(R.string.business);
                    default: return getString(R.string.general);
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        });

        viewPager.setOffscreenPageLimit(6);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return view;
    }
}
