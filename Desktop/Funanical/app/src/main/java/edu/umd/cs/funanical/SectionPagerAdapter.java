package edu.umd.cs.funanical;

/**
 * Created by apple on 4/23/18.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

// https://stackoverflow.com/questions/31313105/how-to-implement-android-tablayout-design-support-libarary-with-swipe-views

public class SectionPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public SectionPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Statistics";
            case 1:
            default:
                return "Categories";
        }
    }
}
