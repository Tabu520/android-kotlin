package taipt4.kotlin.javaandroidtest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BirdFragment extends Fragment {

    public BirdFragment() {
        // Required empty public constructor
    }

    public static BirdFragment newInstance() {
        BirdFragment fragment = new BirdFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bird, container, false);
    }
}