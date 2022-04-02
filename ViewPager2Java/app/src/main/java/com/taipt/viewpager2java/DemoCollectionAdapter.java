package com.taipt.viewpager2java;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class DemoCollectionAdapter extends FragmentStateAdapter {


    public DemoCollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("DemoCollectionAdapter", "createFragment: - position == " + position);
        switch (position) {
            case 0:
                // tab a
                return FragmentOne.newInstance();
            case 1:
                // tab b
                return FragmentTwo.newInstance();
        }
        return FragmentOne.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
