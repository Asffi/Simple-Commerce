package com.example.visualcommerce.login;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class loginAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public loginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                loginTabFragment loginTabFragment = new loginTabFragment();
                return loginTabFragment;
            case 1:
                signupTabFragment signupTabFragment = new signupTabFragment();
                return signupTabFragment;
            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
