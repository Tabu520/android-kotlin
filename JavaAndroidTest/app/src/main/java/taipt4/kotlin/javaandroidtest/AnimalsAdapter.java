package taipt4.kotlin.javaandroidtest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AnimalsAdapter extends FragmentStateAdapter {

    public AnimalsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return DogFragment.newInstance();
            case 2:
                return BirdFragment.newInstance();
        }
        return CatFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
