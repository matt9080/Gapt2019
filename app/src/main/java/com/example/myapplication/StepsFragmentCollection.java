package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragmentCollection extends FragmentStatePagerAdapter {

    public StepsFragmentCollection(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        StepsFragment stepsFragment = new StepsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("steps_type", HomeFragment.activity.getSteps_type().get(i));
        bundle.putString("steps_text", HomeFragment.activity.getSteps_text().get(i));
        bundle.putString("steps_image", HomeFragment.activity.getSteps_image().get(i));

        i++;
        stepsFragment.setArguments(bundle);

        return stepsFragment;
    }

    @Override
    public int getCount() {
        return HomeFragment.activity.getSteps_type().size();
    }
}
