package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class StepsFragmentCollection extends FragmentStatePagerAdapter {

    public StepsFragmentCollection(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        //adding the step content to a bundle to pass to the fragment.
        StepsFragment stepsFragment = new StepsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("steps_type", HomeFragment.curr_activity.getSteps_type().get(i));
        bundle.putString("steps_text", HomeFragment.curr_activity.getSteps_text().get(i));
        bundle.putString("steps_image", HomeFragment.curr_activity.getSteps_image().get(i));

        i++;
        stepsFragment.setArguments(bundle);

        return stepsFragment;
    }

    //getting fragment count (step count)
    @Override
    public int getCount() {
        return HomeFragment.curr_activity.getSteps_type().size();
    }
}
