package com.valucart_project.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.valucart_project.fragments.BuildYourOwnBundleFragment;
import com.valucart_project.fragments.BulkListingFragment;
import com.valucart_project.fragments.BundleListingFragment;

public class AdapterSuperSavorZonePage extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public AdapterSuperSavorZonePage(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                BundleListingFragment tab1 = new BundleListingFragment();
                return tab1;
            case 1:
                BulkListingFragment tab2 = new BulkListingFragment();
                return tab2;
            case 2:
                BuildYourOwnBundleFragment tab4 = new BuildYourOwnBundleFragment();
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
