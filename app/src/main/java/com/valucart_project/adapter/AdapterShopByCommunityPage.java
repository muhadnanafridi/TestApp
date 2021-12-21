package com.valucart_project.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.valucart_project.fragments.BulkCommunityListingFragment;
import com.valucart_project.fragments.BundleCommunityListingFragment;
import com.valucart_project.fragments.ProductCommunityListingFragment;

public class AdapterShopByCommunityPage extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public AdapterShopByCommunityPage(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ProductCommunityListingFragment tab1 = new ProductCommunityListingFragment();
                    return tab1;
                case 1:
                    BulkCommunityListingFragment tab2 = new BulkCommunityListingFragment();
                    return tab2;
                case 2:
                    BundleCommunityListingFragment tab3 = new BundleCommunityListingFragment();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
}
